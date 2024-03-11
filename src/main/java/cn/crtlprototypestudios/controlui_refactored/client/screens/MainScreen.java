package cn.crtlprototypestudios.controlui_refactored.client.screens;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;

public class MainScreen extends BaseUIModelScreen<FlowLayout> {

    private final IBaritone baritone;

    public MainScreen() {
        super(FlowLayout.class, new Identifier("controlui_refactored", "main_screen"));
        baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
    }
    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.childById(ButtonComponent.class, "tab.mining").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Mining Tab");
//            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("#mine minecraft:stone");
//            rootComponent.clearChildren();

            rootComponent.childById(FlowLayout.class, "menu").clearChildren();
        });
        rootComponent.childById(ButtonComponent.class, "tab.quick-actions").onPress(buttonComponent -> {
            System.out.println("[Control UI] Switch to Quick Actions Tab");
        });
    }
}
