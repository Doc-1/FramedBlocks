package xfacthd.framedblocks.client.render.debug.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.camo.block.SimpleBlockCamoContainer;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.render.debug.BlockDebugRenderer;
import xfacthd.framedblocks.api.util.ClientUtils;
import xfacthd.framedblocks.api.util.SingleBlockFakeLevel;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.blockentity.doubled.FramedDoubleBlockEntity;
import xfacthd.framedblocks.common.config.DevToolsConfig;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockParts;

import java.util.Objects;

public class DoubleBlockPartDebugRenderer implements BlockDebugRenderer<FramedDoubleBlockEntity>
{
    public static final DoubleBlockPartDebugRenderer INSTANCE = new DoubleBlockPartDebugRenderer();
    private static final FramedBlockData MODEL_DATA = new FramedBlockData(new SimpleBlockCamoContainer(
            Blocks.STONE.defaultBlockState(), FBContent.FACTORY_BLOCK.get()
    ), false);

    private DoubleBlockPartDebugRenderer() { }

    @Override
    public void render(
            FramedDoubleBlockEntity be,
            BlockHitResult blockHit,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay
    )
    {
        BlockState state = be.getBlockState();
        if (!(state.getBlock() instanceof IFramedBlock)) return;

        DoubleBlockParts parts = be.getParts();
        Player player = Objects.requireNonNull(Minecraft.getInstance().player);
        boolean secondary = be.debugHitSecondary(blockHit, player);
        BlockState partState = secondary ? parts.stateTwo() : parts.stateOne();
        BlockStateModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(partState);

        OutlineBufferSource outlineBuffer = Minecraft.getInstance().renderBuffers().outlineBufferSource();
        outlineBuffer.setColor(
                secondary ? 0x00 : 0xFF,
                secondary ? 0xFF : 0x00,
                0x00,
                0xFF
        );

        ModelData modelData = be.getModelData().derive().with(AbstractFramedBlockData.PROPERTY, MODEL_DATA).build();
        BlockAndTintGetter level = new SingleBlockFakeLevel(Objects.requireNonNull(be.getLevel()), be.getBlockPos(), partState, be, modelData);

        VertexConsumer consumer = outlineBuffer.getBuffer(RenderType.outline(ClientUtils.BLOCK_ATLAS));
        ModelBlockRenderer.renderModel(
                poseStack.last(),
                type -> consumer,
                model,
                1F, 1F, 1F,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                level,
                BlockPos.ZERO,
                partState
        );

        outlineBuffer.endOutlineBatch();
    }

    @Override
    public boolean isEnabled()
    {
        return DevToolsConfig.VIEW.isDoubleBlockPartHitDebugRendererEnabled();
    }
}
