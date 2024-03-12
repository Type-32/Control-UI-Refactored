package cn.crtlprototypestudios.controlui_refactored.client.gui.components;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Animation;
import io.wispforest.owo.ui.core.Easing;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;

public class QuickActionsBarContainer extends FlowLayout {

    protected final Animation<Insets> slideAnimation;

    private String currentMenu = "Menu";

    public QuickActionsBarContainer(){
        super(Sizing.fixed(24), Sizing.fill(80), Algorithm.HORIZONTAL);
        slideAnimation = this.padding().animate(150, Easing.QUADRATIC, margins().get().add(5,0,0,0));
    }

    @Override
    protected void parentUpdate(float delta, int mouseX, int mouseY) {
        if (hovered) {
            slideAnimation.forwards();
        } else {
            slideAnimation.backwards();
        }
    }

    public void setMenu(String menu){
        currentMenu = menu;
    }
    public void resetMenu(){
        setMenu("Menu");
    }
}
