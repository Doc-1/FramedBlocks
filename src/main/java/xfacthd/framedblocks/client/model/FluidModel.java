package xfacthd.framedblocks.client.model;

import com.google.common.base.Preconditions;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.NamedRenderTypeManager;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.NeoForgeModelProperties;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.util.Utils;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class FluidModel implements BakedModel
{
    private static final ModelState SIMPLE_STATE = new SimpleModelState(Transformation.identity());
    public static final ResourceLocation BARE_MODEL = Utils.rl("fluid/bare");
    public static final ResourceLocation BARE_MODEL_SINGLE = Utils.rl("fluid/bare_single");
    @SuppressWarnings("deprecation")
    private static final ResourceLocation BLOCK_ATLAS = TextureAtlas.LOCATION_BLOCKS;
    private static final Function<ResourceLocation, TextureAtlasSprite> SPRITE_GETTER = (loc ->
            Minecraft.getInstance().getTextureAtlas(BLOCK_ATLAS).apply(loc)
    );
    private static final ModelBakery.TextureGetter TEXTURE_GETTER = new ModelBakery.TextureGetter()
    {
        @Override
        public TextureAtlasSprite get(ModelDebugName modelName, Material material)
        {
            return material.sprite();
        }

        @Override
        public TextureAtlasSprite reportMissingReference(ModelDebugName modelName, String ref)
        {
            return SPRITE_GETTER.apply(MissingTextureAtlasSprite.getLocation());
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
    private static final ContextMap BAKING_PROPERTIES = new ContextMap.Builder()
            .withParameter(
                    NeoForgeModelProperties.RENDER_TYPE,
                    NamedRenderTypeManager.get(ResourceLocation.withDefaultNamespace("translucent"))
            )
            .create(ContextKeySet.EMPTY);
    private final RenderType fluidLayer;
    private final ChunkRenderTypeSet fluidLayerSet;
    private final Map<Direction, List<BakedQuad>> quads;
    private final TextureAtlasSprite particles;

    private FluidModel(RenderType fluidLayer, Map<Direction, List<BakedQuad>> quads, TextureAtlasSprite particles)
    {
        this.fluidLayer = fluidLayer;
        this.fluidLayerSet = ChunkRenderTypeSet.of(fluidLayer);
        this.quads = quads;
        this.particles = particles;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random)
    {
        return getQuads(state, side, random, ModelData.EMPTY, RenderType.translucent());
    }

    @Override
    public List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            RandomSource rand,
            ModelData extraData,
            RenderType layer
    )
    {
        if (side == null || layer != fluidLayer)
        {
            return Collections.emptyList();
        }
        return quads.get(side);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data)
    {
        return fluidLayerSet;
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return false;
    }

    @Override
    public boolean isGui3d()
    {
        return false;
    }

    @Override
    public boolean usesBlockLight()
    {
        return false;
    }

    @Override
    public ItemTransforms getTransforms()
    {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return particles;
    }



    public static FluidModel create(Fluid fluid)
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

        ResourceLocation fluidName = Preconditions.checkNotNull(
                NeoForgeRegistries.FLUID_TYPES.getKey(fluid.getFluidType()),
                "Cannot create FluidModel for unregistered FluidType of fluid %s",
                fluid
        );
        ModelResourceLocation modelName = new ModelResourceLocation(
                Utils.rl("fluid/" + fluidName.toString().replace(":", "_")),
                "framedblocks_dynamic_fluid"
        );
        ModelBakery.ModelBakerImpl baker = modelBakery.new ModelBakerImpl(TEXTURE_GETTER, modelName::toString);

        boolean singleTexture = flowingTexture.equals(stillTexture);
        UnbakedModel bareModel = baker.getModel(singleTexture ? BARE_MODEL_SINGLE : BARE_MODEL);
        Preconditions.checkNotNull(bareModel, "Bare fluid model not loaded!");

        TextureSlots textures = new TextureSlots(Map.of(
                "still", new Material(BLOCK_ATLAS, stillTexture),
                "flowing", new Material(BLOCK_ATLAS, flowingTexture)
        ));
        BakedModel model = bareModel.bake(textures, baker, SIMPLE_STATE, true, true, ItemTransforms.NO_TRANSFORMS, BAKING_PROPERTIES);
        Preconditions.checkNotNull(model, "Failed to bake fluid model for fluid %s", fluid);

        Map<Direction, List<BakedQuad>> quads = new EnumMap<>(Direction.class);
        BlockState defState = fluid.defaultFluidState().createLegacyBlock();
        RandomSource random = RandomSource.create();
        RenderType layer = ItemBlockRenderTypes.getRenderLayer(fluid.defaultFluidState());

        for (Direction side : Direction.values())
        {
            quads.put(side, model.getQuads(defState, side, random, ModelData.EMPTY, layer));
        }

        return new FluidModel(layer, quads, SPRITE_GETTER.apply(stillTexture));
    }

    private static IClientFluidTypeExtensions getFluidTypeExtensions(Fluid fluid)
    {
        if (fluid == Fluids.EMPTY)
        {
            return DUMMY_FLUID_TYPE_EXTENSIONS;
        }
        return IClientFluidTypeExtensions.of(fluid);
    }
}
