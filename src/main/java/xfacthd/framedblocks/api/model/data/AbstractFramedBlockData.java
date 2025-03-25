package xfacthd.framedblocks.api.model.data;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

// TODO: make sealed when FramedDoubleBlockData becomes API
@ApiStatus.NonExtendable
public abstract class AbstractFramedBlockData
{
    public static final ModelProperty<AbstractFramedBlockData> PROPERTY = new ModelProperty<>();

    public abstract FramedBlockData unwrap(BlockState partState);

    public abstract FramedBlockData unwrap(boolean secondary);

    public abstract boolean isCamoEmissive();



    @Nullable
    @Contract("_,_,!null -> !null")
    public static FramedBlockData getOrDefault(ModelData modelData, BlockState partState, @Nullable FramedBlockData defaultData)
    {
        AbstractFramedBlockData data = modelData.get(PROPERTY);
        return data != null ? data.unwrap(partState) : defaultData;
    }
}
