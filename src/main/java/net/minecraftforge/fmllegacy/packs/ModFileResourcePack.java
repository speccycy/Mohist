/*
 * Minecraft Forge
 * Copyright (c) 2016-2021.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.fmllegacy.packs;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.PackType;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathResourcePack;

import javax.annotation.Nonnull;

@Deprecated(since="1.18", forRemoval = true) // TODO 1.18: Replace usages with PathResourcePack
public class ModFileResourcePack extends PathResourcePack
{
    private final IModFile modFile;
    private Pack packInfo;

    public ModFileResourcePack(final IModFile modFile)
    {
        super(modFile.getFileName(), modFile.getFilePath());
        this.modFile = modFile;
    }

    public IModFile getModFile() {
        return this.modFile;
    }

    @Nonnull
    @Override
    protected Path resolve(@Nonnull String... paths)
    {
        return modFile.findResource(paths);
    }

    @Override
    public String toString()
    {
        return String.format("%s: %s", getClass().getName(), getModFile().getFileName());
    }

    public InputStream getResource(PackType type, ResourceLocation location) throws IOException {
        if (location.getPath().startsWith("lang/")) {
            return super.getResource(PackType.CLIENT_RESOURCES, location);
        } else {
            return super.getResource(type, location);
        }
    }

    public boolean hasResource(PackType type, ResourceLocation location) {
        if (location.getPath().startsWith("lang/")) {
            return super.hasResource(PackType.CLIENT_RESOURCES, location);
        } else {
            return super.hasResource(type, location);
        }
    }

    @Override
    public void close()
    {

    }

    <T extends Pack> void setPackInfo(final T packInfo) {
        this.packInfo = packInfo;
    }

    <T extends Pack> T getPackInfo() {
        return (T)this.packInfo;
    }
}
