package xfacthd.framedblocks.client.model.geometry.rail;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.model.geometry.PartConsumer;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.client.model.geometry.slope.FramedSlopeGeometry;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.util.FramedUtils;

public class FramedRailSlopeGeometry extends FramedSlopeGeometry
{
    private final BlockState railState;

    private FramedRailSlopeGeometry(GeometryFactory.Context ctx, BlockState railBlock, EnumProperty<RailShape> shapeProperty)
    {
        super(new GeometryFactory.Context(getSlopeState(ctx.state()), ctx.baseModel(), ctx.modelLookup(), ctx.textureLookup()));

        RailShape shape = ctx.state().getValue(PropertyHolder.ASCENDING_RAIL_SHAPE);
        railState = railBlock.setValue(shapeProperty, shape);
    }

    @Override
    public void collectAdditionalPartsUncached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data)
    {
        BlockStateModel model = ModelUtils.getModel(railState);
        consumer.acceptAll(model, level, pos, random, railState, true, false, false, false, railState, null);
    }

    private static BlockState getSlopeState(BlockState state)
    {
        RailShape shape = state.getValue(PropertyHolder.ASCENDING_RAIL_SHAPE);
        Direction dir = FramedUtils.getDirectionFromAscendingRailShape(shape);
        boolean ySlope = state.getValue(FramedProperties.Y_SLOPE);

        return FBContent.BLOCK_FRAMED_SLOPE.value()
                .defaultBlockState()
                .setValue(FramedProperties.FACING_HOR, dir)
                .setValue(FramedProperties.Y_SLOPE, ySlope);
    }



    public static FramedRailSlopeGeometry normal(GeometryFactory.Context ctx)
    {
        return new FramedRailSlopeGeometry(ctx, Blocks.RAIL.defaultBlockState(), BlockStateProperties.RAIL_SHAPE);
    }

    public static FramedRailSlopeGeometry powered(GeometryFactory.Context ctx)
    {
        boolean powered = ctx.state().getValue(BlockStateProperties.POWERED);
        return new FramedRailSlopeGeometry(
                ctx,
                Blocks.POWERED_RAIL.defaultBlockState().setValue(BlockStateProperties.POWERED, powered),
                BlockStateProperties.RAIL_SHAPE_STRAIGHT
        );
    }

    public static FramedRailSlopeGeometry detector(GeometryFactory.Context ctx)
    {
        boolean powered = ctx.state().getValue(BlockStateProperties.POWERED);
        return new FramedRailSlopeGeometry(
                ctx,
                Blocks.DETECTOR_RAIL.defaultBlockState().setValue(BlockStateProperties.POWERED, powered),
                BlockStateProperties.RAIL_SHAPE_STRAIGHT
        );
    }

    public static FramedRailSlopeGeometry activator(GeometryFactory.Context ctx)
    {
        boolean powered = ctx.state().getValue(BlockStateProperties.POWERED);
        return new FramedRailSlopeGeometry(
                ctx,
                Blocks.ACTIVATOR_RAIL.defaultBlockState().setValue(BlockStateProperties.POWERED, powered),
                BlockStateProperties.RAIL_SHAPE_STRAIGHT
        );
    }
}