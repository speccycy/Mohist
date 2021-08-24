package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FluidState;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;

public class BlockStateListPopulator extends DummyGeneratorAccess {
    private final LevelAccessor world;
    private final LinkedHashMap<net.minecraft.core.BlockPos, CraftBlockState> list;

    public BlockStateListPopulator(LevelAccessor world) {
        this(world, new LinkedHashMap<>());
    }

    public BlockStateListPopulator(LevelAccessor world, LinkedHashMap<net.minecraft.core.BlockPos, CraftBlockState> list) {
        this.world = world;
        this.list = list;
    }

    @Override
    public net.minecraft.world.level.block.state.BlockState getBlockState(net.minecraft.core.BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle() : world.getBlockState(bp);
    }

    @Override
    public FluidState getFluidState(net.minecraft.core.BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle().getFluidState() : world.getFluidState(bp);
    }

    @Override
    public boolean setBlock(net.minecraft.core.BlockPos position, net.minecraft.world.level.block.state.BlockState data, int flag) {
        CraftBlockState state = (CraftBlockState) CraftBlock.at(world, position).getState();
        state.setFlag(flag);
        state.setData(data);
        // remove first to keep insertion order
        list.remove(position);
        list.put(position.immutable(), state);
        return true;
    }

    public void updateList() {
        for (BlockState state : list.values()) {
            state.update(true);
        }
    }

    public Set<net.minecraft.core.BlockPos> getBlocks() {
        return list.keySet();
    }

    public List<CraftBlockState> getList() {
        return new ArrayList<>(list.values());
    }

    public LevelAccessor getWorld() {
        return world;
    }

    // For tree generation
    @Override
    public int getMinBuildHeight() {
        return getWorld().getMinBuildHeight();
    }

    @Override
    public int getHeight() {
        return getWorld().getHeight();
    }

    @Override
    public boolean isStateAtPosition(BlockPos blockposition, Predicate<net.minecraft.world.level.block.state.BlockState> predicate) {
        return predicate.test(getBlockState(blockposition));
    }

    @Override
    public DimensionType dimensionType() {
        return world.dimensionType();
    }
}
