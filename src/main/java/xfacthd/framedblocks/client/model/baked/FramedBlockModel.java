package xfacthd.framedblocks.client.model.baked;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.block.cache.StateCache;
import xfacthd.framedblocks.api.camo.CamoContainerHelper;
import xfacthd.framedblocks.api.camo.CamoContent;
import xfacthd.framedblocks.api.camo.block.BlockCamoContent;
import xfacthd.framedblocks.api.camo.empty.EmptyCamoContainer;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.ExtendedBlockModelPart;
import xfacthd.framedblocks.api.model.cache.QuadCacheKey;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.DefaultAO;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.geometry.PartConsumer;
import xfacthd.framedblocks.api.model.geometry.QuadListModifier;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.model.wrapping.DelegateBlockModelPart;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.predicate.contex.ConTexMode;
import xfacthd.framedblocks.api.type.IBlockType;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.client.model.ReinforcementModel;
import xfacthd.framedblocks.client.overlaygen.OverlayModelPartGenerator;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.config.ClientConfig;
import xfacthd.framedblocks.common.data.PropertyHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

public final class FramedBlockModel extends AbstractFramedBlockModel
{
    private static final FramedBlockData DEFAULT_DATA = new FramedBlockData(EmptyCamoContainer.EMPTY, false);
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final Direction[] DIRECTIONS_WITH_NULL = Arrays.copyOfRange(DIRECTIONS, 0, 7);
    private static final int FLAG_NO_CAMO_ATL_MODEL = 0b001;
    private static final int FLAG_NO_CAMO_REINFORCED = 0b010;
    private static final int FLAG_NO_CAMO_SOLID_BG = 0b100;
    private static final BlockCamoContent[] DEFAULT_NO_CAMO_CONTENTS = makeNoCamoContents(FBContent.BLOCK_FRAMED_CUBE.value().defaultBlockState());
    private static final UnaryOperator<BakedQuad> EMISSIVE_PROCESSOR = quad ->
            new BakedQuad(quad.vertices(), quad.tintIndex(), quad.direction(), quad.sprite(), quad.shade(), 15, quad.hasAmbientOcclusion());
    private static final UnaryOperator<BakedQuad> FULL_EMISSIVE_PROCESSOR = quad ->
            new BakedQuad(quad.vertices(), quad.tintIndex(), quad.direction(), quad.sprite(), false, 15, false);
    private static final UnaryOperator<BakedQuad> TINT_INDEX_INVERTER = ModelUtils::invertTintIndex;

    private final Map<QuadCacheKey, List<ExtendedBlockModelPart>> partCache = new ConcurrentHashMap<>();
    private final BlockState state;
    private final Geometry geometry;
    private final IBlockType type;
    private final boolean forceUngeneratedBaseModel;
    private final boolean useBaseModel;
    private final boolean useSolidBase;
    private final boolean uncachedPostProcess;
    private final StateCache stateCache;
    private final BlockCamoContent[] noCamoContents;

