package xfacthd.framedblocks.client.data.ghost;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.blockentity.PackedCollapsibleBlockOffsets;
import xfacthd.framedblocks.common.blockentity.doubled.slab.FramedAdjustableDoubleBlockEntity;
import xfacthd.framedblocks.common.data.component.AdjustableDoubleBlockData;

public final class AdjustableDoubleBlockGhostRenderBehaviour extends DoubleBlockGhostRenderBehaviour
{
    private final FramedAdjustableDoubleBlockEntity.OffsetPacker offsetPacker;

    private AdjustableDoubleBlockGhostRenderBehaviour(FramedAdjustableDoubleBlockEntity.OffsetPacker offsetPacker)
    {
        this.offsetPacker = offsetPacker;
    }

    @Override
    public ModelData appendModelData(ItemStack stack, @Nullable ItemStack proxiedStack, BlockPlaceContext ctx, BlockState renderState, int renderPass, ModelData data)
    {
        AdjustableDoubleBlockData blockData = stack.get(FBContent.DC_TYPE_ADJ_DOUBLE_BLOCK_DATA);
        int firstHeight = blockData != null ? blockData.firstHeight() : FramedAdjustableDoubleBlockEntity.CENTER_PART_HEIGHT;
        return data.derive().with(PackedCollapsibleBlockOffsets.PROPERTY, offsetPacker.packDouble(renderState, firstHeight)).build();
    }



    public static AdjustableDoubleBlockGhostRenderBehaviour standard()
    {
        return new AdjustableDoubleBlockGhostRenderBehaviour(
                FramedAdjustableDoubleBlockEntity::getPackedOffsetsStandard
        );
    }

    public static AdjustableDoubleBlockGhostRenderBehaviour copycat()
    {
        return new AdjustableDoubleBlockGhostRenderBehaviour(
                FramedAdjustableDoubleBlockEntity::getPackedOffsetsCopycat
        );
    }
}
