package xfacthd.framedblocks.api.model.item.block;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;

public final class RegisterBlockItemModelProvidersEvent extends Event implements IModBusEvent
{
    private final BiConsumer<ResourceLocation, BlockItemModelProvider> registrar;

    @ApiStatus.Internal
    public RegisterBlockItemModelProvidersEvent(BiConsumer<ResourceLocation, BlockItemModelProvider> registrar)
    {
        this.registrar = registrar;
    }

    public void register(ResourceLocation id, BlockItemModelProvider tintProvider)
    {
        registrar.accept(id, tintProvider);
    }
}
