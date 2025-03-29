package xfacthd.framedblocks.client.model.geometry.cube;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.model.cache.QuadCacheKey;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.geometry.PartConsumer;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.ChestState;
import xfacthd.framedblocks.common.data.property.LatchType;

public class FramedChestGeometry extends Geometry
{
    private final BlockState state;
    private final BlockStateModel baseModel;
    private final Direction facing;
    private final ChestType type;
    private final boolean closed;
    private final LatchType latch;

    public FramedChestGeometry(GeometryFactory.Context ctx)
    {
        this.state = ctx.state();
        this.baseModel = ctx.baseModel();
        this.facing = ctx.state().getValue(FramedProperties.FACING_HOR);
        this.type = ctx.state().getValue(BlockStateProperties.CHEST_TYPE);
        this.closed = ctx.state().getValue(PropertyHolder.CHEST_STATE) == ChestState.CLOSED;
        this.latch = ctx.state().getValue(PropertyHolder.LATCH_TYPE);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad)
    {
        Direction quadDir = quad.direction();
        if (Utils.isY(quadDir))
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cutTopBottom(facing.getAxis(), 15F/16F))
                    .applyIf(Modifiers.cutTopBottom(facing.getClockWise(), 15F/16F), type != ChestType.LEFT)
                    .applyIf(Modifiers.cutTopBottom(facing.getCounterClockWise(), 15F/16F), type != ChestType.RIGHT)
                    .applyIf(Modifiers.setPosition(closed ? 14F/16F : 10F/16F), quadDir == Direction.UP)
                    .export(quadMap.get(quadDir == Direction.UP ? null : quadDir));
        }
        else if (quadDir.getAxis() == facing.getAxis())
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSideUpDown(false, closed ? 14F/16F : 10F/16F))
                    .applyIf(Modifiers.cutSideLeftRight(facing.getClockWise(), 15F/16F), type != ChestType.LEFT)
                    .applyIf(Modifiers.cutSideLeftRight(facing.getCounterClockWise(), 15F/16F), type != ChestType.RIGHT)
                    .apply(Modifiers.setPosition(15F/16F))
                    .export(quadMap.get(null));
        }
        else
        {
            boolean offset = (type != ChestType.RIGHT || quadDir != facing.getCounterClockWise()) && (type != ChestType.LEFT || quadDir != facing.getClockWise());
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSideUpDown(false, closed ? 14F/16F : 10F/16F))
                    .apply(Modifiers.cutSideLeftRight(15F/16F))
                    .applyIf(Modifiers.setPosition(15F/16F), offset)
                    .export(quadMap.get(offset ? null : quadDir));
        }

        if (latch == LatchType.CAMO && closed)
        {
            makeChestLatch(quadMap, quad, facing, type);
        }
    }

    public static void makeChestLatch(QuadMap quadMap, BakedQuad quad, Direction facing, ChestType type)
    {
        Direction face = quad.direction();
        float length = type == ChestType.SINGLE ? 9F/16F : 1F/16F;

        if (face == facing || face == facing.getOpposite())
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSideUpDown(true, 9F/16F))
                    .apply(Modifiers.cutSideUpDown(false, 11F/16F))
                    .applyIf(Modifiers.cutSideLeftRight(facing.getClockWise(), length), type != ChestType.LEFT)
                    .applyIf(Modifiers.cutSideLeftRight(facing.getCounterClockWise(), length), type != ChestType.RIGHT)
                    .applyIf(Modifiers.setPosition(1F/16F), face != facing)
                    .export(quadMap.get(face == facing ? facing : null));
        }
        else if (Utils.isY(face))
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cutTopBottom(facing.getOpposite(), 1F/16F))
                    .applyIf(Modifiers.cutTopBottom(facing.getClockWise(), length), type != ChestType.LEFT)
                    .applyIf(Modifiers.cutTopBottom(facing.getCounterClockWise(), length), type != ChestType.RIGHT)
                    .apply(Modifiers.setPosition(face == Direction.UP ? 11F/16F : 9F/16F))
                    .export(quadMap.get(null));
        }
        else
        {
            boolean offset = (type != ChestType.RIGHT || face != facing.getCounterClockWise()) && (type != ChestType.LEFT || face != facing.getClockWise());
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSide(0, 7F/16F, 1, 11F/16F))
                    .apply(Modifiers.cutSideLeftRight(facing.getOpposite(), 1F/16F))
                    .applyIf(Modifiers.setPosition(length), offset)
                    .export(quadMap.get(offset ? null : face));
        }
    }

    @Override
    public void collectAdditionalPartsCached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data, QuadCacheKey cacheKey)
    {
        if (closed && latch == LatchType.DEFAULT)
        {
            consumer.acceptAll(baseModel, level, pos, random, state, true, false, false, false, null, null);
        }
    }
}
