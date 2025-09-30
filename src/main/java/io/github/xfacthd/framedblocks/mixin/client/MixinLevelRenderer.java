package io.github.xfacthd.framedblocks.mixin.client;

import io.github.xfacthd.framedblocks.client.util.FramedClientUtils;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.LevelRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer
{
    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private GraphicsStatus framedblocks$lastGraphicsMode = GraphicsStatus.FANCY;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void framedblocks$captureInitialGraphicsMode(
            Minecraft mc,
            EntityRenderDispatcher entityRenderDispatcher,
            BlockEntityRenderDispatcher blockEntityRenderDispatcher,
            RenderBuffers buffers,
            LevelRenderState levelRenderState,
            FeatureRenderDispatcher featureRenderDispatcher,
            CallbackInfo ci
    )
    {
        framedblocks$lastGraphicsMode = mc.options.graphicsMode().get();
    }

    @Inject(method = "allChanged", at = @At("HEAD"))
    private void framedblocks$handleRedrawOnGraphicsModeChange(CallbackInfo ci)
    {
        GraphicsStatus graphicsMode = minecraft.options.graphicsMode().get();
        if (graphicsMode != framedblocks$lastGraphicsMode)
        {
            framedblocks$lastGraphicsMode = graphicsMode;
            FramedClientUtils.clearModelCaches();
        }
    }
}
