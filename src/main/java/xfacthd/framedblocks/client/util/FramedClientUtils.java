package xfacthd.framedblocks.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelManager;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.item.AbstractFramedBlockItemModel;
import xfacthd.framedblocks.client.render.block.FramedChestRenderer;
import xfacthd.framedblocks.common.data.camo.fluid.FluidCamoClientHandler;
import xfacthd.framedblocks.mixin.client.AccessorBlockModelShaper;
import xfacthd.framedblocks.mixin.client.AccessorModelManager;

public final class FramedClientUtils
{
    public static void clearModelCaches()
    {
        ModelManager modelManager = Minecraft.getInstance().getModelManager();

        ((AccessorBlockModelShaper) modelManager.getBlockModelShaper())
                .framedblocks$getModelByStateCache()
                .values()
                .stream()
                .filter(AbstractFramedBlockModel.class::isInstance)
                .map(AbstractFramedBlockModel.class::cast)
                .forEach(AbstractFramedBlockModel::clearCache);

        ((AccessorModelManager) modelManager)
                .framedblocks$getBakedItemStackModels()
                .values()
                .stream()
                .filter(AbstractFramedBlockItemModel.class::isInstance)
                .map(AbstractFramedBlockItemModel.class::cast)
                .forEach(AbstractFramedBlockItemModel::clearCache);

        FramedChestRenderer.clearModelCaches();

        FluidCamoClientHandler.clearModelCache();
    }



    private FramedClientUtils() { }
}
