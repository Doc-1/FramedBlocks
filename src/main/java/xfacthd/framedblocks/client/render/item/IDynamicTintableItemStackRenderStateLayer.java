package xfacthd.framedblocks.client.render.item;

import it.unimi.dsi.fastutil.ints.Int2IntMap;

public interface IDynamicTintableItemStackRenderStateLayer
{
    default void framedblocks$setDynamicItemTintValues(Int2IntMap tintValues)
    {
        throw new AssertionError();
    }
}
