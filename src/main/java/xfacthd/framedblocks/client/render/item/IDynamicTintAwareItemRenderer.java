package xfacthd.framedblocks.client.render.item;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import org.jetbrains.annotations.Nullable;

public interface IDynamicTintAwareItemRenderer
{
    void framedblocks$setItemTintValues(@Nullable Int2IntMap tintValues);
}
