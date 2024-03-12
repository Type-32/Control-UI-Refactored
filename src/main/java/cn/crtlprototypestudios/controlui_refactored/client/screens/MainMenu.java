package cn.crtlprototypestudios.controlui_refactored.client.screens;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;

public class MainMenu extends BaseUIModelScreen<FlowLayout> {

    public MainMenu() {
        super(FlowLayout.class, new Identifier("controlui_refactored", "menus/main_menu"));
    }
    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.childById(ButtonComponent.class, "menu.mining").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Mining Menu");
        });
        rootComponent.childById(ButtonComponent.class, "menu.commands").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Commands Menu");
        });
        rootComponent.childById(ButtonComponent.class, "menu.waypoints").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Waypoints Menu");
        });
        rootComponent.childById(ButtonComponent.class, "menu.settings").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Settings Menu");
        });
    }
}
