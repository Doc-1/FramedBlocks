package io.github.xfacthd.framedblocks.api.ghost;

import io.github.xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import io.github.xfacthd.framedblocks.api.camo.CamoList;
import io.github.xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import io.github.xfacthd.framedblocks.api.model.data.FramedBlockData;
import io.github.xfacthd.framedblocks.api.model.data.FramedDoubleBlockData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

public interface DoubleBlockGhostRenderBehaviour extends GhostRenderBehaviour
{
    DoubleBlockGhostRenderBehaviour INSTANCE = new DoubleBlockGhostRenderBehaviour() {};

    @Override
    default ModelData buildModelData(ItemStack stack, @Nullable ItemStack proxiedStack, BlockPlaceContext ctx, BlockState renderState, int renderPass, CamoList camo)
    {
        return ModelData.of(AbstractFramedBlockData.PROPERTY, new FramedDoubleBlockData(
                ((IFramedDoubleBlock) renderState.getBlock()).getCache(renderState).getParts(),
                new FramedBlockData(camo.getCamo(0), false),
                new FramedBlockData(camo.getCamo(1), true)
        ));
    }
}
