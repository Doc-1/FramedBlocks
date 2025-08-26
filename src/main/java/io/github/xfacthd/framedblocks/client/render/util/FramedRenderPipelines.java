package io.github.xfacthd.framedblocks.client.render.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.client.renderer.RenderPipelines;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

public final class FramedRenderPipelines
{
    public static final RenderPipeline LINES_NO_DEPTH = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
            .withLocation(Utils.rl("pipeline/lines_no_depth"))
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();

    public static void onRegisterRenderPipelines(RegisterRenderPipelinesEvent event)
    {
        event.registerPipeline(LINES_NO_DEPTH);
    }



    private FramedRenderPipelines() { }
}
