package xfacthd.framedblocks.api;

import org.jetbrains.annotations.ApiStatus;
import xfacthd.framedblocks.api.util.Utils;

@ApiStatus.NonExtendable
@SuppressWarnings({ "unused" })
public interface FramedBlocksClientAPI
{
    FramedBlocksClientAPI INSTANCE = Utils.loadService(FramedBlocksClientAPI.class);
}
