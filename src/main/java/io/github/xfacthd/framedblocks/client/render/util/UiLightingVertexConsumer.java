package io.github.xfacthd.framedblocks.client.render.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.LightCoordsUtil;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;

/// Delegating [VertexConsumer] to ensure render paths without overrideable light coordinates
/// render correctly in a UI/PiP context
public final class UiLightingVertexConsumer extends VertexConsumerWrapper
{
    public UiLightingVertexConsumer(VertexConsumer parent)
    {
        super(parent);
    }

    @Override
    public VertexConsumer setLight(int packedLightCoords)
    {
        return parent.setLight(LightCoordsUtil.UI_FULL_BRIGHT);
    }

    @Override
    public VertexConsumer setUv2(int u, int v)
    {
        return parent.setUv2(0, 0);
    }

    @Override
    public void addVertex(float x, float y, float z, int color, float u, float v, int overlayCoords, int lightCoords, float nx, float ny, float nz)
    {
        parent.addVertex(x, y, z, color, u, v, overlayCoords, LightCoordsUtil.UI_FULL_BRIGHT, nx, ny, nz);
    }
}
