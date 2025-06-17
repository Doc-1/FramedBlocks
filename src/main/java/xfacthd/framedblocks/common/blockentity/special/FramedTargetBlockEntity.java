package xfacthd.framedblocks.common.blockentity.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.blueprint.AuxBlueprintData;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.component.TargetColor;

import java.util.Optional;

public class FramedTargetBlockEntity extends FramedBlockEntity
{
    public static final DyeColor DEFAULT_COLOR = DyeColor.RED;

    private DyeColor overlayColor = DEFAULT_COLOR;

    public FramedTargetBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_TARGET.value(), pos, state);
    }

    public boolean setOverlayColor(DyeColor overlayColor)
    {
        if (this.overlayColor != overlayColor)
        {
            if (!level().isClientSide())
            {
                this.overlayColor = overlayColor;

                level().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                setChangedWithoutSignalUpdate();
            }

            return true;
        }
        return false;
    }

    public int getOverlayColor()
    {
        return overlayColor.getTextColor();
    }

    @Override
    protected void writeUpdateTag(ValueOutput valueOutput)
    {
        super.writeUpdateTag(valueOutput);
        valueOutput.putInt("overlay_color", overlayColor.getId());
    }

    @Override
    public void handleUpdateTag(ValueInput valueInput)
    {
        super.handleUpdateTag(valueInput);
        overlayColor = DyeColor.byId(valueInput.getIntOr("overlay_color", DEFAULT_COLOR.getId()));
    }

    @Override
    protected void writeToDataPacket(ValueOutput tag)
    {
        super.writeToDataPacket(tag);
        tag.putInt("overlay_color", overlayColor.getId());
    }

    @Override
    protected boolean readFromDataPacket(ValueInput valueInput)
    {
        boolean colored = false;
        Optional<Integer> optOverlayColor = valueInput.getInt("overlay_color");
        if (optOverlayColor.isPresent())
        {
            DyeColor color = DyeColor.byId(optOverlayColor.get());
            if (overlayColor != color)
            {
                overlayColor = color;
                colored = true;
            }
        }
        return super.readFromDataPacket(valueInput) || colored;
    }

    @Override
    protected Optional<AuxBlueprintData<?>> collectAuxBlueprintData()
    {
        return Optional.of(new TargetColor(overlayColor));
    }

    @Override
    protected void applyAuxDataFromBlueprint(AuxBlueprintData<?> auxData)
    {
        if (auxData instanceof TargetColor(DyeColor color))
        {
            overlayColor = color;
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput valueOutput)
    {
        super.removeComponentsFromTag(valueOutput);
        valueOutput.discard("overlay_color");
    }

    @Override
    protected void collectMiscComponents(DataComponentMap.Builder builder)
    {
        builder.set(FBContent.DC_TYPE_TARGET_COLOR, new TargetColor(overlayColor));
    }

    @Override
    protected void applyMiscComponents(DataComponentGetter input)
    {
        TargetColor color = input.getOrDefault(FBContent.DC_TYPE_TARGET_COLOR, TargetColor.DEFAULT);
        overlayColor = color.color();
    }

    @Override
    public void saveAdditional(ValueOutput tag)
    {
        super.saveAdditional(tag);
        tag.putInt("overlay_color", overlayColor.getId());
    }

    @Override
    public void loadAdditional(ValueInput tag)
    {
        super.loadAdditional(tag);
        overlayColor = DyeColor.byId(tag.getIntOr("overlay_color", DEFAULT_COLOR.getId()));
    }
}
