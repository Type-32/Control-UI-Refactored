package cn.crtlprototypestudios.controlui_refactored.client.screens;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;

public class MainScreen extends BaseUIModelScreen<FlowLayout> {

    public MainScreen() {
        super(FlowLayout.class, new Identifier("controlui_refactored", "main_screen"));
    }
    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.childById(ButtonComponent.class, "main").onPress(buttonComponent -> {
            System.out.println("Clickety Click");
        });
    }
}
