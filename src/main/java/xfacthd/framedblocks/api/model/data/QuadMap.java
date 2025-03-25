package xfacthd.framedblocks.api.model.data;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class QuadMap
{
    private static final int SIDE_COUNT = Direction.values().length + 1;

    @SuppressWarnings("unchecked")
    private final ArrayList<BakedQuad>[] quads = new ArrayList[SIDE_COUNT];

    public ArrayList<BakedQuad> get(@Nullable Direction side)
    {
        int idx = Utils.maskNullDirection(side);
        ArrayList<BakedQuad> list = quads[idx];
        if (list == null)
        {
            list = quads[idx] = new ArrayList<>();
        }
        return list;
    }

    public List<BakedQuad>[] build()
    {
        //noinspection unchecked
        List<BakedQuad>[] quadsOut = new List[SIDE_COUNT];
        for (int i = 0; i < quads.length; i++)
        {
            List<BakedQuad> list = quads[i];
            if (list == null || list.isEmpty())
            {
                quadsOut[i] = Collections.emptyList();
            }
            else
            {
                quadsOut[i] = quads[i];
            }
        }
        return quadsOut;
    }
}
