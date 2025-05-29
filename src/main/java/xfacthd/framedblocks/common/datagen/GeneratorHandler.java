package xfacthd.framedblocks.common.datagen;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.common.compat.ae2.AppliedEnergisticsCompat;
import xfacthd.framedblocks.common.datagen.providers.*;

@Mod(value = FramedConstants.MOD_ID, dist = Dist.CLIENT)
@SuppressWarnings("UtilityClassWithPublicConstructor")
public final class GeneratorHandler
{
    @Nullable
    public static DeferredItem<Item> framingSawPattern;

    public GeneratorHandler(IEventBus modBus)
    {
        if (DatagenModLoader.isRunningDataGen())
        {
            modBus.addListener(GeneratorHandler::onGatherData);

            DeferredRegister.Items items = DeferredRegister.createItems(FramedConstants.MOD_ID);
            items.register(modBus);
            framingSawPattern = items.registerSimpleItem(AppliedEnergisticsCompat.SAW_PATTERN_ID);
        }
    }

    private static void onGatherData(final GatherDataEvent.Client event)
    {
        event.createProvider(FramedSpriteSourceProvider::new);
        event.createProvider(FramedBlockModelProvider::new);
        event.createProvider(FramedItemModelProvider::new);
        event.createProvider(FramedLanguageProvider::new);

        event.createProvider(FramedLootTableProvider::new);
        event.createProvider(FramedRecipeProvider.Runner::new);
        event.createProvider(FramingSawRecipeProvider.Runner::new);
        event.createBlockAndItemTags(FramedBlockTagProvider::new, FramedItemTagProvider::new);
        event.createProvider(FramedDataMapProvider::new);
    }
}
