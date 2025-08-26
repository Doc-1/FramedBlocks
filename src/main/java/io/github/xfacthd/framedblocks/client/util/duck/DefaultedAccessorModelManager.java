package io.github.xfacthd.framedblocks.client.util.duck;

import io.github.xfacthd.framedblocks.mixin.client.AccessorModelManager;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

@SuppressWarnings("unused") // Used via interface injection
public interface DefaultedAccessorModelManager extends AccessorModelManager
{
    @Override
    default Map<ResourceLocation, ItemModel> framedblocks$getBakedItemStackModels()
    {
        throw new AssertionError();
    }
}
