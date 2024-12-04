package xfacthd.framedblocks.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.common.datagen.providers.*;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = FramedConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class GeneratorHandler
{
    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client event)
    {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(true, new FramedSpriteSourceProvider(output, lookupProvider, fileHelper));
        gen.addProvider(true, new FramedBlockStateProvider(output, fileHelper));
        gen.addProvider(true, new FramedItemModelProvider(output, fileHelper));
        gen.addProvider(true, new FramedLanguageProvider(output));

        gen.addProvider(true, new FramedLootTableProvider(output, lookupProvider));
        gen.addProvider(true, new FramedRecipeProvider.Runner(output, lookupProvider));
        gen.addProvider(true, new FramingSawRecipeProvider.Runner(output, lookupProvider));
        BlockTagsProvider tagProvider = new FramedBlockTagProvider(output, lookupProvider, fileHelper);
        gen.addProvider(true, tagProvider);
        gen.addProvider(true, new FramedItemTagProvider(output, lookupProvider, tagProvider.contentsGetter(), fileHelper));
    }



    private GeneratorHandler() { }
}
