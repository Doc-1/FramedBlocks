package io.github.xfacthd.framedblocks.common.blockentity.special;

import io.github.xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import io.github.xfacthd.framedblocks.api.blueprint.BlueprintData;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.blockentity.PackedCollapsibleBlockOffsets;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.component.CollapsibleBlockData;
import io.github.xfacthd.framedblocks.common.data.property.NullableDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.model.data.ModelData;
import org.jspecify.annotations.Nullable;

public class FramedCollapsibleBlockEntity extends FramedBlockEntity implements ICollapsibleBlockEntity
{
    private static final int DIRECTIONS = Direction.values().length;
    private static final int VERTEX_COUNT = 4;
    private static final int BIT_PER_VERTEX = 5;
    private static final int VERTEX_MASK = ~(-1 << BIT_PER_VERTEX);
    private static final NeighborVertex[][] VERTEX_MAPPINGS = makeVertexMapping();

    @Nullable
    private Direction collapsedFace = null;
    private int packedOffsets = 0;

    public FramedCollapsibleBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_COLLAPSIBLE_BLOCK.value(), pos, state);
    }

    public void handleDeform(Player player)
    {
        HitResult hit = player.pick(10D, 1F, false);
        if (!(hit instanceof BlockHitResult blockHit))
        {
            return;
        }

        Direction faceHit = blockHit.getDirection();
        Vec3 hitLoc = Utils.fraction(hit.getLocation());

        if (collapsedFace != null && faceHit != collapsedFace)
        {
            return;
        }

        int vert = vertexFromHit(faceHit, hitLoc);
        if (vert == 4)
        {
            for (int i = 0; i < 4; i++)
            {
                handleDeformOfVertex(player, faceHit, i);
            }
        }
        else
        {
            handleDeformOfVertex(player, faceHit, vert);
        }
    }

    private void handleDeformOfVertex(Player player, Direction faceHit, int vert)
    {
        int offset = getVertexOffset(vert);
        if (player.isShiftKeyDown() && collapsedFace != null && offset > 0)
        {
            int target = offset - 1;

            applyDeformation(vert, target, faceHit);
            deformNeighbors(faceHit, vert, target);
        }
        else if (!player.isShiftKeyDown() && offset < 16)
        {
            int target = offset + 1;

            applyDeformation(vert, target, faceHit);
            deformNeighbors(faceHit, vert, target);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void applyDeformation(int vertex, int offset, Direction faceHit)
    {
        offset = Mth.clamp(offset, 0, 16);

        if (offset == getVertexOffset(vertex))
        {
            return;
        }

        setVertexOffset(vertex, offset);

        if (offset == 0)
        {
            boolean noOffsets = true;
            for (int i = 0; i < 4; i++)
            {
                if (getVertexOffset(i) > 0)
                {
                    noOffsets = false;
                    break;
                }
            }

            if (noOffsets)
            {
                collapsedFace = null;
                level().setBlock(worldPosition, getBlockState().setValue(PropertyHolder.NULLABLE_FACE, NullableDirection.NONE), Block.UPDATE_ALL);
            }
            else
            {
                level().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
        else if (collapsedFace == null)
        {
            collapsedFace = faceHit;
            level().setBlock(worldPosition, getBlockState().setValue(PropertyHolder.NULLABLE_FACE, NullableDirection.fromDirection(collapsedFace)), Block.UPDATE_ALL);
        }
        else
        {
            level().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }

        setChangedWithoutSignalUpdate();
    }

    private void deformNeighbors(Direction faceHit, int srcVert, int offset)
    {
        NeighborVertex[] verts = VERTEX_MAPPINGS[getMappingIndex(faceHit, srcVert)];
        for (int i = 0; i < 3; i++)
        {
            NeighborVertex vert = verts[i];
            BlockPos pos = worldPosition.offset(vert.offset);
            if (level().getBlockEntity(pos) instanceof FramedCollapsibleBlockEntity be)
            {
                if (be.collapsedFace == null || be.collapsedFace == faceHit)
                {
                    be.applyDeformation(vert.targetVert, offset, faceHit);
                }
            }
        }
    }

    private void setVertexOffset(int vertex, int offset)
    {
        int idx = vertex * BIT_PER_VERTEX;
        int mask = VERTEX_MASK << idx;
        packedOffsets = (packedOffsets & ~mask) | (offset << idx);
    }

    public static int vertexFromHit(Direction faceHit, Vec3 loc)
    {
        if (Utils.isY(faceHit))
        {
            double ax = Math.abs((loc.x - .5) * 4D);
            double az = Math.abs((loc.z - .5) * 4D);
            if (ax >= 0D && ax <= 1D && az >= 0D && az <= 1D && az <= (1D - ax))
            {
                return 4;
            }

            if ((loc.z < .5F) == (faceHit == Direction.UP))
            {
                return loc.x < .5F ? 0 : 3;
            }
            else
            {
                return loc.x < .5F ? 1 : 2;
            }
        }
        else
        {
            double xz = Utils.isX(faceHit) ? loc.z : loc.x;
            double axz = Math.abs((xz - .5) * 4D);
            double ay = Math.abs((loc.y - .5D) * 4D);
            if (axz >= 0D && axz <= 1D && ay >= 0D && ay <= 1D && ay <= (1D - axz))
            {
                return 4;
            }

            boolean positive = faceHit == Direction.SOUTH || faceHit == Direction.WEST;
            if (loc.y < .5F)
            {
                return (xz < .5F) == positive ? 1 : 2;
            }
            else
            {
                return (xz < .5F) == positive ? 0 : 3;
            }
        }
    }

    @Nullable
    public Direction getCollapsedFace()
    {
        return collapsedFace;
    }

    public int getVertexOffset(int vertex)
    {
        return packedOffsets >> (vertex * 5) & 0x1F;
    }

    @Override
    public int getVertexOffset(BlockState state, int vertex)
    {
        return getVertexOffset(vertex);
    }

    @Override
    public int getPackedOffsets(BlockState state)
    {
        return packedOffsets;
    }

    @Override
    protected void attachAdditionalModelData(ModelData.Builder builder)
    {
        builder.with(PackedCollapsibleBlockOffsets.PROPERTY, new PackedCollapsibleBlockOffsets.Single(packedOffsets));
    }

    @Override
    protected void writeToDataPacket(ValueOutput valueOutput)
    {
        super.writeToDataPacket(valueOutput);
        valueOutput.putInt("offsets", packedOffsets);
        valueOutput.putByte("face", (byte) (collapsedFace == null ? -1 : collapsedFace.get3DDataValue()));
    }

    @Override
    protected boolean readFromDataPacket(ValueInput valueInput)
    {
        boolean needUpdate = super.readFromDataPacket(valueInput);

        int packed = valueInput.getIntOr("offsets", 0);
        if (packed != packedOffsets)
        {
            packedOffsets = packed;

            needUpdate = true;
            updateCulling(true, false);
        }

        int faceIdx = valueInput.getByteOr("face", (byte) -1);
        Direction face = faceIdx == -1 ? null : Direction.from3DDataValue(faceIdx);
        if (collapsedFace != face)
        {
            collapsedFace = face;

            needUpdate = true;
        }

        return needUpdate;
    }

    @Override
    protected void writeUpdateTag(ValueOutput valueOutput)
    {
        super.writeUpdateTag(valueOutput);
        valueOutput.putInt("offsets", packedOffsets);
        valueOutput.putByte("face", (byte) (collapsedFace == null ? -1 : collapsedFace.get3DDataValue()));
    }

    @Override
    public void handleUpdateTag(ValueInput valueInput)
    {
        packedOffsets = valueInput.getIntOr("offsets", 0);

        int face = valueInput.getByteOr("face", (byte) -1);
        collapsedFace = face == -1 ? null : Direction.from3DDataValue(face);

        super.handleUpdateTag(valueInput);
    }

    @Override
    protected BlueprintData appendCustomBlueprintData(BlueprintData blueprintData)
    {
        return blueprintData.withCustomData(FBContent.DC_TYPE_COLLAPSIBLE_BLOCK_DATA, new CollapsibleBlockData(collapsedFace, packedOffsets));
    }

    @Override
    protected void applyCustomDataFromBlueprint(TypedDataComponent<?> auxData)
    {
        if (auxData.value() instanceof CollapsibleBlockData(NullableDirection face, int offsets))
        {
            collapsedFace = face.toNullableDirection();
            packedOffsets = offsets;
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput valueOutput)
    {
        super.removeComponentsFromTag(valueOutput);
        valueOutput.discard("offsets");
        valueOutput.discard("face");
    }

    @Override
    protected void collectMiscComponents(DataComponentMap.Builder builder)
    {
        builder.set(FBContent.DC_TYPE_COLLAPSIBLE_BLOCK_DATA, new CollapsibleBlockData(collapsedFace, packedOffsets));
    }

    @Override
    protected void applyMiscComponents(DataComponentGetter input)
    {
        CollapsibleBlockData blockData = input.get(FBContent.DC_TYPE_COLLAPSIBLE_BLOCK_DATA);
        if (blockData != null)
        {
            collapsedFace = blockData.collapsedFace().toNullableDirection();
            packedOffsets = blockData.offsets();
        }
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput)
    {
        super.saveAdditional(valueOutput);
        valueOutput.putInt("offsets", packedOffsets);
        valueOutput.putInt("face", collapsedFace == null ? -1 : collapsedFace.get3DDataValue());
    }

    @Override
    public void loadAdditional(ValueInput valueInput)
    {
        super.loadAdditional(valueInput);
        packedOffsets = valueInput.getIntOr("offsets", 0);
        int face = valueInput.getIntOr("face", -1);
        collapsedFace = face == -1 ? null : Direction.from3DDataValue(face);
    }



    public static byte[] unpackOffsets(int packed)
    {
        byte[] offsets = new byte[4];

        for (int i = 0; i < 4; i++)
        {
            offsets[i] = (byte) (packed >> (i * 5) & 0x1F);
        }

        return offsets;
    }

    private static int getMappingIndex(Direction face, int srcVert)
    {
        return face.ordinal() * VERTEX_COUNT + srcVert;
    }

    private static NeighborVertex[][] makeVertexMapping()
    {
        NeighborVertex[][] mappings = new NeighborVertex[DIRECTIONS * VERTEX_COUNT][];

        putMapping(mappings, Direction.UP, 0, new NeighborVertex(new Vec3i(-1, 0,  0), 3), new NeighborVertex(new Vec3i( 0, 0, -1), 1), new NeighborVertex(new Vec3i(-1, 0, -1), 2));
        putMapping(mappings, Direction.UP, 1, new NeighborVertex(new Vec3i(-1, 0,  0), 2), new NeighborVertex(new Vec3i( 0, 0,  1), 0), new NeighborVertex(new Vec3i(-1, 0,  1), 3));
        putMapping(mappings, Direction.UP, 2, new NeighborVertex(new Vec3i( 1, 0,  0), 1), new NeighborVertex(new Vec3i( 0, 0,  1), 3), new NeighborVertex(new Vec3i( 1, 0,  1), 0));
        putMapping(mappings, Direction.UP, 3, new NeighborVertex(new Vec3i( 1, 0,  0), 0), new NeighborVertex(new Vec3i( 0, 0, -1), 2), new NeighborVertex(new Vec3i( 1, 0, -1), 1));

        putMapping(mappings, Direction.DOWN, 0, new NeighborVertex(new Vec3i(-1, 0,  0), 3), new NeighborVertex(new Vec3i( 0, 0,  1), 1), new NeighborVertex(new Vec3i(-1, 0,  1), 2));
        putMapping(mappings, Direction.DOWN, 1, new NeighborVertex(new Vec3i(-1, 0,  0), 2), new NeighborVertex(new Vec3i( 0, 0, -1), 0), new NeighborVertex(new Vec3i(-1, 0, -1), 3));
        putMapping(mappings, Direction.DOWN, 2, new NeighborVertex(new Vec3i( 1, 0,  0), 1), new NeighborVertex(new Vec3i( 0, 0, -1), 3), new NeighborVertex(new Vec3i( 1, 0, -1), 0));
        putMapping(mappings, Direction.DOWN, 3, new NeighborVertex(new Vec3i( 1, 0,  0), 0), new NeighborVertex(new Vec3i( 0, 0,  1), 2), new NeighborVertex(new Vec3i( 1, 0,  1), 1));

        putMapping(mappings, Direction.NORTH, 0, new NeighborVertex(new Vec3i( 1,  0, 0), 3), new NeighborVertex(new Vec3i( 0,  1, 0), 1), new NeighborVertex(new Vec3i( 1,  1, 0), 2));
        putMapping(mappings, Direction.NORTH, 1, new NeighborVertex(new Vec3i( 1,  0, 0), 2), new NeighborVertex(new Vec3i( 0, -1, 0), 0), new NeighborVertex(new Vec3i( 1, -1, 0), 3));
        putMapping(mappings, Direction.NORTH, 2, new NeighborVertex(new Vec3i(-1,  0, 0), 1), new NeighborVertex(new Vec3i( 0, -1, 0), 3), new NeighborVertex(new Vec3i(-1, -1, 0), 0));
        putMapping(mappings, Direction.NORTH, 3, new NeighborVertex(new Vec3i(-1,  0, 0), 0), new NeighborVertex(new Vec3i( 0,  1, 0), 2), new NeighborVertex(new Vec3i(-1,  1, 0), 1));

        putMapping(mappings, Direction.SOUTH, 0, new NeighborVertex(new Vec3i(-1,  0, 0), 3), new NeighborVertex(new Vec3i( 0,  1, 0), 1), new NeighborVertex(new Vec3i(-1,  1, 0), 2));
        putMapping(mappings, Direction.SOUTH, 1, new NeighborVertex(new Vec3i(-1,  0, 0), 2), new NeighborVertex(new Vec3i( 0, -1, 0), 0), new NeighborVertex(new Vec3i(-1, -1, 0), 3));
        putMapping(mappings, Direction.SOUTH, 2, new NeighborVertex(new Vec3i( 1,  0, 0), 1), new NeighborVertex(new Vec3i( 0, -1, 0), 3), new NeighborVertex(new Vec3i( 1, -1, 0), 0));
        putMapping(mappings, Direction.SOUTH, 3, new NeighborVertex(new Vec3i( 1,  0, 0), 0), new NeighborVertex(new Vec3i( 0,  1, 0), 2), new NeighborVertex(new Vec3i( 1,  1, 0), 1));

        putMapping(mappings, Direction.EAST, 0, new NeighborVertex(new Vec3i(0,  0,  1), 3), new NeighborVertex(new Vec3i( 0,  1, 0), 1), new NeighborVertex(new Vec3i(0,  1,  1), 2));
        putMapping(mappings, Direction.EAST, 1, new NeighborVertex(new Vec3i(0,  0,  1), 2), new NeighborVertex(new Vec3i( 0, -1, 0), 0), new NeighborVertex(new Vec3i(0, -1,  1), 3));
        putMapping(mappings, Direction.EAST, 2, new NeighborVertex(new Vec3i(0,  0, -1), 1), new NeighborVertex(new Vec3i( 0, -1, 0), 3), new NeighborVertex(new Vec3i(0, -1, -1), 0));
        putMapping(mappings, Direction.EAST, 3, new NeighborVertex(new Vec3i(0,  0, -1), 0), new NeighborVertex(new Vec3i( 0,  1, 0), 2), new NeighborVertex(new Vec3i(0,  1, -1), 1));

        putMapping(mappings, Direction.WEST, 0, new NeighborVertex(new Vec3i(0,  0, -1), 3), new NeighborVertex(new Vec3i( 0,  1, 0), 1), new NeighborVertex(new Vec3i(0,  1, -1), 2));
        putMapping(mappings, Direction.WEST, 1, new NeighborVertex(new Vec3i(0,  0, -1), 2), new NeighborVertex(new Vec3i( 0, -1, 0), 0), new NeighborVertex(new Vec3i(0, -1, -1), 3));
        putMapping(mappings, Direction.WEST, 2, new NeighborVertex(new Vec3i(0,  0,  1), 1), new NeighborVertex(new Vec3i( 0, -1, 0), 3), new NeighborVertex(new Vec3i(0, -1,  1), 0));
        putMapping(mappings, Direction.WEST, 3, new NeighborVertex(new Vec3i(0,  0,  1), 0), new NeighborVertex(new Vec3i( 0,  1, 0), 2), new NeighborVertex(new Vec3i(0,  1,  1), 1));

        return mappings;
    }

    private static void putMapping(
            NeighborVertex[][] mappings,
            Direction face,
            int srcVert,
            NeighborVertex vertOne,
            NeighborVertex vertTwo,
            NeighborVertex vertBoth
    )
    {
        mappings[getMappingIndex(face, srcVert)] = new NeighborVertex[] { vertOne, vertTwo, vertBoth };
    }

    private record NeighborVertex(Vec3i offset, int targetVert) { }
}
