package io.github.xfacthd.framedblocks.api.camo;

import io.github.xfacthd.framedblocks.api.util.ConfigView;
import net.minecraft.world.item.ItemStack;

public interface CamoCraftingHandler<T extends CamoContainer<?, T>>
{
    /**
     * {@return whether a camo can be created from the stack held by the provided item access in a
     * crafting recipe without level and player context}
     *
     * @param stack The item access holding the stack to create the camo from
     */
    boolean canApply(ItemStack stack);

    /**
     * Compute the camo container to apply to the item in a crafting recipe from the stack held
     * by the provided item access
     *
     * @param stack The item access holding the stack to create the camo from
     * @return The camo container to apply to the resulting stack
     * @implNote This method must not mutate the given stack
     */
    T apply(ItemStack stack);

    /**
     * Compute the crafting remainder after the stack held by the provided item access is used to apply a camo to
     * a framed block in a crafting recipe.
     *
     * @param stack   The {@link ItemStack} the camo was created from. Must not be modified
     * @param consume Whether the camo should be consumed (see {@link ConfigView.Server#shouldConsumeCamoItem()})
     * @return The stack that should remain in the crafting grid
     */
    ItemStack getRemainder(ItemStack stack, boolean consume);
}
