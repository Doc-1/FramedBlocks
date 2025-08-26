package io.github.xfacthd.framedblocks.common.data.shapes.stairs.standard;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.shapes.CommonShapes;
import io.github.xfacthd.framedblocks.api.shapes.ShapeProvider;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class DoubleHalfStairsShapes
{
    public static ShapeProvider generate(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        for (BlockState state : states)
        {
            Direction dir = state.getValue(FramedProperties.FACING_HOR);
            boolean right = state.getValue(PropertyHolder.RIGHT);
            dir = right ? dir.getClockWise() : dir.getCounterClockWise();
            builder.put(state, CommonShapes.PANEL.get(dir));
        }

        return ShapeProvider.of(builder.build());
    }



    private DoubleHalfStairsShapes() { }
}
