package io.github.xfacthd.framedblocks.common.data.shapes.slope;

import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.shapes.ShapeCache;
import io.github.xfacthd.framedblocks.api.shapes.ShapeProvider;
import io.github.xfacthd.framedblocks.api.shapes.ShapeUtils;
import io.github.xfacthd.framedblocks.common.data.property.SlopeType;
import io.github.xfacthd.framedblocks.common.data.shapes.SplitShapeGenerator;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class ThreewayCornerShapes implements SplitShapeGenerator
{
    public static final ThreewayCornerShapes OUTER = new ThreewayCornerShapes(BooleanOp.AND);
    public static final ThreewayCornerShapes INNER = new ThreewayCornerShapes(BooleanOp.OR);

    private final BooleanOp joinOp;

    private ThreewayCornerShapes(BooleanOp joinOp)
    {
        this.joinOp = joinOp;
    }

    @Override
    public ShapeProvider generate(List<BlockState> states)
    {
        return generate(states, SlopeShapes.SHAPES);
    }

    @Override
    public ShapeProvider generateOcclusionShapes(List<BlockState> states)
    {
        return generate(states, SlopeShapes.OCCLUSION_SHAPES);
    }

    private ShapeProvider generate(List<BlockState> states, ShapeCache<SlopeType> shapeCache)
    {
        Map<BlockState, VoxelShape> map = new IdentityHashMap<>(states.size());

        VoxelShape shapeTop = Shapes.joinUnoptimized(
                Shapes.joinUnoptimized(
                        shapeCache.get(SlopeType.TOP),
                        shapeCache.get(SlopeType.HORIZONTAL),
                        joinOp
                ),
                ShapeUtils.rotateShapeUnoptimizedAroundY(
                        Direction.NORTH, Direction.WEST, shapeCache.get(SlopeType.TOP)
                ),
                joinOp
        );

        VoxelShape shapeBottom = Shapes.joinUnoptimized(
                Shapes.joinUnoptimized(
                        shapeCache.get(SlopeType.BOTTOM),
                        shapeCache.get(SlopeType.HORIZONTAL),
                        joinOp
                ),
                ShapeUtils.rotateShapeUnoptimizedAroundY(
                        Direction.NORTH, Direction.WEST, shapeCache.get(SlopeType.BOTTOM)
                ),
                joinOp
        );

        VoxelShape[] shapes = ShapeUtils.makeHorizontalRotationsWithFlag(shapeBottom, shapeTop, Direction.NORTH);

        for (BlockState state : states)
        {
            Direction dir = state.getValue(FramedProperties.FACING_HOR);
            boolean top = state.getValue(FramedProperties.TOP);
            map.put(state, shapes[dir.get2DDataValue() + (top ? 4 : 0)]);
        }

        return ShapeProvider.of(map);
    }
}
