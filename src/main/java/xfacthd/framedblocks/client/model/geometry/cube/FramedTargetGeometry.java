package xfacthd.framedblocks.client.model.geometry.cube;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.geometry.PartConsumer;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.item.ItemModelInfo;
import xfacthd.framedblocks.api.util.Utils;

public class FramedTargetGeometry extends Geometry
{
    public static final ResourceLocation OVERLAY_LOCATION = Utils.rl("block/target_overlay");
    public static final String OVERLAY_KEY = "overlay";
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
    private final BlockStateModel overlayModel;

    public FramedTargetGeometry(GeometryFactory.Context ctx)
    {
        this.state = ctx.state();
        this.overlayModel = ctx.auxModels().getModel(OVERLAY_KEY);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad, ModelData modelData) { }

    @Override
    public void collectAdditionalPartsUncached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data)
    {
        consumer.acceptAll(overlayModel, level, pos, random, state, false, false, true, false, null, null);
    }

    @Override
    public ItemModelInfo getItemModelInfo()
    {
        return ITEM_MODEL_INFO;
    }
}
