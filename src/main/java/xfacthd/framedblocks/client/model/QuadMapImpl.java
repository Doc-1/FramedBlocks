package xfacthd.framedblocks.client.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class QuadMapImpl extends QuadMap
{
    private static final int SIDE_COUNT = Direction.values().length + 1;

    @SuppressWarnings("unchecked")
    private final List<BakedQuad>[] quads = new List[SIDE_COUNT];

    @Override
    public ArrayList<BakedQuad> get(@Nullable Direction side)
    {
        int idx = Utils.maskNullDirection(side);
        ArrayList<BakedQuad> list = (ArrayList<BakedQuad>) quads[idx];
        if (list == null)
        {
            quads[idx] = list = new ArrayList<>();
        }
        return list;
    }

    /**
     * Forcefully insert an existing list into this map. Must only be used if the list for the provided side
     * is known to never be retrieved via {@link #get(Direction)} after this operation!
     */
    public void set(@Nullable Direction side, List<BakedQuad> list)
    {
        quads[Utils.maskNullDirection(side)] = list;
    }

    public List<BakedQuad>[] build()
    {
        for (int i = 0; i < quads.length; i++)
        {
            List<BakedQuad> list = quads[i];
            if (list == null || list.isEmpty())
            {
                quads[i] = Collections.emptyList();
            }
        }
        return quads;
    }
}
