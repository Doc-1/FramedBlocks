package io.github.xfacthd.framedblocks.common.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.xfacthd.framedblocks.api.blueprint.AuxBlueprintData;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CollapsibleCopycatBlockData(int offsets) implements AuxBlueprintData<CollapsibleCopycatBlockData>
{
    public static final Codec<CollapsibleCopycatBlockData> CODEC = Codec.INT
            .xmap(CollapsibleCopycatBlockData::new, CollapsibleCopycatBlockData::offsets);
    public static final MapCodec<CollapsibleCopycatBlockData> MAP_CODEC = CODEC.fieldOf("offsets");
    public static final StreamCodec<ByteBuf, CollapsibleCopycatBlockData> STREAM_CODEC = ByteBufCodecs.VAR_INT
            .map(CollapsibleCopycatBlockData::new, CollapsibleCopycatBlockData::offsets);
    public static final CollapsibleCopycatBlockData EMPTY = new CollapsibleCopycatBlockData(0);

    @Override
    public Type<CollapsibleCopycatBlockData> type()
    {
        return FBContent.AUX_TYPE_COLLAPSIBLE_COPYCAT_BLOCK_DATA.value();
    }
}
