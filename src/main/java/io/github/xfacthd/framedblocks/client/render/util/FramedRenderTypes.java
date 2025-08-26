package io.github.xfacthd.framedblocks.client.render.util;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public final class FramedRenderTypes
{
    public static final RenderType LINES_NO_DEPTH = RenderType.create(
            "lines",
            256,
            false,
            false,
            FramedRenderPipelines.LINES_NO_DEPTH,
            RenderType.CompositeState.builder()
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
                    .setOutputState(RenderType.ITEM_ENTITY_TARGET)
                    .createCompositeState(false)
    );



    private FramedRenderTypes() { }
}
