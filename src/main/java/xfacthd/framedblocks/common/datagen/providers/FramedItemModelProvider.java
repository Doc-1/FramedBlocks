package xfacthd.framedblocks.common.datagen.providers;

import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.compat.ae2.AppliedEnergisticsCompat;

@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue" })
public final class FramedItemModelProvider extends ItemModelProvider
{
    public FramedItemModelProvider(PackOutput output, ExistingFileHelper fileHelper)
    {
        super(output, FramedConstants.MOD_ID, fileHelper);
    }

    @Override
    protected void registerModels()
    {
        handheldItem(FBContent.ITEM_FRAMED_HAMMER, "cutout");
        handheldItem(FBContent.ITEM_FRAMED_WRENCH, "cutout");
        handheldItem(FBContent.ITEM_FRAMED_KEY, "cutout");
        handheldItem(FBContent.ITEM_FRAMED_SCREWDRIVER, "cutout");

        simpleItem(FBContent.ITEM_FRAMED_REINFORCEMENT, "cutout");
        simpleItem(FBContent.ITEM_PHANTOM_PASTE, "cutout");

        ResourceLocation patternTexture = Utils.rl("ae2", "item/crafting_pattern");
        if (!AppliedEnergisticsCompat.isLoaded())
        {
            // Pretend that the texture exists when AE2 is not present so this doesn't crash
            existingFileHelper.trackGenerated(patternTexture, ModelProvider.TEXTURE);
        }
        singleTexture("framing_saw_pattern", mcLoc("item/generated"), "layer0", patternTexture);

        simpleItem(FBContent.ITEM_FRAMED_BLUEPRINT, "cutout");
        simpleItem("framed_blueprint_written", "cutout");
    }

    private ItemModelBuilder handheldItem(Holder<Item> item, String renderType)
    {
        String name = Utils.getKeyOrThrow(item).location().getPath();
        return singleTexture(name, mcLoc("item/handheld"), "layer0", modLoc("item/" + name)).renderType(renderType);
    }

    private ItemModelBuilder simpleItem(Holder<Item> item, String renderType)
    {
        return simpleItem(Utils.getKeyOrThrow(item).location().getPath(), renderType);
    }

    private ItemModelBuilder simpleItem(String name, String renderType)
    {
        return singleTexture(name, mcLoc("item/generated"), "layer0", modLoc("item/" + name)).renderType(renderType);
    }

    private ItemModelBuilder builder(Holder<Item> item)
    {
        return getBuilder(Utils.getKeyOrThrow(item).location().getPath());
    }
}