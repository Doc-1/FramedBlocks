package xfacthd.framedblocks.mixin.client;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.WeightedVariants;
import net.minecraft.util.random.WeightedList;
import org.jetbrains.annotations.UnknownNullability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xfacthd.framedblocks.api.model.util.WeightedBakedModelAccess;

@Mixin(WeightedVariants.class)
public class MixinWeightedVariants implements WeightedBakedModelAccess
{
    @UnknownNullability
    private BlockStateModel framedblocks$parent;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void framedblocks$captureParent(WeightedList<BlockStateModel> models, CallbackInfo ci)
    {
        framedblocks$parent = models.unwrap().getFirst().value();
    }

    @Override
    public BlockStateModel framedblocks$getParentModel()
    {
        return framedblocks$parent;
    }
}
