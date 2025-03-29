package xfacthd.framedblocks.api.internal;

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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.model.ExtendedBlockModelPart;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.item.DynamicItemTintProvider;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.api.model.wrapping.statemerger.StateMerger;
import xfacthd.framedblocks.api.render.debug.BlockDebugRenderer;
import xfacthd.framedblocks.api.util.Utils;

import java.util.Map;

@ApiStatus.Internal
public interface InternalClientAPI
{
    InternalClientAPI INSTANCE = Utils.loadService(InternalClientAPI.class);



    void registerModelWrapper(Holder<Block> block, GeometryFactory geometryFactory, StateMerger stateMerger);

    void registerSpecialModelWrapper(Holder<Block> block, ModelFactory modelFactory, StateMerger stateMerger);

    void registerCopyingModelWrapper(Holder<Block> block, Holder<Block> srcBlock, StateMerger stateMerger);

    BlockDebugRenderer<FramedBlockEntity> getConnectionDebugRenderer();

    BlockDebugRenderer<FramedBlockEntity> getQuadWindingDebugRenderer();

    void enqueueClientTask(int delay, Runnable task);

    ItemModel.Unbaked createFramedBlockItemModel(Block block, DynamicItemTintProvider tintProvider, ResourceLocation baseModel);

    ExtendedBlockModelPart makeBlockModelPart(QuadMap quadMap, TriState partAO, TextureAtlasSprite particleSprite, RenderType renderType, @Nullable BlockState shaderState);

    BlockModelDefinition createFramedBlockDefinition(Either<BlockModelDefinition, SingleVariant.Unbaked> wrapped, Map<String, SingleVariant.Unbaked> auxModels);
}
