package xfacthd.framedblocks.common.net.payload;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.crafting.saw.FramingSawRecipe;
import xfacthd.framedblocks.common.crafting.saw.FramingSawRecipeCache;

import java.util.List;

public record ClientboundFramingSawRecipesPayload(List<RecipeHolder<FramingSawRecipe>> recipes) implements CustomPacketPayload
{
    public static final Type<ClientboundFramingSawRecipesPayload> TYPE = Utils.payloadType("framing_saw_recipes");
    private static final StreamCodec<RegistryFriendlyByteBuf, RecipeHolder<FramingSawRecipe>> HOLDER_STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.RECIPE),
            RecipeHolder::id,
            FramingSawRecipe.STREAM_CODEC,
            RecipeHolder::value,
            RecipeHolder::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundFramingSawRecipesPayload> STREAM_CODEC =
            HOLDER_STREAM_CODEC.apply(ByteBufCodecs.list())
                    .map(ClientboundFramingSawRecipesPayload::new, ClientboundFramingSawRecipesPayload::recipes);

    public void handle(@SuppressWarnings("unused") IPayloadContext ctx)
    {
        FramingSawRecipeCache.get(true).update(recipes);
    }

    @Override
    public Type<ClientboundFramingSawRecipesPayload> type()
    {
        return TYPE;
    }
}
