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

        SetMenuName();

        if(quickActionBarHolder != null){
            pauseButton = quickActionBarHolder.childById(ButtonComponent.class, "action.pause-all");
            stopButton = quickActionBarHolder.childById(ButtonComponent.class, "action.stop-all");
            homeButton = quickActionBarHolder.childById(ButtonComponent.class, "action.home-menu");
        }

        if (pauseButton != null || stopButton != null || homeButton != null) {
            pauseButton.onPress(buttonComponent -> {
                BaritoneWrapper.pauseAllActions();
                System.out.println("[Control UI] Paused All Baritone Actions");
            });
            stopButton.onPress(buttonComponent -> {
                BaritoneWrapper.stopAllActions();
                System.out.println("[Control UI] Stopped All Baritone Actions");
            });
            homeButton.onPress(buttonComponent -> {
                this.client.setScreen(new MainMenuScreen());
                System.out.println("[Control UI] Switch to Main Menu");
            });
        }
    }

    @Override
    protected void build(FlowLayout rootComponent){

    }

    static {
        UIParsing.registerFactory("quick-actions-bar-container", element -> new QuickActionsBarContainer());
    }

    public void SetMenuName(){
        if(quickActionBarHolder == null) return;
        quickActionBarHolder.<FlowLayout>configure(component -> {
            component.clearChildren();
            component.child(this.model.expandTemplate(QuickActionsBarContainer.class, "quick-actions-bar@controlui_refactored:components/quick_actions_bar", Map.of(
                    "current-menu", menuName
            )));
        });
    }
}
