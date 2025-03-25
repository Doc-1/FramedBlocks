package xfacthd.framedblocks.api.model.util;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.QuadCollection;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelBaker;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import xfacthd.framedblocks.api.model.wrapping.ModelLookup;
import xfacthd.framedblocks.api.model.wrapping.TextureLookup;

public final class StandaloneModels
{
    public static final StandaloneModelBaker<BlockModelPart> BLOCK_PART_STANDALONE_BAKER =
            (model, baker) ->
            {
                TextureSlots textures = model.getTopTextureSlots();
                QuadCollection quads = model.bakeTopGeometry(textures, baker, BlockModelRotation.X0_Y0);
                RenderTypeGroup renderTypeGroup = model.getTopAdditionalProperties().getOptional(NeoForgeModelProperties.RENDER_TYPE);
                RenderType renderType = renderTypeGroup == null || renderTypeGroup.isEmpty() ? null : renderTypeGroup.block();
                return new SimpleModelWrapper(quads, model.getTopAmbientOcclusion(), model.resolveParticleSprite(textures, baker), renderType);
            };
    public static final StandaloneModelBaker<BlockStateModel> BLOCK_STATE_STANDALONE_BAKER =
            (model, baker) -> new SingleVariant(BLOCK_PART_STANDALONE_BAKER.bake(model, baker));
    public static final StandaloneModelBaker<QuadCollection> QUAD_COLLECTION_STANDALONE_BAKER =
            (model, baker) -> model.bakeTopGeometry(model.getTopTextureSlots(), baker, BlockModelRotation.X0_Y0);

    public static BlockStateModel getBlockModel(ModelLookup modelLookup, StandaloneModelKey<BlockStateModel> key)
    {
        BlockStateModel model = modelLookup.getStandaloneModel(key);
        return model != null ? model : modelLookup.getMissingBlockModel();
    }

    public static BlockModelPart getBlockModelPart(ModelLookup modelLookup, TextureLookup textureLookup, StandaloneModelKey<BlockModelPart> key)
    {
        BlockModelPart part = modelLookup.getStandaloneModel(key);
        if (part != null)
        {
            return part;
        }
        TextureAtlasSprite missingSprite = textureLookup.get(MissingTextureAtlasSprite.getLocation());
        return new SimpleModelWrapper(QuadCollection.EMPTY, false, missingSprite, RenderType.solid());
    }

    public static QuadCollection getQuadCollection(ModelLookup modelLookup, StandaloneModelKey<QuadCollection> key)
    {
        QuadCollection quads = modelLookup.getStandaloneModel(key);
        return quads != null ? quads : QuadCollection.EMPTY;
    }



    private StandaloneModels() { }
}
