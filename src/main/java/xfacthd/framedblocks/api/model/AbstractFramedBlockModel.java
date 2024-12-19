package xfacthd.framedblocks.api.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.DelegateBakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.model.wrapping.itemmodel.ItemModelInfo;
import xfacthd.framedblocks.api.util.CamoList;
import xfacthd.framedblocks.api.util.ConfigView;
import xfacthd.framedblocks.api.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractFramedBlockModel extends DelegateBakedModel
{
    //private final List<BakedModel> defaultItemRenderPass = List.of(new ItemModel.BakedModelWithOverrides(this, BakedOverrides.EMPTY));
    private final BlockState state;
    private final ItemModelInfo itemModelInfo;
    private final boolean dataRequired;
    private final Map<CamoList, List<BakedModel>> itemModelCache;

    protected AbstractFramedBlockModel(BakedModel baseModel, BlockState state, ItemModelInfo itemModelInfo)
    {
        super(baseModel);
        this.state = state;
        boolean isItemModel = state.getBlock() instanceof IFramedBlock block && block.getItemModelSource() == state;
        this.itemModelInfo = isItemModel ? itemModelInfo : null;
        this.dataRequired = isItemModel && itemModelInfo.isDataRequired();
        this.itemModelCache = isItemModel ? new ConcurrentHashMap<>() : null;
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

    // TODO: replace with custom ItemModel
    /*@Override
    public List<BakedModel> getRenderPasses(ItemStack stack)
    {
        boolean showCamo = ConfigView.Client.INSTANCE.shouldRenderItemModelsWithCamo();
        if (itemModelInfo == null || (!dataRequired && !showCamo))
        {
            return defaultItemRenderPass;
        }

        CamoList camos = showCamo ? stack.getOrDefault(Utils.DC_TYPE_CAMO_LIST, CamoList.EMPTY) : CamoList.EMPTY;
        if (!dataRequired && camos.isEmpty()) return defaultItemRenderPass;

        List<BakedModel> models = itemModelCache.get(camos);
        if (models == null)
        {
            ModelData data = itemModelInfo.buildItemModelData(state, camos);
            models = new ArrayList<>();
            for (RenderType renderType : getRenderTypes(state, RandomSource.create(), data))
            {
                models.add(new DataAwareItemModel(this, data, renderType));
            }
            itemModelCache.put(camos, models);
        }
        return models;
    }*/

    public void clearCache()
    {
        if (itemModelCache != null)
        {
            itemModelCache.clear();
        }
    }
}
