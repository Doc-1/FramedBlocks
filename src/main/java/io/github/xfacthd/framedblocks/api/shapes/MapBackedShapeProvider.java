package io.github.xfacthd.framedblocks.api.shapes;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

final class MapBackedShapeProvider implements ShapeProvider
{
    private final Map<BlockState, VoxelShape> shapes;

    MapBackedShapeProvider(Map<BlockState, VoxelShape> shapes)
    {
        if (!(shapes instanceof IdentityHashMap<BlockState, VoxelShape>))
        {
            shapes = new IdentityHashMap<>(shapes);
        }
        this.shapes = shapes;
    }

    @Override
    public VoxelShape get(BlockState state)
    {
        return shapes.get(state);
    }

    @Override
    public boolean isEmpty()
    {
        return shapes.isEmpty();
    }

    @Override
    public void forEach(BiConsumer<BlockState, VoxelShape> consumer)
    {
        shapes.forEach(consumer);
    }
}
