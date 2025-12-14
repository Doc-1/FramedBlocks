package io.github.xfacthd.framedblocks.api.util;

import io.github.xfacthd.framedblocks.api.camo.CamoContainerFactory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

public final class FramedConstants
{
    public static final String MOD_ID = "framedblocks";
    public static final Identifier CAMO_CONTAINER_FACTORY_REGISTRY_NAME = Utils.id("camo_containers");
    public static final ResourceKey<Registry<CamoContainerFactory<?>>> CAMO_CONTAINER_FACTORY_REGISTRY_KEY = ResourceKey.createRegistryKey(CAMO_CONTAINER_FACTORY_REGISTRY_NAME);

    private FramedConstants() { }
}
