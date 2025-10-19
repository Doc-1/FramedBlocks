package io.github.xfacthd.framedblocks.api.shapes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public sealed interface ShapeLookup permits EmptyShapeLookup, SingleShapeLookup, MapBackedShapeLookup, ReloadableShapeLookup
{
    ShapeLookup EMPTY = EmptyShapeLookup.INSTANCE;

    VoxelShape getShape(BlockState state);

    VoxelShape getOcclusionShape(BlockState state);

    boolean hasSeparateOcclusionShapes();

    boolean occludesBeaconBeam(BlockState state);

    static ShapeLookup of(ShapeGenerator generator, Block owner)
    {
        if (generator == ShapeGenerator.EMPTY) return EMPTY;
        return ReloadableShapeLookup.of(generator, owner.getStateDefinition().getPossibleStates());
    }
}
