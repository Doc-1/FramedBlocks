package xfacthd.framedblocks.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelLoader;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.util.Utils;

import java.util.Objects;

public final class ReinforcementModel
{
    public static final BlockState SHADER_STATE = Blocks.OBSIDIAN.defaultBlockState();
    private static final ResourceLocation LOCATION = Utils.rl("block/framed_reinforcement");
    public static final StandaloneModelKey<QuadCollection> MODEL_KEY = new StandaloneModelKey<>(LOCATION);
    private static final Direction[] DIRECTIONS = Direction.values();
    @Nullable
    private static SimpleModelWrapper baseModel = null;
    private static final BlockModelPart[] CACHED_FILTERED_PARTS = new BlockModelPart[256];

    public static BlockModelPart getFiltered(int faceMask, TriState ambientOcclusion)
    {
        faceMask |= ambientOcclusion.ordinal() << 6;

        BlockModelPart part = CACHED_FILTERED_PARTS[faceMask];
        if (part == null)
        {
            Objects.requireNonNull(baseModel);

            QuadMap quadMap = new QuadMap();
            for (Direction side : DIRECTIONS)
            {
                if ((faceMask & (1 << side.ordinal())) != 0)
                {
                    quadMap.get(side).add(baseModel.getQuads(side).getFirst());
                }
            }
            CACHED_FILTERED_PARTS[faceMask] = part = ModelUtils.makeModelPart(
                    quadMap,
                    ambientOcclusion,
                    baseModel.particleIcon(),
                    RenderType.cutout(),
                    SHADER_STATE
            );
        }
        return part;
    }

    public static void reload(StandaloneModelLoader.BakedModels models)
    {
        QuadCollection quads = Objects.requireNonNull(models.get(MODEL_KEY));
        baseModel = new SimpleModelWrapper(quads, true, quads.getAll().getFirst().sprite(), RenderType.cutout());
    }



    private ReinforcementModel() { }
}
