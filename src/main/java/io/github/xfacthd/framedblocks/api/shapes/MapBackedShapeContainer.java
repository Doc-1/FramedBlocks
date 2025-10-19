package io.github.xfacthd.framedblocks.api.shapes;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

final class MapBackedShapeContainer implements ShapeContainer
{
    private final Map<BlockState, VoxelShape> shapes;

    MapBackedShapeContainer(Map<BlockState, VoxelShape> shapes)
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

    Map<BlockState, VoxelShape> getShapes()
    {
        return shapes;
    }

    @Nullable
    static MapBackedShapeContainer unwrap(@Nullable ShapeContainer provider)
    {
        if (provider == null || provider.isEmpty())
        {
            return null;
        }
        if (provider instanceof MapBackedShapeContainer mapBacked)
        {
            return mapBacked;
        }
        throw new IllegalArgumentException("Expected MapBackedShapeProvider, got " + provider);
    }
}
