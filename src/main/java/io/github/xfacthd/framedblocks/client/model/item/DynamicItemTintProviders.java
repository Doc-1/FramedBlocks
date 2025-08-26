package io.github.xfacthd.framedblocks.client.model.item;

import com.mojang.serialization.Codec;
import io.github.xfacthd.framedblocks.api.model.item.tint.DynamicItemTintProvider;
import io.github.xfacthd.framedblocks.api.model.item.tint.RegisterItemTintProvidersEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.fml.ModLoader;

public final class DynamicItemTintProviders
{
    private static final ExtraCodecs.LateBoundIdMapper<ResourceLocation, DynamicItemTintProvider> TINT_PROVIDERS = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<DynamicItemTintProvider> CODEC = TINT_PROVIDERS.codec(ResourceLocation.CODEC);

    public static void init()
    {
        ModLoader.postEvent(new RegisterItemTintProvidersEvent(TINT_PROVIDERS::put));
    }



    private DynamicItemTintProviders() { }
}
