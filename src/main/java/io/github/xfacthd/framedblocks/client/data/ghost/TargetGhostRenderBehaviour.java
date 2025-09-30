package io.github.xfacthd.framedblocks.client.data.ghost;

import io.github.xfacthd.framedblocks.api.ghost.GhostRenderBehaviour;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedTargetBlockEntity;
import io.github.xfacthd.framedblocks.common.data.component.TargetColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

public final class TargetGhostRenderBehaviour implements GhostRenderBehaviour
{
    @Override
    public ModelData appendModelData(ItemStack stack, @Nullable ItemStack proxiedStack, BlockPlaceContext ctx, BlockState renderState, int renderPass, ModelData data)
    {
        TargetColor targetColor = stack.get(FBContent.DC_TYPE_TARGET_COLOR);
        DyeColor overlayColor = targetColor != null ? targetColor.color() : DyeColor.RED;
        return data.derive().with(FramedTargetBlockEntity.COLOR_PROPERTY, overlayColor).build();
    }
}
