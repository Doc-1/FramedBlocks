package io.github.xfacthd.framedblocks.client.model;

import com.google.common.base.Preconditions;
import io.github.xfacthd.framedblocks.api.util.ClientUtils;
import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
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

import java.util.Map;

public final class FluidModel
{
    public static final ResourceLocation BARE_MODEL = Utils.rl("fluid/bare");
    public static final ResourceLocation BARE_MODEL_SINGLE = Utils.rl("fluid/bare_single");
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
                "end", new Material(ClientUtils.BLOCK_ATLAS, stillTexture),
                "side", new Material(ClientUtils.BLOCK_ATLAS, flowingTexture),
                "particle", new Material(ClientUtils.BLOCK_ATLAS, stillTexture)
        ));
        QuadCollection fluidQuads = bareModel.getTopGeometry().bake(
                textures,
                baker,
                BlockModelRotation.X0_Y0,
                bareModel,
                bareModel.getTopAdditionalProperties()
        );
        Preconditions.checkNotNull(fluidQuads, "Failed to bake fluid model for fluid %s", fluid);
        ChunkSectionLayer chunkLayer = ItemBlockRenderTypes.getRenderLayer(fluid.defaultFluidState());

        return new SingleVariant(new SimpleModelWrapper(fluidQuads, false, ClientUtils.getBlockSprite(stillTexture), chunkLayer));
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
