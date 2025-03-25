package xfacthd.framedblocks.api.model;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelBaker;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelLoader;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Vector3f;
import xfacthd.framedblocks.api.util.ClientUtils;
import xfacthd.framedblocks.api.util.Utils;

public final class ErrorModel
{
    private static final ResourceLocation LOCATION = Utils.rl("item/error");
    @ApiStatus.Internal
    public static final StandaloneModelKey<BakedErrorModel> MODEL_KEY = new StandaloneModelKey<>(LOCATION);
    @ApiStatus.Internal
    public static final StandaloneModelBaker<BakedErrorModel> MODEL_BAKER = (model, baker) ->
    {
        TextureSlots textureSlots = model.getTopTextureSlots();
        QuadCollection quads = model.bakeTopGeometry(textureSlots, baker, BlockModelRotation.X0_Y0);
        ModelRenderProperties renderProps = ModelRenderProperties.fromResolvedModel(baker, model, textureSlots);
        return new BakedErrorModel(quads, renderProps);
    };
    private static final Lazy<Vector3f[]> ITEM_EXTENTS = Lazy.of(() -> BlockModelWrapper.computeExtents(get().getAll()));
    private static QuadCollection errorQuads = QuadCollection.EMPTY;
    @UnknownNullability
    private static ModelRenderProperties itemRenderProperties = null;

    public static QuadCollection get()
    {
        return errorQuads;
    }

    public static void setupForItem(ItemStackRenderState.LayerRenderState layer, ItemDisplayContext ctx)
    {
        layer.setExtents(ITEM_EXTENTS);
        layer.prepareQuadList().addAll(ErrorModel.get().getAll());
        layer.setRenderType(Sheets.solidBlockSheet());
        itemRenderProperties.applyToLayer(layer, ctx);
    }

    @ApiStatus.Internal
    public static void reload(StandaloneModelLoader.BakedModels models)
    {
        BakedErrorModel model = models.get(MODEL_KEY);
        if (model != null)
        {
            errorQuads = model.quads;
            itemRenderProperties = model.renderProps;
        }
        else
        {
            errorQuads = QuadCollection.EMPTY;
            itemRenderProperties = new ModelRenderProperties(
                    true,
                    ClientUtils.getBlockSprite(MissingTextureAtlasSprite.getLocation()),
                    ItemTransforms.NO_TRANSFORMS
            );
        }

        ITEM_EXTENTS.invalidate();
    }

    @ApiStatus.Internal
    public record BakedErrorModel(QuadCollection quads, ModelRenderProperties renderProps) { }

    private ErrorModel() { }
}
