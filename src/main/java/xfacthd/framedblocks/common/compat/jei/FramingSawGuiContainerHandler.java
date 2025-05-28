package xfacthd.framedblocks.common.compat.jei;

import mezz.jei.api.gui.builder.IClickableIngredientFactory;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.world.item.ItemStack;
import xfacthd.framedblocks.client.screen.FramingSawScreen;

import java.util.Optional;

public sealed class FramingSawGuiContainerHandler<T extends FramingSawScreen> implements IGuiContainerHandler<T> permits FramingSawWithEncoderGuiContainerHandler
{
    @Override
    public Optional<IClickableIngredient<ItemStack>> getClickableIngredientUnderMouse(
            IClickableIngredientFactory factory, T screen, double mouseX, double mouseY
    )
    {
        FramingSawScreen.PointedRecipe recipe = screen.getRecipeAt(mouseX, mouseY);
        if (recipe != null)
        {
            return factory.createBuilder(recipe.recipe().getResult()).buildWithArea(recipe.area());
        }
        return Optional.empty();
    }
}
