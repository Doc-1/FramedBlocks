package io.github.xfacthd.framedblocks.common.data.shapes.pane;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.shapes.ShapeProvider;
import io.github.xfacthd.framedblocks.api.shapes.ShapeUtils;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.SlopeType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class CornerStripShapes
{
    public static ShapeProvider generate(ImmutableList<BlockState> states)
    {
        VoxelShape[] shapes = ShapeUtils.makeHorizontalRotationsWithFlag(
                Block.box(0,  0, 0, 16,  1, 1),
                Block.box(0, 15, 0, 16, 16, 1),
                Direction.NORTH
        );
        VoxelShape[] vertShapes = ShapeUtils.makeHorizontalRotations(
                Block.box(0, 0, 0, 1, 16, 1),
                Direction.NORTH
        );

        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();
        for (BlockState state : states)
        {
            Direction dir = state.getValue(FramedProperties.FACING_HOR);
            SlopeType type = state.getValue(PropertyHolder.SLOPE_TYPE);
            if (type == SlopeType.HORIZONTAL)
            {
                builder.put(state, vertShapes[dir.get2DDataValue()]);
            }
            else
            {
                int offset = type == SlopeType.TOP ? 4 : 0;
                builder.put(state, shapes[dir.get2DDataValue() + offset]);
            }
        }
        return ShapeProvider.of(builder.build());
    }



    private CornerStripShapes() { }
}
