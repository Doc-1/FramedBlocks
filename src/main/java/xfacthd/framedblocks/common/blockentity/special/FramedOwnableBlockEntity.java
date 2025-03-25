package xfacthd.framedblocks.common.blockentity.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.common.FBContent;

import java.util.UUID;

public class FramedOwnableBlockEntity extends FramedBlockEntity
{
    @Nullable
    private UUID owner;

    protected FramedOwnableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public FramedOwnableBlockEntity(BlockPos pos, BlockState state)
    {
        this(FBContent.BE_TYPE_FRAMED_OWNABLE_BLOCK.value(), pos, state);
    }

    public void setOwner(UUID owner, boolean forceSync)
    {
        this.owner = owner;
        setChangedWithoutSignalUpdate();

        if (forceSync)
        {
            level().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Nullable
    public UUID getOwner()
    {
        return owner;
    }

    @Override
    protected void writeToDataPacket(CompoundTag nbt, HolderLookup.Provider lookupProvider)
    {
        super.writeToDataPacket(nbt, lookupProvider);
        nbt.storeNullable("owner", UUIDUtil.CODEC, owner);
    }

    @Override
    protected boolean readFromDataPacket(CompoundTag nbt, HolderLookup.Provider lookupProvider)
    {
        owner = nbt.read("owner", UUIDUtil.CODEC).orElse(null);
        return super.readFromDataPacket(nbt, lookupProvider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider)
    {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.storeNullable("owner", UUIDUtil.CODEC, owner);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt, HolderLookup.Provider provider)
    {
        super.handleUpdateTag(nbt, provider);
        owner = nbt.read("owner", UUIDUtil.CODEC).orElse(null);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.saveAdditional(tag, provider);
        tag.storeNullable("owner", UUIDUtil.CODEC, owner);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.loadAdditional(tag, provider);
        owner = tag.read("owner", UUIDUtil.CODEC).orElse(null);
    }
}
