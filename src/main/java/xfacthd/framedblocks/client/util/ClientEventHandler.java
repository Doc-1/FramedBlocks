package xfacthd.framedblocks.client.util;

import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import xfacthd.framedblocks.common.crafting.saw.FramingSawRecipeCache;

public final class ClientEventHandler
{
    public static void onClientDisconnect(@SuppressWarnings("unused") ClientPlayerNetworkEvent.LoggingOut event)
    {
        FramingSawRecipeCache.get(true).clear();
    }



    private ClientEventHandler() { }
}
