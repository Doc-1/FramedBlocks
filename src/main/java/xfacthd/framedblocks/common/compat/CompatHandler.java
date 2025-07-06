package xfacthd.framedblocks.common.compat;

import net.neoforged.bus.api.IEventBus;
import xfacthd.framedblocks.common.compat.additionalplacements.AdditionalPlacementsCompat;
import xfacthd.framedblocks.common.compat.ae2.AppliedEnergisticsCompat;
import xfacthd.framedblocks.common.compat.amendments.AmendmentsCompat;
import xfacthd.framedblocks.common.compat.atlasviewer.AtlasViewerCompat;
import xfacthd.framedblocks.common.compat.buildinggadgets.BuildingGadgetsCompat;
import xfacthd.framedblocks.common.compat.create.CreateCompat;
import xfacthd.framedblocks.common.compat.diagonalblocks.DiagonalBlocksCompat;
import xfacthd.framedblocks.common.compat.jei.JeiCompat;
import xfacthd.framedblocks.common.compat.searchables.SearchablesCompat;

public final class CompatHandler
{
    public static void init(IEventBus modBus)
    {
        AdditionalPlacementsCompat.init();
        AppliedEnergisticsCompat.init(modBus);
        AmendmentsCompat.init();
        AtlasViewerCompat.init(modBus);
        BuildingGadgetsCompat.init(modBus);
        DiagonalBlocksCompat.init(modBus);
        JeiCompat.init();
        SearchablesCompat.init();
    }

    public static void commonSetup()
    {
        CreateCompat.commonSetup();
    }



    private CompatHandler() { }
}
