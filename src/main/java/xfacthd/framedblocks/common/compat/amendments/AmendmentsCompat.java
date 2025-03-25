package xfacthd.framedblocks.common.compat.amendments;

import net.mehvahdjukaar.amendments.Amendments;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.util.Utils;

public final class AmendmentsCompat
{
    private static final String MOD_ID = "amendments";
    private static boolean loaded = false;

    public static void init()
    {
        loaded = ModList.get().isLoaded(MOD_ID);
    }

    public static boolean isLoaded()
    {
        return loaded;
    }

    public static boolean canSurviveHanging(LevelReader level, BlockPos pos)
    {
        if (loaded)
        {
            return GuardedAccess.canSurviveHanging(level, pos);
        }
        return false;
    }



    private static final class GuardedAccess
    {
        private static boolean failedPreviously = false;

        public static boolean canSurviveHanging(LevelReader level, BlockPos pos)
        {
            if (failedPreviously)
            {
                return true;
            }

            try
            {
                return Amendments.isSupportingCeiling(pos, level);
            }
            catch (Throwable e)
            {
                if (!failedPreviously)
                {
                    failedPreviously = true;
                    FramedBlocks.LOGGER.error("[AmendmentsCompat] Encountered an error while checking hanging pot surviving", e);
                }
                return true;
            }
        }
    }

    public static final class Client
    {
        private static final ResourceLocation HANGING_MODEL_LOCATION = Utils.rl(MOD_ID, "block/hanging_flower_pot_rope");
        public static final StandaloneModelKey<BlockModelPart> HANGING_MODEL_KEY = new StandaloneModelKey<>(HANGING_MODEL_LOCATION);



        private Client() { }
    }



    private AmendmentsCompat() { }
}
