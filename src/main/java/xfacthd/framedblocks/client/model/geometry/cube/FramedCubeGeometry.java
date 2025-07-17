package xfacthd.framedblocks.client.model.geometry.cube;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;

public class FramedCubeGeometry extends Geometry
{
    public FramedCubeGeometry(@SuppressWarnings("unused") GeometryFactory.Context ctx) { }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad, ModelData modelData) { }

    @Override
    public boolean forceUngeneratedBaseModel()
    {
        return true;
    }
}
