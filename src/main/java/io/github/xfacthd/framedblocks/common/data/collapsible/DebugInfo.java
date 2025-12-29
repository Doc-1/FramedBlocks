package io.github.xfacthd.framedblocks.common.data.collapsible;

import io.github.xfacthd.framedblocks.api.util.Triangle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record DebugInfo(
        BlockPos pos,
        Direction face,
        Triangle tri1,
        Triangle tri2,
        @Nullable Vec3 target,
        float[] heights,
        boolean rotated
) { }
