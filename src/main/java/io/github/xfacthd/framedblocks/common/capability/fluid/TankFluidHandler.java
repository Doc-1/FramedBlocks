package io.github.xfacthd.framedblocks.common.capability.fluid;

import io.github.xfacthd.framedblocks.common.blockentity.special.FramedTankBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public final class TankFluidHandler implements IFluidHandler, ValueIOSerializable
{
    public static final int CAPACITY = 16 * FluidType.BUCKET_VOLUME;
    public static final String FLUID_NBT_KEY = "fluid";

    private final FramedTankBlockEntity blockEntity;
    private FluidStack fluid = FluidStack.EMPTY;

    public TankFluidHandler(FramedTankBlockEntity blockEntity)
    {
        this.blockEntity = blockEntity;
    }

    @Override
    public int getTanks()
    {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return tank == 0 ? fluid : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return tank == 0 ? CAPACITY : 0;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack)
    {
        return tank == 0;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        if (!resource.isEmpty() && (fluid.isEmpty() || FluidStack.isSameFluidSameComponents(resource, fluid)))
        {
            int toFill = Math.min(CAPACITY - fluid.getAmount(), resource.getAmount());
            if (toFill > 0 && action.execute())
            {
                if (fluid.isEmpty())
                {
                    fluid = resource.copyWithAmount(toFill);
                }
                else
                {
                    fluid.grow(toFill);
                }
            }
            return toFill;
        }
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)
    {
        if (!resource.isEmpty() && FluidStack.isSameFluidSameComponents(resource, fluid))
        {
            return drain(resource.getAmount(), action);
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        if (maxDrain > 0 && !fluid.isEmpty())
        {
            int toDrain = Math.min(maxDrain, fluid.getAmount());
            if (toDrain > 0)
            {
                if (action.execute())
                {
                    if (toDrain == fluid.getAmount())
                    {
                        fluid = FluidStack.EMPTY;
                    }
                    else
                    {
                        fluid.shrink(toDrain);
                    }
                    onContentsChanged();
                }
                return fluid.copyWithAmount(toDrain);
            }
        }
        return FluidStack.EMPTY;
    }

    public void setFluid(FluidStack fluid)
    {
        this.fluid = fluid;
        onContentsChanged();
    }

    public FluidStack getFluid()
    {
        return fluid;
    }

    private void onContentsChanged()
    {
        blockEntity.onTankContentsChanged();
    }

    @Override
    public void deserialize(ValueInput valueInput)
    {
        fluid = valueInput.read(FLUID_NBT_KEY, FluidStack.OPTIONAL_CODEC).orElse(FluidStack.EMPTY);
    }

    @Override
    public void serialize(ValueOutput valueOutput)
    {
        if (!fluid.isEmpty())
        {
            valueOutput.store(FLUID_NBT_KEY, FluidStack.OPTIONAL_CODEC, fluid);
        }
    }
}
