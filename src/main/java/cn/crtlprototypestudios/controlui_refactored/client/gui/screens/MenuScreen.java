package cn.crtlprototypestudios.controlui_refactored.client.gui.screens;

import cn.crtlprototypestudios.controlui_refactored.client.BaritoneWrapper;
import cn.crtlprototypestudios.controlui_refactored.client.gui.components.QuickActionsBarContainer;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.parsing.UIParsing;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MenuScreen extends BaseUIModelScreen<FlowLayout> {

    private String menuName = "Menu";
    @Nullable
    protected FlowLayout quickActionBarHolder = null;
    @Nullable
    protected ButtonComponent pauseButton, stopButton, homeButton;

    public MenuScreen(String path, String name) {
        super(FlowLayout.class, new Identifier("controlui_refactored", path));
        menuName = name;
    }

    public MenuScreen(){
        this("menus/main_menu", "Menu");
    }

    @Override
    protected void init(){
        super.init();

        if (this.uiAdapter != null) {
            quickActionBarHolder = this.uiAdapter.rootComponent.childById(FlowLayout.class, "bar-holder");
        }
        if(quickActionBarHolder != null){
            quickActionBarHolder.<FlowLayout>configure(component -> {
                component.clearChildren();
                component.child(this.model.expandTemplate(QuickActionsBarContainer.class, "quick-actions-bar@controlui_refactored:menus/main_menu", Map.of(
                        "current-menu", menuName
                )));
            });

            pauseButton = this.uiAdapter.rootComponent.childById(ButtonComponent.class, "action.pause-all");
            stopButton = this.uiAdapter.rootComponent.childById(ButtonComponent.class, "action.stop-all");
            homeButton = this.uiAdapter.rootComponent.childById(ButtonComponent.class, "action.home-menu");
        }
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

        if(pauseButton != null || stopButton != null || homeButton != null) {
            pauseButton.onPress(buttonComponent -> {
                BaritoneWrapper.pauseAllActions();
            });
            stopButton.onPress(buttonComponent -> {
                BaritoneWrapper.stopAllActions();
            });
            homeButton.onPress(buttonComponent -> {
                this.client.setScreen(new MenuScreen());
            });
        }
    }

    static {
        UIParsing.registerFactory("quick-actions-bar-container", element -> new QuickActionsBarContainer());
    }

    public void SetMenuName(){
        if(quickActionBarHolder == null) return;
        quickActionBarHolder.<FlowLayout>configure(component -> {
            component.clearChildren();
            component.child(this.model.expandTemplate(QuickActionsBarContainer.class, "quick-actions-bar@controlui_refactored:menus/main_menu", Map.of(
                    "current-menu", menuName
            )));
        });
    }
}
