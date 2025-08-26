package io.github.xfacthd.framedblocks.client.model.item;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.xfacthd.framedblocks.api.block.IFramedBlock;
import io.github.xfacthd.framedblocks.api.camo.CamoList;
import io.github.xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import io.github.xfacthd.framedblocks.api.model.ModelPartCollectionFakeLevel;
import io.github.xfacthd.framedblocks.api.model.item.AbstractFramedBlockItemModel;
import io.github.xfacthd.framedblocks.api.model.item.ItemModelInfo;
import io.github.xfacthd.framedblocks.api.model.item.block.BlockItemModelProvider;
import io.github.xfacthd.framedblocks.api.model.item.tint.DynamicItemTintProvider;
import io.github.xfacthd.framedblocks.api.model.item.tint.FramedBlockItemTintProvider;
import io.github.xfacthd.framedblocks.api.model.util.ModelUtils;
import io.github.xfacthd.framedblocks.api.util.ConfigView;
import io.github.xfacthd.framedblocks.api.util.Utils;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class FramedBlockItemModel extends AbstractFramedBlockItemModel
{
    private static final RandomSource RANDOM = RandomSource.create();
    private static final Direction[] DIRECTIONS = Arrays.copyOf(Direction.values(), 7);
    private static final ResourceLocation ERROR_MODEL_LOCATION = Utils.rl("item/error");

    private final Map<CamoList, ModelSet> itemModelCache = new Object2ObjectOpenHashMap<>();
    private final BlockState state;
    private final Supplier<BlockStateModel> modelSupplier;
    private final boolean nonStandardModelProvider;
    private final DynamicItemTintProvider tintProvider;
    private final ItemTransforms itemTransforms;
    private final ItemModel errorModel;
    private final Supplier<Vector3f[]> extents;

    private FramedBlockItemModel(
            BlockState state,
            Supplier<BlockStateModel> modelSupplier,
            boolean nonStandardModelProvider,
            DynamicItemTintProvider tintProvider,
            ItemTransforms itemTransforms,
            ItemModel errorModel
    )
    {
        this.state = state;
        this.modelSupplier = Lazy.of(modelSupplier);
        this.nonStandardModelProvider = nonStandardModelProvider;
        this.tintProvider = tintProvider;
        this.itemTransforms = itemTransforms;
        this.errorModel = errorModel;
        this.extents = Suppliers.memoize(() ->
        {
            BlockStateModel model = this.modelSupplier.get();
            ItemModelInfo modelInfo = ItemModelInfo.DEFAULT;
            if (model instanceof AbstractFramedBlockModel blockModel)
            {
                modelInfo = Objects.requireNonNullElse(blockModel.getItemModelInfo(), ItemModelInfo.DEFAULT);
            }
            ModelSet modelSet = getOrCreateModelSet(ItemStack.EMPTY, CamoList.EMPTY, modelInfo);
            ArrayList<BakedQuad> allQuads = new ArrayList<>();
            for (ModelEntry modelEntry : modelSet.models)
            {
                Utils.copyAll(modelEntry.quads, allQuads);
            }
            return BlockModelWrapper.computeExtents(allQuads);
        });
    }

    @Override
    public void update(
            ItemStackRenderState renderState,
            ItemStack stack,
            ItemModelResolver resolver,
            ItemDisplayContext ctx,
            @Nullable ClientLevel level,
            @Nullable LivingEntity entity,
            int seed
    )
    {
        BlockStateModel model = modelSupplier.get();
        ItemModelInfo itemModelInfo;
        if (!(model instanceof AbstractFramedBlockModel blockModel) || (itemModelInfo = blockModel.getItemModelInfo()) == null)
        {
            errorModel.update(renderState, stack, resolver, ctx, level, entity, seed);
            return;
        }

        boolean showCamo = ConfigView.Client.INSTANCE.shouldRenderItemModelsWithCamo();
        CamoList camos = showCamo ? stack.getOrDefault(Utils.DC_TYPE_CAMO_LIST, CamoList.EMPTY) : CamoList.EMPTY;

        ModelSet modelSet;
        try
        {
            modelSet = getOrCreateModelSet(stack, camos, itemModelInfo);
        }
        catch (Throwable ignored)
        {
            errorModel.update(renderState, stack, resolver, ctx, level, entity, seed);
            return;
        }

        renderState.appendModelIdentityElement(this);
        if (!modelSet.camos.isEmpty())
        {
            renderState.appendModelIdentityElement(modelSet.camos);
        }
        if (modelSet.animated)
        {
            renderState.setAnimated();
        }
        for (ModelEntry layerModel : modelSet.models)
        {
            ItemStackRenderState.LayerRenderState layer = renderState.newLayer();
            layer.setExtents(extents);
            layer.prepareQuadList().addAll(layerModel.quads);
            layer.setRenderType(layerModel.renderType);
            modelSet.properties.applyToLayer(layer, ctx);
            if (modelSet.tintValues != null)
            {
                layer.framedblocks$setDynamicItemTintValues(modelSet.tintValues);
            }
        }
    }

    private ModelSet getOrCreateModelSet(ItemStack stack, CamoList camos, ItemModelInfo itemModelInfo)
    {
        ModelSet modelSet = itemModelCache.get(camos);
        if (modelSet == null)
        {
            BlockStateModel model = modelSupplier.get();
            ModelData data = itemModelInfo.isDataRequired() || !camos.isEmpty() ? itemModelInfo.buildItemModelData(state, camos) : ModelData.EMPTY;
            BlockAndTintGetter level = new ModelPartCollectionFakeLevel(state, data);

            List<ModelEntry> models = new ArrayList<>();
            IntSet tintIndices = new IntOpenHashSet();
            boolean animated = false;

            RANDOM.setSeed(42);
            for (BlockModelPart modelPart : model.collectParts(level, BlockPos.ZERO, state, RANDOM))
            {
                ArrayList<BakedQuad> allQuads = new ArrayList<>();
                for (Direction face : DIRECTIONS)
                {
                    RANDOM.setSeed(42);
                    Utils.copyAll(modelPart.getQuads(face), allQuads);
                }
                for (BakedQuad quad : allQuads)
                {
                    int tintIndex = quad.tintIndex();
                    if (tintIndex != -1)
                    {
                        tintIndices.add(tintIndex);
                    }
                    if (quad.sprite().isAnimated())
                    {
                        animated = true;
                    }
                }
                ChunkSectionLayer chunkLayer = modelPart.getRenderType(state);
                models.add(new ModelEntry(allQuads, RenderTypeHelper.getEntityRenderType(chunkLayer)));
            }

            ModelRenderProperties renderProps = new ModelRenderProperties(true, model.particleIcon(EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, state), itemTransforms);
            modelSet = new ModelSet(models, collectTintValues(tintIndices, tintProvider, stack, camos), renderProps, camos, animated);
            itemModelCache.put(camos, modelSet);
        }
        return modelSet;
    }

    @Nullable
    private static Int2IntMap collectTintValues(IntSet tintIndices, DynamicItemTintProvider tintProvider, ItemStack stack, CamoList camos)
    {
        if (tintIndices.isEmpty()) return null;

        Int2IntMap tintValues = new Int2IntOpenHashMap();
        for (int tintIndex : tintIndices)
        {
            int color = tintProvider.getColor(stack, camos, tintIndex);
            if (color != -1)
            {
                tintValues.put(tintIndex, color);
            }
        }
        return tintValues.isEmpty() ? null : tintValues;
    }

    public ItemTransforms getItemTransforms()
    {
        return itemTransforms;
    }

    @Override
    public void clearCache()
    {
        itemModelCache.clear();
        // Assume that models provided by non-standard providers are "freestanding" and therefore don't get caught by
        // the clearCache() call on all AbstractFramedBlockModels in the block model "registry"
        if (nonStandardModelProvider && modelSupplier.get() instanceof AbstractFramedBlockModel framedModel)
        {
            framedModel.clearCache();
        }
    }



    private record ModelSet(List<ModelEntry> models, @Nullable Int2IntMap tintValues, ModelRenderProperties properties, CamoList camos, boolean animated) { }

    private record ModelEntry(List<BakedQuad> quads, RenderType renderType) { }



    public record Unbaked(Block block, BlockItemModelProvider modelProvider, DynamicItemTintProvider tintProvider, ResourceLocation baseModel) implements ItemModel.Unbaked
    {
        public static final ResourceLocation ID = Utils.rl("block");
        public static final MapCodec<FramedBlockItemModel.Unbaked> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").validate(FramedBlockItemModel.Unbaked::validateBlock).forGetter(FramedBlockItemModel.Unbaked::block),
                BlockItemModelProviders.CODEC.optionalFieldOf("model_provider", BlockItemModelProvider.DEFAULT).forGetter(FramedBlockItemModel.Unbaked::modelProvider),
                DynamicItemTintProviders.CODEC.optionalFieldOf("tint_provider", FramedBlockItemTintProvider.INSTANCE_SINGLE).forGetter(FramedBlockItemModel.Unbaked::tintProvider),
                ResourceLocation.CODEC.fieldOf("base_model").forGetter(FramedBlockItemModel.Unbaked::baseModel)
        ).apply(inst, FramedBlockItemModel.Unbaked::new));
        private static final ModelBaker.SharedOperationKey<ItemModel> ERROR_MODEL_KEY = ModelUtils.makeSharedOpsKey(baker ->
        {
            ResolvedModel model = baker.getModel(ERROR_MODEL_LOCATION);
            SimpleModelWrapper bakedModel = SimpleModelWrapper.bake(baker, ERROR_MODEL_LOCATION, BlockModelRotation.X0_Y0);
            ModelRenderProperties renderProps = ModelRenderProperties.fromResolvedModel(baker, model, model.getTopTextureSlots());
            return new BlockModelWrapper(List.of(), bakedModel.quads().getAll(), renderProps);
        });

        public Unbaked
        {
            Preconditions.checkArgument(block instanceof IFramedBlock, "Expected IFramedBlock, got %s", block);
        }

        @Override
        public FramedBlockItemModel bake(BakingContext context)
        {
            BlockState state = Objects.requireNonNull(((IFramedBlock) block).getItemModelSource());
            Supplier<BlockStateModel> modelSupplier = modelProvider.create(state, context.blockModelBaker());
            boolean nonStandardModelProvider = modelProvider != BlockItemModelProvider.DEFAULT;
            ItemTransforms transforms = context.blockModelBaker().getModel(baseModel).getTopTransforms();
            ItemModel errorModel = context.blockModelBaker().compute(ERROR_MODEL_KEY);
            return new FramedBlockItemModel(state, modelSupplier, nonStandardModelProvider, tintProvider, transforms, errorModel);
        }

        @Override
        public void resolveDependencies(Resolver resolver)
        {
            resolver.markDependency(baseModel);
            resolver.markDependency(ERROR_MODEL_LOCATION);
        }

        @Override
        public MapCodec<? extends ItemModel.Unbaked> type()
        {
            return CODEC;
        }

        private static DataResult<Block> validateBlock(Block block)
        {
            if (block instanceof IFramedBlock)
            {
                return DataResult.success(block);
            }
            return DataResult.error(() -> "Expected IFramedBlock, got " + block);
        }
    }
}
