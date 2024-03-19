package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.modals;

import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus.MenuScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPresetType;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPresetsData;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ControlUIRefactoredStorage;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.FileNameReferences;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ModalStorage;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.block.Block;

import java.util.Map;

public class NewMiningPresetModalScreen extends MenuScreen {
    public NewMiningPresetModalScreen() {
        super("modals/new_mining_preset_modal", "Create New Mining Preset Modal", false);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
        rootComponent.childById(ButtonComponent.class, "action.cancel-create-new-mining-preset").onPress(component -> {
            ScreenStackUtils.back();
        });
        rootComponent.childById(ButtonComponent.class, "action.create-new-mining-preset").onPress(component -> {
            ControlUIRefactoredStorage storage = new ControlUIRefactoredStorage();
            MiningPresetsData data = storage.loadData(MiningPresetsData.class, FileNameReferences.MINING_PRESETS_FILENAME, false);

            MiningPreset preset = new MiningPreset(
                rootComponent.childById(TextBoxComponent.class, "input.preset-name").getText(),
                rootComponent.childById(TextBoxComponent.class, "input.preset-desc").getText(),
                MiningPresetType.ArrayBased,
                ModalStorage.miningPreset.getBlocks(),
                64
            );

            data.presets.add(preset);
            storage.saveData(data, FileNameReferences.MINING_PRESETS_FILENAME, true);
            ScreenStackUtils.back();
        });
        FlowLayout blockPreviewHolder = rootComponent.childById(FlowLayout.class, "block-item-preview-holder");
        assert blockPreviewHolder != null;
        blockPreviewHolder.clearChildren();
        blockPreviewHolder.<FlowLayout>configure(component -> {
            for (Block block : ModalStorage.miningPreset.getBlocks()) {
                FlowLayout blockItemPreview = component.child(this.model.expandTemplate(FlowLayout.class, "new_mining_preset_block_item@controlui_refactored:components/new_mining_preset_block_item", Map.of(
                    "block-id", block.asItem().getDefaultStack().toString(),
                    "block-name", block.asItem().getDefaultStack().getName().toString()
                )));
                blockItemPreview.childById(ButtonComponent.class, "action.remove-new-mining-preset-block-item").onPress(button -> {
                    ModalStorage.miningPreset.getBlocks().remove(block);
                });
            }
        });
    }
}
