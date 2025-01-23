package xfacthd.framedblocks.api.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.DelegateBakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.model.wrapping.itemmodel.ItemModelInfo;

public abstract class AbstractFramedBlockModel extends DelegateBakedModel
{
    @Nullable
    private final ItemModelInfo itemModelInfo;

    protected AbstractFramedBlockModel(BakedModel baseModel, BlockState state, ItemModelInfo itemModelInfo)
    {
        super(baseModel);
        boolean isItemModel = state.getBlock() instanceof IFramedBlock block && block.getItemModelSource() == state;
        this.itemModelInfo = isItemModel ? itemModelInfo : null;
    }

    @Override
    public void applyTransform(ItemDisplayContext ctx, PoseStack poseStack, boolean leftHand)
    {
        super.applyTransform(ctx, poseStack, leftHand);
        if (itemModelInfo != null)
        {
            itemModelInfo.applyItemTransform(poseStack, ctx, leftHand);
        }
    }

    public void clearCache() { }

    @Nullable
    public ItemModelInfo getItemModelInfo()
    {
        return itemModelInfo;
    }
}
