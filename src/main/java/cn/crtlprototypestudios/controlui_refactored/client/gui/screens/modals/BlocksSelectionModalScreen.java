package cn.crtlprototypestudios.controlui_refactored.client.gui.screens.modals;

import cn.crtlprototypestudios.controlui_refactored.client.gui.screens.menus.MenuScreen;
import cn.crtlprototypestudios.controlui_refactored.client.gui.utils.ScreenStackUtils;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.BlocksSelectionModalStorage;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.util.EventSource;
import net.minecraft.block.Block;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class BlocksSelectionModalScreen extends MenuScreen {
    private Consumer<ArrayList<Block>> confirm;
    private boolean selectMultiple;
    private ArrayList<Block> selectedBlocks;
    private String modalTitle = "";

    public BlocksSelectionModalScreen(boolean selectMultiple, ArrayList<Block> initialBlocks) {
        super("modals/blocks_selection_modal", "Blocks Selection", false);
        this.selectMultiple = selectMultiple;
        if(initialBlocks != null)
            selectedBlocks = new ArrayList<Block>(initialBlocks);
        else
            selectedBlocks = new ArrayList<>();
    }
    public BlocksSelectionModalScreen(boolean selectMultiple, ArrayList<Block> initialBlocks, Consumer<ArrayList<Block>> confirm) {
        this(selectMultiple, initialBlocks);
        this.confirm = confirm;
    }
    public BlocksSelectionModalScreen(boolean selectMultiple, ArrayList<Block> initialBlocks, Consumer<ArrayList<Block>> confirm, String modalTitle) {
        this(selectMultiple, initialBlocks);
        this.confirm = confirm;
        this.modalTitle = modalTitle;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
        rootComponent.childById(ButtonComponent.class, "action.cancel-block-selection").onPress(component -> {
            ScreenStackUtils.back();
            confirm.accept(null);
        });

        ButtonComponent saveButton = rootComponent.childById(ButtonComponent.class, "action.save-block-selection");
        saveButton.onPress(component -> {
            ScreenStackUtils.back();
            confirm.accept(selectedBlocks);
        });
        if(!selectMultiple){
            saveButton.active(false);
            rootComponent.removeChild(saveButton);
        }

        rootComponent.childById(LabelComponent.class, "preview.modal-title").text(Text.of(modalTitle));
    }

    @Override
    protected void init(){
        super.init();
        injectBlockListPreviewTemplate(BlocksSelectionModalStorage.cachedBlocksRegistry);
        EventSource<TextBoxComponent.OnChanged> onSearchTextChanged = this.uiAdapter.rootComponent.childById(TextBoxComponent.class, "input.block-search-bar").onChanged();
        onSearchTextChanged.subscribe(component -> {
            // TODO: Optimize this function, potential stack overflow
            ArrayList<Block> temp = new ArrayList<>();
            for (Block block : BlocksSelectionModalStorage.cachedBlocksRegistry.stream().filter(block -> block.asItem().getDefaultStack().getName().getString().toLowerCase().contains(component.toLowerCase())).toList())
                temp.add(block);

            injectBlockListPreviewTemplate(temp);
        });
    }

    protected void injectBlockListPreviewTemplate(ArrayList<Block> registry) {
        if (this.uiAdapter == null) return;

        FlowLayout blockPreviewHolder = this.uiAdapter.rootComponent.childById(FlowLayout.class, "block-item-list-holder");
        assert blockPreviewHolder != null;

        blockPreviewHolder.clearChildren();

        blockPreviewHolder.<FlowLayout>configure(component -> {
            BlocksSelectionModalStorage.refreshRegistry();
            for (Block block : registry) {
//                System.out.println(block.asItem().getDefaultStack().toString());

                FlowLayout blockItemPreview = component.child(this.model.expandTemplate(FlowLayout.class, String.format("%s@controlui_refactored:components/blocks_selection_list_item", selectMultiple ? "block-toggle-list-item" : "block-item-list-item"), Map.of(
                        "block-name", block.asItem().getDefaultStack().getName().getString()
                )));
                blockItemPreview.childById(ItemComponent.class, "icon.preview-block-item").stack(block.asItem().getDefaultStack());

                if(selectMultiple){
                    CheckboxComponent check = blockItemPreview.childById(CheckboxComponent.class, "action.toggle-block-item");
                    if (selectedBlocks.contains(block)) {
                        check.checked(true);
                        check.active = false;
                    }
                    check.onChanged(checkbox -> {
                        if(checkbox){
                            selectedBlocks.add(block);
                        }else{
                            selectedBlocks.remove(block);
                        }
                    });
                }else{
                    ButtonComponent selectButton = blockItemPreview.childById(ButtonComponent.class, "action.select-block-item");
                    if (selectedBlocks.contains(block)) {
                        selectButton.active(false);
                        selectButton.tooltip(Text.of("This block is already selected."));
                    }
                    selectButton.onPress(button -> {
                        ScreenStackUtils.back();
                        selectedBlocks.add(block);
                        confirm.accept(selectedBlocks);
                    });
                }
            }
        });
    }
}
