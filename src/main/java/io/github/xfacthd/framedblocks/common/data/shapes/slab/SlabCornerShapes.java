package io.github.xfacthd.framedblocks.common.data.shapes.slab;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.shapes.ShapeProvider;
import io.github.xfacthd.framedblocks.api.shapes.ShapeUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class SlabCornerShapes
{
    public static ShapeProvider generate(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        VoxelShape shapeBot = Block.box(0, 0, 0, 8,  8, 8);
        VoxelShape shapeTop = Block.box(0, 8, 0, 8, 16, 8);
        VoxelShape[] shapes = ShapeUtils.makeHorizontalRotationsWithFlag(shapeBot, shapeTop, Direction.NORTH);

        for (BlockState state : states)
        {
            Direction dir = state.getValue(FramedProperties.FACING_HOR);
            boolean top = state.getValue(FramedProperties.TOP);
            builder.put(state, shapes[dir.get2DDataValue() + (top ? 4 : 0)]);
        }

        return ShapeProvider.of(builder.build());
    }



    private SlabCornerShapes() { }
}
