package xfacthd.framedblocks.api.screen.overlay;

public enum OverlayDisplayMode
{
    HIDDEN,
    ICON,
    DETAILED;

    public OverlayDisplayMode constrain(OverlayDisplayMode other)
    {
        return ordinal() < other.ordinal() ? this : other;
    }
}
