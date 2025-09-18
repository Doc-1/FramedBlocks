package io.github.xfacthd.framedblocks.client.render.special;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.xfacthd.framedblocks.api.util.ClientUtils;
import io.github.xfacthd.framedblocks.common.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;

abstract sealed class GhostBlockRenderConfig
{
    private static final GhostBlockRenderConfig DEFAULT = new Default();
    private static final GhostBlockRenderConfig FALLBACK = new Fallback();

    static GhostBlockRenderConfig get()
    {
        return ClientConfig.VIEW.useAltGhostRenderer() ? FALLBACK : DEFAULT;
    }

    void setupRenderState()
    {
        AbstractTexture texture = Minecraft.getInstance()
                .getTextureManager()
                .getTexture(ClientUtils.BLOCK_ATLAS);
        texture.setUseMipmaps(true);
        RenderSystem.setShaderTexture(0, texture.getTextureView());

        Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
    }

    void clearRenderState()
    {
        Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
    }

    void setupSamplers(RenderPass renderPass)
    {
        renderPass.bindSampler("Sampler0", RenderSystem.getShaderTexture(0));
        renderPass.bindSampler("Sampler1", RenderSystem.getShaderTexture(1));
        renderPass.bindSampler("Sampler2", RenderSystem.getShaderTexture(2));
    }

    abstract RenderPipeline getPipeline();

    abstract RenderTarget getRenderTarget();

    private static final class Default extends GhostBlockRenderConfig
    {
        @Override
        RenderPipeline getPipeline()
        {
            return RenderPipelines.TRANSLUCENT;
        }

        @Override
        RenderTarget getRenderTarget()
        {
            RenderTarget particlesTarget = Minecraft.getInstance().levelRenderer.getParticlesTarget();
            return particlesTarget != null ? particlesTarget : Minecraft.getInstance().getMainRenderTarget();
        }
    }

    private static final class Fallback extends GhostBlockRenderConfig
    {
        @Override
        void setupRenderState()
        {
            super.setupRenderState();
            Minecraft.getInstance().gameRenderer.overlayTexture().setupOverlayColor();
        }

        @Override
        void clearRenderState()
        {
            super.clearRenderState();
            Minecraft.getInstance().gameRenderer.overlayTexture().teardownOverlayColor();
        }

        @Override
        RenderPipeline getPipeline()
        {
            return RenderPipelines.ITEM_ENTITY_TRANSLUCENT_CULL;
        }

        @Override
        RenderTarget getRenderTarget()
        {
            RenderTarget itemEntityTarget = Minecraft.getInstance().levelRenderer.getItemEntityTarget();
            return itemEntityTarget != null ? itemEntityTarget : Minecraft.getInstance().getMainRenderTarget();
        }
    }
}
