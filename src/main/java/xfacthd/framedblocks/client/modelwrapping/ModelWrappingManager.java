package xfacthd.framedblocks.client.modelwrapping;

import com.google.common.base.Stopwatch;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.loading.FMLEnvironment;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.model.AbstractFramedBlockModel;
import xfacthd.framedblocks.api.model.wrapping.RegisterModelWrappersEvent;
import xfacthd.framedblocks.common.config.DevToolsConfig;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ModelWrappingManager
{
    private static final Map<Block, ModelWrappingHandler> HANDLERS = new IdentityHashMap<>();
    private static boolean locked = true;

    public static void fireRegistration()
    {
        Stopwatch stopwatch = Stopwatch.createStarted();

        locked = false;
        boolean debugLogging = DevToolsConfig.VIEW.isStateMergerDebugLoggingEnabled();
        if (debugLogging)
        {
            FramedBlocks.LOGGER.info("=============== Model Wrapper Registration Start ===============");
            FramedBlocks.LOGGER.info("\"%-70s | %-150s | %-150s\"".formatted(
                    "Block", "Unhandled properties", "Handled or ignored properties"
            ));
        }
        ModLoader.postEvent(new RegisterModelWrappersEvent());
        if (debugLogging)
        {
            FramedBlocks.LOGGER.info("=============== Model Wrapper Registration End =================");
        }
        locked = true;

        stopwatch.stop();
        FramedBlocks.LOGGER.debug("Registered model wrappers for {} blocks in {}", HANDLERS.size(), stopwatch);
    }

    public static void register(Holder<Block> block, ModelWrappingHandler handler)
    {
        if (locked)
        {
            throw new IllegalStateException("ModelWrappingHandler registration is locked");
        }

        ModelWrappingHandler oldHandler = HANDLERS.put(block.value(), handler);
        if (oldHandler != null)
        {
            throw new IllegalStateException("ModelWrappingHandler for '" + block + "' already registered");
        }
    }

    public static void reset()
    {
        HANDLERS.values().forEach(ModelWrappingHandler::reset);
    }

    public static ModelWrappingHandler getHandler(Block block)
    {
        ModelWrappingHandler handler = HANDLERS.get(block);
        if (handler == null)
        {
            throw new NullPointerException("No ModelWrappingHandler registered for block '" + block + "'");
        }
        return handler;
    }

    public static void printWrappingInfo(Map<BlockState, BlockStateModel> models)
    {
        int stateCount = 0;
        Set<BlockStateModel> distinctModels = new ReferenceOpenHashSet<>();
        for (Block block : HANDLERS.keySet())
        {
            List<BlockState> states = block.getStateDefinition().getPossibleStates();
            for (BlockState state : states)
            {
                distinctModels.add(models.get(state));
            }
            stateCount += states.size();
        }

        FramedBlocks.LOGGER.debug(
                "Wrapped {} unique block models ({} total) for {} blocks",
                distinctModels.size(),
                stateCount,
                HANDLERS.size()
        );

        if (!FMLEnvironment.production)
        {
            Map<BlockStateModel, Block> nonWrappedModels = new Reference2ObjectOpenHashMap<>();
            for (Block block : HANDLERS.keySet())
            {
                List<BlockState> states = block.getStateDefinition().getPossibleStates();
                for (BlockState state : states)
                {
                    BlockStateModel model = models.get(state);
                    if (!(model instanceof AbstractFramedBlockModel))
                    {
                        nonWrappedModels.put(model, state.getBlock());
                    }
                }
                stateCount += states.size();
            }
            if (!nonWrappedModels.isEmpty())
            {
                Set<String> blocks = nonWrappedModels.values()
                        .stream()
                        .distinct()
                        .map(Block::toString)
                        .collect(Collectors.toSet());
                FramedBlocks.LOGGER.warn(
                        "Found {} unwrapped models for {} blocks:\n\t{}",
                        nonWrappedModels.size(),
                        blocks.size(),
                        String.join("\n\t", blocks)
                );
            }
        }
    }



    private ModelWrappingManager() { }
}
