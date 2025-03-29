package xfacthd.framedblocks.client.model.unbaked;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.neoforged.neoforge.client.model.block.CustomBlockModelDefinition;
import xfacthd.framedblocks.client.modelwrapping.ModelWrappingHandler;
import xfacthd.framedblocks.client.modelwrapping.ModelWrappingManager;
import xfacthd.framedblocks.common.data.StateCacheBuilder;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public record FramedBlockModelDefinition(
        Either<BlockModelDefinition, SingleVariant.Unbaked> baseModel,
        Map<String, SingleVariant.Unbaked> auxModels
) implements CustomBlockModelDefinition
{
    public static final MapCodec<FramedBlockModelDefinition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.mapEither(BlockModelDefinition.VANILLA_CODEC, SingleVariant.Unbaked.MAP_CODEC.fieldOf("base_model"))
                    .forGetter(FramedBlockModelDefinition::baseModel),
            Codec.unboundedMap(Codec.STRING, SingleVariant.Unbaked.CODEC)
                    .optionalFieldOf("aux_models", Map.of())
                    .xmap(Map::copyOf, Function.identity())
                    .forGetter(FramedBlockModelDefinition::auxModels)
    ).apply(inst, FramedBlockModelDefinition::new));

    @Override
    public Map<BlockState, BlockStateModel.UnbakedRoot> instantiate(StateDefinition<Block, BlockState> states, Supplier<String> sourceSupplier)
    {
        Map<BlockState, BlockStateModel.UnbakedRoot> models = baseModel.map(
                def -> def.instantiateVanilla(states, sourceSupplier),
                variant ->
                {
                    BlockStateModel.UnbakedRoot variantRoot = variant.asRoot();
                    return new IdentityHashMap<>(Maps.toMap(states.getPossibleStates(), state -> variantRoot));
                }
        );

        StateCacheBuilder.ensureStateCachesInitialized();
        ModelWrappingHandler handler = ModelWrappingManager.getHandler(states.any().getBlock());
        handler.reset();
        models.replaceAll((state, model) -> handler.wrapBlockModel(state, model, auxModels));

        return models;
    }

    @Override
    public MapCodec<FramedBlockModelDefinition> codec()
    {
        return CODEC;
    }
}
