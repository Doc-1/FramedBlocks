package xfacthd.framedblocks.client.apiimpl;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import xfacthd.framedblocks.api.block.render.NullCullPredicate;
import xfacthd.framedblocks.api.internal.InternalClientAPI;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.ExtendedBlockModelPart;
import xfacthd.framedblocks.api.model.item.ItemModelInfo;
import xfacthd.framedblocks.api.model.item.block.BlockItemModelProvider;
import xfacthd.framedblocks.api.model.standalone.CachingModel;
import xfacthd.framedblocks.api.model.standalone.StandaloneModelFactory;
import xfacthd.framedblocks.api.model.standalone.StandaloneWrapperKey;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.model.wrapping.AuxModelProvider;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.api.model.wrapping.TextureLookup;
import xfacthd.framedblocks.client.model.FramedBlockModelPart;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.item.tint.DynamicItemTintProvider;
import xfacthd.framedblocks.api.model.wrapping.statemerger.StateMerger;
import xfacthd.framedblocks.client.model.QuadMapImpl;
import xfacthd.framedblocks.client.model.ReinforcementModel;
import xfacthd.framedblocks.client.model.item.FramedBlockItemModel;
import xfacthd.framedblocks.client.model.baked.FramedBlockModel;
import xfacthd.framedblocks.client.model.unbaked.FramedBlockModelDefinition;
import xfacthd.framedblocks.client.model.unbaked.UnbakedFramedBlockModel;
import xfacthd.framedblocks.client.model.unbaked.UnbakedCopyingFramedBlockModel;
import xfacthd.framedblocks.client.model.unbaked.UnbakedFramedDoubleBlockModel;
import xfacthd.framedblocks.client.model.wrapping.ModelWrappingHandler;
import xfacthd.framedblocks.client.model.wrapping.ModelWrappingManager;
import xfacthd.framedblocks.client.model.wrapping.StandaloneModelWrappingHandler;
import xfacthd.framedblocks.client.util.ClientTaskQueue;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class InternalClientApiImpl implements InternalClientAPI
{
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    @Override
    public void registerModelWrapper(Holder<Block> block, GeometryFactory geometryFactory, StateMerger stateMerger)
    {
        Preconditions.checkArgument(block.value() instanceof IFramedBlock, "Cannot register model wrapper for non-IFramedBlock");
        registerSpecialModelWrapper(block, ctx -> new UnbakedFramedBlockModel(ctx, geometryFactory), stateMerger);
    }

    @Override
    public void registerDoubleModelWrapper(Holder<Block> block, NullCullPredicate nullCullPredicate, ItemModelInfo itemModelInfo, StateMerger stateMerger)
    {
        Preconditions.checkArgument(block.value() instanceof IFramedDoubleBlock, "Cannot register double model wrapper for non-IFramedDoubleBlock");
        registerSpecialModelWrapper(block, ctx -> new UnbakedFramedDoubleBlockModel(ctx, nullCullPredicate, itemModelInfo), stateMerger);
    }

    @Override
    public void registerSpecialModelWrapper(Holder<Block> block, ModelFactory modelFactory, StateMerger stateMerger)
    {
        ModelWrappingManager.register(block, new ModelWrappingHandler(block, modelFactory, stateMerger));
    }

    @Override
    public void registerCopyingModelWrapper(Holder<Block> block, Holder<Block> srcBlock, StateMerger stateMerger)
    {
        registerSpecialModelWrapper(block, ctx -> new UnbakedCopyingFramedBlockModel(ctx, srcBlock.value()), stateMerger);
    }

    @Override
    public <T extends CachingModel> void registerStandaloneModelWrapper(
            StandaloneWrapperKey<T> wrapperKey,
            GeometryFactory geometryFactory,
            StandaloneModelFactory<T> modelFactory,
            StateMerger stateMerger
    )
    {
        Holder<Block> block = wrapperKey.block();
        Preconditions.checkArgument(block.value() instanceof IFramedBlock, "Cannot register model wrapper for non-IFramedBlock");
        ModelFactory blockModelFactory = ctx -> new UnbakedFramedBlockModel(ctx, geometryFactory);
        ModelWrappingManager.register(wrapperKey, new StandaloneModelWrappingHandler<>(block, blockModelFactory, stateMerger, modelFactory));
    }

    @Override
    public void enqueueClientTask(int delay, Runnable task)
    {
        ClientTaskQueue.enqueueClientTask(delay, task);
    }

    @Override
    public ItemModel.Unbaked createFramedBlockItemModel(Block block, BlockItemModelProvider modelProvider, DynamicItemTintProvider tintProvider, ResourceLocation baseModel)
    {
        return new FramedBlockItemModel.Unbaked(block, modelProvider, tintProvider, baseModel);
    }

    @Override
    public ExtendedBlockModelPart makeBlockModelPart(QuadMap quadMap, TriState partAO, TextureAtlasSprite particleSprite, ChunkSectionLayer chunkLayer, @Nullable BlockState shaderState)
    {
        if (shaderState == AIR)
        {
            shaderState = null;
        }
        return new FramedBlockModelPart(((QuadMapImpl) quadMap).build(), partAO, particleSprite, chunkLayer, shaderState);
    }

    @Override
    public BlockModelDefinition createFramedBlockDefinition(
            Either<BlockModelDefinition, SingleVariant.Unbaked> wrapped,
            Map<String, SingleVariant.Unbaked> auxModels,
            Optional<StandaloneWrapperKey<?>> wrapperKey
    )
    {
        return new BlockModelDefinition(new FramedBlockModelDefinition(wrapped, auxModels, wrapperKey));
    }

    @Override
    public Supplier<BlockStateModel> createBlockItemModelProviderForGeometry(BlockState state, BlockState srcState, GeometryFactory geometry, ModelBaker baker)
    {
        return () ->
        {
            BlockStateModel baseModel = ModelUtils.getModel(srcState);
            if (baseModel instanceof AbstractFramedBlockModel framedModel)
            {
                baseModel = framedModel.getBaseModel();
            }
            GeometryFactory.Context ctx = new GeometryFactory.Context(state, baseModel, AuxModelProvider.invalid(), TextureLookup.runtime());
            ReinforcementModel reinforcement = ReinforcementModel.getOrCreate(baker);
            return new FramedBlockModel(ctx, geometry.create(ctx), reinforcement);
        };
    }
}
