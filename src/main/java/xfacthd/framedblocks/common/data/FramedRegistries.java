package xfacthd.framedblocks.common.data;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import xfacthd.framedblocks.api.blueprint.AuxBlueprintData;
import xfacthd.framedblocks.api.camo.CamoContainerFactory;
import xfacthd.framedblocks.api.util.FramedConstants;

import java.util.function.Consumer;

public final class FramedRegistries
{
    public static final Registry<CamoContainerFactory<?>> CAMO_CONTAINER_FACTORIES = create(
            FramedConstants.CAMO_CONTAINER_FACTORY_REGISTRY_KEY,
            builder -> builder.sync(true)
    );
    public static final Registry<AuxBlueprintData.Type<?>> AUX_BLUEPRINT_DATA_TYPES = create(
            FramedConstants.AUX_BLUEPRINT_DATA_TYPE_REGISTRY_KEY,
            builder -> builder.sync(true)
    );

    private static <T> Registry<T> create(ResourceKey<Registry<T>> key, Consumer<RegistryBuilder<T>> consumer)
    {
        RegistryBuilder<T> builder = new RegistryBuilder<>(key);
        consumer.accept(builder);
        return builder.create();
    }

    public static void onRegisterNewRegistries(final NewRegistryEvent event)
    {
        event.register(CAMO_CONTAINER_FACTORIES);
        event.register(AUX_BLUEPRINT_DATA_TYPES);
    }



    private FramedRegistries() { }
}
