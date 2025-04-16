package xfacthd.framedblocks.client.data.ghost;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.ghost.GhostRenderBehaviour;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.PropertyHolder;

public final class BoardGhostRenderBehaviour implements GhostRenderBehaviour
{
    @Override
    @Nullable
    public BlockState getRenderState(
            ItemStack stack,
            @Nullable ItemStack proxiedStack,
            BlockHitResult hit,
            BlockPlaceContext ctx,
            BlockState hitState,
            int renderPass
    )
    {
        BlockState renderState = GhostRenderBehaviour.super.getRenderState(stack, proxiedStack, hit, ctx, hitState, renderPass);
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (renderState != null && state.getBlock() == FBContent.BLOCK_FRAMED_BOARD.value())
        {
            int faces = renderState.getValue(PropertyHolder.FACES);
            faces &= ~state.getValue(PropertyHolder.FACES);
            return faces == 0 ? null : renderState.setValue(PropertyHolder.FACES, faces);
        }
        return renderState;
    }
}
