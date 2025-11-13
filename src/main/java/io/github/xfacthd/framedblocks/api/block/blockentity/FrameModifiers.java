package io.github.xfacthd.framedblocks.api.block.blockentity;

import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.item.ItemResource;

public final class FrameModifiers
{
    public static boolean isGlowstone(ItemStack stack)
    {
        return isGlowstone(ItemResource.of(stack));
    }

    public static boolean isGlowstone(ItemResource resource)
    {
        return resource.is(Tags.Items.DUSTS_GLOWSTONE);
    }

    public static boolean isPhantomPaste(ItemStack stack)
    {
        return isPhantomPaste(ItemResource.of(stack));
    }

    public static boolean isPhantomPaste(ItemResource resource)
    {
        return resource.is(Utils.PHANTOM_PASTE);
    }

    public static boolean isReinforcement(ItemStack stack)
    {
        return isReinforcement(ItemResource.of(stack));
    }

    public static boolean isReinforcement(ItemResource resource)
    {
        return resource.is(Utils.FRAMED_REINFORCEMENT);
    }

    public static boolean isGlowPaste(ItemStack stack)
    {
        return isGlowPaste(ItemResource.of(stack));
    }

    public static boolean isGlowPaste(ItemResource resource)
    {
        return resource.is(Utils.GLOW_PASTE);
    }

    public static boolean isModifier(ItemStack stack)
    {
        return isModifier(ItemResource.of(stack));
    }

    public static boolean isModifier(ItemResource resource)
    {
        return isGlowstone(resource) || isPhantomPaste(resource) || isReinforcement(resource) || isGlowPaste(resource);
    }

    private FrameModifiers() { }
}
