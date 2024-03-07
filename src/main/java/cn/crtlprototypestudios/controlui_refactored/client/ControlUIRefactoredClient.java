package cn.crtlprototypestudios.controlui_refactored.client;

import cn.crtlprototypestudios.controlui_refactored.client.hud.hint.ClickHint;
import cn.crtlprototypestudios.controlui_refactored.client.screens.MainScreen;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.hud.Hud;
import io.wispforest.owo.ui.hud.HudContainer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ControlUIRefactoredClient implements ClientModInitializer {

    private static final KeyBinding TOGGLEUI = new KeyBinding("key.control_ui.toggle", GLFW.GLFW_KEY_B, "key.category.control_ui");
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(TOGGLEUI);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLEUI.wasPressed()){
                client.setScreen(new MainScreen());
            }
        });
        System.out.println("Control UI Mod Client-Side Initialized!");
    }
}
