package xfacthd.framedblocks.api.model.data;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.data.*;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.CamoContent;

public final class FramedBlockData
{
    public static final ModelProperty<FramedBlockData> PROPERTY = new ModelProperty<>();
    public static final ModelProperty<ModelData> CAMO_DATA = new ModelProperty<>();
    public static final ModelProperty<ModelData> AUX_DATA = new ModelProperty<>();
    public static final boolean[] NO_CULLED_FACES = new boolean[0];

    private final CamoContainer<?, ?> camoContainer;
    private final CamoContent<?> camoContent;
    private final byte hidden;
    private final boolean altModel;
    private final boolean reinforced;

    public FramedBlockData(CamoContainer<?, ?> camoContent, boolean altModel)
    {
        this(camoContent, NO_CULLED_FACES, altModel, false);
    }

    public FramedBlockData(CamoContainer<?, ?> camoContainer, boolean[] hidden, boolean altModel, boolean reinforced)
    {
        this.camoContainer = camoContainer;
        this.camoContent = camoContainer.getContent();
        byte mask = 0;
        for (int i = 0; i < hidden.length; i++)
        {
            if (hidden[i])
            {
                mask |= (byte) (1 << i);
            }
        }
        this.hidden = mask;
        this.altModel = altModel;
        this.reinforced = reinforced;
    }

    public CamoContainer<?, ?> getCamoContainer()
    {
        return camoContainer;
    }

    public CamoContent<?> getCamoContent()
    {
        return camoContent;
    }

    public boolean isSideHidden(Direction side)
    {
        return (hidden & (1 << side.ordinal())) != 0;
    }

    public boolean useAltModel()
    {
        return altModel;
    }

    public boolean isReinforced()
    {
        return reinforced;
    }
}
