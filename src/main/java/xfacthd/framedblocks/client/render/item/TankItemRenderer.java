package xfacthd.framedblocks.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.client.render.block.FramedTankRenderer;
import xfacthd.framedblocks.common.FBContent;

import java.util.Set;

public final class TankItemRenderer implements SpecialModelRenderer<SimpleFluidContent>
{
    private static final TankItemRenderer INSTANCE = new TankItemRenderer();

    private TankItemRenderer() { }

    @Override
    public void render(@Nullable SimpleFluidContent content, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, boolean hasGlint)
    {
        if (content == null || content.isEmpty()) return;

        IClientFluidTypeExtensions fluidExt = IClientFluidTypeExtensions.of(content.getFluid());
        ResourceLocation stillTex = fluidExt.getStillTexture();
        ResourceLocation flowTex = fluidExt.getFlowingTexture();
        int tint = fluidExt.getTintColor();
        ChunkSectionLayer chunkLayer = ItemBlockRenderTypes.getRenderLayer(content.getFluid().defaultFluidState());

        FramedTankRenderer.renderContents(poseStack, buffer, chunkLayer, light, content.getAmount(), stillTex, flowTex, tint);
    }

    @Override
    public SimpleFluidContent extractArgument(ItemStack stack)
    {
        return stack.getOrDefault(FBContent.DC_TYPE_TANK_CONTENTS, SimpleFluidContent.EMPTY);
    }

    @Override
    public void getExtents(Set<Vector3f> extents)
    {
        // NO-OP: this is always combined with another model which already provides correct extents
    }



    public static final class Unbaked implements SpecialModelRenderer.Unbaked
    {
        public static final ResourceLocation ID = Utils.rl("tank");
        public static final TankItemRenderer.Unbaked INSTANCE = new TankItemRenderer.Unbaked();
        public static final MapCodec<TankItemRenderer.Unbaked> CODEC = MapCodec.unit(INSTANCE);

        private Unbaked() { }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet entityModels)
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
