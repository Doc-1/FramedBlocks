package io.github.xfacthd.framedblocks.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.client.render.block.FramedTankRenderer;
import io.github.xfacthd.framedblocks.common.FBContent;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public final class TankItemRenderer implements SpecialModelRenderer<SimpleFluidContent>
{
    private static final TankItemRenderer INSTANCE = new TankItemRenderer();

    private TankItemRenderer() { }

    @Override
    public void submit(
            @Nullable SimpleFluidContent content,
            ItemDisplayContext ctx,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int light,
            int overlay,
            boolean hasGlint,
            int outlineColor
    )
    {
        if (content == null || content.isEmpty()) return;

        IClientFluidTypeExtensions fluidExt = IClientFluidTypeExtensions.of(content.getFluid());
        Identifier stillTex = fluidExt.getStillTexture();
        Identifier flowTex = fluidExt.getFlowingTexture();
        int tint = fluidExt.getTintColor();
        ChunkSectionLayer chunkLayer = ItemBlockRenderTypes.getRenderLayer(content.getFluid().defaultFluidState());

        FramedTankRenderer.renderContents(poseStack, submitNodeCollector, chunkLayer, light, content.getAmount(), stillTex, flowTex, tint);
    }

    @Override
    public SimpleFluidContent extractArgument(ItemStack stack)
    {
        return stack.getOrDefault(FBContent.DC_TYPE_TANK_CONTENTS, SimpleFluidContent.EMPTY);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> extents)
    {
        // NO-OP: this is always combined with another model which already provides correct extents
    }

    public static final class Unbaked implements SpecialModelRenderer.Unbaked
    {
        public static final Identifier ID = Utils.id("tank");
        public static final TankItemRenderer.Unbaked INSTANCE = new TankItemRenderer.Unbaked();
        public static final MapCodec<TankItemRenderer.Unbaked> CODEC = MapCodec.unit(INSTANCE);

        private Unbaked() { }

        @Override
        public SpecialModelRenderer<?> bake(BakingContext ctx)
        {
            return TankItemRenderer.INSTANCE;
        }

        @Override
        public MapCodec<TankItemRenderer.Unbaked> type()
        {
            return CODEC;
        }
    }
}
