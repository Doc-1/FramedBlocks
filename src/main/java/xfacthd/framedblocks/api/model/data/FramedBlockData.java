package xfacthd.framedblocks.api.model.data;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.cache.StateCache;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.CamoContent;
import xfacthd.framedblocks.api.camo.empty.EmptyCamoContainer;

public final class FramedBlockData extends AbstractFramedBlockData
{
    public static final boolean[] NO_CULLED_FACES = new boolean[0];
    public static final FramedBlockData EMPTY = new FramedBlockData(EmptyCamoContainer.EMPTY, false);
    private static final Direction[] DIRECTIONS = Direction.values();

    private final CamoContainer<?, ?> camoContainer;
    private final CamoContent<?> camoContent;
    private final byte hidden;
    private final boolean secondPart;
    private final boolean reinforced;
    private final boolean emissive;

    public FramedBlockData(CamoContainer<?, ?> camoContent, boolean secondPart)
    {
        this(camoContent, NO_CULLED_FACES, secondPart, false, false);
    }

    public FramedBlockData(CamoContainer<?, ?> camoContainer, boolean[] hidden, boolean secondPart, boolean reinforced, boolean emissive)
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
        this.secondPart = secondPart;
        this.reinforced = reinforced;
        this.emissive = emissive;
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

    public boolean isSecondPart()
    {
        return secondPart;
    }

    public boolean isReinforced()
    {
        return reinforced;
    }

    public boolean isEmissive()
    {
        return emissive;
    }

    /**
     * Computes a bit mask of visible faces according to the occlusion state stored in this {@code FramedBlockData}
     * and the full-face info stored in the provided {@link StateCache}
     *
     * @param stateCache The {@link StateCache} to pull the full-face info from
     * @param forCached Whether the mask should be computed for the cached path ({@link StateCache#isFullFace(Direction)}
     *                  returns false) or the uncached path ({@link StateCache#isFullFace(Direction)} returns true)
     */
    public int computeFaceMask(StateCache stateCache, boolean forCached)
    {
        int mask = 0;
        for (Direction side : DIRECTIONS)
        {
            if (stateCache.isFullFace(side) ^ forCached)
            {
                mask |= 1 << side.ordinal();
            }
        }
        return mask & ~hidden;
    }

    @Override
    public FramedBlockData unwrap(BlockState partState)
    {
        return this;
    }

    @Override
    public FramedBlockData unwrap(boolean secondary)
    {
        return this;
    }

    @Override
    public boolean isCamoEmissive()
    {
        return camoContent.isEmissive();
    }
}
