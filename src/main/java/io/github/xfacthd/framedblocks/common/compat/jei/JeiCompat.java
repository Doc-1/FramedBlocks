package io.github.xfacthd.framedblocks.common.compat.jei;

import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

public final class JeiCompat
{
    public static void init()
    {
        if (ModList.get().isLoaded("jei"))
        {
            GuardedAccess.init();
        }
    }

    private static final class GuardedAccess
    {
        public static void init()
        {
            NeoForge.EVENT_BUS.addListener(FramedJeiPlugin::onRecipesReceived);
        }
    }

    private JeiCompat() { }
}
