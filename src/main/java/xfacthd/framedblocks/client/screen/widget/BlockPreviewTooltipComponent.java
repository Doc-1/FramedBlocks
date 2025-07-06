package xfacthd.framedblocks.client.screen.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import xfacthd.framedblocks.client.screen.pip.SpinningItemPictureInPictureRenderer;

public final class BlockPreviewTooltipComponent implements ClientTooltipComponent
{
    private static final int SIZE = 36;
    private static final float STACK_SCALE = 48;

    private final TrackingItemStackRenderState renderState;

    public BlockPreviewTooltipComponent(Component component)
    {
        this.renderState = component.renderState;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics)
    {
        graphics.submitPictureInPictureRenderState(new SpinningItemPictureInPictureRenderer.RenderState(
                renderState,
                (int) (System.currentTimeMillis() / 20 % 360),
                x, y, x + SIZE, y + SIZE, STACK_SCALE, graphics.peekScissorStack()
        ));
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



    public record Component(TrackingItemStackRenderState renderState) implements TooltipComponent { }
}
