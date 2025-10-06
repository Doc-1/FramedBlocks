package io.github.xfacthd.framedblocks.client.render.block.state;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Quaternionfc;

public final class FramedChestRenderState extends BlockEntityRenderState
{
    public BlockAndTintGetter level = EmptyBlockAndTintGetter.INSTANCE;
    public BlockPos pos = BlockPos.ZERO;
    @UnknownNullability
    public BlockState state = null;
    @UnknownNullability
    public BlockStateModel model = null;
    public float rotOriginX;
    public float rotOriginZ;
    @UnknownNullability
    public Quaternionfc lidAngle = null;
}
