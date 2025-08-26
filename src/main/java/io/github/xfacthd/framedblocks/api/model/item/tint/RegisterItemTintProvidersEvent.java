package io.github.xfacthd.framedblocks.api.model.item.tint;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;

public final class RegisterItemTintProvidersEvent extends Event implements IModBusEvent
{
    private final BiConsumer<ResourceLocation, DynamicItemTintProvider> registrar;

    @ApiStatus.Internal
    public RegisterItemTintProvidersEvent(BiConsumer<ResourceLocation, DynamicItemTintProvider> registrar)
    {
        this.registrar = registrar;
    }

    public void register(ResourceLocation id, DynamicItemTintProvider tintProvider)
    {
        registrar.accept(id, tintProvider);
    }
}
