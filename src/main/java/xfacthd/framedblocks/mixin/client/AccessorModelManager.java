package xfacthd.framedblocks.mixin.client;

import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ModelManager.class)
public interface AccessorModelManager
{
    @Accessor("bakedBlockStateModels")
    Map<ModelResourceLocation, BakedModel> framedblocks$getBakedBlockStateModels();

    @Accessor("bakedItemStackModels")
    Map<ResourceLocation, ItemModel> framedblocks$getBakedItemStackModels();
}
