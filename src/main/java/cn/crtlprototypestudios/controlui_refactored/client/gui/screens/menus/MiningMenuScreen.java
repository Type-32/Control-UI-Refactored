package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus;

import cn.crtlprototypestudios.controlui_refactored.client.BaritoneWrapper;
import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.modals.NewMiningPresetModalScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.PresetsData;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ControlUIRefactoredStorage;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.FileNameReferences;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.container.FlowLayout;

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
        rootComponent.childById(ButtonComponent.class, "actions.new-mining-preset").onPress(component -> {
            ScreenStackUtils.to(new NewMiningPresetModalScreen());
        });
    }

    @Override
    protected void init(){
        super.init();
        loadAvailablePresets();
    }

    public void loadAvailablePresets(){
        PresetsData data = storage.loadData(PresetsData.class, FileNameReferences.MINING_PRESETS_FILENAME, false, PresetsData::new);

        FlowLayout presetsHolder = this.uiAdapter.rootComponent.childById(FlowLayout.class, "available-presets-holder");
        assert presetsHolder != null;
        presetsHolder.<FlowLayout>configure(component -> {
            component.clearChildren();
            if(data == null) return;
            for (MiningPreset preset : data.miningPresets) {
                FlowLayout presetItem = this.model.expandTemplate(FlowLayout.class, "mining-menu-preset-item@controlui_refactored:components/mining_menu_preset_item", Map.of(
                        "preset-name", preset.getPresetName(),
                        "preset-desc", preset.getPresetDescription()
                ));
                System.out.println(preset.getPresetName());
                presetItem.childById(ItemComponent.class, "preview.preset-first-item-preview").stack(preset.getBlocks().get(0).asItem().getDefaultStack());
                component.child(presetItem);
                presetItem.childById(ButtonComponent.class, "action.edit-preset").onPress(component1 -> {
                    ScreenStackUtils.to(new NewMiningPresetModalScreen(preset));
                });
                presetItem.childById(ButtonComponent.class, "action.delete-preset").onPress(component1 -> {
                    data.miningPresets.remove(preset);
                    storage.saveData(data, FileNameReferences.MINING_PRESETS_FILENAME, false);
                    loadAvailablePresets();
                });
                presetItem.childById(ButtonComponent.class, "action.activate-preset").onPress(component1 -> {
                    System.out.println(preset.toCommand());
                    BaritoneWrapper.getInstance().getCommandManager().execute(preset.toCommand());
                    ScreenStackUtils.exit();
                });
            }
        });
    }
}
