package xfacthd.framedblocks.mixin.client;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xfacthd.framedblocks.client.render.item.IDynamicTintAwareItemRenderer;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer implements IDynamicTintAwareItemRenderer
{
    @Nullable
    private static Int2IntMap framedblocks$itemTintValues = null;

    @Override
    public void framedblocks$setItemTintValues(@Nullable Int2IntMap tintValues)
    {
        framedblocks$itemTintValues = tintValues;
    }

    @Inject(method = "getLayerColorSafe", at = @At("HEAD"), cancellable = true)
    private static void framedblocks$applyDynamicItemTint(int[] tints, int idx, CallbackInfoReturnable<Integer> cir)
    {
        if (framedblocks$itemTintValues != null)
        {
            cir.setReturnValue(framedblocks$itemTintValues.getOrDefault(idx, -1));
        }
        else if (idx < -1)
        {
            cir.setReturnValue(-1);
        }
    }
}
