package xfacthd.framedblocks.client.apiimpl;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.internal.InternalClientAPI;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.ExtendedBlockModelPart;
import xfacthd.framedblocks.api.model.item.block.BlockItemModelProvider;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.model.wrapping.AuxModelProvider;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.api.model.wrapping.TextureLookup;
import xfacthd.framedblocks.client.model.FramedBlockModelPart;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.item.tint.DynamicItemTintProvider;
import xfacthd.framedblocks.api.model.wrapping.statemerger.StateMerger;
import xfacthd.framedblocks.client.itemmodel.FramedBlockItemModel;
import xfacthd.framedblocks.client.model.baked.FramedBlockModel;
import xfacthd.framedblocks.client.model.unbaked.FramedBlockModelDefinition;
import xfacthd.framedblocks.client.model.unbaked.UnbakedFramedBlockModel;
import xfacthd.framedblocks.client.model.unbaked.UnbakedCopyingFramedBlockModel;
import xfacthd.framedblocks.client.modelwrapping.ModelWrappingHandler;
import xfacthd.framedblocks.client.modelwrapping.ModelWrappingManager;
import xfacthd.framedblocks.client.util.ClientTaskQueue;

import java.util.Map;
import java.util.function.Supplier;

public final class InternalClientApiImpl implements InternalClientAPI
{
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    @Override
    public void registerModelWrapper(Holder<Block> block, GeometryFactory geometryFactory, StateMerger stateMerger)
    {
        registerSpecialModelWrapper(block, ctx -> new UnbakedFramedBlockModel(ctx, geometryFactory), stateMerger);
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
    public ExtendedBlockModelPart makeBlockModelPart(QuadMap quadMap, TriState partAO, TextureAtlasSprite particleSprite, RenderType renderType, @Nullable BlockState shaderState)
    {
        if (shaderState == AIR)
        {
            shaderState = null;
        }
        return new FramedBlockModelPart(quadMap.build(), partAO, particleSprite, renderType, shaderState);
    }

    @Override
    public BlockModelDefinition createFramedBlockDefinition(Either<BlockModelDefinition, SingleVariant.Unbaked> wrapped, Map<String, SingleVariant.Unbaked> auxModels)
    {
        return new BlockModelDefinition(new FramedBlockModelDefinition(wrapped, auxModels));
    }

    @Override
    public Supplier<BlockStateModel> createBlockItemModelProviderForGeometry(BlockState state, BlockState srcState, GeometryFactory geometry)
    {
        return () ->
        {
            BlockStateModel baseModel = ModelUtils.getModel(srcState);
            if (baseModel instanceof AbstractFramedBlockModel framedModel)
            {
                baseModel = framedModel.getBaseModel();
            }
            GeometryFactory.Context ctx = new GeometryFactory.Context(state, baseModel, AuxModelProvider.invalid(), TextureLookup.runtime());
            return new FramedBlockModel(ctx, geometry.create(ctx));
        };
    }
}
