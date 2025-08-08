package xfacthd.framedblocks.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelManager;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.item.AbstractFramedBlockItemModel;
import xfacthd.framedblocks.client.model.unbaked.UnbakedStandaloneFramedBlockModel;
import xfacthd.framedblocks.common.data.camo.fluid.FluidCamoClientHandler;

public final class FramedClientUtils
{
    public static void clearModelCaches()
    {
        ModelManager modelManager = Minecraft.getInstance().getModelManager();

        modelManager.getBlockModelShaper()
                .framedblocks$getModelByStateCache()
                .values()
                .stream()
                .filter(AbstractFramedBlockModel.class::isInstance)
                .map(AbstractFramedBlockModel.class::cast)
                .forEach(AbstractFramedBlockModel::clearCache);

        modelManager.framedblocks$getBakedItemStackModels()
                .values()
                .stream()
                .filter(AbstractFramedBlockItemModel.class::isInstance)
                .map(AbstractFramedBlockItemModel.class::cast)
                .forEach(AbstractFramedBlockItemModel::clearCache);

        UnbakedStandaloneFramedBlockModel.clearCaches();

        FluidCamoClientHandler.clearModelCache();
    }



    private FramedClientUtils() { }
}
