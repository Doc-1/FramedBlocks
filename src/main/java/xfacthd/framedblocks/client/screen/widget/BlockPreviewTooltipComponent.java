package xfacthd.framedblocks.client.screen.widget;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public final class BlockPreviewTooltipComponent implements ClientTooltipComponent
{
    private static final int SIZE = 36;
    private static final float CENTER_OFF = SIZE / 2F;
    private static final float Z_OFF = 100;
    private static final float STACK_SCALE = 48;
    private static final Quaternionf ROT_180_ZP = Axis.ZP.rotationDegrees(180);
    private static final Quaternionf ROT_22_5_XN = Axis.XN.rotationDegrees(22.5F);

    private final ItemStackRenderState renderState;

    public BlockPreviewTooltipComponent(Component component)
    {
        this.renderState = component.renderState;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics)
    {
        graphics.pose().pushPose();

        graphics.pose().translate(x + CENTER_OFF, y + CENTER_OFF, Z_OFF);
        graphics.pose().scale(STACK_SCALE, STACK_SCALE, -STACK_SCALE);
        long rotY = (System.currentTimeMillis() / 20) % 360;
        graphics.pose().mulPose(new Matrix4f()
                .rotate(ROT_180_ZP)
                .rotate(ROT_22_5_XN)
                .rotate(Axis.YP.rotationDegrees(rotY))
        );

        graphics.flush();
        Lighting.setupForEntityInInventory();
        graphics.drawSpecial(buffer -> renderState.render(
                graphics.pose(),
                buffer,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY
        ));
        Lighting.setupFor3DItems();

        graphics.pose().popPose();
    }

    @Override
    public int getWidth(Font font)
    {
        return SIZE;
    }

    @Override
    public int getHeight(Font font)
    {
        return SIZE;
    }



    public record Component(ItemStackRenderState renderState) implements TooltipComponent { }
}
