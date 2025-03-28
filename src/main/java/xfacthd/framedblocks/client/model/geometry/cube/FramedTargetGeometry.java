package xfacthd.framedblocks.client.model.geometry.cube;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.geometry.PartConsumer;
import xfacthd.framedblocks.api.model.util.StandaloneModels;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.item.ItemModelInfo;
import xfacthd.framedblocks.api.util.Utils;

public class FramedTargetGeometry extends Geometry
{
    private static final ResourceLocation OVERLAY_LOCATION = Utils.rl("block/target_overlay");
    public static final StandaloneModelKey<BlockModelPart> OVERLAY_KEY = new StandaloneModelKey<>(OVERLAY_LOCATION);
    public static final int OVERLAY_TINT_IDX = 1024;
    private static final ItemModelInfo ITEM_MODEL_INFO = new ItemModelInfo()
    {
        @Override
        public boolean isDataRequired()
        {
            return true;
        }
    };

    private final BlockState state;
    private final BlockModelPart overlayModel;

    public FramedTargetGeometry(GeometryFactory.Context ctx)
    {
        this.state = ctx.state();
        this.overlayModel = StandaloneModels.getBlockModelPart(ctx.modelLookup(), ctx.textureLookup(), OVERLAY_KEY);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad) { }

    @Override
    public void collectAdditionalPartsUncached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data)
    {
        consumer.accept(overlayModel, state, false, false, true, false, null, null);
    }

    @Override
    public ItemModelInfo getItemModelInfo()
    {
        return ITEM_MODEL_INFO;
    }
}
