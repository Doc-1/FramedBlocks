package xfacthd.framedblocks.client.itemmodel;

import com.google.common.base.Preconditions;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.DataAwareItemModel;
import xfacthd.framedblocks.api.model.ErrorModel;
import xfacthd.framedblocks.api.model.item.AbstractFramedBlockItemModel;
import xfacthd.framedblocks.api.model.wrapping.itemmodel.ItemModelInfo;
import xfacthd.framedblocks.api.model.item.FramedBlockItemTintProvider;
import xfacthd.framedblocks.api.util.CamoList;
import xfacthd.framedblocks.api.util.ConfigView;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.api.model.item.DynamicItemTintProvider;
import xfacthd.framedblocks.client.render.item.IDynamicTintableItemStackRenderStateLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class FramedBlockItemModel extends AbstractFramedBlockItemModel
{
    private static final RandomSource RANDOM = RandomSource.create();
    private static final Direction[] DIRECTIONS = Arrays.copyOf(Direction.values(), 7);

    private final Map<CamoList, ModelSet> itemModelCache = new Object2ObjectOpenHashMap<>();
    private final BlockState state;
    private final DynamicItemTintProvider tintProvider;

    private FramedBlockItemModel(BlockState state, DynamicItemTintProvider tintProvider)
    {
        this.state = state;
        this.tintProvider = tintProvider;
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
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        ItemModelInfo itemModelInfo;
        if (!(model instanceof AbstractFramedBlockModel blockModel) || (itemModelInfo = blockModel.getItemModelInfo()) == null)
        {
            renderState.newLayer().setupBlockModel(ErrorModel.get(), Sheets.solidBlockSheet());
            return;
        }

        boolean dataRequired = itemModelInfo.isDataRequired();
        boolean showCamo = ConfigView.Client.INSTANCE.shouldRenderItemModelsWithCamo();
        if (!dataRequired && !showCamo)
        {
            renderState.newLayer().setupBlockModel(model, Sheets.cutoutBlockSheet());
            return;
        }

        CamoList camos = showCamo ? stack.getOrDefault(Utils.DC_TYPE_CAMO_LIST, CamoList.EMPTY) : CamoList.EMPTY;
        if (!dataRequired && camos.isEmpty())
        {
            renderState.newLayer().setupBlockModel(model, Sheets.cutoutBlockSheet());
            return;
        }

        ModelSet modelSet = itemModelCache.get(camos);
        if (modelSet == null)
        {
            ModelData data = itemModelInfo.buildItemModelData(state, camos);
            List<DataAwareItemModel> models = new ArrayList<>();
            RANDOM.setSeed(42);
            IntSet tintIndices = new IntOpenHashSet();
            for (RenderType renderType : model.getRenderTypes(state, RANDOM, data))
            {
                DataAwareItemModel itemModel = new DataAwareItemModel(model, data, renderType);
                collectTintIndices(tintIndices, itemModel, state, data, renderType);
                models.add(itemModel);
            }
            modelSet = new ModelSet(models, collectTintValues(tintIndices, tintProvider, stack, camos));
            itemModelCache.put(camos, modelSet);
        }
        for (BakedModel layerModel : modelSet.models)
        {
            ItemStackRenderState.LayerRenderState layer = renderState.newLayer();
            layer.setupBlockModel(layerModel, layerModel.getRenderType(stack));
            if (modelSet.tintValues != null)
            {
                ((IDynamicTintableItemStackRenderStateLayer) layer).framedblocks$setDynamicItemTintValues(modelSet.tintValues);
            }
        }
    }

    private static void collectTintIndices(IntSet tintIndices, DataAwareItemModel itemModel, BlockState state, ModelData data, RenderType renderType)
    {
        for (Direction face : DIRECTIONS)
        {
            for (BakedQuad quad : itemModel.getQuads(state, face, RANDOM, data, renderType))
            {
                int tintIndex = quad.getTintIndex();
                if (tintIndex != -1)
                {
                    tintIndices.add(tintIndex);
                }
            }
        }
    }

    @Nullable
    private static Int2IntMap collectTintValues(IntSet tintIndices, DynamicItemTintProvider tintProvider, ItemStack stack, CamoList camos)
    {
        if (tintIndices.isEmpty()) return null;

        Int2IntMap tintValues = new Int2IntOpenHashMap();
        for (int tintIndex : tintIndices)
        {
            tintValues.put(tintIndex, tintProvider.getColor(stack, camos, tintIndex));
        }
        return tintValues;
    }

    @Override
    public void clearCache()
    {
        itemModelCache.clear();
    }



    private record ModelSet(List<DataAwareItemModel> models, @Nullable Int2IntMap tintValues) { }



    public record Unbaked(Block block, DynamicItemTintProvider tintProvider) implements ItemModel.Unbaked
    {
        public static final ResourceLocation ID = Utils.rl("block");
        public static final MapCodec<FramedBlockItemModel.Unbaked> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").validate(block ->
                {
                    if (block instanceof IFramedBlock)
                    {
                        return DataResult.success(block);
                    }
                    return DataResult.error(() -> "Expected IFramedBlock, got " + block);
                }).forGetter(FramedBlockItemModel.Unbaked::block),
                DynamicItemTintProviders.CODEC.optionalFieldOf("tint_provider", FramedBlockItemTintProvider.INSTANCE_SINGLE).forGetter(Unbaked::tintProvider)
        ).apply(inst, FramedBlockItemModel.Unbaked::new));

        public Unbaked
        {
            Preconditions.checkArgument(block instanceof IFramedBlock, "Expected IFramedBlock, got %s", block);
        }

        @Override
        public ItemModel bake(BakingContext context)
        {
            BlockState state = ((IFramedBlock) block).getItemModelSource();
            return new FramedBlockItemModel(Objects.requireNonNull(state), tintProvider);
        }

        @Override
        public void resolveDependencies(Resolver resolver) { }

        @Override
        public MapCodec<? extends ItemModel.Unbaked> type()
        {
            return CODEC;
        }
    }
}
