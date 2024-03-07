package cn.crtlprototypestudios.controlui_refactored.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ControlUIRefactoredClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Control UI Mod Client-Side Initialized!");
    }
}
