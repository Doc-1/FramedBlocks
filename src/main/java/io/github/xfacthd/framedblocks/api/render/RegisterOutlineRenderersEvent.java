package io.github.xfacthd.framedblocks.api.render;

import io.github.xfacthd.framedblocks.api.block.IBlockType;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;

public final class RegisterOutlineRenderersEvent extends Event implements IModBusEvent
{
    private final BiConsumer<IBlockType, OutlineRenderer<?>> registrar;

    @ApiStatus.Internal
    public RegisterOutlineRenderersEvent(BiConsumer<IBlockType, OutlineRenderer<?>> registrar)
    {
        this.registrar = registrar;
    }

    /**
     * Register an {@link OutlineRenderer} for the given {@link IBlockType}
     * @param type The {@link IBlockType}, must return true for {@link IBlockType#hasSpecialHitbox()}
     */
    public void register(IBlockType type, OutlineRenderer<?> renderer)
    {
        registrar.accept(type, renderer);
    }
}
