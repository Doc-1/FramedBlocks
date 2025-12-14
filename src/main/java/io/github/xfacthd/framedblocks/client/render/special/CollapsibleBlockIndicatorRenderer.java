package io.github.xfacthd.framedblocks.client.render.special;

import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.Quaternions;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.client.render.util.FramedRenderTypes;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedCollapsibleBlockEntity;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.NullableDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;

public final class CollapsibleBlockIndicatorRenderer
{
    private static final float[] VERTEX_NO_OFFSET = new float[] { 1F, 1F, 1F, 1F };
    private static final int LINE_COLOR = ARGB.color(153, 255, 0, 0);

    public static void onRenderBlockHighlight(ExtractBlockOutlineRenderStateEvent event)
    {
        //noinspection ConstantConditions
        ItemStack heldItem = Minecraft.getInstance().player.getMainHandItem();
        if (heldItem.getItem() != FBContent.ITEM_FRAMED_HAMMER.value())
        {
            return;
        }

        BlockHitResult hit = event.getHitResult();
        Level level = Minecraft.getInstance().level;
        //noinspection ConstantConditions
        BlockState state = level.getBlockState(hit.getBlockPos());
        if (state.getBlock() != FBContent.BLOCK_FRAMED_COLLAPSIBLE_BLOCK.value())
        {
            return;
        }

        NullableDirection face = state.getValue(PropertyHolder.NULLABLE_FACE);
        Direction faceDir = hit.getDirection();
        if (face != NullableDirection.NONE && face.toDirection() != faceDir)
        {
            return;
        }

        Vec3 offset = Vec3.atLowerCornerOf(hit.getBlockPos()).subtract(event.getCamera().position());
        Vec3 hitLocation = hit.getLocation();
        float[] vY = getVertexHeights(level, hit.getBlockPos(), face);

        event.addCustomRenderer((renderState, buffer, poseStack, translucentPass, levelRenderState) ->
        {
            if (translucentPass)
            {
                poseStack.pushPose();
                poseStack.translate(offset.x + .5, offset.y + .5, offset.z + .5);
                if (faceDir == Direction.DOWN)
                {
                    poseStack.mulPose(Quaternions.XP_180);
                }
                else if (faceDir != Direction.UP)
                {
                    poseStack.mulPose(OutlineRenderer.YN_DIR[faceDir.get2DDataValue()]);
                    poseStack.mulPose(Quaternions.XP_90);
                }
                poseStack.translate(-.5, -.5, -.5);

                OutlineRenderer.LineDrawer drawer = new BlockOutlineRenderer.DefaultLineDrawer(
                        poseStack.last(), buffer.getBuffer(FramedRenderTypes.LINES_NO_DEPTH), LINE_COLOR
                );
                drawSectionOverlay(drawer, vY);
                drawCornerMarkers(drawer, faceDir, hitLocation, vY);

                poseStack.popPose();
            }
            return false;
        });
    }

    private static float[] getVertexHeights(Level level, BlockPos pos, NullableDirection face)
    {
        if (face == NullableDirection.NONE || !(level.getBlockEntity(pos) instanceof FramedCollapsibleBlockEntity be))
        {
            return VERTEX_NO_OFFSET;
        }
        return new float[] {
                1F - (be.getVertexOffset(0) / 16F),
                1F - (be.getVertexOffset(1) / 16F),
                1F - (be.getVertexOffset(2) / 16F),
                1F - (be.getVertexOffset(3) / 16F)
        };
    }

    private static void drawSectionOverlay(OutlineRenderer.LineDrawer drawer, float[] vY)
    {
        float cenx = Mth.lerp(.5F, vY[0], vY[1]); // center edge negative X
        float cepx = Mth.lerp(.5F, vY[3], vY[2]); // center edge positive X
        float cenz = Mth.lerp(.5F, vY[0], vY[3]); // center edge negative Z
        float cepz = Mth.lerp(.5F, vY[1], vY[2]); // center edge positive Z

        float cinx = Mth.lerp(.25F, cenx, cepx); // center inset negative X
        float cipx = Mth.lerp(.75F, cenx, cepx); // center inset positive X
        float cinz = Mth.lerp(.25F, cenz, cepz); // center inset negative Z
        float cipz = Mth.lerp(.75F, cenz, cepz); // center inset positive Z

        drawer.drawLine( .5F, cenz,   0F,  .5F, cinz, .25F);
        drawer.drawLine( .5F, cipz, .75F,  .5F, cepz,   1F);
        drawer.drawLine(  0F, cenx,  .5F, .25F, cinx,  .5F);
        drawer.drawLine(.75F, cipx,  .5F,   1F, cepx,  .5F);

        drawer.drawLine(.5F, cinz, .25F, .25F, cinx, .5F);
        drawer.drawLine(.5F, cinz, .25F, .75F, cipx, .5F);
        drawer.drawLine(.5F, cipz, .75F, .25F, cinx, .5F);
        drawer.drawLine(.5F, cipz, .75F, .75F, cipx, .5F);
    }

    private static void drawCornerMarkers(OutlineRenderer.LineDrawer drawer, Direction faceDir, Vec3 hitLocation, float[] vY)
    {
        int vert = FramedCollapsibleBlockEntity.vertexFromHit(faceDir, Utils.fraction(hitLocation));
        if (vert == 0 || vert == 4)
        {
            drawCubeFrame(drawer,  0.25F/16F,  0.25F/16F, vY[0]);
        }
        if (vert == 1 || vert == 4)
        {
            drawCubeFrame(drawer,  0.25F/16F, 15.75F/16F, vY[1]);
        }
        if (vert == 2 || vert == 4)
        {
            drawCubeFrame(drawer, 15.75F/16F, 15.75F/16F, vY[2]);
        }
        if (vert == 3 || vert == 4)
        {
            drawCubeFrame(drawer, 15.75F/16F,  0.25F/16F, vY[3]);
        }
    }

    private static void drawCubeFrame(OutlineRenderer.LineDrawer drawer, float x, float z, float vY)
    {
        float minX = x - .5F/16F;
        float maxX = x + .5F/16F;
        float minZ = z - .5F/16F;
        float maxZ = z + .5F/16F;

        float minY = vY - .75F/16F;
        float maxY = vY + .25F/16F;

        // Bottom
        drawer.drawLine(minX, minY, minZ, minX, minY, maxZ);
        drawer.drawLine(minX, minY, minZ, maxX, minY, minZ);
        drawer.drawLine(maxX, minY, minZ, maxX, minY, maxZ);
        drawer.drawLine(minX, minY, maxZ, maxX, minY, maxZ);

        // Top
        drawer.drawLine(minX, maxY, minZ, minX, maxY, maxZ);
        drawer.drawLine(minX, maxY, minZ, maxX, maxY, minZ);
        drawer.drawLine(maxX, maxY, minZ, maxX, maxY, maxZ);
        drawer.drawLine(minX, maxY, maxZ, maxX, maxY, maxZ);

        // Vertical
        drawer.drawLine(minX, minY, minZ, minX, maxY, minZ);
        drawer.drawLine(minX, minY, maxZ, minX, maxY, maxZ);
        drawer.drawLine(maxX, minY, minZ, maxX, maxY, minZ);
        drawer.drawLine(maxX, minY, maxZ, maxX, maxY, maxZ);
    }

    private CollapsibleBlockIndicatorRenderer() { }
}
