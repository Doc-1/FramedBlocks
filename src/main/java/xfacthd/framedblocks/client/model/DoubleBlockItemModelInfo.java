package xfacthd.framedblocks.client.model;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import xfacthd.framedblocks.api.camo.CamoList;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedDoubleBlockData;
import xfacthd.framedblocks.api.model.item.ItemModelInfo;

public class DoubleBlockItemModelInfo implements ItemModelInfo
{
    public static final DoubleBlockItemModelInfo INSTANCE = new DoubleBlockItemModelInfo();

    @Override
    public final ModelData buildItemModelData(BlockState state, CamoList camos)
    {
        AbstractFramedBlockData fbData = new FramedDoubleBlockData(
                ((IFramedDoubleBlock) state.getBlock()).getCache(state).getParts(),
                new FramedBlockData(camos.getCamo(0), false),
                new FramedBlockData(camos.getCamo(1), true)
        );

        ModelData.Builder builder = ModelData.builder().with(AbstractFramedBlockData.PROPERTY, fbData);
        appendItemModelData(builder, state);
        return builder.build();
    }

    protected void appendItemModelData(ModelData.Builder builder, BlockState state) { }
}
