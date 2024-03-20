package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus;

import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.modals.NewMiningPresetModalScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPresets;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ControlUIRefactoredStorage;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.StackLayout;
import net.minecraft.block.Block;

import java.util.Map;

public class MiningMenuScreen extends MenuScreen{
    private ControlUIRefactoredStorage storage = new ControlUIRefactoredStorage();
    public MiningMenuScreen(){
        super("menus/mining_menu", "Mining", true);
        storage = new ControlUIRefactoredStorage();
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
        LoadAvailablePresets();
        rootComponent.childById(ButtonComponent.class, "actions.new-mining-preset").onPress(component -> {
            ScreenStackUtils.to(new NewMiningPresetModalScreen());
        });
    }

    public void LoadAvailablePresets(){
        MiningPresets data = storage.loadData(MiningPresets.class, "mining_presets", false);

        StackLayout stackLayout = this.uiAdapter.rootComponent.childById(StackLayout.class, "available-presets-holder");
        assert stackLayout != null;
        stackLayout.<StackLayout>configure(component -> {
            component.clearChildren();
            for (MiningPreset preset : data.presets) {
                FlowLayout presetItem = this.model.expandTemplate(FlowLayout.class, "quick-actions-bar@controlui_refactored:components/active_mining_preset_item", Map.of(
                        "preset-name", preset.getPresetName(),
                        "preset-description", preset.getPresetDescription()
                ));
                for (Block block : preset.getBlocks()) {
                    presetItem.childById(FlowLayout.class, "preset-blocks-preview-list").child(
                        Components.item(block.asItem().getDefaultStack())
                    );
                }
                component.child(presetItem);
            }
        });
    }
}
