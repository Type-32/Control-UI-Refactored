package cn.crtlprototypestudios.controlui_refactored.client.gui.components;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.block.Block;

public class NewMiningPresetModalBlockItem extends FlowLayout {
    public Block storedBlockInfo;
    public NewMiningPresetModalBlockItem() {
        super(Sizing.fill(99), Sizing.fixed(22), Algorithm.HORIZONTAL);
        storedBlockInfo = null;
    }
    public NewMiningPresetModalBlockItem(Block storedBlockInfo) {
        super(Sizing.fill(99), Sizing.fixed(22), Algorithm.HORIZONTAL);
        this.storedBlockInfo = storedBlockInfo;
    }

    public void injectTemplate(Block storedBlockInfo) {
        this.storedBlockInfo = storedBlockInfo;
//        this.
    }
}
