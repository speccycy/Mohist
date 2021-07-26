package org.bukkit.craftbukkit.util;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EmptyTickList;
import net.minecraft.world.ITickList;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;

public class DummyGeneratorAccess implements IWorld {

    public static final IWorld INSTANCE = new DummyGeneratorAccess();

    protected DummyGeneratorAccess() {
    }

    @Override
    public ITickList<Block> getBlockTicks() {
        return EmptyTickList.empty();
    }

    @Override
    public ITickList<Fluid> getLiquidTicks() {
        return EmptyTickList.empty();
    }

    @Override
    public IWorldInfo getLevelData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public AbstractChunkProvider getChunkSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Random getRandom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void playSound(PlayerEntity entityhuman, BlockPos blockposition, SoundEvent soundeffect, SoundCategory soundcategory, float f, float f1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParticle(IParticleData particleparam, double d0, double d1, double d2, double d3, double d4, double d5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void levelEvent(PlayerEntity entityhuman, int i, BlockPos blockposition, int j) {
        // Used by PowderSnowBlock.removeFluid
    }

    @Override
    public void a(Entity entity, GameEvent gameevent, BlockPos blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldServer getMinecraftWorld() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IRegistryCustom t() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getEntities(Entity entity, AxisAlignedBB aabb, Predicate<? super Entity> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Entity> List<T> a(EntityTypeTest<Entity, T> ett, AxisAlignedBB aabb, Predicate<? super T> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IChunkAccess getChunkAt(int i, int i1, ChunkStatus cs, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int a(HeightMap.Type type, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int n_() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BiomeManager r_() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BiomeBase a(int i, int i1, int i2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public int getSeaLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DimensionManager getDimensionManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float a(EnumDirection ed, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LightEngine k_() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TileEntity getTileEntity(BlockPosition blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IBlockData getType(BlockPosition blockposition) {
        return Blocks.AIR.getBlockData(); // SPIGOT-6515
    }

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        return FluidTypes.EMPTY.h(); // SPIGOT-6634
    }

    @Override
    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean a(BlockPosition bp, Predicate<IBlockData> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean b(BlockPosition bp, Predicate<Fluid> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean a(BlockPosition blockposition, IBlockData iblockdata, int i, int j) {
        return false;
    }

    @Override
    public boolean a(BlockPosition blockposition, boolean flag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean a(BlockPosition blockposition, boolean flag, Entity entity, int i) {
        return false; // SPIGOT-6515
    }
}
