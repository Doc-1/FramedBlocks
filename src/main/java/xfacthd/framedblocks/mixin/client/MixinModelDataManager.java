package xfacthd.framedblocks.mixin.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xfacthd.framedblocks.api.block.render.AppearanceHelper;

import java.util.Collections;
import java.util.Map;

@Mixin(ModelDataManager.class)
@SuppressWarnings("UnstableApiUsage")
public class MixinModelDataManager implements AppearanceHelper.ModelDataAccessor
{
    @Shadow(remap = false)
    @Final
    private Map<ChunkPos, Map<BlockPos, ModelData>> modelDataCache;

    @Override
    @Nullable
    public ModelData framedblocks$getCachedAt(BlockPos pos)
    {
        return modelDataCache.getOrDefault(new ChunkPos(pos), Collections.emptyMap()).get(pos);
    }
}
