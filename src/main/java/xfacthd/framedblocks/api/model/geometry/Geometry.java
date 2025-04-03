package xfacthd.framedblocks.api.model.geometry;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.BlockModelPartExtension;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.camo.CamoContent;
import xfacthd.framedblocks.api.model.cache.QuadCacheKey;
import xfacthd.framedblocks.api.model.cache.SimpleQuadCacheKey;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.model.item.ItemModelInfo;
import xfacthd.framedblocks.api.predicate.fullface.FullFacePredicate;
import xfacthd.framedblocks.api.util.ConfigView;

@SuppressWarnings("MethodMayBeStatic")
public abstract class Geometry
{
    /**
     * Called for each {@link BakedQuad} of the camo block's model for whose side this block's
     * {@link FullFacePredicate#test(BlockState, Direction)} returns {@code false}.
     * @param quadMap The target map to put all final quads into
     * @param quad The source quad. Must not be modified directly, use {@link QuadModifier}s to
     *             modify the quad
     * @param data The {@link ModelData}
     */
    public void transformQuad(QuadMap quadMap, BakedQuad quad, ModelData data)
    {
        transformQuad(quadMap, quad);
    }

    /**
     * Called for each {@link BakedQuad} of the camo block's model for whose side this block's
     * {@link FullFacePredicate#test(BlockState, Direction)} returns {@code false}.
     * @param quadMap The target map to put all final quads into
     * @param quad The source quad. Must not be modified directly, use {@link QuadModifier}s to
     *             modify the quad
     */
    public abstract void transformQuad(QuadMap quadMap, BakedQuad quad);

    /**
     * Return true if the base model loaded from JSON should be used when no camo is applied without going
     * through the quad manipulation process
     */
    public boolean forceUngeneratedBaseModel()
    {
        return false;
    }

    /**
     * Return true if the base model loaded from JSON should be used instead of the Framed Cube model
     * when no camo is applied. Quad manipulation will still be done if
     * {@link Geometry#forceUngeneratedBaseModel()} returns false
     * @apiNote Must return true if {@link Geometry#forceUngeneratedBaseModel()} returns true
     */
    public boolean useBaseModel()
    {
        return forceUngeneratedBaseModel();
    }

    /**
     * {@return the {@link BlockStateModel} to use as the base model when no camo is applied}
     * @apiNote Only called if {@link #useBaseModel()} returns {@code true}
     */
    public BlockStateModel getBaseModel(BlockStateModel baseModel, boolean useAltModel)
    {
        return baseModel;
    }

    /**
     * Return true if all quads should be submitted for transformation, even if their cull-face would be filtered
     * by the {@link FullFacePredicate}
     */
    public boolean transformAllQuads()
    {
        return false;
    }

    /**
     * Add additional {@link BlockModelPart}s which should not be cached.
     * The result of this method will NOT be cached, execution should therefore be as fast as possible
     */
    public void collectAdditionalPartsUncached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data) { }

    /**
     * Add additional {@link BlockModelPart}s which should be cached.
     * The result of this method will be cached, processing time is therefore not critical
     */
    public void collectAdditionalPartsCached(PartConsumer consumer, BlockAndTintGetter level, BlockPos pos, RandomSource random, ModelData data, QuadCacheKey cacheKey) { }

    /**
     * Add additional generated quads based on the full set of previously generated quads to avoid z-fighting with the
     * other quads below the overlay on faces that return {@code false} from {@link FullFacePredicate#test(BlockState, Direction)}
     *
     * @param generator The {@link OverlayPartGenerator} used to generate the overlay parts
     * @param cacheKey  The {@link QuadCacheKey} used to cache the generated parts together with other geometry
     */
    public void generateOverlayParts(OverlayPartGenerator generator, RandomSource rand, ModelData data, QuadCacheKey cacheKey) { }

    /**
     * Return a custom {@link QuadCacheKey} that holds additional metadata which influences the resulting quads.
     *
     * @param level    The {@linkplain BlockAndTintGetter level} the block is being rendered in
     * @param pos      The {@link BlockPos} the block is being rendered at
     * @param random   The {@link RandomSource} to use for randomization
     * @param camo     The {@link CamoContent} of the camo applied to the block
     * @param ctCtx    The current connected textures context object, may be null
     * @param emissive Whether the generated quads should be emissive
     * @param data     The {@link ModelData} from the {@link FramedBlockEntity}
     * @implNote The resulting object must at least store the given {@link BlockState}, connected textures context object
     * and emissivity state and should either be a record or have an otherwise properly implemented {@code hashCode()}
     * and {@code equals()} implementation
     */
    public QuadCacheKey makeCacheKey(BlockAndTintGetter level, BlockPos pos, RandomSource random, CamoContent<?> camo, @Nullable Object ctCtx, boolean emissive, ModelData data)
    {
        // Avoid allocating a key if the CT context is null
        return ctCtx != null || emissive ? new SimpleQuadCacheKey(camo, ctCtx, emissive) : camo;
    }

    /**
     * {@return whether the model should use a solid model when no camo is applied}
     * @apiNote Only has an effect if {@link #useBaseModel()} returns {@code false}
     */
    public boolean useSolidNoCamoModel()
    {
        return false;
    }

    /**
     * Compute the default AO behaviour which is used unless the source {@link BlockModelPart} specifies something else
     * @see BlockModelPartExtension#ambientOcclusion()
     */
    public DefaultAO computeDefaultAmbientOcclusion(BlockState state, ModelData data)
    {
        FramedBlockData fbData = AbstractFramedBlockData.getOrDefault(data, state, null);
        if (fbData != null)
        {
            if (fbData.isEmissive())
            {
                return DefaultAO.FORCE_DISABLE;
            }

            CamoContent<?> camoContent = fbData.getCamoContent();
            if (!camoContent.isEmpty() && (camoContent.getLightEmission() != 0 || camoContent.isEmissive()))
            {
                return DefaultAO.DISABLE;
            }
        }
        if (ConfigView.Client.INSTANCE.shouldForceAmbientOcclusionOnGlowingBlocks())
        {
            return DefaultAO.ENABLE;
        }
        return DefaultAO.DEFAULT;
    }

    /**
     * {@return the {@link ItemModelInfo} used to supply camo data and additional transformations to item rendering}
     */
    public ItemModelInfo getItemModelInfo()
    {
        return ItemModelInfo.DEFAULT;
    }
}
