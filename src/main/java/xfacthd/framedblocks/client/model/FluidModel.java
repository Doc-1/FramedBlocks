package xfacthd.framedblocks.client.model;

import com.google.common.base.Preconditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.SpriteGetter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelBaker;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import xfacthd.framedblocks.api.util.ClientUtils;
import xfacthd.framedblocks.api.util.Utils;

import java.util.Map;

public final class FluidModel
{
    private static final ResourceLocation BARE_MODEL = Utils.rl("fluid/bare");
    private static final ResourceLocation BARE_MODEL_SINGLE = Utils.rl("fluid/bare_single");
    public static final StandaloneModelKey<Void> BARE_MODEL_KEY = new StandaloneModelKey<>(BARE_MODEL);
    public static final StandaloneModelKey<Void> BARE_MODEL_SINGLE_KEY = new StandaloneModelKey<>(BARE_MODEL_SINGLE);
    @SuppressWarnings("ConstantConditions")
    public static final StandaloneModelBaker<Void> DUMMY_STANDALONE_BAKER = (model, baker) -> null;
    @SuppressWarnings("deprecation")
    private static final ResourceLocation BLOCK_ATLAS = TextureAtlas.LOCATION_BLOCKS;
    private static final SpriteGetter TEXTURE_GETTER = new SpriteGetter()
    {
        @Override
        public TextureAtlasSprite get(Material material, ModelDebugName modelName)
        {
            return material.sprite();
        }

        @Override
        public TextureAtlasSprite reportMissingReference(String ref, ModelDebugName modelName)
        {
            return ClientUtils.getBlockSprite(MissingTextureAtlasSprite.getLocation());
        }
    };
    private static final IClientFluidTypeExtensions DUMMY_FLUID_TYPE_EXTENSIONS = new IClientFluidTypeExtensions()
    {
        @Override
        public ResourceLocation getStillTexture()
        {
            return MissingTextureAtlasSprite.getLocation();
        }

        @Override
        public ResourceLocation getFlowingTexture()
        {
            return MissingTextureAtlasSprite.getLocation();
        }
    };

    public static BlockStateModel create(Fluid fluid)
    {
        ModelBakery modelBakery = Minecraft.getInstance().getModelManager().getModelBakery();

        // Use dummy extensions for empty fluid to prevent crashes due to null textures
        IClientFluidTypeExtensions props = getFluidTypeExtensions(fluid);
        ResourceLocation stillTexture = Preconditions.checkNotNull(
                props.getStillTexture(),
                "Fluid %s returned null from IClientFluidTypeExtensions#getStillTexture()",
                fluid
        );
        ResourceLocation flowingTexture = Preconditions.checkNotNull(
                props.getFlowingTexture(),
                "Fluid %s returned null from IClientFluidTypeExtensions#getFlowingTexture()",
                fluid
        );

        ModelBakery.ModelBakerImpl baker = modelBakery.new ModelBakerImpl(TEXTURE_GETTER);

        boolean singleTexture = flowingTexture.equals(stillTexture);
        ResolvedModel bareModel = baker.getModel(singleTexture ? BARE_MODEL_SINGLE : BARE_MODEL);
        Preconditions.checkNotNull(bareModel, "Bare fluid model not loaded!");

        TextureSlots textures = new TextureSlots(Map.of(
                "end", new Material(BLOCK_ATLAS, stillTexture),
                "side", new Material(BLOCK_ATLAS, flowingTexture),
                "particle", new Material(BLOCK_ATLAS, stillTexture)
        ));
        QuadCollection fluidQuads = bareModel.bakeTopGeometry(textures, baker, BlockModelRotation.X0_Y0);
        Preconditions.checkNotNull(fluidQuads, "Failed to bake fluid model for fluid %s", fluid);
        RenderType layer = ItemBlockRenderTypes.getRenderLayer(fluid.defaultFluidState());

        return new SingleVariant(new SimpleModelWrapper(fluidQuads, false, ClientUtils.getBlockSprite(stillTexture), layer));
    }

    private static IClientFluidTypeExtensions getFluidTypeExtensions(Fluid fluid)
    {
        if (fluid == Fluids.EMPTY)
        {
            return DUMMY_FLUID_TYPE_EXTENSIONS;
        }
        return IClientFluidTypeExtensions.of(fluid);
    }



    private FluidModel() { }
}
