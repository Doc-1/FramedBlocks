package io.github.xfacthd.framedblocks.common.blockentity.special;

import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.capability.fluid.TankFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FramedTankBlockEntity extends FramedBlockEntity
{
    private final TankFluidHandler fluidHandler = new TankFluidHandler(this);

    public FramedTankBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_TANK.value(), pos, state);
    }

    public InteractionResult handleTankInteraction(Player player, InteractionHand hand)
    {
        if (FluidUtil.interactWithFluidHandler(player, hand, fluidHandler))
        {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    public FluidStack getContents()
    {
        return fluidHandler.getFluid();
    }

    public IFluidHandler getFluidHandler()
    {
        return fluidHandler;
    }

    public void onTankContentsChanged()
    {
        if (level == null || level.isClientSide()) return;

        setChanged();
        if (!getBlockState().getValue(FramedProperties.SOLID))
        {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public int getAnalogSignal()
    {
        return fluidHandler.getFluid().getAmount() * 15 / TankFluidHandler.CAPACITY;
    }

    @Override
    protected void writeUpdateTag(ValueOutput valueOutput)
    {
        super.writeUpdateTag(valueOutput);
        fluidHandler.serialize(valueOutput);
    }

    @Override
    public void handleUpdateTag(ValueInput valueInput)
    {
        super.handleUpdateTag(valueInput);
        fluidHandler.deserialize(valueInput);
    }

    @Override
    protected void writeToDataPacket(ValueOutput valueOutput)
    {
        super.writeToDataPacket(valueOutput);
        fluidHandler.serialize(valueOutput);
    }

    @Override
    protected boolean readFromDataPacket(ValueInput valueInput)
    {
        fluidHandler.deserialize(valueInput);
        return super.readFromDataPacket(valueInput);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput valueOutput)
    {
        super.removeComponentsFromTag(valueOutput);
        valueOutput.discard(TankFluidHandler.FLUID_NBT_KEY);
    }

    @Override
    protected void applyMiscComponents(DataComponentGetter input)
    {
        FluidStack contents = input.getOrDefault(FBContent.DC_TYPE_TANK_CONTENTS, SimpleFluidContent.EMPTY).copy();
        if (!contents.isEmpty())
        {
            fluidHandler.setFluid(contents);
        }
    }

    @Override
    protected void collectMiscComponents(DataComponentMap.Builder builder)
    {
        FluidStack contents = fluidHandler.getFluid();
        if (!contents.isEmpty())
        {
            builder.set(FBContent.DC_TYPE_TANK_CONTENTS, SimpleFluidContent.copyOf(contents));
        }
    }

    @Override
    public void loadAdditional(ValueInput valueInput)
    {
        super.loadAdditional(valueInput);
        fluidHandler.deserialize(valueInput);
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput)
    {
        super.saveAdditional(valueOutput);
        fluidHandler.serialize(valueOutput);
    }
}
