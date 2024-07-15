package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.modals;

import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus.MenuScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.MiningPreset;
import cn.crtlprototypestudios.controlui_refactored.client.storage.types.PresetsData;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.ControlUIRefactoredStorage;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.FileNameReferences;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.MiningModalStorage;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.block.Block;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Map;

public class NewMiningPresetModalScreen extends MenuScreen {
    private boolean isEditing = false;
    private int index = 0;
    public NewMiningPresetModalScreen() {
        super("modals/new_mining_preset_modal", "Create New Mining Preset Modal", false);
    }
    public NewMiningPresetModalScreen(MiningPreset preset) {
        this();
        MiningModalStorage.miningPreset = preset;
//        BlocksSelectionModalStorage.blocksSelection = MiningModalStorage.miningPreset.getBlocks();
        isEditing = true;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
        ButtonComponent saveButton = rootComponent.childById(ButtonComponent.class, "action.create-new-mining-preset");

        if(isEditing){
            rootComponent.childById(TextBoxComponent.class, "input.preset-name").setText(MiningModalStorage.miningPreset.getPresetName());
            rootComponent.childById(TextBoxComponent.class, "input.preset-desc").setText(MiningModalStorage.miningPreset.getPresetDescription());

            ControlUIRefactoredStorage storage = new ControlUIRefactoredStorage();
            PresetsData data = storage.loadData(PresetsData.class, FileNameReferences.MINING_PRESETS_FILENAME, false, PresetsData::new);

            index = data.miningPresets.indexOf(MiningModalStorage.miningPreset);

            saveButton.setMessage(Text.literal("Edit"));
        }

        rootComponent.childById(ButtonComponent.class, "action.cancel-create-new-mining-preset").onPress(component -> {
            ScreenStackUtils.back();
        });


        saveButton.onPress(component -> {
            ControlUIRefactoredStorage storage = new ControlUIRefactoredStorage();
            PresetsData data = storage.loadData(PresetsData.class, FileNameReferences.MINING_PRESETS_FILENAME, false, PresetsData::new);
            if(data == null)
                data = new PresetsData();

            MiningModalStorage.miningPreset.setPresetName(rootComponent.childById(TextBoxComponent.class, "input.preset-name").getText());
            MiningModalStorage.miningPreset.setPresetDescription(rootComponent.childById(TextBoxComponent.class, "input.preset-desc").getText());
            MiningModalStorage.miningPreset.setPresetType(MiningPreset.Types.ArrayBased);
            MiningModalStorage.miningPreset.setAmount(64);

            if(isEditing) {
                data.miningPresets.set(index, MiningModalStorage.miningPreset);
            }else{
                data.miningPresets.add(MiningModalStorage.miningPreset);
            }

            storage.saveData(data, FileNameReferences.MINING_PRESETS_FILENAME, false);
            MiningModalStorage.clearCache();
            ScreenStackUtils.back();
        });

        BlocksSelectionModalScreen modal = new BlocksSelectionModalScreen(true, MiningModalStorage.miningPreset.getBlocks(),
            result -> {
                if(result != null)
                    MiningModalStorage.miningPreset.getBlocks().addAll(result);
            }
        );
        rootComponent.childById(ButtonComponent.class, "action.add-block-to-preset").onPress(component -> {
            ScreenStackUtils.to(modal);
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
            if(MiningModalStorage.miningPreset == null)
                MiningModalStorage.miningPreset = new MiningPreset("", "", MiningPreset.Types.ArrayBased, new ArrayList<>(), 64);

//            if (!MiningModalStorage.blocksSelection.isEmpty()) MiningModalStorage.miningPreset.setBlocks(MiningModalStorage.blocksSelection);
//            else return;

            for (Block block : MiningModalStorage.miningPreset.getBlocks()) {

                FlowLayout blockItemPreview = component.child(this.model.expandTemplate(FlowLayout.class, "new_mining_preset_block_item@controlui_refactored:components/new_mining_preset_block_item", Map.of(
                        "block-name", block.asItem().getDefaultStack().getName().getString()
                )));

                blockItemPreview.childById(ItemComponent.class, "preview.mining-preset-block-item").stack(block.asItem().getDefaultStack());

                blockItemPreview.childById(ButtonComponent.class, "action.remove-new-mining-preset-block-item").onPress(button -> {
                    System.out.println("Remove Block: " + block.asItem().getDefaultStack().getName().getString());
                    MiningModalStorage.miningPreset.getBlocks().remove(block);
                    injectSelectedBlockPreviewTemplate();
                });
            }
        });
    }
}