    public FramedBlockModel(GeometryFactory.Context ctx, Geometry geometry)
    {
        super(ctx.baseModel(), ctx.state(), geometry.getItemModelInfo());
        this.state = ctx.state();
        this.geometry = geometry;
        this.type = ((IFramedBlock) state.getBlock()).getBlockType();
        this.forceUngeneratedBaseModel = geometry.forceUngeneratedBaseModel();
        this.useBaseModel = geometry.useBaseModel();
        this.useSolidBase = geometry.useSolidNoCamoModel();
        this.uncachedPostProcess = geometry.hasUncachedPostProcessing();
        this.stateCache = state.framedblocks$getCache();
        boolean isBaseCube = state.getBlock() == FBContent.BLOCK_FRAMED_CUBE.value();
        this.noCamoContents = isBaseCube ? makeNoCamoContents(state) : DEFAULT_NO_CAMO_CONTENTS;

        Preconditions.checkState(
                this.useBaseModel || !this.forceUngeneratedBaseModel,
                "Geometry::useBaseModel() must return true when Geometry::forceUngeneratedBaseModel() returns true"
        );
        Preconditions.checkState(
                !this.useSolidBase || !this.useBaseModel,
                "Geometry#useSolidNoCamoModel() and Geometry#useBaseModel() cannot both return true"
        );
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState unusedState, RandomSource random, List<BlockModelPart> partsOut)
    {
        ModelData extraData = level.getModelData(pos);
        FramedBlockData fbData = AbstractFramedBlockData.getOrDefault(extraData, this.state, DEFAULT_DATA);
        CamoContent<?> camoContent = fbData.getCamoContent();

        boolean empty = camoContent.isEmpty();
        BlockStateModel camoModel;
        boolean needCtCtxUncached = false;
        boolean needCtCtxCached = false;
        boolean reinforce = empty && useBaseModel && fbData.isReinforced();
        DefaultAO defaultAO = geometry.computeDefaultAmbientOcclusion(this.state, extraData);
        boolean camoEmissive;
        boolean forceEmissive = fbData.isEmissive();
        boolean secondPart = fbData.isSecondPart();
        long seed = state.getSeed(pos);

        if (empty)
        {
            camoContent = getNoCamoModelSourceContent(fbData);
            camoModel = getCamoModel(camoContent, useBaseModel, secondPart);
            camoEmissive = false;
        }
        else
        {
            boolean canCt = type.supportsConnectedTextures();
            needCtCtxUncached = canCt && needCtContext(true, type.getMinimumConTexMode());
            needCtCtxCached = canCt && needCtContext(false, type.getMinimumConTexMode());

            camoModel = getCamoModel(camoContent, false, false);
            camoEmissive = camoContent.isEmissive();
        }

        random.setSeed(seed);
        List<BlockModelPart> srcPartsUncached = ModelUtils.collectModelParts(camoModel, level, pos, this.state, random, needCtCtxUncached);
        int uncachedFaceMask = fbData.computeFaceMask(stateCache, false);
        PartConsumer partConsumer = makePartConsumer(partsOut, geometry, uncachedFaceMask, defaultAO, uncachedPostProcess, camoEmissive, forceEmissive, secondPart);

        BlockState camoState = camoContent.getAsBlockState();
        for (BlockModelPart modelPart : srcPartsUncached)
        {
            partConsumer.accept(modelPart, camoState, false, true, true, true, camoState, null);
        }
        if (!(empty && forceUngeneratedBaseModel))
        {
            random.setSeed(seed);
            Object ctCtx = needCtCtxCached ? camoModel.createGeometryKey(level, pos, this.state, random) : null;
            random.setSeed(seed);
            QuadCacheKey key = geometry.makeCacheKey(level, pos, random, camoContent, ctCtx, forceEmissive, extraData);
            List<ExtendedBlockModelPart> cachedParts = partCache.get(key);
            if (cachedParts == null)
            {
                random.setSeed(seed);
                List<BlockModelPart> srcParts = ModelUtils.collectModelParts(camoModel, level, pos, this.state, random, ctCtx != null);
                cachedParts = buildPartCache(key, srcParts, level, pos, random, seed, extraData, reinforce, camoEmissive, forceEmissive, secondPart, defaultAO);
                partCache.put(key, cachedParts);
            }
            int cachedFaceMask = fbData.computeFaceMask(stateCache, true);
            for (ExtendedBlockModelPart part : cachedParts)
            {
                partsOut.add(new CullableBlockModelPart(part, cachedFaceMask));
            }
        }
        random.setSeed(seed);
        geometry.collectAdditionalPartsUncached(partConsumer, level, pos, random, extraData);
        if (reinforce)
        {
            BlockModelPart reinforcement = ReinforcementModel.getFiltered(uncachedFaceMask, defaultAO.apply(TriState.DEFAULT));
            if (uncachedPostProcess || forceEmissive)
            {
                partConsumer.accept(reinforcement, ReinforcementModel.SHADER_STATE, false, false, true, false, ReinforcementModel.SHADER_STATE, null);
            }
            else
            {
                partsOut.add(reinforcement);
            }
        }
    }

