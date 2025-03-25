package xfacthd.framedblocks.client.modelwrapping;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelLookup;
import xfacthd.framedblocks.api.model.wrapping.TextureLookup;
import xfacthd.framedblocks.api.model.wrapping.statemerger.StateMerger;

import java.util.IdentityHashMap;
import java.util.Map;

public final class ModelWrappingHandler
{
    private final Map<BlockState, BlockStateModel> visitedStates = new IdentityHashMap<>();
    private final Holder<Block> block;
    private final ModelFactory blockModelFactory;
    private final StateMerger stateMerger;

    public ModelWrappingHandler(Holder<Block> block, ModelFactory blockModelFactory, StateMerger stateMerger)
    {
        this.block = block;
        this.blockModelFactory = blockModelFactory;
        this.stateMerger = stateMerger;
    }

    public synchronized BlockStateModel wrapBlockModel(
            BlockStateModel srcModel, BlockState state, ModelLookup modelLookup, TextureLookup textureLookup, @Nullable ModelCounter counter
    )
    {
        BlockState mergedState = stateMerger.apply(state);
        if (counter != null)
        {
            counter.increment(mergedState == state);
        }
        return visitedStates.computeIfAbsent(mergedState, keyState ->
                blockModelFactory.create(new GeometryFactory.Context(keyState, srcModel, modelLookup, textureLookup))
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
