package io.github.xfacthd.framedblocks.common.data.collapsible;

import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public record HammerTarget(Direction face, Vec3 pos)
{
    public HammerTarget(Direction face, Vec3 hitLoc, boolean relative)
    {
        this(face, relative ? Utils.fraction(hitLoc) : hitLoc);
    }
}
