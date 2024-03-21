package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.modals;

import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus.MenuScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPresetType;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.PresetsData;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ControlUIRefactoredStorage;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.FileNameReferences;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ModalStorage;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.block.Block;

import java.util.ArrayList;
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
            PresetsData data = storage.loadData(PresetsData.class, FileNameReferences.MINING_PRESETS_FILENAME, false, PresetsData::new);

            MiningPreset preset = new MiningPreset(
                rootComponent.childById(TextBoxComponent.class, "input.preset-name").getText(),
                rootComponent.childById(TextBoxComponent.class, "input.preset-desc").getText(),
                MiningPresetType.ArrayBased,
                ModalStorage.miningPreset.getBlocks(),
                64
            );

            data.miningPresets.add(preset);
            storage.saveData(data, FileNameReferences.MINING_PRESETS_FILENAME, false);
            ModalStorage.clearCache();
            ScreenStackUtils.back();
        });

        rootComponent.childById(ButtonComponent.class, "action.add-block-to-preset").onPress(component -> {
            ScreenStackUtils.to(new BlocksSelectionModalScreen());
        });
    }

    @Override
    protected void init(){
        super.init();
        injectSelectedBlockPreviewTemplate();
    }

    protected void injectSelectedBlockPreviewTemplate() {
        if (this.uiAdapter == null) return;

        FlowLayout blockPreviewHolder = this.uiAdapter.rootComponent.childById(FlowLayout.class, "block-item-preview-holder");
        assert blockPreviewHolder != null;

        blockPreviewHolder.clearChildren();

        blockPreviewHolder.<FlowLayout>configure(component -> {
            if (!ModalStorage.blocksSelection.isEmpty()) ModalStorage.miningPreset.setBlocks(ModalStorage.blocksSelection);
            else return;

            for (Block block : ModalStorage.miningPreset.getBlocks()) {

                FlowLayout blockItemPreview = component.child(this.model.expandTemplate(FlowLayout.class, "new_mining_preset_block_item@controlui_refactored:components/new_mining_preset_block_item", Map.of(
                        "block-name", block.asItem().getDefaultStack().getName().getString()
                )));

                blockItemPreview.childById(ItemComponent.class, "preview.mining-preset-block-item").stack(block.asItem().getDefaultStack());

                blockItemPreview.childById(ButtonComponent.class, "action.remove-new-mining-preset-block-item").onPress(button -> {
                    System.out.println("Remove Block: " + block.asItem().getDefaultStack().getName().getString());
                    ModalStorage.blocksSelection.remove(block);
                    injectSelectedBlockPreviewTemplate();
                });
            }
        });
    }
}
