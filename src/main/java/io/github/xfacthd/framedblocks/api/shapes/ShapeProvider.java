package io.github.xfacthd.framedblocks.api.shapes;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public interface ShapeProvider
{
    ShapeProvider EMPTY = NoShapeProvider.INSTANCE;

    VoxelShape get(BlockState state);

    boolean isEmpty();

    void forEach(BiConsumer<BlockState, VoxelShape> consumer);

    static ShapeProvider of(Map<BlockState, VoxelShape> shapes)
    {
        return new MapBackedShapeProvider(shapes);
    }

    static ShapeProvider singleShape(List<BlockState> states, VoxelShape shape)
    {
        return new SingleShapeProvider(states, shape);
    }
}
