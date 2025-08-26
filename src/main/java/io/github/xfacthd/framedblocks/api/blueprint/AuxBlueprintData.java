package io.github.xfacthd.framedblocks.api.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.xfacthd.framedblocks.api.FramedBlocksAPI;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public interface AuxBlueprintData<T extends AuxBlueprintData<T>> extends TooltipProvider
{
    Registry<Type<?>> REGISTRY = FramedBlocksAPI.INSTANCE.getAuxBlueprintDataTypeRegistry();
    Codec<AuxBlueprintData<?>> CODEC = REGISTRY.byNameCodec().dispatch(AuxBlueprintData::type, Type::codec);
    StreamCodec<RegistryFriendlyByteBuf, AuxBlueprintData<?>> STREAM_CODEC = ByteBufCodecs.registry(REGISTRY.key())
            .dispatch(AuxBlueprintData::type, Type::streamCodec);

    Type<T> type();

    int hashCode();

    boolean equals(Object other);

    @Override
    default void addToTooltip(Item.TooltipContext context, Consumer<Component> appender, TooltipFlag tooltipFlag, DataComponentGetter componentGetter) { }



    record Type<T extends AuxBlueprintData<?>>(MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) { }
}
