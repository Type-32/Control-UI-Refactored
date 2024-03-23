package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus;

import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;

public class MainMenuScreen extends MenuScreen {

    public MainMenuScreen(){
        super("menus/main_menu", "Menu", true);
    }

    @Override
    protected void init(){
        super.init();
        if (this.homeButton != null) this.homeButton.active(false);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
        rootComponent.childById(ButtonComponent.class, "menu.mining").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Mining Menu");
            ScreenStackUtils.to(new MiningMenuScreen());
        });
        rootComponent.childById(ButtonComponent.class, "menu.commands").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Commands Menu");
        });
        rootComponent.childById(ButtonComponent.class, "menu.construction").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Commands Menu");
            ScreenStackUtils.to(new ConstructionMenuScreen());
        });
        rootComponent.childById(ButtonComponent.class, "menu.waypoints").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Waypoints Menu");
        });
        rootComponent.childById(ButtonComponent.class, "menu.settings").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Settings Menu");
        });
    }
}
