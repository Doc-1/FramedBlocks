package xfacthd.framedblocks.client.util;

import net.minecraft.client.Minecraft;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.item.AbstractFramedBlockItemModel;
import xfacthd.framedblocks.client.render.block.FramedChestRenderer;
import xfacthd.framedblocks.mixin.client.AccessorModelManager;

public final class FramedClientUtils
{
    public static void clearModelCaches()
    {
        AccessorModelManager modelManager = ((AccessorModelManager) Minecraft.getInstance().getModelManager());

        modelManager.framedblocks$getBakedBlockStateModels()
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

        FramedChestRenderer.clearModelCaches();
    }



    private FramedClientUtils() { }
}
