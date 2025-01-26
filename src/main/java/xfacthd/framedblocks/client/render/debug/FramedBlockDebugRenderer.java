package xfacthd.framedblocks.client.render.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.FrameGraphSetupEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.render.debug.AttachDebugRenderersEvent;
import xfacthd.framedblocks.api.render.debug.BlockDebugRenderer;
import xfacthd.framedblocks.common.config.DevToolsConfig;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class FramedBlockDebugRenderer
{
    private static final Map<BlockEntityType<? extends FramedBlockEntity>, Set<BlockDebugRenderer<? extends FramedBlockEntity>>> RENDERERS_BY_TYPE = new IdentityHashMap<>();
    private static final Set<BlockDebugRenderer<?>> RENDERERS = new ReferenceOpenHashSet<>();
    private static boolean anyEnabled = false;

    @SuppressWarnings("unchecked")
    private static void onRenderLevelStage(final RenderLevelStageEvent event)
    {
        if (!anyEnabled || event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) return;

        HitResult hit = Minecraft.getInstance().hitResult;
        if (!(hit instanceof BlockHitResult blockHit)) return;

        ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);
        BlockPos pos = blockHit.getBlockPos();
        if (!(level.getBlockEntity(pos) instanceof FramedBlockEntity be)) return;

        Set<BlockDebugRenderer<? extends FramedBlockEntity>> renderers = RENDERERS_BY_TYPE.get(be.getType());
        if (renderers.isEmpty()) return;

        DeltaTracker delta = event.getPartialTick();
        Camera camera = event.getCamera();
        PoseStack poseStack = event.getPoseStack();

        Vec3 offset = Vec3.atLowerCornerOf(pos).subtract(camera.getPosition());
        poseStack.pushPose();
        poseStack.translate(offset.x, offset.y, offset.z);

        float partialTick = delta.getGameTimeDeltaPartialTick(false);
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        for (BlockDebugRenderer<? extends FramedBlockEntity> renderer : renderers)
        {
            if (!renderer.isEnabled()) continue;

            poseStack.pushPose();
            ((BlockDebugRenderer<FramedBlockEntity>) renderer).render(be, blockHit, partialTick, poseStack, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
        poseStack.popPose();
        buffer.endBatch();
    }

    private static void onFrameGraphSetup(final FrameGraphSetupEvent event)
    {
        anyEnabled = false;
        for (BlockDebugRenderer<?> renderer : RENDERERS)
        {
            anyEnabled |= renderer.isEnabled();
        }

        if (DevToolsConfig.VIEW.isDoubleBlockPartHitDebugRendererEnabled())
        {
            event.enableOutlineProcessing();
        }
    }

    public static void init()
    {
        if (FMLEnvironment.production) return;

        ModLoader.postEvent(new AttachDebugRenderersEvent((type, renderer) ->
        {
            RENDERERS_BY_TYPE.computeIfAbsent(type, $ -> new ReferenceOpenHashSet<>()).add(renderer);
            RENDERERS.add(renderer);
        }));

        NeoForge.EVENT_BUS.addListener(FramedBlockDebugRenderer::onFrameGraphSetup);
        NeoForge.EVENT_BUS.addListener(FramedBlockDebugRenderer::onRenderLevelStage);
    }

    private FramedBlockDebugRenderer() { }
}
