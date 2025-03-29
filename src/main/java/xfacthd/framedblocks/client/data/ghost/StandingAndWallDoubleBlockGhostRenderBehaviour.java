package xfacthd.framedblocks.client.data.ghost;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.camo.CamoList;

public final class StandingAndWallDoubleBlockGhostRenderBehaviour extends StandingAndWallBlockGhostRenderBehaviour
{
    @Override
    public ModelData buildModelData(ItemStack stack, @Nullable ItemStack proxiedStack, BlockPlaceContext ctx, BlockState renderState, int renderPass, CamoList camo)
    {
        return DoubleBlockGhostRenderBehaviour.buildModelData(renderState, camo);
    }
}
