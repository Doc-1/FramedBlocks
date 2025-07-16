package xfacthd.framedblocks.mixin.client;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xfacthd.framedblocks.client.render.item.DynamicTintHolder;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer
{
    @Inject(method = "getLayerColorSafe", at = @At("HEAD"), cancellable = true)
    private static void framedblocks$applyDynamicItemTint(int[] tints, int idx, CallbackInfoReturnable<Integer> cir)
    {
        Int2IntMap tintValues = DynamicTintHolder.tintValues;
        if (tintValues != null)
        {
            cir.setReturnValue(tintValues.getOrDefault(idx, -1));
        }
    }
}
