package xfacthd.framedblocks.common.crafting.saw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public final class FramingSawRecipeSerializer implements RecipeSerializer<FramingSawRecipe>
{
    private static final MapCodec<FramingSawRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("material").forGetter(FramingSawRecipe::getMaterialAmount),
            FramingSawRecipeAdditive.CODEC.sizeLimitedListOf(FramingSawRecipe.MAX_ADDITIVE_COUNT).optionalFieldOf("additives", List.of()).forGetter(FramingSawRecipe::getAdditives),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(FramingSawRecipe::getResult),
            Codec.BOOL.optionalFieldOf("disabled", false).forGetter(FramingSawRecipe::isDisabled)
    ).apply(inst, FramingSawRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FramingSawRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            FramingSawRecipe::getMaterialAmount,
            FramingSawRecipeAdditive.STREAM_CODEC.apply(ByteBufCodecs.list()),
            FramingSawRecipe::getAdditives,
            ItemStack.STREAM_CODEC,
            FramingSawRecipe::getResult,
            ByteBufCodecs.BOOL,
            FramingSawRecipe::isDisabled,
            FramingSawRecipe::new
    );

    @Override
    public MapCodec<FramingSawRecipe> codec()
    {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FramingSawRecipe> streamCodec()
    {
        return STREAM_CODEC;
    }
}
