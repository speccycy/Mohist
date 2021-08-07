package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;

public class BlockStateListPopulator extends DummyGeneratorAccess {
    private final World world;
    private final LinkedHashMap<BlockPos, CraftBlockState> list;

    public BlockStateListPopulator(World world) {
        this(world, new LinkedHashMap<>());
    }

    public BlockStateListPopulator(World world, LinkedHashMap<BlockPos, CraftBlockState> list) {
        this.world = world;
        this.list = list;
    }

    @Override
    public net.minecraft.block.BlockState getType(BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle() : world.getType(bp);
    }

    @Override
    public Fluid getFluid(BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle().getFluid() : world.getFluid(bp);
    }

    @Override
    public boolean setTypeAndData(BlockPos position, net.minecraft.block.BlockState data, int flag) {
        CraftBlockState state = CraftBlockState.getBlockState(world, position, flag);
        state.setData(data);
        // remove first to keep insertion order
        list.remove(position);
        list.put(position.immutableCopy(), state);
        return true;
    }

    public void updateList() {
        for (BlockState state : list.values()) {
            state.update(true);
        }
    }

    public Set<BlockPos> getBlocks() {
        return list.keySet();
    }

    public List<CraftBlockState> getList() {
        return new ArrayList<>(list.values());
    }

    public World getWorld() {
        return world;
    }
}
