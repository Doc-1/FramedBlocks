package xfacthd.framedblocks.client.apiimpl;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.internal.InternalClientAPI;
import xfacthd.framedblocks.api.model.ExtendedBlockModelPart;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.client.model.FramedBlockModelPart;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.item.tint.DynamicItemTintProvider;
import xfacthd.framedblocks.api.model.wrapping.statemerger.StateMerger;
import xfacthd.framedblocks.api.render.debug.BlockDebugRenderer;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.client.itemmodel.FramedBlockItemModel;
import xfacthd.framedblocks.client.model.unbaked.FramedBlockModelDefinition;
import xfacthd.framedblocks.client.model.unbaked.UnbakedFramedBlockModel;
import xfacthd.framedblocks.client.model.unbaked.UnbakedCopyingFramedBlockModel;
import xfacthd.framedblocks.client.modelwrapping.ModelWrappingHandler;
import xfacthd.framedblocks.client.modelwrapping.ModelWrappingManager;
import xfacthd.framedblocks.client.render.debug.impl.ConnectionPredicateDebugRenderer;
import xfacthd.framedblocks.client.render.debug.impl.QuadWindingDebugRenderer;
import xfacthd.framedblocks.client.util.ClientTaskQueue;
import xfacthd.framedblocks.common.config.DevToolsConfig;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class InternalClientApiImpl implements InternalClientAPI
{
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    @Override
    public void registerModelWrapper(Holder<Block> block, GeometryFactory geometryFactory, StateMerger stateMerger)
    {
        registerSpecialModelWrapper(
                block,
                ctx -> new UnbakedFramedBlockModel(ctx, geometryFactory),
                stateMerger
        );
    }

    @Override
    public void registerSpecialModelWrapper(Holder<Block> block, ModelFactory modelFactory, StateMerger stateMerger)
    {
        debugStateMerger(block, stateMerger);

        ModelWrappingManager.register(block, new ModelWrappingHandler(block, modelFactory, stateMerger));
    }

    @Override
    public void registerCopyingModelWrapper(Holder<Block> block, Holder<Block> srcBlock, StateMerger stateMerger)
    {
        registerSpecialModelWrapper(
                block,
                ctx -> new UnbakedCopyingFramedBlockModel(ctx, srcBlock.value()),
                stateMerger
        );
    }

    @Override
    public BlockDebugRenderer<FramedBlockEntity> getConnectionDebugRenderer()
    {
        return ConnectionPredicateDebugRenderer.INSTANCE;
    }

    @Override
    public BlockDebugRenderer<FramedBlockEntity> getQuadWindingDebugRenderer()
    {
        return QuadWindingDebugRenderer.INSTANCE;
    }

    @Override
    public void enqueueClientTask(int delay, Runnable task)
    {
        ClientTaskQueue.enqueueClientTask(delay, task);
    }

    @Override
    public ItemModel.Unbaked createFramedBlockItemModel(Block block, DynamicItemTintProvider tintProvider, ResourceLocation baseModel)
    {
        return new FramedBlockItemModel.Unbaked(block, tintProvider, baseModel);
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



    private static void debugStateMerger(Holder<Block> block, StateMerger stateMerger)
    {
        if (!DevToolsConfig.VIEW.isStateMergerDebugLoggingEnabled()) return;

        Pattern debugFilterPattern = DevToolsConfig.VIEW.getStateMergerDebugFilter();
        if (debugFilterPattern != null)
        {
            String key = Utils.getKeyOrThrow(block).location().toString();
            if (!debugFilterPattern.matcher(key).matches()) return;
        }

        Set<Property<?>> props = new HashSet<>(block.value().getStateDefinition().getProperties());
        Set<Property<?>> ignoredProps = stateMerger.getHandledProperties(block);

        props.removeAll(ignoredProps);

        FramedBlocks.LOGGER.info("%-70s | %-150s | %-150s".formatted(
                block.value(), propsToString(props), propsToString(ignoredProps)
        ));
    }

    private static String propsToString(Collection<Property<?>> properties)
    {
        return properties.stream()
                .map(Property::getName)
                .collect(Collectors.joining(", ", "[ ", " ]"));
    }
}
