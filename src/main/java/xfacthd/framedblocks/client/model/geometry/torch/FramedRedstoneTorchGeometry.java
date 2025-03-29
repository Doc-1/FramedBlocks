package xfacthd.framedblocks.client.model.geometry.torch;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.model.cache.QuadCacheKey;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.geometry.PartConsumer;
import xfacthd.framedblocks.api.model.geometry.QuadListModifier;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.util.Utils;

public class FramedRedstoneTorchGeometry extends Geometry
{
    private static final BlockState AUX_SHADER_STATE = Blocks.REDSTONE_TORCH.defaultBlockState();

    // Ensure glowing torch parts have no AO even if the rest does
    static final QuadListModifier HEAD_MODIFIER = QuadListModifier.replacing(quad ->
            QuadModifier.of(quad).ambientOcclusion(false).exportDirect()
    );

    private static final float MIN = 7F/16F;
    private static final float MAX = 9F/16F;
    private static final float TOP = 8F/16F;
    private static final float TOP_LIT = 7F/16F;

    private final BlockState state;
    private final BlockStateModel baseModel;
    private final boolean lit;

    public FramedRedstoneTorchGeometry(GeometryFactory.Context ctx)
    {
        this.state = ctx.state();
        this.baseModel = ctx.baseModel();
        this.lit = ctx.state().getValue(BlockStateProperties.LIT);
    }

    @Override
    public void collectAdditionalPartsCached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data, QuadCacheKey cacheKey)
    {
        consumer.acceptAll(baseModel, level, pos, random, state, true, false, false, false, AUX_SHADER_STATE, HEAD_MODIFIER);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad)
    {
        Direction quadDir = quad.direction();
        if (Utils.isY(quadDir))
        {
            boolean top = quadDir == Direction.UP;
            QuadModifier.of(quad)
                    .apply(Modifiers.cutTopBottom(MIN, MIN, MAX, MAX))
                    .applyIf(Modifiers.setPosition(lit ? TOP_LIT : TOP), top)
                    .export(quadMap.get(top ? null : quadDir));
        }
        else
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSide(MIN, 0, MAX, lit ? TOP_LIT : TOP))
                    .apply(Modifiers.setPosition(MAX))
                    .export(quadMap.get(null));
        }
    }

    @Override
    public boolean useSolidNoCamoModel()
    {
        return true;
    }
}
