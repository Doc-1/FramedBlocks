package io.github.xfacthd.framedblocks.common.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xfacthd.framedblocks.common.data.property.NullableDirection;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

public record CollapsibleBlockData(NullableDirection collapsedFace, int offsets)
{
    public static final Codec<CollapsibleBlockData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            NullableDirection.CODEC.fieldOf("collapsed_face").forGetter(CollapsibleBlockData::collapsedFace),
            Codec.INT.fieldOf("offsets").forGetter(CollapsibleBlockData::offsets)
    ).apply(inst, CollapsibleBlockData::new));
    public static final StreamCodec<FriendlyByteBuf, CollapsibleBlockData> STREAM_CODEC = StreamCodec.composite(
            NullableDirection.STREAM_CODEC,
            CollapsibleBlockData::collapsedFace,
            ByteBufCodecs.VAR_INT,
            CollapsibleBlockData::offsets,
            CollapsibleBlockData::new
    );
    public static final CollapsibleBlockData EMPTY = new CollapsibleBlockData(NullableDirection.NONE, 0);

    public CollapsibleBlockData(@Nullable Direction collapsedFace, int offsets)
    {
        this(NullableDirection.fromDirection(collapsedFace), offsets);
    }
}
