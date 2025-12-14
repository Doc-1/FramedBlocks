package io.github.xfacthd.framedblocks.api.model;

import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface ExtendedBlockModelPart extends BlockModelPart
{
    // Soft-override of an Iris extension to provide the "shader state" to its pipeline
    @Nullable
    BlockState getBlockAppearance();
}
