package io.github.xfacthd.framedblocks.common.compat.create.schematic.requirements;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import io.github.xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import io.github.xfacthd.framedblocks.api.compat.create.FramedBlockEntityItemRequirement;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedItemFrameBlockEntity;
import net.createmod.catnip.components.ComponentProcessors;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class FramedItemFrameBlockEntityItemRequirement extends FramedBlockEntityItemRequirement
{
    @Override
    protected void collectAdditionalRequirements(FramedBlockEntity blockEntity, List<ItemRequirement.StackRequirement> requirements)
    {
        if (blockEntity instanceof FramedItemFrameBlockEntity itemFrame && itemFrame.hasItem())
        {
            ItemStack stack = ComponentProcessors.withUnsafeComponentsDiscarded(itemFrame.getItem().copy());
            requirements.add(consumeStrict(stack));
        }
    }
}
