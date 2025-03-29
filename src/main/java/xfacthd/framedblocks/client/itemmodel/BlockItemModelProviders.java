package xfacthd.framedblocks.client.itemmodel;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.fml.ModLoader;
import xfacthd.framedblocks.api.model.item.block.BlockItemModelProvider;
import xfacthd.framedblocks.api.model.item.block.RegisterBlockItemModelProvidersEvent;

public final class BlockItemModelProviders
{
    private static final ExtraCodecs.LateBoundIdMapper<ResourceLocation, BlockItemModelProvider> MODEL_PROVIDERS = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<BlockItemModelProvider> CODEC = MODEL_PROVIDERS.codec(ResourceLocation.CODEC);

    public static void init()
    {
        ModLoader.postEvent(new RegisterBlockItemModelProvidersEvent(MODEL_PROVIDERS::put));
    }



    private BlockItemModelProviders() { }
}
