package org.bukkit.craftbukkit.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.ResourceLocation;
import org.bukkit.Fluid;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftFluidTag extends CraftTag<net.minecraft.fluid.Fluid, Fluid> {

    public CraftFluidTag(ITagCollection<net.minecraft.fluid.Fluid> registry, ResourceLocation tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(Fluid fluid) {
        return getHandle().contains(CraftMagicNumbers.getFluid(fluid));
    }

    @Override
    public Set<Fluid> getValues() {
        return Collections.unmodifiableSet(getHandle().getValues().stream().map(CraftMagicNumbers::getFluid).collect(Collectors.toSet()));
    }
}
