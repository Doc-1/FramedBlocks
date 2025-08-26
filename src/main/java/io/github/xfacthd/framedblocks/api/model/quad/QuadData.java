package io.github.xfacthd.framedblocks.api.model.quad;

import io.github.xfacthd.framedblocks.api.model.util.ModelUtils;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import org.joml.Vector3f;

import java.util.Arrays;

public final class QuadData
{
    final BakedQuad quad;
    final int[] vertexData;
    private final boolean uvRotated;

    public QuadData(BakedQuad quad)
    {
        this.quad = quad;
        int[] vertexData = quad.vertices();
        this.vertexData = Arrays.copyOf(vertexData, vertexData.length);
        this.uvRotated = ModelUtils.isQuadRotated(this);
    }

    QuadData(QuadData data)
    {
        this.quad = data.quad;
        this.vertexData = Arrays.copyOf(data.vertexData, data.vertexData.length);
        this.uvRotated = data.uvRotated;
    }

    public BakedQuad quad()
    {
        return quad;
    }

    public boolean uvRotated()
    {
        return uvRotated;
    }

    public float pos(int vert, int idx)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
        return Float.intBitsToFloat(vertexData[offset + idx]);
    }

    public void pos(int vert, float[] out, int startIdx)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
        out[startIdx] = Float.intBitsToFloat(vertexData[offset]);
        out[startIdx + 1] = Float.intBitsToFloat(vertexData[offset + 1]);
        out[startIdx + 2] = Float.intBitsToFloat(vertexData[offset + 2]);
    }

    public Vector3f pos(int vert, Vector3f out)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
        out.x = Float.intBitsToFloat(vertexData[offset]);
        out.y = Float.intBitsToFloat(vertexData[offset + 1]);
        out.z = Float.intBitsToFloat(vertexData[offset + 2]);
        return out;
    }

    public void pos(int vert, int idx, float val)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
        vertexData[offset + idx] = Float.floatToRawIntBits(val);
    }

    public void pos(int vert, float x, float y, float z)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
        vertexData[offset] = Float.floatToRawIntBits(x);
        vertexData[offset + 1] = Float.floatToRawIntBits(y);
        vertexData[offset + 2] = Float.floatToRawIntBits(z);
    }

    public float uv(int vert, int idx)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
        return Float.intBitsToFloat(vertexData[offset + idx]);
    }

    public void uv(int vert, float[] out, int startIdx)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
        out[startIdx] = Float.intBitsToFloat(vertexData[offset]);
        out[startIdx + 1] = Float.intBitsToFloat(vertexData[offset + 1]);
    }

    public void uv(int vert, int idx, float val)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
        vertexData[offset + idx] = Float.floatToRawIntBits(val);
    }

    public void uv(int vert, float u, float v)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
        vertexData[offset] = Float.floatToRawIntBits(u);
        vertexData[offset + 1] = Float.floatToRawIntBits(v);
    }

    public float normal(int vert, int idx)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.NORMAL;
        int packedNormal = vertexData[offset];
        return ((byte) ((packedNormal >> (8 * idx)) & 0xFF)) / 127F;
    }

    public int color(int vert, int idx)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.COLOR;
        int packedColor = vertexData[offset];
        return (packedColor >> (8 * idx)) & 0xFF;
    }

    public void color(int vert, int idx, int val)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.COLOR;
        int packedColor = vertexData[offset];
        vertexData[offset] = ((val & 0xFF) << (8 * idx)) | (packedColor & ~(0x000000FF << (8 * idx)));
    }

    public int light(int vert)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.UV2;
        return vertexData[offset];
    }

    public void light(int vert, int val)
    {
        int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.UV2;
        vertexData[offset] = val;
    }

    public Direction recomputeNormals()
    {
        int normal = ClientHooks.computeQuadNormal(vertexData);

        for (int vert = 0; vert < 4; vert++)
        {
            int offset = vert * IQuadTransformer.STRIDE + IQuadTransformer.NORMAL;
            vertexData[offset] = (normal & 0x00FFFFFF) | (vertexData[offset] & 0xFF000000);
        }

        float nX = ((byte) ( normal        & 0xFF)) / 127F;
        float nY = ((byte) ((normal >>  8) & 0xFF)) / 127F;
        float nZ = ((byte) ((normal >> 16) & 0xFF)) / 127F;
        return Direction.getApproximateNearest(nX, nY, nZ);
    }
}