    private static PartConsumer makePartConsumer(
            List<? super ExtendedBlockModelPart> destParts,
            Geometry geometry,
            int cullMask,
            DefaultAO defaultAO,
            boolean uncachedPostProcess,
            boolean camoEmissive,
            boolean forceEmissive,
            boolean invertTintIndex
    )
    {
        return (part, partState, includeNull, reclaimFromNull, cullNonNull, camoPart, shaderState, modifier) ->
        {
            Preconditions.checkArgument(!(includeNull && reclaimFromNull), "Cannot both include null faces and reclaim cullable faces from them");

            QuadMap quadMap = new QuadMap();
            boolean hasModifier = modifier != null;
            for (Direction side : DIRECTIONS_WITH_NULL)
            {
                boolean nullSide = side == null;
                if (nullSide && !includeNull) continue;
                if (!nullSide && cullNonNull && isSideHidden(cullMask, side)) continue;

                List<BakedQuad> srcQuads = part.getQuads(side);
                ArrayList<BakedQuad> quads = hasModifier ? new ArrayList<>(srcQuads.size()) : quadMap.get(side);
                Utils.copyAll(srcQuads, quads);
                if (!nullSide && quads.isEmpty() && reclaimFromNull)
                {
                    ModelUtils.getFilteredNullQuads(quads, part, side);
                }
                if (hasModifier)
                {
                    modifier.modify(quadMap, quads, side);
                    // Copy to final destination at the end in case the modifier wants to iterate or clear the list
                    Utils.copyAll(quads, quadMap.get(side));
                }
            }
            if ((camoPart && camoEmissive) || forceEmissive || uncachedPostProcess || invertTintIndex)
            {
                for (Direction side : DIRECTIONS_WITH_NULL)
                {
                    ArrayList<BakedQuad> quads = quadMap.get(side);
                    if (quads.isEmpty()) continue;

                    if (forceEmissive)
                    {
                        quads.replaceAll(FULL_EMISSIVE_PROCESSOR);
                    }
                    else if (camoPart && camoEmissive)
                    {
                        quads.replaceAll(EMISSIVE_PROCESSOR);
                    }
                    if (uncachedPostProcess)
                    {
                        geometry.postProcessUncachedQuads(quads);
                    }
                    if (invertTintIndex)
                    {
                        quads.replaceAll(TINT_INDEX_INVERTER);
                    }
                }
            }
            destParts.add(ModelUtils.makeModelPart(part, quadMap, partState, defaultAO, shaderState));
        };
    }

    private static boolean isSideHidden(int cullMask, @Nullable Direction side)
    {
        return side != null && (cullMask & (1 << side.ordinal())) == 0;
    }

    private List<ExtendedBlockModelPart> buildPartCache(
            QuadCacheKey cacheKey,
            List<BlockModelPart> srcParts,
            BlockAndTintGetter level,
            BlockPos pos,
            RandomSource random,
            long seed,
            ModelData data,
            boolean reinforce,
            boolean camoEmissive,
            boolean forceEmissive,
            boolean secondPart,
            DefaultAO defaultAO
    )
    {
        ArrayList<ExtendedBlockModelPart> parts = new ArrayList<>();
        int cullMask = DEFAULT_DATA.computeFaceMask(stateCache, true);
        PartConsumer partConsumer = makePartConsumer(parts, geometry, cullMask, defaultAO, false, camoEmissive, forceEmissive, secondPart);
        boolean xformAll = geometry.transformAllQuads();

        QuadListModifier modifier = (quadMap, quads, side) ->
        {
            ArrayList<BakedQuad> srcQuads = Utils.copyAll(quads, new ArrayList<>(quads.size()));
            quads.clear();

            for (BakedQuad quad : srcQuads)
            {
                geometry.transformQuad(quadMap, quad, data);
            }
        };

        BlockState camoState = cacheKey.camo().getAsBlockState();
        for (BlockModelPart srcPart : srcParts)
        {
            partConsumer.accept(srcPart, camoState, false, true, !xformAll, true, camoState, modifier);
        }
        if (reinforce)
        {
            BlockModelPart srcPart = ReinforcementModel.getFiltered(xformAll ? 0b00111111 : cullMask, defaultAO.apply(TriState.DEFAULT));
            partConsumer.accept(srcPart, ReinforcementModel.SHADER_STATE, false, true, !xformAll, false, ReinforcementModel.SHADER_STATE, modifier);
        }
        random.setSeed(seed);
        geometry.collectAdditionalPartsCached(partConsumer, level, pos, random, data, cacheKey);

        OverlayModelPartGenerator overlayGenerator = new OverlayModelPartGenerator(parts, defaultAO.apply(TriState.DEFAULT));
        random.setSeed(seed);
        geometry.generateOverlayParts(overlayGenerator, random, data, cacheKey);
        overlayGenerator.flush();

        return new ObjectArrayList<>(parts);
    }

