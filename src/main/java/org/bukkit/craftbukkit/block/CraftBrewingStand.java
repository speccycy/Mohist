package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;

public class CraftBrewingStand extends CraftContainer<BrewingStandBlockEntity> implements BrewingStand {

    public CraftBrewingStand(World world, final BrewingStandBlockEntity te) {
        super(world, te);
    }

    @Override
    public BrewerInventory getSnapshotInventory() {
        return new CraftInventoryBrewer(this.getSnapshot());
    }

    @Override
    public BrewerInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryBrewer(this.getTileEntity());
    }

    @Override
    public int getBrewingTime() {
        return this.getSnapshot().brewTime;
    }

    @Override
    public void setBrewingTime(int brewTime) {
        this.getSnapshot().brewTime = brewTime;
    }

    @Override
    public int getFuelLevel() {
        return this.getSnapshot().fuel;
    }

    @Override
    public void setFuelLevel(int level) {
        this.getSnapshot().fuel = level;
    }
}
