package xfacthd.framedblocks.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xfacthd.framedblocks.client.render.item.IDynamicTintAwareItemRenderer;
import xfacthd.framedblocks.client.render.item.IDynamicTintableItemStackRenderStateLayer;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public class MixinItemStackRenderStateLayer implements IDynamicTintableItemStackRenderStateLayer
{
    @Nullable
    private Int2IntMap framedblocks$tintValues = null;

    @Override
    public void framedblocks$setDynamicItemTintValues(Int2IntMap tintValues)
    {
        framedblocks$tintValues = tintValues;
    }

    @Inject(method = "clear", at = @At("TAIL"))
    private void framedblocks$clearDynamicItemTintProvider(CallbackInfo ci)
    {
        framedblocks$tintValues = null;
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderItem(Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II[ILjava/util/List;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"
            )
    )
    private void framedblocks$applyItemTintValues(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci)
    {
        if (framedblocks$tintValues != null)
        {
            ((IDynamicTintAwareItemRenderer) Minecraft.getInstance().getItemRenderer()).framedblocks$setItemTintValues(framedblocks$tintValues);
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderItem(Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II[ILjava/util/List;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void framedblocks$removeItemTintValues(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci)
    {
        if (framedblocks$tintValues != null)
        {
            ((IDynamicTintAwareItemRenderer) Minecraft.getInstance().getItemRenderer()).framedblocks$setItemTintValues(null);
        }
    }
}
