package io.github.xfacthd.framedblocks.api.model.util;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.neoforged.neoforge.client.model.quad.BakedColors;

public final class QuadUtils
{
    /**
     * Creates a shallow copy of the given BakedQuad in order to invert the tint index for BakedQuads
     * used by the second model of a double block
     */
    public static BakedQuad invertTintIndex(BakedQuad quad)
    {
        // Avoid the unnecessary copy if the quad isn't tinted at all
        if (quad.tintIndex() == -1) return quad;

        return new BakedQuad(
                quad.position0(),
                quad.position1(),
                quad.position2(),
                quad.position3(),
                quad.packedUV0(),
                quad.packedUV1(),
                quad.packedUV2(),
                quad.packedUV3(),
                ModelUtils.encodeSecondaryTintIndex(quad.tintIndex()),
                quad.direction(),
                quad.sprite(),
                quad.shade(),
                quad.lightEmission(),
                quad.bakedNormals(),
                quad.bakedColors(),
                quad.hasAmbientOcclusion()
        );
    }

    public static BakedQuad setLightEmission(BakedQuad quad, int lightEmission)
    {
        return setLightEmission(quad, lightEmission, quad.shade(), quad.hasAmbientOcclusion());
    }

    public static BakedQuad setLightEmission(BakedQuad quad, int lightEmission, boolean shade, boolean ao)
    {
        return new BakedQuad(
                quad.position0(),
                quad.position1(),
                quad.position2(),
                quad.position3(),
                quad.packedUV0(),
                quad.packedUV1(),
                quad.packedUV2(),
                quad.packedUV3(),
                quad.tintIndex(),
                quad.direction(),
                quad.sprite(),
                shade,
                lightEmission,
                quad.bakedNormals(),
                quad.bakedColors(),
                ao
        );
    }

    public static BakedQuad setBakedColors(BakedQuad quad, BakedColors colors, boolean clearTintIndex)
    {
        return new BakedQuad(
                quad.position0(),
                quad.position1(),
                quad.position2(),
                quad.position3(),
                quad.packedUV0(),
                quad.packedUV1(),
                quad.packedUV2(),
                quad.packedUV3(),
                clearTintIndex ? -1 : quad.tintIndex(),
                quad.direction(),
                quad.sprite(),
                quad.shade(),
                quad.lightEmission(),
                quad.bakedNormals(),
                colors,
                quad.hasAmbientOcclusion()
        );
    }

    private QuadUtils() { }
}
