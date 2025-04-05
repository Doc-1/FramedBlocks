package xfacthd.framedblocks.client.render.debug.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.model.data.ModelData;
import org.joml.Vector3f;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.camo.block.SimpleBlockCamoContainer;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.quad.QuadData;
import xfacthd.framedblocks.api.render.debug.BlockDebugRenderer;
import xfacthd.framedblocks.api.util.SingleBlockFakeLevel;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.config.DevToolsConfig;

import java.util.Objects;

public class QuadWindingDebugRenderer implements BlockDebugRenderer<FramedBlockEntity>
{
    public static final QuadWindingDebugRenderer INSTANCE = new QuadWindingDebugRenderer();
    private static final RandomSource RANDOM = RandomSource.create();
    private static final FramedBlockData FRAMED_BLOCK_DATA = new FramedBlockData(
            new SimpleBlockCamoContainer(Blocks.STONE.defaultBlockState(), FBContent.FACTORY_BLOCK.get()), new boolean[6], false, false, false, TriState.DEFAULT
    );

    @Override
    public void render(FramedBlockEntity be, BlockHitResult blockHit, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        BlockStateModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(be.getBlockState());
        Vector3f pos = new Vector3f();
        Vector3f norm = new Vector3f();
        Vec3 viewVector = Objects.requireNonNull(Minecraft.getInstance().player).getViewVector(partialTick).normalize();

        ModelData modelData = Objects.requireNonNull(be.getLevel())
                .getModelData(be.getBlockPos())
                .derive()
                .with(AbstractFramedBlockData.PROPERTY, FRAMED_BLOCK_DATA)
                .build();

        BlockAndTintGetter level = new SingleBlockFakeLevel(Objects.requireNonNull(be.getLevel()), be.getBlockPos(), be.getBlockState(), be, modelData);
        for (BlockModelPart part : model.collectParts(level, be.getBlockPos(), be.getBlockState(), RANDOM))
        {
            Utils.forAllDirections(side ->
            {
                for (BakedQuad quad : part.getQuads(side))
                {
                    QuadData data = new QuadData(quad);

                    norm.set(data.normal(0, 0), data.normal(0, 1), data.normal(0, 2)).normalize();
                    float dot = norm.dot((float) viewVector.x, (float) viewVector.y, (float) viewVector.z);
                    if (dot > -.75F) continue;

                    for (int i = 0; i < 4; i++)
                    {
                        data.pos(i, pos);

                        poseStack.pushPose();
                        poseStack.translate(pos.x, pos.y, pos.z);
                        poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
                        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                        poseStack.scale(1F / 16F, 1F / 16F, 1F / 16F);

                        Minecraft.getInstance().font.drawInBatch(
                                Integer.toString(i),
                                -2.5F,
                                -3.5F,
                                0xFFFFFFFF,
                                false,
                                poseStack.last().pose(),
                                Minecraft.getInstance().renderBuffers().bufferSource(),
                                Font.DisplayMode.NORMAL,
                                0x00000000,
                                LightTexture.FULL_BRIGHT
                        );

                        poseStack.popPose();
                    }
                }
            });
        }
    }

    @Override
    public boolean isEnabled()
    {
        return DevToolsConfig.VIEW.isQuadWindingDebugRendererEnabled();
    }
}
