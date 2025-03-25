package xfacthd.framedblocks.api;

import xfacthd.framedblocks.api.util.Utils;

@SuppressWarnings({ "unused" })
public interface FramedBlocksClientAPI
{
    FramedBlocksClientAPI INSTANCE = Utils.loadService(FramedBlocksClientAPI.class);
}
