package io.github.xfacthd.framedblocks.api.camo;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;

public abstract class CamoClientHandler<T extends CamoContent<T>>
{
    /**
     * {@return the {@link BlockStateModel} to be rendered for the given {@link CamoContent}}
     * @implNote this method must be backed by a cache
     */
    public abstract BlockStateModel getOrCreateModel(T camo);

    /**
     * {@return a {@link Particle} to be spawned when a block with the given {@link CamoContent} is punched or broken}
     */
    public abstract Particle makeHitDestroyParticle(
            ClientLevel level, double x, double y, double z, double sx, double sy, double sz, T camo, BlockPos pos
    );
}
