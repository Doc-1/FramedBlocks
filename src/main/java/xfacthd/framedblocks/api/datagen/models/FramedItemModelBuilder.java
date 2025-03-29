package xfacthd.framedblocks.api.datagen.models;

import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.internal.InternalClientAPI;
import xfacthd.framedblocks.api.model.item.tint.DynamicItemTintProvider;
import xfacthd.framedblocks.api.model.item.tint.FramedBlockItemTintProvider;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public final class FramedItemModelBuilder
{
    private final Holder<Block> block;
    @Nullable
    private DynamicItemTintProvider tintProvider = null;
    private ResourceLocation itemBaseModel = AbstractFramedBlockModelProvider.FRAMED_CUBE_MODEL;

    FramedItemModelBuilder(Holder<Block> block)
    {
        Preconditions.checkArgument(
                block.value() instanceof IFramedBlock,
                "Expected IFramedBlock, got %s", block.value()
        );
        Preconditions.checkArgument(
                ((IFramedBlock) block.value()).getItemModelSource() != null,
                "Framed block %s does not provide an item model source state", block.value()
        );
        this.block = block;
    }

    /**
     * Specify the {@link DynamicItemTintProvider} to use for tint computation
     */
    public FramedItemModelBuilder tintProvider(DynamicItemTintProvider tintProvider)
    {
        this.tintProvider = tintProvider;
        return this;
    }

    /**
     * Specify the model from which the {@link ItemTransforms} should be pulled
     */
    public FramedItemModelBuilder itemBaseModel(ResourceLocation itemBaseModel)
    {
        this.itemBaseModel = itemBaseModel;
        return this;
    }

    public ItemModel.Unbaked build()
    {
        if (tintProvider == null)
        {
            tintProvider = FramedBlockItemTintProvider.of((IFramedBlock) block.value());
        }
        return InternalClientAPI.INSTANCE.createFramedBlockItemModel(block.value(), tintProvider, itemBaseModel);
    }
}
