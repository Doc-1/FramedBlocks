package io.github.xfacthd.framedblocks.common.data.shapes.slopepanelcorner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.shapes.ShapeCache;
import io.github.xfacthd.framedblocks.api.shapes.ShapeProvider;
import io.github.xfacthd.framedblocks.api.shapes.ShapeUtils;
import io.github.xfacthd.framedblocks.common.data.CornerSlopePanelShape;
import io.github.xfacthd.framedblocks.common.data.shapes.SplitShapeGenerator;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class InverseDoubleCornerSlopePanelShapes implements SplitShapeGenerator
{
    @Override
    public ShapeProvider generate(ImmutableList<BlockState> states)
    {
        return generate(states, CornerSlopePanelShapes.SHAPES);
    }

    @Override
    public ShapeProvider generateOcclusionShapes(ImmutableList<BlockState> states)
    {
        return generate(states, CornerSlopePanelShapes.OCCLUSION_SHAPES);
    }

    private static ShapeProvider generate(ImmutableList<BlockState> states, ShapeCache<CornerSlopePanelShape> cache)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        VoxelShape shapeBottom = ShapeUtils.orUnoptimized(
                cache.get(CornerSlopePanelShape.LARGE_BOTTOM),
                ShapeUtils.rotateShapeUnoptimizedAroundY(
                        Direction.NORTH,
                        Direction.SOUTH,
                        cache.get(CornerSlopePanelShape.SMALL_INNER_TOP))
        );
        VoxelShape shapeTop = ShapeUtils.orUnoptimized(
                cache.get(CornerSlopePanelShape.LARGE_TOP),
                ShapeUtils.rotateShapeUnoptimizedAroundY(
                        Direction.NORTH,
                        Direction.SOUTH,
                        cache.get(CornerSlopePanelShape.SMALL_INNER_BOTTOM)
                )
        );
        VoxelShape[] shapes = ShapeUtils.makeHorizontalRotationsWithFlag(shapeBottom, shapeTop, Direction.NORTH);

        for (BlockState state : states)
        {
            Direction dir = state.getValue(FramedProperties.FACING_HOR);
            boolean top = state.getValue(FramedProperties.TOP);
            builder.put(state, shapes[dir.get2DDataValue() + (top ? 4 : 0)]);
        }

        return ShapeProvider.of(builder.build());
    }
}
