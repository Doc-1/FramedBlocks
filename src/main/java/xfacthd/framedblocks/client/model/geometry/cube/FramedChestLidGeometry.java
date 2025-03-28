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
import xfacthd.framedblocks.api.util.*;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.LatchType;

public class FramedChestLidGeometry extends Geometry
{
    private final BlockState state;
    private final BlockStateModel baseModel;
    private final Direction facing;
    private final ChestType type;
    private final LatchType latch;

    public FramedChestLidGeometry(GeometryFactory.Context ctx)
    {
        this.state = ctx.state();
        this.baseModel = ctx.baseModel();
        this.facing = ctx.state().getValue(FramedProperties.FACING_HOR);
        this.type = ctx.state().getValue(BlockStateProperties.CHEST_TYPE);
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
                    .apply(Modifiers.setPosition(quadDir == Direction.UP ? 14F/16F : 7F/16F))
                    .export(quadMap.get(quadDir == Direction.UP ? null : quadDir));
        }
        else if (quadDir.getAxis() == facing.getAxis())
        {
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSideUpDown(false, 14F/16F))
                    .apply(Modifiers.cutSideUpDown(true, 7F/16F))
                    .applyIf(Modifiers.cutSideLeftRight(facing.getClockWise(), 15F/16F), type != ChestType.LEFT)
                    .applyIf(Modifiers.cutSideLeftRight(facing.getCounterClockWise(), 15F/16F), type != ChestType.RIGHT)
                    .apply(Modifiers.setPosition(15F/16F))
                    .export(quadMap.get(null));
        }
        else
        {
            boolean offset = (type != ChestType.RIGHT || quadDir != facing.getCounterClockWise()) && (type != ChestType.LEFT || quadDir != facing.getClockWise());
            QuadModifier.of(quad)
                    .apply(Modifiers.cutSideUpDown(false, 14F/16F))
                    .apply(Modifiers.cutSideUpDown(true, 7F/16F))
                    .apply(Modifiers.cutSideLeftRight(15F/16F))
                    .applyIf(Modifiers.setPosition(15F/16F), offset)
                    .export(quadMap.get(offset ? null : quadDir));
        }

        if (latch == LatchType.CAMO)
        {
            FramedChestGeometry.makeChestLatch(quadMap, quad, facing, type);
        }
    }

    @Override
    public void collectAdditionalPartsCached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data, QuadCacheKey cacheKey)
    {
        if (latch == LatchType.DEFAULT)
        {
            consumer.acceptAll(baseModel, level, pos, random, state, true, false, false, false, null, null);
        }
    }
}
