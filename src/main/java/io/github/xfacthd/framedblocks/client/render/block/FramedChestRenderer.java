package io.github.xfacthd.framedblocks.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.model.standalone.StandaloneWrapperKey;
import io.github.xfacthd.framedblocks.api.render.RenderUtils;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.client.model.special.FramedChestLidModel;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.block.cube.FramedChestBlock;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedChestBlockEntity;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.ChestState;
import io.github.xfacthd.framedblocks.common.data.property.LatchType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class FramedChestRenderer implements BlockEntityRenderer<FramedChestBlockEntity>
{
    private static final ResourceLocation BLOCKSTATE_LOC = Utils.rl("framed_chest_lid");
    public static final StandaloneWrapperKey<FramedChestLidModel> WRAPPER_KEY = new StandaloneWrapperKey<>(FBContent.BLOCK_FRAMED_CHEST, BLOCKSTATE_LOC);
    private static final RandomSource RANDOM = RandomSource.create();

    @Nullable
    private final FramedChestLidModel lidModel;

    @SuppressWarnings("unused")
    public FramedChestRenderer(BlockEntityRendererProvider.Context ctx)
    {
        this.lidModel = ctx.getBlockRenderDispatcher()
                .getBlockModelShaper()
                .getModelManager()
                .getStandaloneModel(WRAPPER_KEY.modelKey());
    }

    @Override
    public void render(
            FramedChestBlockEntity be,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay,
            Vec3 cameraPos
    )
    {
        Level level = be.getLevel();
        if (level == null || be.isRemoved() || lidModel == null) return;

        BlockState state = be.getBlockState();

        var result = FramedChestBlock.combine(be, true);
        ChestState chestState = result.apply(FramedChestBlock.STATE_COMBINER);

        Direction dir = state.getValue(FramedProperties.FACING_HOR);
        ChestType type = state.getValue(BlockStateProperties.CHEST_TYPE);
        LatchType latch = state.getValue(PropertyHolder.LATCH_TYPE);

        long lastChange = result.apply(FramedChestBlock.OPENNESS_COMBINER).orElse(0L);
        float angle = calculateAngle(level, chestState, dir, lastChange, partialTicks);

        float xOff = Utils.isX(dir) ? (Utils.isPositive(dir) ? 1F/16F : 15F/16F) : 0;
        float zOff = Utils.isZ(dir) ? (Utils.isPositive(dir) ? 1F/16F : 15F/16F) : 0;

        poseStack.pushPose();

        poseStack.translate(xOff, 9F/16F, zOff);
        poseStack.mulPose(Utils.isX(dir) ? Axis.ZP.rotationDegrees(angle) : Axis.XN.rotationDegrees(angle));
        poseStack.translate(-xOff, -9F/16F, -zOff);

        BlockStateModel model = lidModel.getModel(dir, type, latch);

        RANDOM.setSeed(42);
        // Cannot use BlockRenderDispatcher#renderBatched() due to incorrect shading of rotated surfaces
        RenderUtils.renderModel(
                state,
                level,
                be.getBlockPos(),
                poseStack.last(),
                buffer,
                model,
                RANDOM,
                light,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
    }

    private static float calculateAngle(Level level, ChestState chestState, Direction dir, long lastChange, float partialTicks)
    {
        float diff = (float) (level.getGameTime() - lastChange) + partialTicks;

        float factor = Mth.lerp(diff / 10F, 0, 1);
        if (chestState == ChestState.CLOSING) { factor = 1F - factor; }

        factor = 1.0F - factor;
        factor = 1.0F - factor * factor * factor;

        float angle = Mth.clamp(factor * 90F, 0F, 90F);
        if (!Utils.isPositive(dir)) { angle *= -1F; }

        return angle;
    }

    @Override
    public boolean shouldRender(FramedChestBlockEntity be, Vec3 camera)
    {
        ChestState state = FramedChestBlock.combine(be, true).apply(FramedChestBlock.STATE_COMBINER);
        return state != ChestState.CLOSED && BlockEntityRenderer.super.shouldRender(be, camera);
    }

    @Override
    public AABB getRenderBoundingBox(FramedChestBlockEntity blockEntity)
    {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() - .25, pos.getY() + .5625, pos.getZ() - .25, pos.getX() + 1.25, pos.getY() + 1.5, pos.getZ() + 1.25);
    }
}
