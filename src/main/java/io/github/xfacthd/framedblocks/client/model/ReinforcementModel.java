package io.github.xfacthd.framedblocks.client.model;

import io.github.xfacthd.framedblocks.api.model.data.QuadMap;
import io.github.xfacthd.framedblocks.api.model.util.ModelUtils;
import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class ReinforcementModel
{
    public static final BlockState SHADER_STATE = Blocks.OBSIDIAN.defaultBlockState();
    public static final ResourceLocation MODEL_ID = Utils.rl("block/framed_reinforcement");
    private static final ModelBaker.SharedOperationKey<ReinforcementModel> REINFORCEMENT_KEY = ModelUtils.makeSharedOpsKey(
            baker -> new ReinforcementModel(SimpleModelWrapper.bake(baker, ReinforcementModel.MODEL_ID, BlockModelRotation.X0_Y0))
    );
    private static final Direction[] DIRECTIONS = Direction.values();
    private final SimpleModelWrapper baseModel;
    private final BlockModelPart[] cachedFilteredParts = new BlockModelPart[256];

    public static ReinforcementModel getOrCreate(ModelBaker baker)
    {
        return baker.compute(REINFORCEMENT_KEY);
    }

    private ReinforcementModel(SimpleModelWrapper baseModel)
    {
        this.baseModel = baseModel;
    }

    public BlockModelPart getFiltered(int faceMask, TriState ambientOcclusion)
    {
        faceMask |= ambientOcclusion.ordinal() << 6;

        BlockModelPart part = cachedFilteredParts[faceMask];
        if (part == null)
        {
            QuadMap quadMap = new QuadMapImpl();
            for (Direction side : DIRECTIONS)
            {
                if ((faceMask & (1 << side.ordinal())) != 0)
                {
                    quadMap.get(side).add(baseModel.getQuads(side).getFirst());
                }
            }
            cachedFilteredParts[faceMask] = part = ModelUtils.makeModelPart(
                    quadMap,
                    ambientOcclusion,
                    baseModel.particleIcon(),
                    ChunkSectionLayer.CUTOUT,
                    SHADER_STATE
            );
        }
        return part;
    }
}
