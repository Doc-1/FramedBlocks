package io.github.xfacthd.framedblocks.common.datagen.providers;

import io.github.xfacthd.framedblocks.api.datagen.models.AbstractFramedItemModelProvider;
import io.github.xfacthd.framedblocks.api.util.FramedConstants;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.client.model.item.property.BlueprintProperty;
import io.github.xfacthd.framedblocks.client.model.loader.fallback.FallbackLoaderBuilder;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.compat.ae2.AppliedEnergisticsCompat;
import io.github.xfacthd.framedblocks.common.datagen.GeneratorHandler;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.Objects;
import java.util.stream.Stream;

public final class FramedItemModelProvider extends AbstractFramedItemModelProvider
{
    private static final ModelTemplate FLAT_CUTOUT = ModelTemplates.FLAT_ITEM.extend().renderType("cutout").build();
    private static final ModelTemplate HANDHELD_CUTOUT = ModelTemplates.FLAT_HANDHELD_ITEM.extend().renderType("cutout").build();

    public FramedItemModelProvider(PackOutput output)
    {
        super(output, FramedConstants.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        itemModels.generateFlatItem(FBContent.ITEM_FRAMED_HAMMER.value(), HANDHELD_CUTOUT);
        itemModels.generateFlatItem(FBContent.ITEM_FRAMED_WRENCH.value(), HANDHELD_CUTOUT);
        itemModels.generateFlatItem(FBContent.ITEM_FRAMED_KEY.value(), HANDHELD_CUTOUT);
        itemModels.generateFlatItem(FBContent.ITEM_FRAMED_SCREWDRIVER.value(), HANDHELD_CUTOUT);

        itemModels.generateFlatItem(FBContent.ITEM_FRAMED_REINFORCEMENT.value(), FLAT_CUTOUT);
        itemModels.generateFlatItem(FBContent.ITEM_PHANTOM_PASTE.value(), FLAT_CUTOUT);
        itemModels.generateFlatItem(FBContent.ITEM_GLOW_PASTE.value(), FLAT_CUTOUT);

        ResourceLocation patternTexture = Utils.rl(AppliedEnergisticsCompat.MOD_ID, "item/crafting_pattern");
        Item patternItem = Objects.requireNonNull(GeneratorHandler.framingSawPattern).asItem();
        itemModels.itemModelOutput.accept(patternItem, ItemModelUtils.plainModel(
                ModelTemplates.FLAT_ITEM
                        .extend()
                        .customLoader(FallbackLoaderBuilder::new, loader ->
                                loader.addCondition(new ModLoadedCondition(AppliedEnergisticsCompat.MOD_ID))
                                        // Random fallback to avoid texture reference errors when AE2 is not present
                                        .setFallback(mcLocation("item/paper"))
                        )
                        .build()
                        .create(patternItem, TextureMapping.layer0(patternTexture), itemModels.modelOutput)
        ));

        itemModels.itemModelOutput.accept(FBContent.ITEM_FRAMED_BLUEPRINT.value(), ItemModelUtils.conditional(
                BlueprintProperty.INSTANCE,
                ItemModelUtils.plainModel(itemModels.createFlatItemModel(FBContent.ITEM_FRAMED_BLUEPRINT.value(), "_written", FLAT_CUTOUT)),
                ItemModelUtils.plainModel(itemModels.createFlatItemModel(FBContent.ITEM_FRAMED_BLUEPRINT.value(), FLAT_CUTOUT))
        ));
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems()
    {
        return Stream.of(
                FBContent.ITEM_FRAMED_HAMMER,
                FBContent.ITEM_FRAMED_WRENCH,
                FBContent.ITEM_FRAMED_KEY,
                FBContent.ITEM_FRAMED_SCREWDRIVER,
                FBContent.ITEM_FRAMED_REINFORCEMENT,
                FBContent.ITEM_PHANTOM_PASTE,
                FBContent.ITEM_GLOW_PASTE,
                FBContent.ITEM_FRAMED_BLUEPRINT
        );
    }
}
