package xfacthd.framedblocks.common.blockentity.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.blueprint.AuxBlueprintData;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.component.PottedFlower;

import java.util.List;
import java.util.Optional;

public class FramedFlowerPotBlockEntity extends FramedBlockEntity
{
    public static final ModelProperty<Block> FLOWER_BLOCK = new ModelProperty<>();

    private Block flowerBlock = Blocks.AIR;

    public FramedFlowerPotBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_FLOWER_POT.value(), pos, state);
    }

    public void setFlowerBlock(Block flowerBlock)
    {
        if (flowerBlock != this.flowerBlock)
        {
            this.flowerBlock = flowerBlock;

            setChangedWithoutSignalUpdate();
            level().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean hasFlowerBlock()
    {
        return flowerBlock != Blocks.AIR;
    }

    public Block getFlowerBlock()
    {
        return flowerBlock;
    }

    @Override
    public void addAdditionalDrops(List<ItemStack> drops, boolean dropCamo)
    {
        super.addAdditionalDrops(drops, dropCamo);
        if (flowerBlock != Blocks.AIR)
        {
            drops.add(new ItemStack(flowerBlock));
        }
    }

    @Override
    protected void attachAdditionalModelData(ModelData.Builder builder)
    {
        builder.with(FLOWER_BLOCK, flowerBlock);
    }

    @Override
    protected void writeToDataPacket(ValueOutput valueOutput)
    {
        super.writeToDataPacket(valueOutput);
        valueOutput.putString("flower", BuiltInRegistries.BLOCK.getKey(flowerBlock).toString());
    }

    @Override
    protected boolean readFromDataPacket(ValueInput valueInput)
    {
        Block flower = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(valueInput.getStringOr("flower", "")));

        boolean update = flower != flowerBlock;
        if (update)
        {
            flowerBlock = flower;
        }

        return super.readFromDataPacket(valueInput) || update;
    }

    @Override
    protected void writeUpdateTag(ValueOutput valueOutput)
    {
        super.writeUpdateTag(valueOutput);
        valueOutput.putString("flower", BuiltInRegistries.BLOCK.getKey(flowerBlock).toString());
    }

    @Override
    public void handleUpdateTag(ValueInput valueInput)
    {
        super.handleUpdateTag(valueInput);

        Block flower = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(valueInput.getStringOr("flower", "")));
        if (flower != flowerBlock)
        {
            flowerBlock = flower;
        }
    }

    @Override
    protected Optional<AuxBlueprintData<?>> collectAuxBlueprintData()
    {
        return Optional.of(new PottedFlower(flowerBlock));
    }

    @Override
    protected void applyAuxDataFromBlueprint(AuxBlueprintData<?> auxData)
    {
        if (auxData instanceof PottedFlower flower && !flower.isEmpty())
        {
            flowerBlock = flower.flower();
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput valueOutput)
    {
        super.removeComponentsFromTag(valueOutput);
        valueOutput.discard("flower");
    }

    @Override
    protected void collectMiscComponents(DataComponentMap.Builder builder)
    {
        if (hasFlowerBlock())
        {
            builder.set(FBContent.DC_TYPE_POTTED_FLOWER, new PottedFlower(flowerBlock));
        }
    }

    @Override
    protected void applyMiscComponents(DataComponentGetter input)
    {
        PottedFlower flower = input.getOrDefault(FBContent.DC_TYPE_POTTED_FLOWER, PottedFlower.EMPTY);
        if (!flower.isEmpty())
        {
            flowerBlock = flower.flower();
        }
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput)
    {
        valueOutput.putString("flower", BuiltInRegistries.BLOCK.getKey(flowerBlock).toString());
        super.saveAdditional(valueOutput);
    }

    @Override
    public void loadAdditional(ValueInput valueInput)
    {
        super.loadAdditional(valueInput);
        flowerBlock = BuiltInRegistries.BLOCK.getValue(ResourceLocation.tryParse(valueInput.getStringOr("flower", "")));
    }
}
