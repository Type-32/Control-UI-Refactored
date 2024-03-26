package cn.crtlprototypestudios.controlui_refactored.client.storage.types;

import baritone.api.schematic.CompositeSchematic;
import net.minecraft.util.math.Vec3i;

public class SelectionClipboard {
    private CompositeSchematic schematic;
    private Vec3i offset;
    public SelectionClipboard(CompositeSchematic schematic, Vec3i offset){
        this.schematic = schematic;
        this.offset = offset;
    }

    public CompositeSchematic getSchematic() {
        return schematic;
    }

    public void setSchematic(CompositeSchematic schematic) {
        this.schematic = schematic;
    }

    public Vec3i getOffset() {
        return offset;
    }

    public void setOffset(Vec3i offset) {
        this.offset = offset;
    }
}
