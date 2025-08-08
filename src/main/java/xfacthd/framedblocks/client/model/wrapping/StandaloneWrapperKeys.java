package xfacthd.framedblocks.client.model.wrapping;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import xfacthd.framedblocks.api.model.standalone.StandaloneWrapperKey;

public final class StandaloneWrapperKeys
{
    private static final ExtraCodecs.LateBoundIdMapper<ResourceLocation, StandaloneWrapperKey<?>> REGISTRY = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<StandaloneWrapperKey<?>> CODEC = REGISTRY.codec(ResourceLocation.CODEC);

    static void registerKey(StandaloneWrapperKey<?> wrapperKey)
    {
        REGISTRY.put(wrapperKey.definitionFile(), wrapperKey);
    }

    private StandaloneWrapperKeys() {}
}
