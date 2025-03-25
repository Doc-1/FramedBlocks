package xfacthd.framedblocks.api.camo.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.WeightedVariants;
import net.minecraft.core.BlockPos;
import xfacthd.framedblocks.api.camo.CamoClientHandler;
import xfacthd.framedblocks.api.model.util.WeightedBakedModelAccess;
import xfacthd.framedblocks.api.util.ConfigView;

final class BlockCamoClientHandler extends CamoClientHandler<BlockCamoContent>
{
    static final CamoClientHandler<BlockCamoContent> INSTANCE = new BlockCamoClientHandler();

    private BlockCamoClientHandler() { }

    @Override
    public BlockStateModel getOrCreateModel(BlockCamoContent camo)
    {
        BlockStateModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(camo.getState());
        if (model instanceof WeightedVariants weighted && !ConfigView.Client.INSTANCE.supportWeightedVariants())
        {
            model = ((WeightedBakedModelAccess) weighted).framedblocks$getParentModel();
        }
        return model;
    }

    @Override
    public Particle makeHitDestroyParticle(
            ClientLevel level, double x, double y, double z, double sx, double sy, double sz, BlockCamoContent camo, BlockPos pos
    )
    {
        return new TerrainParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, camo.getState(), pos);
    }
}
