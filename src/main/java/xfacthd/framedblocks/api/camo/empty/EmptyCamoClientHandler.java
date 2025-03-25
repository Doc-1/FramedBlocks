package xfacthd.framedblocks.api.camo.empty;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.FramedBlocksAPI;
import xfacthd.framedblocks.api.camo.CamoClientHandler;

final class EmptyCamoClientHandler extends CamoClientHandler<EmptyCamoContent>
{
    static final CamoClientHandler<EmptyCamoContent> INSTANCE = new EmptyCamoClientHandler();

    private EmptyCamoClientHandler() { }

    @Override
    public BlockStateModel getOrCreateModel(EmptyCamoContent camo)
    {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(FramedBlocksAPI.INSTANCE.getDefaultModelState());
    }

    @Override
    public Particle makeHitDestroyParticle(
            ClientLevel level, double x, double y, double z, double sx, double sy, double sz, EmptyCamoContent camo, BlockPos pos
    )
    {
        BlockState state = FramedBlocksAPI.INSTANCE.getDefaultModelState();
        return new TerrainParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, state, pos);
    }
}
