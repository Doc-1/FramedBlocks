package xfacthd.framedblocks.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.ConcatenatedListView;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.blockentity.IFramedDoubleBlockEntity;
import xfacthd.framedblocks.api.camo.empty.EmptyCamoContainer;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.model.item.ItemModelInfo;
import xfacthd.framedblocks.common.block.IFramedDoubleBlock;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockParts;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockStateCache;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockTopInteractionMode;
import xfacthd.framedblocks.common.data.doubleblock.NullCullPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FramedDoubleBlockModel extends AbstractFramedBlockModel
{
    private static final ModelData EMPTY_FRAME = makeDefaultData(false);
    private static final ModelData EMPTY_ALT_FRAME = makeDefaultData(true);

    private final DoubleBlockTopInteractionMode particleMode;
    private final DoubleBlockParts parts;
    private final boolean canCullLeft;
    private final boolean canCullRight;
    @Nullable
    private PartModels models = null;

    public FramedDoubleBlockModel(GeometryFactory.Context ctx, NullCullPredicate cullPredicate, ItemModelInfo itemModelInfo)
    {
        super(ctx.baseModel(), ctx.state(), itemModelInfo);
        BlockState state = ctx.state();
        DoubleBlockStateCache cache = ((IFramedDoubleBlock) state.getBlock()).getCache(state);
        this.parts = cache.getParts();
        this.particleMode = cache.getTopInteractionMode();
        this.canCullLeft = cullPredicate.testLeft(state);
        this.canCullRight = cullPredicate.testRight(state);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType layer)
    {
        ModelData dataLeft = Objects.requireNonNullElse(extraData.get(IFramedDoubleBlockEntity.DATA_ONE), EMPTY_FRAME);
        ModelData dataRight = Objects.requireNonNullElse(extraData.get(IFramedDoubleBlockEntity.DATA_TWO), EMPTY_ALT_FRAME);

        boolean leftVisible = true;
        boolean rightVisible = true;
        if (side == null)
        {
            if (canCullLeft && hasSolidCamo(dataRight)) leftVisible = false;
            if (canCullRight && hasSolidCamo(dataLeft)) rightVisible = false;
            if (!leftVisible && !rightVisible) return List.of();
        }

        List<List<BakedQuad>> quads = new ArrayList<>(2);
        PartModels models = getModels();

        if (leftVisible) quads.add(models.modelOne.getQuads(parts.stateOne(), side, rand, dataLeft, layer));
        if (rightVisible) quads.add(invertTintIndizes(models.modelTwo().getQuads(parts.stateTwo(), side, rand, dataRight, layer)));

        return ConcatenatedListView.of(quads);
    }

    private static boolean hasSolidCamo(ModelData data)
    {
        FramedBlockData fbData = data.get(FramedBlockData.PROPERTY);
        return fbData != null && fbData.getCamoContent().isSolid();
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand)
    {
        return getQuads(state, side, rand, ModelData.EMPTY, RenderType.cutout());
    }

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data)
    {
        return switch (particleMode)
        {
            case FIRST -> getSpriteOrDefault(data, false);
            case SECOND -> getSpriteOrDefault(data, true);
            case EITHER ->
            {
                TextureAtlasSprite sprite = getSprite(data, false);
                if (sprite != null)
                {
                    yield sprite;
                }

                sprite = getSprite(data, true);
                if (sprite != null)
                {
                    yield sprite;
                }

                //noinspection deprecation
                yield parent.getParticleIcon();
            }
        };
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data)
    {
        PartModels models = getModels();

        ModelData dataLeft = Objects.requireNonNullElse(data.get(IFramedDoubleBlockEntity.DATA_ONE), ModelData.EMPTY);
        ModelData dataRight = Objects.requireNonNullElse(data.get(IFramedDoubleBlockEntity.DATA_TWO), ModelData.EMPTY);

        return ChunkRenderTypeSet.union(
                models.modelOne.getRenderTypes(parts.stateOne(), rand, dataLeft),
                models.modelTwo().getRenderTypes(parts.stateTwo(), rand, dataRight)
        );
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData tileData)
    {
        PartModels models = getModels();

        ModelData dataLeft = Objects.requireNonNullElse(tileData.get(IFramedDoubleBlockEntity.DATA_ONE), ModelData.EMPTY);
        ModelData dataRight = Objects.requireNonNullElse(tileData.get(IFramedDoubleBlockEntity.DATA_TWO), ModelData.EMPTY);

        dataLeft = models.modelOne.getModelData(level, pos, parts.stateOne(), dataLeft);
        dataRight = models.modelTwo().getModelData(level, pos, parts.stateTwo(), dataRight);

        return tileData.derive()
                .with(IFramedDoubleBlockEntity.DATA_ONE, dataLeft)
                .with(IFramedDoubleBlockEntity.DATA_TWO, dataRight)
                .build();
    }

    @Override
    public TriState useAmbientOcclusion(BlockState state, ModelData data, RenderType renderType)
    {
        PartModels models = getModels();

        ModelData dataLeft = Objects.requireNonNullElse(data.get(IFramedDoubleBlockEntity.DATA_ONE), ModelData.EMPTY);
        TriState aoLeft = models.modelOne.useAmbientOcclusion(parts.stateOne(), dataLeft, renderType);
        if (aoLeft == TriState.TRUE)
        {
            return aoLeft;
        }

        ModelData dataRight = Objects.requireNonNullElse(data.get(IFramedDoubleBlockEntity.DATA_TWO), ModelData.EMPTY);
        TriState aoRight = models.modelTwo.useAmbientOcclusion(parts.stateTwo(), dataRight, renderType);
        if (aoRight == TriState.TRUE)
        {
            return aoRight;
        }

        return TriState.DEFAULT;
    }

    private PartModels getModels()
    {
        if (models == null)
        {
            BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
            models = new PartModels(
                    dispatcher.getBlockModel(parts.stateOne()),
                    dispatcher.getBlockModel(parts.stateTwo())
            );
        }
        return models;
    }

    /**
     * Returns the camo-dependent particle texture of the side given by {@code key} when the camo is not air,
     * else returns the basic "framed block" sprite
     */
    private TextureAtlasSprite getSpriteOrDefault(ModelData data, boolean secondary)
    {
        TextureAtlasSprite sprite = getSprite(data, secondary);
        //noinspection deprecation
        return sprite != null ? sprite : parent.getParticleIcon();
    }

    @Nullable
    private TextureAtlasSprite getSprite(ModelData data, boolean secondary)
    {
        ModelData innerData = data.get(secondary ? IFramedDoubleBlockEntity.DATA_TWO : IFramedDoubleBlockEntity.DATA_ONE);
        if (innerData != null)
        {
            FramedBlockData fbData = innerData.get(FramedBlockData.PROPERTY);
            if (fbData != null && !fbData.getCamoContent().isEmpty())
            {
                BakedModel model = secondary ? getModels().modelTwo : getModels().modelOne;
                return model.getParticleIcon(innerData);
            }
        }
        return null;
    }

    private static List<BakedQuad> invertTintIndizes(List<BakedQuad> quads)
    {
        List<BakedQuad> inverted = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads)
        {
            inverted.add(ModelUtils.invertTintIndex(quad));
        }
        return inverted;
    }

    private static ModelData makeDefaultData(boolean altModel)
    {
        FramedBlockData data = new FramedBlockData(EmptyCamoContainer.EMPTY, altModel);
        return ModelData.builder().with(FramedBlockData.PROPERTY, data).build();
    }

    private record PartModels(BakedModel modelOne, BakedModel modelTwo) { }
}
