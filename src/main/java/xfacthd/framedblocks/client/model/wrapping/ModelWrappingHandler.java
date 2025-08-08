package xfacthd.framedblocks.client.model.wrapping;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.api.model.wrapping.statemerger.StateMerger;

import java.util.IdentityHashMap;
import java.util.Map;

public sealed class ModelWrappingHandler permits StandaloneModelWrappingHandler
{
    private final Map<BlockState, BlockStateModel.UnbakedRoot> visitedStates = new IdentityHashMap<>();
    private final Holder<Block> block;
    private final ModelFactory blockModelFactory;
    private final StateMerger stateMerger;

    public ModelWrappingHandler(Holder<Block> block, ModelFactory blockModelFactory, StateMerger stateMerger)
    {
        this.block = block;
        this.blockModelFactory = blockModelFactory;
        this.stateMerger = stateMerger;
    }

    public synchronized BlockStateModel.UnbakedRoot wrapBlockModel(
            BlockState state,
            BlockStateModel.UnbakedRoot srcModel,
            Map<String, SingleVariant.Unbaked> auxModels
    )
    {
        BlockState mergedState = stateMerger.apply(state);
        return visitedStates.computeIfAbsent(mergedState, keyState ->
                blockModelFactory.create(new ModelFactory.Context(keyState, srcModel, auxModels))
        );
    }

    public Block getBlock()
    {
        return block.value();
    }

    public synchronized void reset()
    {
        visitedStates.clear();
        blockModelFactory.reset();
    }

    public StateMerger getStateMerger()
    {
        return stateMerger;
    }

    public int getVisitedStateCount()
    {
        return visitedStates.size();
    }
}
