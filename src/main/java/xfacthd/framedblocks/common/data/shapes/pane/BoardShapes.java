package xfacthd.framedblocks.common.data.shapes.pane;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;
import xfacthd.framedblocks.api.shapes.ShapeProvider;
import xfacthd.framedblocks.api.shapes.ShapeUtils;

public final class BoardShapes
{
    public static ShapeProvider generate(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        VoxelShape shape = Block.box(0, 0, 0, 16, 16, 1);
        VoxelShape[] shapesHor = ShapeUtils.makeHorizontalRotations(shape, Direction.NORTH);
        VoxelShape shapeBottom = Block.box(0, 0, 0, 16, 1, 16);
        VoxelShape shapeTop = Block.box(0, 15, 0, 16, 16, 16);

        for (BlockState state : states)
        {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            builder.put(state, switch (dir)
            {
                case DOWN -> shapeBottom;
                case UP -> shapeTop;
                default -> shapesHor[dir.get2DDataValue()];
            });
        }

        return ShapeProvider.of(builder.build());
    }



    private BoardShapes() { }
}
