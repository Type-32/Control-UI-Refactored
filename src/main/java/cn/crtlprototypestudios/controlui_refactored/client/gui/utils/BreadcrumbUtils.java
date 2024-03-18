package cn.crtlprototypestudios.controlui_refactored.client.gui.utils;

import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.MainMenuScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.MenuScreen;
import net.minecraft.client.MinecraftClient;

import java.util.Stack;

public class BreadcrumbUtils {
    private static Stack<MenuScreen> screens = new Stack<>();

    public static MenuScreen to(MenuScreen screen) {
        return render(screens.push(screen)); // Runs push() method first then returns the pushed stack result.
    }

    public static MenuScreen back() {
        return render(screens.pop()); // Runs pop() method first then returns the popped stack result.
    }

    public static MenuScreen render(MenuScreen screen){
        if (screens.size() <= 0) {
            MinecraftClient.getInstance().setScreen(new MainMenuScreen());
            return null;
        }
        MinecraftClient.getInstance().setScreen(screen);
        return screen;
    }

    public static void clear() {
        screens.clear();
    }

    public static MenuScreen getCurrent() {
        if (screens.size() <= 0) return null;
        return screens.peek();
    }
}
