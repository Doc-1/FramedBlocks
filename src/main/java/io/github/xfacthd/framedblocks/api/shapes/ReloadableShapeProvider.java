package io.github.xfacthd.framedblocks.api.shapes;

import io.github.xfacthd.framedblocks.api.internal.InternalAPI;
import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.BiConsumer;

public final class ReloadableShapeProvider implements ShapeProvider
{
    private final ShapeGenerator generator;
    private final List<BlockState> states;
    private ShapeProvider wrapped;

    public static ShapeProvider of(ShapeGenerator generator, List<BlockState> states)
    {
        if (!Utils.PRODUCTION && generator != ShapeGenerator.EMPTY)
        {
            return new ReloadableShapeProvider(generator, states);
        }
        return generator.generate(states);
    }

    private ReloadableShapeProvider(ShapeGenerator generator, List<BlockState> states)
    {
        this.generator = generator;
        this.states = states;
        this.wrapped = generator.generate(states);
        InternalAPI.INSTANCE.registerReloadableShapeProvider(this);
    }

    @Override
    public VoxelShape get(BlockState state)
    {
        return wrapped.get(state);
    }

    @Override
    public boolean isEmpty()
    {
        return wrapped.isEmpty();
    }

    @Override
    public void forEach(BiConsumer<BlockState, VoxelShape> consumer)
    {
        wrapped.forEach(consumer);
    }

    @ApiStatus.Internal
    public void reload()
    {
        wrapped = generator.generate(states);
    }

    @ApiStatus.Internal
    public List<BlockState> getStates()
    {
        return states;
    }
}
