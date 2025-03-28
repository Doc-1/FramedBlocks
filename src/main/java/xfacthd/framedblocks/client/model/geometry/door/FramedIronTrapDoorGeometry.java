package xfacthd.framedblocks.client.model.geometry.door;

import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;

public class FramedIronTrapDoorGeometry extends FramedTrapDoorGeometry
{
    public FramedIronTrapDoorGeometry(GeometryFactory.Context ctx)
    {
        super(ctx);
    }

    @Override
    public boolean useBaseModel()
    {
        return true;
    }
}