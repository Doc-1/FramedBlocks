package xfacthd.framedblocks.mixin.client;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.DelegateBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DelegateBakedModel.class)
public interface AccessorDelegateBakedModel
{
    @Accessor("parent")
    BakedModel framedblocks$getParentModel();
}
