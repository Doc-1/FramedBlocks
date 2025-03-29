package xfacthd.framedblocks.client.model.geometry.torch;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.model.cache.QuadCacheKey;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.geometry.PartConsumer;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.util.Utils;

public class FramedTorchGeometry extends Geometry
{
    private static final float MIN = 7F/16F;
    private static final float MAX = 9F/16F;
    private static final float TOP = 8F/16F;

    private final BlockState state;
    private final BlockStateModel baseModel;
    private final BlockState auxShaderState;

    private FramedTorchGeometry(GeometryFactory.Context ctx, BlockState auxShaderState)
    {
        this.state = ctx.state();
        this.baseModel = ctx.baseModel();
        this.auxShaderState = auxShaderState;
    }

    @Override
    public void collectAdditionalPartsCached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data, QuadCacheKey cacheKey)
    {
        consumer.acceptAll(baseModel, level, pos, random, state, true, false, false, false, auxShaderState, null);
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
                    .applyIf(Modifiers.setPosition(TOP), top)
                    .export(quadMap.get(top ? null : quadDir));
        }
        else
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSide(MIN, 0, MAX, TOP))
                    .apply(Modifiers.setPosition(MAX))
                    .export(quadMap.get(null));
        }
    }

    @Override
    public boolean useSolidNoCamoModel()
    {
        return true;
    }

    public static FramedTorchGeometry normal(GeometryFactory.Context ctx)
    {
        return new FramedTorchGeometry(ctx, Blocks.TORCH.defaultBlockState());
    }

    public static FramedTorchGeometry soul(GeometryFactory.Context ctx)
    {
        return new FramedTorchGeometry(ctx, Blocks.SOUL_TORCH.defaultBlockState());
    }
}
