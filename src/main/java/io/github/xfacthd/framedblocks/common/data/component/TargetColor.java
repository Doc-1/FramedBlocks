package io.github.xfacthd.framedblocks.common.data.component;

import com.mojang.serialization.Codec;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedTargetBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;

public record TargetColor(DyeColor color)
{
    public static final Codec<TargetColor> CODEC = DyeColor.CODEC.xmap(TargetColor::new, TargetColor::color);
    public static final StreamCodec<ByteBuf, TargetColor> STREAM_CODEC = DyeColor.STREAM_CODEC.map(TargetColor::new, TargetColor::color);
    public static final TargetColor DEFAULT = new TargetColor(FramedTargetBlockEntity.DEFAULT_COLOR);
}
