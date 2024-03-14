package cn.crtlprototypestudios.controlui_refactored.client.gui.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;

public class QuickActionsBarContainer extends FlowLayout {

    protected final Animation<Positioning> slideAnimation;

    public QuickActionsBarContainer(){
        super(Sizing.fixed(24), Sizing.fill(80), Algorithm.HORIZONTAL);
        slideAnimation = positioning().animate(150, Easing.QUADRATIC, positioning().get().withY(20));
//        child()
    }

    @Override
    protected void parentUpdate(float delta, int mouseX, int mouseY) {
        if (hovered) {
            slideAnimation.forwards();
        } else {
            slideAnimation.backwards();
        }
    }
}
