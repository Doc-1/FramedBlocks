package io.github.xfacthd.framedblocks.common.data.shapes;

import com.google.common.base.Stopwatch;
import com.mojang.logging.LogUtils;
import io.github.xfacthd.framedblocks.api.shapes.ReloadableShapeProvider;
import io.github.xfacthd.framedblocks.api.shapes.ShapeCache;
import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class ShapeReloader implements ResourceManagerReloadListener
{
    public static final ShapeReloader INSTANCE = new ShapeReloader();
    public static final ResourceLocation LISTENER_ID = Utils.rl("voxel_shapes");
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<ShapeCache<?>> CACHES = new ArrayList<>();
    private static final List<ReloadableShapeProvider> PROVIDERS = new ArrayList<>();

    private ShapeReloader() { }

    public static synchronized void addCache(ShapeCache<?> cache)
    {
        CACHES.add(cache);
    }

    public static synchronized void addProvider(ReloadableShapeProvider provider)
    {
        PROVIDERS.add(provider);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager)
    {
        LOGGER.info("Reloading {} caches and {} reloadable shape providers...", CACHES.size(), PROVIDERS.size());
        Stopwatch watch = Stopwatch.createStarted();
        try
        {
            CACHES.forEach(ShapeCache::reload);
            PROVIDERS.forEach(ReloadableShapeProvider::reload);
        }
        catch (Throwable t)
        {
            LogUtils.getLogger().error("Encountered an error while reloading shapes", t);
        }
        watch.stop();
        LOGGER.info("{} caches and {} reloadable shape providers reloaded, took {}", CACHES.size(), PROVIDERS.size(), watch);

        watch = Stopwatch.createStarted();
        List<BlockState> states = PROVIDERS.stream()
                .map(ReloadableShapeProvider::getStates)
                .flatMap(List::stream)
                .distinct()
                .toList();
        // Rebuild vanilla state cache to update cached occlusion shapes
        states.forEach(BlockBehaviour.BlockStateBase::initCache);
        watch.stop();
        LOGGER.info("Rebuilt vanilla state caches for {} states, took {}", states.size(), watch);
    }
}