    private static boolean needCtContext(boolean mayHaveUncachedQuads, ConTexMode minMode)
    {
        ConTexMode cfgMode = ClientConfig.VIEW.getConTexMode();
        if (cfgMode == ConTexMode.NONE)
        {
            return false;
        }
        return mayHaveUncachedQuads || (cfgMode.atleast(ConTexMode.FULL_EDGE) && cfgMode.atleast(minMode));
    }

    private CamoContent<?> getNoCamoModelSourceContent(FramedBlockData fbData)
    {
        int idx = 0;
        if (fbData.isSecondPart()) idx |= FLAG_NO_CAMO_ATL_MODEL;
        if (fbData.isReinforced()) idx |= FLAG_NO_CAMO_REINFORCED;
        if (ClientConfig.VIEW.getSolidFrameMode().useSolidFrame(useSolidBase)) idx |= FLAG_NO_CAMO_SOLID_BG;
        return noCamoContents[idx];
    }

    private static BlockCamoContent[] makeNoCamoContents(BlockState state)
    {
        BlockCamoContent[] contents = new BlockCamoContent[1 << 3];
        for (int i = 0; i < contents.length; i++)
        {
            BlockState stateOut = state;
            if ((i & FLAG_NO_CAMO_ATL_MODEL) != 0) stateOut = stateOut.setValue(PropertyHolder.ALT, true);
            if ((i & FLAG_NO_CAMO_REINFORCED) != 0) stateOut = stateOut.setValue(PropertyHolder.REINFORCED, true);
            if ((i & FLAG_NO_CAMO_SOLID_BG) != 0) stateOut = stateOut.setValue(PropertyHolder.SOLID_BG, true);
            contents[i] = new BlockCamoContent(stateOut);
        }
        return contents;
    }

    /**
     * Return the {@link BlockStateModel} to use as the camo model for the given camoState
     *
     * @param camoContent The {@link CamoContent} used as camo
     * @param useBaseModel If true, the {@link DelegateBlockStateModel#delegate} is requested instead of the model of the given state
     * @param useAltModel Whether an alternative base model for the second component of a double model should be used
     *                    (only has an effect if {@code useBaseModel} is true)
     *
     * @apiNote Most models shouldn't need to override this. If the model loaded from JSON should be used when no camo
     * is applied, return true from {@link Geometry#useBaseModel()}. If the model loaded from JSON should be
     * used without applying any quad modifications when no camo is applied, return true from
     * {@link Geometry#forceUngeneratedBaseModel()} as well
     */
    private BlockStateModel getCamoModel(CamoContent<?> camoContent, boolean useBaseModel, boolean useAltModel)
    {
        if (useBaseModel)
        {
            return geometry.getBaseModel(delegate, useAltModel);
        }
        return CamoContainerHelper.Client.getOrCreateModel(camoContent);
    }

    @Override
    public void clearCache()
    {
        super.clearCache();
        partCache.clear();
    }

    private record CullableBlockModelPart(ExtendedBlockModelPart wrapped, int cullMask) implements DelegateBlockModelPart
    {
        @Override
        public List<BakedQuad> getQuads(@Nullable Direction side)
        {
            return isSideHidden(cullMask, side) ? Collections.emptyList() : wrapped.getQuads(side);
        }
    }
}
