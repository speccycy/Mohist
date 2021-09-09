package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.bukkit.HeightMap;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftHeightMap;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator {

    private final net.minecraft.world.level.chunk.ChunkGenerator delegate;
    private final ChunkGenerator generator;
    private final ServerLevel world;
    private final Random random = new Random();
    private boolean newApi;
    private boolean implementBaseHeight = true;

    @Deprecated
    private class CustomBiomeGrid implements BiomeGrid {

        private final ChunkBiomeContainer biome; // SPIGOT-5529: stored in 4x4 grid

        public CustomBiomeGrid(ChunkBiomeContainer biome) {
            this.biome = biome;
        }

        @Override
        public Biome getBiome(int x, int z) {
            return getBiome(x, 0, z);
        }

        @Override
        public void setBiome(int x, int z, Biome bio) {
            for (int y = 0; y < world.getWorld().getMaxHeight(); y += 4) {
                setBiome(x, y, z, bio);
            }
        }

        @Override
        public Biome getBiome(int x, int y, int z) {
            return CraftBlock.biomeBaseToBiome((net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome>) biome.biomeRegistry, biome.getNoiseBiome(x >> 2, y >> 2, z >> 2));
        }

        @Override
        public void setBiome(int x, int y, int z, Biome bio) {
            Preconditions.checkArgument(bio != Biome.CUSTOM, "Cannot set the biome to %s", bio);
            biome.setBiome(x >> 2, y >> 2, z >> 2, CraftBlock.biomeToBiomeBase((net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome>) biome.biomeRegistry, bio));
        }
    }

    public CustomChunkGenerator(ServerLevel world, net.minecraft.world.level.chunk.ChunkGenerator delegate, ChunkGenerator generator) {
        super(delegate.getBiomeSource(), delegate.getSettings());

        this.world = world;
        this.delegate = delegate;
        this.generator = generator;
    }

    @Override
    public net.minecraft.world.level.chunk.ChunkGenerator withSeed(long i) {
        return new CustomChunkGenerator(this.world, delegate.withSeed(i), this.generator);
    }

    @Override
    public BiomeSource getBiomeSource() {
        return delegate.getBiomeSource();
    }

    @Override
    public int getSeaLevel() {
        return delegate.getSeaLevel();
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion regionlimitedworldaccess, ChunkAccess ichunkaccess) {
        if (generator.shouldGenerateSurface()) {
            delegate.buildSurface(regionlimitedworldaccess, ichunkaccess);
        }

        CraftChunkData chunkData = new CraftChunkData(this.world.getWorld(), ichunkaccess);
        WorldgenRandom random = new WorldgenRandom();
        int x = ichunkaccess.getPos().x;
        int z = ichunkaccess.getPos().z;
        random.setBaseChunkSeed(x, z); // PAIL rename surfaceSeeded
        generator.generateSurface(this.world.getWorld(), random, x, z, chunkData);

        if (generator.shouldGenerateBedrock()) {
            random = new WorldgenRandom();
            random.setBaseChunkSeed(x, z); // PAIL rename surfaceSeeded
            delegate.buildBedrock(ichunkaccess, random);
        }

        random = new WorldgenRandom();
        random.setBaseChunkSeed(x, z); // PAIL rename surfaceSeeded
        generator.generateBedrock(this.world.getWorld(), random, x, z, chunkData);
        chunkData.breakLink();

        // return if new api is used
        if (newApi) {
            return;
        }

        // old ChunkGenerator logic, for backwards compatibility
        // Call the bukkit ChunkGenerator before structure generation so correct biome information is available.
        this.random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid(new ChunkBiomeContainer(world.registryAccess().registryOrThrow(net.minecraft.core.Registry.BIOME_REGISTRY), regionlimitedworldaccess, ichunkaccess.getPos(), this.getBiomeSource()));

        ChunkData data;
        try {
            if (generator.isParallelCapable()) {
                data = generator.generateChunkData(this.world.getWorld(), this.random, x, z, biomegrid);
            } else {
                synchronized (this) {
                    data = generator.generateChunkData(this.world.getWorld(), this.random, x, z, biomegrid);
                }
            }
        } catch (UnsupportedOperationException exception) {
            newApi = true;
            return;
        }

        Preconditions.checkArgument(data instanceof OldCraftChunkData, "Plugins must use createChunkData(World) rather than implementing ChunkData: %s", data);
        OldCraftChunkData craftData = (OldCraftChunkData) data;
        LevelChunkSection[] sections = craftData.getRawChunkData();

        LevelChunkSection[] csect = ichunkaccess.getSections();
        int scnt = Math.min(csect.length, sections.length);

        // Loop through returned sections
        for (int sec = 0; sec < scnt; sec++) {
            if (sections[sec] == null) {
                continue;
            }
            LevelChunkSection section = sections[sec];

            csect[sec] = section;
        }

        // Set biome grid
        ((ProtoChunk) ichunkaccess).setBiomes(biomegrid.biome);

        if (craftData.getTiles() != null) {
            for (BlockPos pos : craftData.getTiles()) {
                int tx = pos.getX();
                int ty = pos.getY();
                int tz = pos.getZ();
                net.minecraft.world.level.block.state.BlockState block = craftData.getTypeId(tx, ty, tz);

                if (block.hasBlockEntity()) {
                    BlockEntity tile = ((EntityBlock) block.getBlock()).newBlockEntity(new BlockPos((x << 4) + tx, ty, (z << 4) + tz), block);
                    ichunkaccess.setBlockEntity(tile);
                }
            }
        }

        // Apply captured light blocks
        for (BlockPos lightPosition : craftData.getLights()) {
            ((ProtoChunk) ichunkaccess).addLight(new BlockPos((x << 4) + lightPosition.getX(), lightPosition.getY(), (z << 4) + lightPosition.getZ())); // PAIL rename addLightBlock
        }
    }

    @Override
    public void createStructures(long seed, BiomeManager biomemanager, ChunkAccess ichunkaccess, GenerationStep.Carving worldgenstage_features) {
        if (generator.shouldGenerateCaves()) {
            super.doCarving(seed, biomemanager, ichunkaccess, worldgenstage_features);
        }

        if (worldgenstage_features == GenerationStep.Carving.LIQUID) { // stage check ensures that the method is only called once
            CraftChunkData chunkData = new CraftChunkData(this.world.getWorld(), ichunkaccess);
            WorldgenRandom random = new WorldgenRandom();
            int x = ichunkaccess.getPos().x;
            int z = ichunkaccess.getPos().z;
            random.setBaseChunkSeed(seed, 0, 0); // PAIL rename carvingSeeded

            generator.generateCaves(this.world.getWorld(), random, x, z, chunkData);
            chunkData.breakLink();
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> createStructures(Executor executor, StructureFeatureManager structuremanager, ChunkAccess ichunkaccess) {
        CompletableFuture<ChunkAccess> future = null;
        if (generator.shouldGenerateNoise()) {
            future = delegate.createStructures(executor, structuremanager, ichunkaccess);
        }

        java.util.function.Function<ChunkAccess, ChunkAccess> function = (ichunkaccess1) -> {
            CraftChunkData chunkData = new CraftChunkData(this.world.getWorld(), ichunkaccess1);
            WorldgenRandom random = new WorldgenRandom();
            int x = ichunkaccess1.getPos().x;
            int z = ichunkaccess1.getPos().z;
            random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

            generator.generateNoise(this.world.getWorld(), random, x, z, chunkData);
            chunkData.breakLink();
            return ichunkaccess1;
        };

        return future == null ? CompletableFuture.supplyAsync(() -> function.apply(ichunkaccess), net.minecraft.Util.f()) : future.thenApply(function);
    }

    @Override
    public int getBaseHeight(int i, int j, Heightmap.Types heightmap_type, LevelHeightAccessor levelheightaccessor) {
        if (implementBaseHeight) {
            try {
                WorldgenRandom random = new WorldgenRandom();
                int xChunk = i >> 4;
                int zChunk = j >> 4;
                random.setSeed((long) xChunk * 341873128712L + (long) zChunk * 132897987541L);

                return generator.getBaseHeight(this.world.getWorld(), random, i, j, CraftHeightMap.fromNMS(heightmap_type));
            } catch (UnsupportedOperationException exception) {
                implementBaseHeight = false;
            }
        }

        return delegate.getBaseHeight(i, j, heightmap_type, levelheightaccessor);
    }

    @Override
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(net.minecraft.world.level.biome.Biome biomebase, StructureFeatureManager structuremanager, MobCategory enumcreaturetype, BlockPos blockposition) {
        return delegate.getMobsAt(biomebase, structuremanager, enumcreaturetype, blockposition);
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion regionlimitedworldaccess, StructureFeatureManager structuremanager) {
        super.addDecorations(regionlimitedworldaccess, structuremanager, generator.shouldGenerateDecorations());
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion regionlimitedworldaccess) {
        if (generator.shouldGenerateMobs()) {
            delegate.spawnOriginalMobs(regionlimitedworldaccess);
        }
    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor levelheightaccessor) {
        return delegate.getSpawnHeight(levelheightaccessor);
    }

    @Override
    public int getGenDepth() {
        return delegate.getGenDepth();
    }

    @Override
    public NoiseColumn getBaseColumn(int i, int j, LevelHeightAccessor levelheightaccessor) {
        return delegate.getBaseColumn(i, j, levelheightaccessor);
    }

    @Override
    protected Codec<? extends net.minecraft.world.level.chunk.ChunkGenerator> codec() {
        throw new UnsupportedOperationException("Cannot serialize CustomChunkGenerator");
    }
}
