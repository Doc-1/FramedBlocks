package xfacthd.framedblocks.api.internal.duck;

import xfacthd.framedblocks.api.block.cache.StateCache;

public interface StateCacheAccessor
{
    default void framedblocks$initCache(StateCache cache)
    {
        throw new AssertionError();
    }

    default StateCache framedblocks$getCache()
    {
        throw new AssertionError();
    }
}
