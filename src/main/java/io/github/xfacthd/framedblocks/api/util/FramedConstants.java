package io.github.xfacthd.framedblocks.api.util;

import io.github.xfacthd.framedblocks.api.blueprint.AuxBlueprintData;
import io.github.xfacthd.framedblocks.api.camo.CamoContainerFactory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class FramedConstants
{
    public static final String MOD_ID = "framedblocks";
    public static final ResourceLocation CAMO_CONTAINER_FACTORY_REGISTRY_NAME = Utils.rl("camo_containers");
    public static final ResourceKey<Registry<CamoContainerFactory<?>>> CAMO_CONTAINER_FACTORY_REGISTRY_KEY = ResourceKey.createRegistryKey(CAMO_CONTAINER_FACTORY_REGISTRY_NAME);
    public static final ResourceLocation AUX_BLUEPRINT_DATA_TYPE_REGISTRY_NAME = Utils.rl("aux_blueprint_data");
    public static final ResourceKey<Registry<AuxBlueprintData.Type<?>>> AUX_BLUEPRINT_DATA_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(AUX_BLUEPRINT_DATA_TYPE_REGISTRY_NAME);



    private FramedConstants() { }
}
