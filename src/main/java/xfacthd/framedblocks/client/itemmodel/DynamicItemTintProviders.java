package xfacthd.framedblocks.client.itemmodel;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.fml.ModLoader;
import xfacthd.framedblocks.api.model.item.DynamicItemTintProvider;
import xfacthd.framedblocks.api.model.item.RegisterItemTintProvidersEvent;

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
