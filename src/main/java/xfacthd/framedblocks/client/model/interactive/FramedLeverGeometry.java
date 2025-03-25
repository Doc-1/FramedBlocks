package xfacthd.framedblocks.client.model.interactive;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
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
import xfacthd.framedblocks.api.util.ClientUtils;
import xfacthd.framedblocks.api.util.Utils;

public class FramedLeverGeometry extends Geometry
{
    private static final QuadListModifier HANDLE_FILTER = QuadListModifier.filtering(ClientUtils::isDummyTexture);
    private static final BlockState AUX_SHADER_STATE = Blocks.LEVER.defaultBlockState();

    private static final float MIN_SMALL = 5F/16F;
    private static final float MAX_SMALL = 11F/16F;
    private static final float MIN_LARGE = 4F/16F;
    private static final float MAX_LARGE = 12F/16F;
    private static final float HEIGHT = 3F/16F;

    private final BlockState state;
    private final BlockStateModel baseModel;
    private final Direction dir;
    private final AttachFace face;

    public FramedLeverGeometry(GeometryFactory.Context ctx)
    {
        this.state = ctx.state();
        this.baseModel = ctx.baseModel();
        this.dir = ctx.state().getValue(BlockStateProperties.HORIZONTAL_FACING);
        this.face = ctx.state().getValue(BlockStateProperties.ATTACH_FACE);
    }

    @Override
    public void collectAdditionalPartsCached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data, QuadCacheKey cacheKey)
    {
        consumer.acceptAll(baseModel, level, pos, random, state, true, false, false, false, AUX_SHADER_STATE, HANDLE_FILTER);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad)
    {
        Direction quadDir = quad.direction();
        Direction facing = getFacing();
        boolean quadInDir = quadDir == facing;

        if (Utils.isY(facing))
        {
            if (quadDir.getAxis() == facing.getAxis())
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(dir.getAxis(), MAX_LARGE))
                        .apply(Modifiers.cutTopBottom(dir.getClockWise().getAxis(), MAX_SMALL))
                        .applyIf(Modifiers.setPosition(HEIGHT), quadInDir)
                        .export(quadMap.get(quadInDir ? null : quadDir));
            }
            else
            {
                boolean smallSide = dir.getAxis() == quadDir.getAxis();

                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideUpDown(facing == Direction.DOWN, HEIGHT))
                        .apply(Modifiers.cutSideLeftRight(smallSide ? MAX_SMALL : MAX_LARGE))
                        .apply(Modifiers.setPosition(smallSide ? MAX_LARGE : MAX_SMALL))
                        .export(quadMap.get(null));
            }
        }
        else
        {
            if (quadDir.getAxis() == facing.getAxis())
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSide(MIN_SMALL, MIN_LARGE, MAX_SMALL, MAX_LARGE))
                        .applyIf(Modifiers.setPosition(HEIGHT), quadInDir)
                        .export(quadMap.get(quadInDir ? null : quadDir));
            }
            else if (Utils.isY(quadDir))
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutTopBottom(dir, HEIGHT))
                        .apply(Modifiers.cutTopBottom(dir.getClockWise().getAxis(), MAX_SMALL))
                        .apply(Modifiers.setPosition(MAX_LARGE))
                        .export(quadMap.get(null));
            }
            else
            {
                QuadModifier.of(quad)
                        .apply(Modifiers.cutSideLeftRight(dir, HEIGHT))
                        .apply(Modifiers.cutSideUpDown(MAX_LARGE))
                        .apply(Modifiers.setPosition(MAX_SMALL))
                        .export(quadMap.get(null));
            }
        }
    }

    private Direction getFacing()
    {
        return switch (face)
        {
            case FLOOR -> Direction.UP;
            case WALL -> dir;
            case CEILING -> Direction.DOWN;
        };
    }

    @Override
    public boolean useSolidNoCamoModel()
    {
        return true;
    }
}