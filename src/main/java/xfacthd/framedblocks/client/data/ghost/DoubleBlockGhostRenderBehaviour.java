package xfacthd.framedblocks.client.data.ghost;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import xfacthd.framedblocks.api.camo.CamoList;
import xfacthd.framedblocks.api.ghost.GhostRenderBehaviour;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedDoubleBlockData;

public sealed class DoubleBlockGhostRenderBehaviour implements GhostRenderBehaviour permits AdjustableDoubleBlockGhostRenderBehaviour
{
    @Override
    public ModelData buildModelData(ItemStack stack, @Nullable ItemStack proxiedStack, BlockPlaceContext ctx, BlockState renderState, int renderPass, CamoList camo)
    {
        return buildModelData(renderState, camo);
    }

    public static ModelData buildModelData(BlockState state, CamoList camo)
    {
        return ModelData.of(AbstractFramedBlockData.PROPERTY, new FramedDoubleBlockData(
                ((IFramedDoubleBlock) state.getBlock()).getCache(state).getParts(),
                new FramedBlockData(camo.getCamo(0), false),
                new FramedBlockData(camo.getCamo(1), true)
        ));
    }
}
