package io.github.xfacthd.framedblocks.api.model.data;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@ApiStatus.NonExtendable
public abstract class QuadMap
{
    public abstract ArrayList<BakedQuad> get(@Nullable Direction side);
}
