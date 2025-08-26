package io.github.xfacthd.framedblocks.common.data.shapes.slope;

import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.shapes.CommonShapes;
import io.github.xfacthd.framedblocks.api.shapes.ShapeCache;
import io.github.xfacthd.framedblocks.api.shapes.ShapeProvider;
import io.github.xfacthd.framedblocks.api.shapes.ShapeUtils;
import io.github.xfacthd.framedblocks.common.data.property.SlopeType;
import io.github.xfacthd.framedblocks.common.data.shapes.SplitShapeGenerator;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class VerticalHalfSlopeShapes implements SplitShapeGenerator
{
    public static final ShapeCache<Boolean> SHAPES = ShapeCache.createIdentity(map ->
    {
        map.put(Boolean.FALSE, ShapeUtils.andUnoptimized(
                SlopeShapes.SHAPES.get(SlopeType.HORIZONTAL),
                CommonShapes.SLAB.get(Boolean.FALSE)
        ));
        map.put(Boolean.TRUE, ShapeUtils.andUnoptimized(
                SlopeShapes.SHAPES.get(SlopeType.HORIZONTAL),
                CommonShapes.SLAB.get(Boolean.TRUE)
        ));
    });
    public static final ShapeCache<Boolean> OCCLUSION_SHAPES = ShapeCache.createIdentity(map ->
    {
        map.put(Boolean.FALSE, ShapeUtils.andUnoptimized(
                SlopeShapes.OCCLUSION_SHAPES.get(SlopeType.HORIZONTAL),
                CommonShapes.SLAB.get(Boolean.FALSE)
        ));
        map.put(Boolean.TRUE, ShapeUtils.andUnoptimized(
                SlopeShapes.OCCLUSION_SHAPES.get(SlopeType.HORIZONTAL),
                CommonShapes.SLAB.get(Boolean.TRUE)
        ));
    });

    @Override
    public ShapeProvider generate(List<BlockState> states)
    {
        return generateShapes(states, SHAPES);
    }

    @Override
    public ShapeProvider generateOcclusionShapes(List<BlockState> states)
    {
        return generateShapes(states, OCCLUSION_SHAPES);
    }

    private static ShapeProvider generateShapes(List<BlockState> states, ShapeCache<Boolean> shapeCache)
    {
        Map<BlockState, VoxelShape> map = new IdentityHashMap<>(states.size());

        VoxelShape[] shapes = ShapeUtils.makeHorizontalRotationsWithFlag(
                shapeCache.get(Boolean.FALSE), shapeCache.get(Boolean.TRUE), Direction.NORTH
        );

        for (BlockState state : states)
        {
            Direction dir = state.getValue(FramedProperties.FACING_HOR);
            boolean top = state.getValue(FramedProperties.TOP);
            map.put(state, shapes[dir.get2DDataValue() + (top ? 4 : 0)]);
        }

        return ShapeProvider.of(map);
    }
}
