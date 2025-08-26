package io.github.xfacthd.framedblocks.api.shapes;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public interface ShapeGenerator
{
    ShapeGenerator EMPTY = states -> NoShapeProvider.INSTANCE;

    ShapeProvider generate(List<BlockState> states);

    static ShapeGenerator singleShape(VoxelShape shape)
    {
        return states -> new SingleShapeProvider(states, shape);
    }
}
