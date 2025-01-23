package xfacthd.framedblocks.api.model.data;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface QuadMap
{
    ArrayList<BakedQuad> get(@Nullable Direction side);

    ArrayList<BakedQuad> put(@Nullable Direction side, ArrayList<BakedQuad> quadList);
}
