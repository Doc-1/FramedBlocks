package xfacthd.framedblocks.client.util.duck;

import xfacthd.framedblocks.mixin.client.AccessorMultiPlayerGameMode;

@SuppressWarnings("unused") // Used via interface injection
public interface DefaultedAccessorMultiPlayerGameMode extends AccessorMultiPlayerGameMode
{
    @Override
    default void framedblocks$setDestroyDelay(int delay)
    {
        throw new AssertionError();
    }
}
