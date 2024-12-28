package xfacthd.framedblocks.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.client.itemmodel.DynamicItemTintProviders;
import xfacthd.framedblocks.common.compat.ae2.AppliedEnergisticsCompat;
import xfacthd.framedblocks.common.datagen.providers.*;

import java.util.concurrent.CompletableFuture;

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
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // TODO: needs a better solution that works when other mods generate data
        DynamicItemTintProviders.init();

        gen.addProvider(true, new FramedSpriteSourceProvider(output, lookupProvider));
        gen.addProvider(true, new FramedBlockModelProvider(output));
        gen.addProvider(true, new FramedItemModelProvider(output));
        gen.addProvider(true, new FramedLanguageProvider(output));

        gen.addProvider(true, new FramedLootTableProvider(output, lookupProvider));
        gen.addProvider(true, new FramedRecipeProvider.Runner(output, lookupProvider));
        gen.addProvider(true, new FramingSawRecipeProvider.Runner(output, lookupProvider));
        BlockTagsProvider tagProvider = new FramedBlockTagProvider(output, lookupProvider);
        gen.addProvider(true, tagProvider);
        gen.addProvider(true, new FramedItemTagProvider(output, lookupProvider, tagProvider.contentsGetter()));
    }
}
