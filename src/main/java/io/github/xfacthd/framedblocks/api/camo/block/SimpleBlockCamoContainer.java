package io.github.xfacthd.framedblocks.api.camo.block;

import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public final class SimpleBlockCamoContainer extends AbstractBlockCamoContainer<SimpleBlockCamoContainer>
{
    private final SimpleBlockCamoContainerFactory factory;

    public SimpleBlockCamoContainer(BlockState state, SimpleBlockCamoContainerFactory factory)
    {
        super(state);
        this.factory = factory;
    }

    @Override
    public int hashCode()
    {
        return content.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != SimpleBlockCamoContainer.class) return false;
        return content.equals(((SimpleBlockCamoContainer) obj).content);
    }

    @Override
    public String toString()
    {
        return "SimpleBlockCamoContainer{content=" + content + "}";
    }

    @Override
    public SimpleBlockCamoContainerFactory getFactory()
    {
        return factory;
    }
}
