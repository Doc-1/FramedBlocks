package xfacthd.framedblocks.common.compat.jade;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import snownee.jade.api.ui.Element;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.render.Quaternions;
import xfacthd.framedblocks.api.util.SingleBlockFakeLevel;
import xfacthd.framedblocks.common.config.ClientConfig;

import java.util.List;
import java.util.Objects;

final class FramedBlockElement extends Element
{
    private static final float SIZE = 18F;
    private static final Vec2 SIZE_VEC = new Vec2(SIZE, SIZE);
    private static final float Z_OFFSET = 15F;
    private static final float RENDER_SIZE = 16F;
    private static final Vector3f DIFFUSE_LIGHT_0 = new Vector3f(0.2F, 1.0F, 0.7F).rotate(Quaternions.YP_180).normalize();
    private static final Vector3f DIFFUSE_LIGHT_1 = new Vector3f(0.2F, 1.0F, 0.7F).rotate(Quaternions.YP_180).normalize();
    private static final ItemTransform DEFAULT_TRANSFORM = new ItemTransform(
            new Vector3f(30, 225, 0), new Vector3f(), new Vector3f(0.625F, 0.625F, 0.625F)
    );
    private static final RandomSource RANDOM = RandomSource.create();

    private final BlockState state;
    private final SingleBlockFakeLevel fakeLevel;
    private final BlockStateModel model;
    private final float scale;

    FramedBlockElement(BlockState state, FramedBlockEntity blockEntity)
    {
        IFramedBlock block = (IFramedBlock) state.getBlock();
        this.state = block.getJadeRenderState(state);
        this.model = Minecraft.getInstance().getBlockRenderer().getBlockModel(this.state);
        boolean renderCamo = ClientConfig.VIEW.shouldRenderCamoInJade();
        ModelData modelData = renderCamo ? blockEntity.getModelData(false) : ModelData.EMPTY;
        this.fakeLevel = new SingleBlockFakeLevel(Objects.requireNonNull(blockEntity.getLevel()), blockEntity.getBlockPos(), this.state, blockEntity, modelData);
        this.scale = block.getJadeRenderScale(this.state);
    }

    @Override
    public Vec2 getSize()
    {
        return SIZE_VEC;
    }

    @Override
    public void render(GuiGraphics graphics, float x, float y, float maxX, float maxY)
    {
        if (!state.isAir())
        {
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();

            poseStack.translate(x + (SIZE / 2F), y + (SIZE / 2F), Z_OFFSET);
            poseStack.scale(RENDER_SIZE * scale, -RENDER_SIZE * scale, RENDER_SIZE * scale);
            DEFAULT_TRANSFORM.apply(false, poseStack.last());
            poseStack.translate(-.5F, -.5F, -.5F);

            long seed = state.getSeed(BlockPos.ZERO);
            RANDOM.setSeed(seed);
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            List<BlockModelPart> modelParts = model.collectParts(fakeLevel, BlockPos.ZERO, state, RANDOM);
            Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateBlock(
                    fakeLevel,
                    modelParts,
                    state,
                    BlockPos.ZERO,
                    poseStack,
                    renderType -> buffer.getBuffer(RenderTypeHelper.getEntityRenderType(renderType)),
                    false,
                    OverlayTexture.NO_OVERLAY
            );

            RenderSystem.setupGuiFlatDiffuseLighting(DIFFUSE_LIGHT_0, DIFFUSE_LIGHT_1);
            buffer.endBatch();
            Lighting.setupFor3DItems();

            poseStack.popPose();
        }
    }

    @Override
    @Nullable
    public String getMessage()
    {
        return state.isAir() ? null : state.getBlock().getDescriptionId();
    }
}
