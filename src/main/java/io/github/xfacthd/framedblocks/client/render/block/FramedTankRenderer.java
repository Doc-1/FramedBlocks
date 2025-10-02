package io.github.xfacthd.framedblocks.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.client.render.block.state.FramedTankRenderState;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedTankBlockEntity;
import io.github.xfacthd.framedblocks.common.capability.fluid.TankFluidResourceHandler;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.textures.FluidSpriteCache;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class FramedTankRenderer implements BlockEntityRenderer<FramedTankBlockEntity, FramedTankRenderState>
{
    private static final float OFFSET = .01F;
    private static final float MIN_XZ = OFFSET;
    private static final float MAX_XZ = 1F - OFFSET;

    public FramedTankRenderer(@SuppressWarnings("unused") BlockEntityRendererProvider.Context ctx) { }

    @Override
    public void submit(FramedTankRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        int fluidAmount = renderState.fluidAmount;
        if (fluidAmount == 0) return;

        ChunkSectionLayer chunkLayer = renderState.chunkLayer;
        int light = renderState.lightCoords;
        ResourceLocation stillTex = renderState.stillTex;
        ResourceLocation flowTex = renderState.flowTex;
        int tint = renderState.tint;
        renderContents(poseStack, submitNodeCollector, chunkLayer, light, fluidAmount, stillTex, flowTex, tint);
    }

    @Override
    public FramedTankRenderState createRenderState()
    {
        return new FramedTankRenderState();
    }

    @Override
    public void extractRenderState(
            FramedTankBlockEntity blockEntity,
            FramedTankRenderState renderState,
            float partialTick,
            Vec3 cameraPos,
            @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    )
    {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);

        FluidStack fluid = blockEntity.getContents();
        Level level = blockEntity.getLevel();
        if (fluid.isEmpty() || level == null) return;

        BlockPos pos = blockEntity.getBlockPos();
        IClientFluidTypeExtensions fluidExt = IClientFluidTypeExtensions.of(fluid.getFluid());
        FluidState fluidState = fluid.getFluid().defaultFluidState();
        renderState.tint = fluidExt.getTintColor(fluidState, level, pos);
        renderState.stillTex = fluidExt.getStillTexture(fluidState, level, pos);
        renderState.flowTex = fluidExt.getFlowingTexture(fluidState, level, pos);
        renderState.chunkLayer = ItemBlockRenderTypes.getRenderLayer(fluidState);
        renderState.fluidAmount = fluid.getAmount();
    }

    public static void renderContents(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            ChunkSectionLayer chunkLayer,
            int light,
            int fluidAmount,
            ResourceLocation stillTex,
            ResourceLocation flowTex,
            int tint
    )
    {
        float height = Mth.clamp(fluidAmount / (float) TankFluidResourceHandler.CAPACITY, OFFSET, 1F - OFFSET);
        boolean sameTex = stillTex.equals(flowTex);

        RenderType bufferType = RenderTypeHelper.getEntityRenderType(chunkLayer);
        submitNodeCollector.submitCustomGeometry(poseStack, bufferType, (pose, consumer) ->
        {
            TextureAtlasSprite sprite = FluidSpriteCache.getSprite(flowTex);
            float minU = sprite.getU(MIN_XZ);
            float maxU = sameTex ? sprite.getU(MAX_XZ) : sprite.getU(8F / 16F - OFFSET);
            float minV = sameTex ? sprite.getV(1F - height) : sprite.getV(8F / 16F * (1F - height));
            float maxV = sameTex ? sprite.getV(MAX_XZ) : sprite.getV(8F / 16F - OFFSET);

            // West
            consumer.addVertex(pose, MIN_XZ, height, MIN_XZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, -1F, 0F, 0F);
            consumer.addVertex(pose, MIN_XZ, OFFSET, MIN_XZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, -1F, 0F, 0F);
            consumer.addVertex(pose, MIN_XZ, OFFSET, MAX_XZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, -1F, 0F, 0F);
            consumer.addVertex(pose, MIN_XZ, height, MAX_XZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, -1F, 0F, 0F);

            // East
            consumer.addVertex(pose, MAX_XZ, height, MAX_XZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1F, 0F, 0F);
            consumer.addVertex(pose, MAX_XZ, OFFSET, MAX_XZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1F, 0F, 0F);
            consumer.addVertex(pose, MAX_XZ, OFFSET, MIN_XZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1F, 0F, 0F);
            consumer.addVertex(pose, MAX_XZ, height, MIN_XZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1F, 0F, 0F);

            // North
            consumer.addVertex(pose, MAX_XZ, height, MIN_XZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, -1F);
            consumer.addVertex(pose, MAX_XZ, OFFSET, MIN_XZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, -1F);
            consumer.addVertex(pose, MIN_XZ, OFFSET, MIN_XZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, -1F);
            consumer.addVertex(pose, MIN_XZ, height, MIN_XZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, -1F);

            // South
            consumer.addVertex(pose, MIN_XZ, height, MAX_XZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, 1F);
            consumer.addVertex(pose, MIN_XZ, OFFSET, MAX_XZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, 1F);
            consumer.addVertex(pose, MAX_XZ, OFFSET, MAX_XZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, 1F);
            consumer.addVertex(pose, MAX_XZ, height, MAX_XZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, 1F);

            if (!sameTex)
            {
                sprite = FluidSpriteCache.getSprite(stillTex);
            }
            minU = sprite.getU(MIN_XZ);
            maxU = sprite.getU(MAX_XZ);
            minV = sprite.getV(MIN_XZ);
            maxV = sprite.getV(MAX_XZ);

            // Up
            consumer.addVertex(pose, MAX_XZ, height, MAX_XZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 1F, 0F);
            consumer.addVertex(pose, MAX_XZ, height, MIN_XZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 1F, 0F);
            consumer.addVertex(pose, MIN_XZ, height, MIN_XZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 1F, 0F);
            consumer.addVertex(pose, MIN_XZ, height, MAX_XZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 1F, 0F);

            // Down
            consumer.addVertex(pose, MIN_XZ, OFFSET, MAX_XZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, -1F, 0F);
            consumer.addVertex(pose, MIN_XZ, OFFSET, MIN_XZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, -1F, 0F);
            consumer.addVertex(pose, MAX_XZ, OFFSET, MIN_XZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, -1F, 0F);
            consumer.addVertex(pose, MAX_XZ, OFFSET, MAX_XZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, -1F, 0F);
        });
    }

    @Override
    public boolean shouldRender(FramedTankBlockEntity be, Vec3 camera)
    {
        return !be.getBlockState().getValue(FramedProperties.SOLID) && BlockEntityRenderer.super.shouldRender(be, camera);
    }
}
