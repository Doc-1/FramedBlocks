package xfacthd.framedblocks.common.data.camo.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.camo.TriggerRegistrar;
import xfacthd.framedblocks.api.camo.block.SimpleBlockCamoContainerFactory;
import xfacthd.framedblocks.api.util.CamoMessageVerbosity;
import xfacthd.framedblocks.api.util.ConfigView;
import xfacthd.framedblocks.api.util.Utils;

public final class BlockCamoContainerFactory extends SimpleBlockCamoContainerFactory
{
    @Override
    protected boolean isValidBlock(BlockState camoState, BlockGetter level, BlockPos pos, @Nullable Player player)
    {
        Block block = camoState.getBlock();
        if (block instanceof IFramedBlock)
        {
            return false;
        }

        if (camoState.is(Utils.BLOCK_BLACKLIST))
        {
            displayValidationMessage(player, MSG_BLACKLISTED, CamoMessageVerbosity.DEFAULT);
            return false;
        }
        if (camoState.hasBlockEntity() && !ConfigView.Server.INSTANCE.allowBlockEntities() && !camoState.is(Utils.BE_WHITELIST))
        {
            displayValidationMessage(player, MSG_BLOCK_ENTITY, CamoMessageVerbosity.DEFAULT);
            return false;
        }
        if (!camoState.isSolidRender() && !camoState.is(Utils.FRAMEABLE))
        {
            displayValidationMessage(player, MSG_NON_SOLID, CamoMessageVerbosity.DETAILED);
            return false;
        }
        return true;
    }

    @Override
    public void registerTriggerItems(TriggerRegistrar registrar) { }
}
