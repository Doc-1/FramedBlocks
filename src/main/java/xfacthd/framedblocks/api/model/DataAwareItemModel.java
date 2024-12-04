package xfacthd.framedblocks.api.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.DelegateBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public final class DataAwareItemModel extends DelegateBakedModel
{
    private final ModelData itemData;
    private final RenderType blockRenderType;
    private final RenderType itemRenderType;

    public DataAwareItemModel(BakedModel baseModel, ModelData itemData, RenderType renderType)
    {
        super(baseModel);
        this.itemData = itemData;
        this.blockRenderType = renderType;
        this.itemRenderType = RenderTypeHelper.getEntityRenderType(renderType);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType)
    {
        if (this.blockRenderType == renderType)
        {
            try
            {
                return parent.getQuads(state, side, rand, itemData, renderType);
            }
            catch (Throwable t)
            {
                return ErrorModel.get().getQuads(state, side, rand, data, renderType);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand)
    {
        return getQuads(state, side, rand, itemData, blockRenderType);
    }

    @Override
    public RenderType getRenderType(ItemStack stack)
    {
        return itemRenderType;
    }
}
