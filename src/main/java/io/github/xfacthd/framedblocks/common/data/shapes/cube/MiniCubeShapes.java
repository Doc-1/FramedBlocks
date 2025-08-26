package io.github.xfacthd.framedblocks.common.data.shapes.cube;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.shapes.ShapeProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class MiniCubeShapes
{
    public static ShapeProvider generate(ImmutableList<BlockState> states)
    {
        VoxelShape bottomShape = Block.box(4, 0, 4, 12, 8, 12);
        VoxelShape topShape = Block.box(4, 8, 4, 12, 16, 12);

        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();
        for (BlockState state : states)
        {
            boolean top = state.getValue(FramedProperties.TOP);
            builder.put(state, top ? topShape : bottomShape);
        }
        return ShapeProvider.of(builder.build());
    }



    private MiniCubeShapes() { }
}
