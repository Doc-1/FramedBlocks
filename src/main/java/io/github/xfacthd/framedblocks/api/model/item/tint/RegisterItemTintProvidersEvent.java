package io.github.xfacthd.framedblocks.api.model.item.tint;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;

public final class RegisterItemTintProvidersEvent extends Event implements IModBusEvent
{
    private final BiConsumer<Identifier, DynamicItemTintProvider> registrar;

    @ApiStatus.Internal
    public RegisterItemTintProvidersEvent(BiConsumer<Identifier, DynamicItemTintProvider> registrar)
    {
        this.registrar = registrar;
    }

    public void register(Identifier id, DynamicItemTintProvider tintProvider)
    {
        registrar.accept(id, tintProvider);
    }
}
