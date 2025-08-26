package io.github.xfacthd.framedblocks.client.render.debug.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import io.github.xfacthd.framedblocks.api.model.quad.QuadData;
import io.github.xfacthd.framedblocks.api.render.debug.BlockDebugRenderer;
import io.github.xfacthd.framedblocks.api.util.SingleBlockFakeLevel;
import io.github.xfacthd.framedblocks.api.util.Triangle;
import io.github.xfacthd.framedblocks.common.config.DevToolsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.model.data.ModelData;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.Objects;

public class QuadWindingDebugRenderer implements BlockDebugRenderer<FramedBlockEntity>
{
    public static final QuadWindingDebugRenderer INSTANCE = new QuadWindingDebugRenderer();
    private static final Direction[] DIRECTIONS = Arrays.copyOf(Direction.values(), 7);
    private static final int[] VERT_INDEX_COLORS = { 0xFFFFFFFF, 0xFFFF0000, 0xFF00FF00, 0xFF0000FF };
    private static final RandomSource RANDOM = RandomSource.create();

    @Override
    public void render(FramedBlockEntity be, BlockHitResult blockHit, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        BlockPos pos = be.getBlockPos();
        BlockState state = be.getBlockState();
        LocalPlayer player = Objects.requireNonNull(Minecraft.getInstance().player);

        BlockStateModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        Vec3 eyePos = player.getEyePosition(partialTick).subtract(pos.getX(), pos.getY(), pos.getZ());
        Vec3 viewVector = player.getViewVector(partialTick).normalize();
        boolean sneak = player.isShiftKeyDown();

        ModelData modelData = Objects.requireNonNull(be.getLevel()).getModelData(pos);
        BlockAndTintGetter level = new SingleBlockFakeLevel(Objects.requireNonNull(be.getLevel()), pos, pos, state, be, modelData);
        Vector3f vertPos = new Vector3f();
        Vector3f vertNorm = new Vector3f();
        for (BlockModelPart part : model.collectParts(level, pos, state, RANDOM))
        {
            for (Direction side : DIRECTIONS)
            {
                for (BakedQuad quad : part.getQuads(side))
                {
                    QuadData data = new QuadData(quad);

                    vertNorm.set(data.normal(0, 0), data.normal(0, 1), data.normal(0, 2)).normalize();
                    float dot = vertNorm.dot((float) viewVector.x, (float) viewVector.y, (float) viewVector.z);
                    if (dot > -.75F) continue;

                    if (!sneak && !checkViewIntersectsQuad(data, eyePos, viewVector)) continue;

                    for (int i = 0; i < 4; i++)
                    {
                        data.pos(i, vertPos);

                        poseStack.pushPose();
                        poseStack.translate(vertPos.x, vertPos.y, vertPos.z);
                        poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
                        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                        poseStack.scale(1F / 16F, 1F / 16F, 1F / 16F);

                        Minecraft.getInstance().font.drawInBatch(
                                Integer.toString(i),
                                -2.5F,
                                -3.5F,
                                VERT_INDEX_COLORS[i],
                                false,
                                poseStack.last().pose(),
                                Minecraft.getInstance().renderBuffers().bufferSource(),
                                Font.DisplayMode.SEE_THROUGH,
                                0x00000000,
                                LightTexture.FULL_BRIGHT
                        );

                        poseStack.popPose();
                    }
                }
            }
        }
    }

    private static boolean checkViewIntersectsQuad(QuadData quadData, Vec3 eyePos, Vec3 viewVector)
    {
        Vector3f posVec = new Vector3f();

        Triangle triOne = new Triangle(
                new Vec3(quadData.pos(0, posVec)),
                new Vec3(quadData.pos(1, posVec)),
                new Vec3(quadData.pos(2, posVec))
        );
        if (triOne.intersects(eyePos, viewVector)) return true;

        Triangle triTwo = new Triangle(
                new Vec3(quadData.pos(2, posVec)),
                new Vec3(quadData.pos(3, posVec)),
                new Vec3(quadData.pos(0, posVec))
        );
        return triTwo.intersects(eyePos, viewVector);
    }

    @Override
    public boolean isEnabled()
    {
        return DevToolsConfig.VIEW.isQuadWindingDebugRendererEnabled();
    }
}
