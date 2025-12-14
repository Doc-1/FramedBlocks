package io.github.xfacthd.framedblocks.client.render.block.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.UnknownNullability;

public final class FramedTankRenderState extends BlockEntityRenderState
{
    public int tint;
    @UnknownNullability
    public Identifier stillTex;
    @UnknownNullability
    public Identifier flowTex;
    @UnknownNullability
    public ChunkSectionLayer chunkLayer;
    public int fluidAmount;
}
