package xfacthd.framedblocks.client.util;

import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import xfacthd.framedblocks.common.crafting.FramingSawRecipeCache;

public final class ClientEventHandler
{
    public static void onClientDisconnect(@SuppressWarnings("unused") final ClientPlayerNetworkEvent.LoggingOut event)
    {
        FramingSawRecipeCache.get(true).clear();
    }



    private ClientEventHandler() { }
}
