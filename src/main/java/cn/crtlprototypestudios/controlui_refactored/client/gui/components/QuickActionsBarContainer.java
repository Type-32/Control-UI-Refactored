package cn.crtlprototypestudios.controlui_refactored.client.gui.components;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Sizing;

public class QuickActionsBarContainer extends FlowLayout {

    public QuickActionsBarContainer(){
        super(Sizing.fixed(20), Sizing.fill(100), Algorithm.HORIZONTAL);
//        child()
    }

    @Override
    protected void parentUpdate(float delta, int mouseX, int mouseY) {

    }
}
