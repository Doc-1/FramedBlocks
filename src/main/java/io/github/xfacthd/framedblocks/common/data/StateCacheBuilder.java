package io.github.xfacthd.framedblocks.common.data;

import com.google.common.base.Stopwatch;
import io.github.xfacthd.framedblocks.FramedBlocks;
import io.github.xfacthd.framedblocks.api.block.IFramedBlock;
import io.github.xfacthd.framedblocks.api.block.cache.StateCache;
import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class StateCacheBuilder
{
    private static volatile boolean cachesBuilt = false;

    public static void ensureStateCachesInitialized()
    {
        if (!cachesBuilt)
        {
            synchronized (StateCacheBuilder.class)
            {
                if (!cachesBuilt)
                {
                    initializeStateCaches();
                    cachesBuilt = true;
                }
            }
        }
    }

    private static void initializeStateCaches()
    {
        FramedBlocks.LOGGER.debug("Initializing custom state metadata caches");
        Stopwatch watch = Stopwatch.createStarted();
        long[] counts = new long[] { 0, 1 }; // +1 for the empty instance
        BuiltInRegistries.BLOCK.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(block -> block instanceof IFramedBlock)
                .map(Block::getStateDefinition)
                .map(StateDefinition::getPossibleStates)
                .forEach(states ->
                {
                    Map<StateCache, StateCache> cacheDedup = new HashMap<>();
                    for (BlockState state : states)
                    {
                        StateCache cache = ((IFramedBlock) state.getBlock()).initCache(state);
                        if (cache.equals(StateCache.EMPTY))
                        {
                            state.framedblocks$initCache(StateCache.EMPTY);
                        }
                        else
                        {
                            state.framedblocks$initCache(
                                    Objects.requireNonNullElse(cacheDedup.putIfAbsent(cache, cache), cache)
                            );
                        }
                    }
                    counts[0] += states.size();
                    counts[1] += cacheDedup.size();
                });
        watch.stop();
        FramedBlocks.LOGGER.debug("Initialized {} unique caches for {} states in {}", counts[1], counts[0], watch);
    }



    public static final class CacheReloader implements ResourceManagerReloadListener
    {
        public static final CacheReloader INSTANCE = new CacheReloader();
        public static final ResourceLocation LISTENER_ID = Utils.rl("state_caches");

        private CacheReloader() { }

        @Override
        public void onResourceManagerReload(ResourceManager mgr)
        {
            initializeStateCaches();
        }
    }



    private StateCacheBuilder() { }
}
