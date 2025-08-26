package io.github.xfacthd.framedblocks.common.data.blueprint;

import io.github.xfacthd.framedblocks.api.blueprint.BlueprintCopyBehaviour;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;

public final class TubeCopyBehaviour implements BlueprintCopyBehaviour
{
    @Override
    public List<Property<?>> getPropertiesToCopy(BlockState state)
    {
        return List.of(PropertyHolder.THICK);
    }
}
