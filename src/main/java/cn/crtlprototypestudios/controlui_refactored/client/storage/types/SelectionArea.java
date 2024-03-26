package cn.crtlprototypestudios.controlui_refactored.client.storage.types;

import baritone.api.cache.ICachedRegion;
import baritone.api.schematic.*;
import baritone.api.schematic.mask.shape.CylinderMask;
import baritone.api.schematic.mask.shape.SphereMask;
import baritone.api.selection.ISelection;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.BlockOptionalMeta;
import baritone.api.utils.BlockOptionalMetaLookup;
import baritone.api.utils.IPlayerContext;
import cn.crtlprototypestudios.controlui_refactored.client.BaritoneWrapper;
import cn.crtlprototypestudios.controlui_refactored.client.storage.utils.SelectionStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;

public class SelectionArea {
    private BlockPos pos1, pos2;
    private ChunkPos chunkPos;
    private ICachedRegion cachedRegion;

    public SelectionArea(BlockPos pos1, BlockPos pos2){
        this.pos1 = pos1;
        this.pos2 = pos2;
        chunkPos = new ChunkPos(pos1);
        cachedRegion = BaritoneWrapper.getInstance().getWorldProvider().getCurrentWorld().getCachedWorld().getRegion(pos1.getX() >> 9, pos1.getZ() >> 9);
        MinecraftClient.getInstance().world.getChunkManager().getChunk(pos1.getX() >> 4, pos1.getZ() >> 4);
    }

    public SelectionArea(ISelection selection){
        this.pos1 = selection.pos1();
        this.pos2 = selection.pos2();
    }


    public BlockPos getPos1() {
        return pos1;
    }

    public void setPos1(BlockPos pos1) {
        this.pos1 = pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public void setPos2(BlockPos pos2) {
        this.pos2 = pos2;
    }

    public Block getBlock1(){
        assert MinecraftClient.getInstance().world != null;
        return MinecraftClient.getInstance().world.getBlockState(pos1).getBlock();
    }

    public Block getBlock2(){
        assert MinecraftClient.getInstance().world != null;
        return MinecraftClient.getInstance().world.getBlockState(pos2).getBlock();
    }

    public int getVolume(){
        return (pos2.getX() - pos1.getX() + 1) * (pos2.getY() - pos1.getY() + 1) * (pos2.getZ() - pos1.getZ() + 1);
    }

    public int getArea(){
        return (pos2.getX() - pos1.getX() + 1) * (pos2.getZ() - pos1.getZ() + 1);
    }

    public int getHeight(){
        return pos2.getY() - pos1.getY() + 1;
    }


    /**
     * Fills the selection area based on the specified fill type and parameters.
     *
     * @param fillType The type of fill action to perform (e.g., Replace, Cylinder, Sphere).
     * @param replacing List of blocks to replace in the selection.
     * @param replaceWith The block to replace the specified blocks with.
     * @param shape The facing axis for cylindrical fills (nullable if fill type is not Cylindrical).
     * @return The updated selection area after the fill action.
     */
    public ISelection basicFillAction(SelectionFillType fillType, List<Block> replacing, Block replaceWith, Direction.Axis shape){
        // Code Disclaimer: I do not own the major parts of this code, as some are copied directly from the Baritone Mod by Leijurv; I am not responsible for any issues that arise from using these parts of code.

        ISelection sel = getFromBaritone();
        if(sel == null){
            sel = addToBaritone();
        }

        System.out.println(sel.toString());

        BlockOptionalMeta replaceWithMeta = new BlockOptionalMeta(replaceWith); // Not Nullable; This is used to replace every block in the selection.
        final BlockOptionalMetaLookup replaces; // Is Nullable; If this is null, it won't look to replace any block in the selection.
        final Direction.Axis alignment; // Is Nullable When Fill Type is not Cylindrical; This determines the filled cylinder's facing axis.

        if (fillType == SelectionFillType.Replace) { // If the Fill Type or mode is for replacing desired blocks in the selection, this scope looks for the desired blocks.
            List<BlockOptionalMeta> replacesList = new ArrayList<>();
            List<Block> temp = new ArrayList<>(replacing == null ? new ArrayList<>() : replacing);
            replacesList.add(replaceWithMeta);
            while (!temp.isEmpty())
                replacesList.add(new BlockOptionalMeta(temp.remove(0)));

            replaces = new BlockOptionalMetaLookup(replacesList.toArray(new BlockOptionalMeta[0]));
            alignment = null;
        } else if (fillType == SelectionFillType.Cylinder || fillType == SelectionFillType.HCylinder) { // If the Fill Type or mode is for filling the selection with blocks in a Cylindrical shape, this scope checks for the axis that the cylinder will be faced off of.
            alignment = shape != null ? shape : Direction.Axis.Y;
            replaces = null;
        } else { // Any Other fill types doesn't need preprocessing.
            replaces = null;
            alignment = null;
        }

        BetterBlockPos origin = sel.min(); // The Origin is the Lowest Corner of the Selection.
        CompositeSchematic composite = new CompositeSchematic(0, 0, 0); // This is for compositing many other selections into one big schematic, but I don't have a good use for it here so just used in default behavior for now.

        Vec3i size = sel.size(); // Gets the size of the selection.
        BetterBlockPos min = sel.min(); // Gets the minimum corner of the selection.

        // Java 8 so no switch expressions 😿
        UnaryOperator<ISchematic> create = fill -> { // Creates the Schematic, fuck knows what the fuck is happening here
            final int w = fill.widthX();
            final int h = fill.heightY();
            final int l = fill.lengthZ();

            switch (fillType) {
                case Walls:
                    return new WallsSchematic(fill);
                case Shell:
                    return new ShellSchematic(fill);
                case Replace:
                    return new ReplaceSchematic(fill, replaces);
                case Sphere:
                    return MaskSchematic.create(fill, new SphereMask(w, h, l, true).compute());
                case HSphere:
                    return MaskSchematic.create(fill, new SphereMask(w, h, l, false).compute());
                case Cylinder:
                    return MaskSchematic.create(fill, new CylinderMask(w, h, l, true, alignment).compute());
                case HCylinder:
                    return MaskSchematic.create(fill, new CylinderMask(w, h, l, false, alignment).compute());
                default:
                    // Silent fail
                    return fill;
            }
        };

        System.out.println(replaces);
        System.out.println(replaceWithMeta);

        ISchematic schematic = create.apply(new FillSchematic(size.getX(), size.getY(), size.getZ(), replaceWithMeta));
        composite.put(schematic, min.x - origin.x, min.y - origin.y, min.z - origin.z);

        System.out.println(composite);

        BaritoneWrapper.getInstance().getBuilderProcess().build("Fill", composite, origin);

        System.out.printf("%s %s %s / %s / ", size.getX(), size.getY(), size.getZ(), composite.toString(), schematic);

        return sel;
    }

    /**
     * Fills the entire area of a selection with a specified block, replacing all existing blocks.
     *
     * @param fillingBlock The block to fill the selection area with.
     * @return The updated selection area after the fill action.
     */
    public ISelection fillArea(Block fillingBlock){
        return basicFillAction(SelectionFillType.Default, null, fillingBlock, null);
    }

    /**
     * Fills a selection area with a specified block in a specific shape based on the fill type.
     *
     * @param fillType The shape type to fill the selection with. Replace and Default types are invalid.
     * @param fillingBlock The block to use for filling the selection.
     * @param shapeFacingAxis The axis the shape is aligned with, required for cylindrical shapes.
     * @return The updated selection area after the fill action.
     * @throws Exception if the fill type is Replace or Default, which are invalid for this method.
     */
    public ISelection fillShape(SelectionFillType fillType, Block fillingBlock, Direction.Axis shapeFacingAxis) throws Exception {
        if (fillType == SelectionFillType.Default) throw new Exception("Invalid Fill Type for filling a Shape in the Selection");
        return basicFillAction(fillType, null, fillingBlock, shapeFacingAxis);
    }

    /**
     * Replaces specific blocks in a selection area with another block.
     *
     * @param replacingBlock The block to replace the specified blocks with.
     * @param replacedBlocks The list of blocks to be replaced in the selection.
     * @return The updated selection area after the replacement.
     */
    public ISelection replaceArea(Block replacingBlock, List<Block> replacedBlocks){
        return basicFillAction(SelectionFillType.Replace, replacedBlocks, replacingBlock, null);
    }

    /**
     * Clears the entire area of a selection by replacing all blocks within it with air.
     *
     * @return The updated selection area after clearing it.
     */
    public ISelection clearArea(){
        return basicFillAction(SelectionFillType.Default, null, Blocks.AIR, null);
    }

    public SelectionClipboard getAreaAsSchematic(BlockPos setPosition){
//        BaritoneWrapper.getInstance().getWorldProvider().getCurrentWorld().
        IPlayerContext ctx = BaritoneWrapper.getInstance().getPlayerContext();
        BetterBlockPos playerPos = ctx.viewerPos();
        BetterBlockPos pos = setPosition != null ? new BetterBlockPos(setPosition) : playerPos;

        ISelection selection = getFromBaritone();
        if(selection == null){
            selection = addToBaritone();
        }

        BetterBlockPos origin = selection.min();
        CompositeSchematic composite = new CompositeSchematic(0, 0, 0);

        BetterBlockPos min = selection.min();
        origin = new BetterBlockPos(
                Math.min(origin.x, min.x),
                Math.min(origin.y, min.y),
                Math.min(origin.z, min.z)
        );

        Vec3i size = selection.size();
        BlockState[][][] blockstates = new BlockState[size.getX()][size.getZ()][size.getY()];
        for (int x = 0; x < size.getX(); x++) {
            for (int y = 0; y < size.getY(); y++) {
                for (int z = 0; z < size.getZ(); z++) {
                    blockstates[x][z][y] = get0(min.x + x, min.y + y, min.z + z, true);
                }
            }
        }

        ISchematic schematic = new IStaticSchematic() {
            @Override
            public BlockState desiredState(int i, int i1, int i2, BlockState blockState, List<BlockState> list) {
                return null;
            }

            @Override
            public int widthX() {
                return size.getX();
            }

            @Override
            public int heightY() {
                return size.getY();
            }

            @Override
            public int lengthZ() {
                return size.getZ();
            }

            @Override
            public BlockState getDirect(int i, int i1, int i2) {
                return blockstates[i][i1][i2];
            }
        };
        composite.put(schematic, min.x - origin.x, min.y - origin.y, min.z - origin.z);

        return new SelectionClipboard(composite, origin.subtract(pos));
    }

    public SelectionClipboard copyArea(BlockPos targetPos){
        SelectionStorage.clipboard = getAreaAsSchematic(targetPos);
        return SelectionStorage.clipboard;
    }

    public static BlockState getFromChunk(Chunk chunk, int x, int y, int z) {
        ChunkSection section = chunk.getSection(y >> 4);
        AtomicBoolean hasOnlyAir = new AtomicBoolean(true);
        section.getBlockStateContainer().forEachValue(blockState -> {
            if (blockState != Blocks.AIR.getDefaultState()){
                hasOnlyAir.set(false);
                return;
            }
        });
        if (hasOnlyAir.get()) {
            return Blocks.AIR.getDefaultState();
        }
        return section.getBlockState(x & 15, y & 15, z & 15);
    }

    private BlockState get0(int x, int y, int z, boolean useTheRealWorld) { // Mickey resigned
        ClientWorld world = MinecraftClient.getInstance().world;
        // Invalid vertical position
        if (y < 0 || y >= world.getDimension().height()) {
            return Blocks.AIR.getDefaultState();
        }

        if (useTheRealWorld) {
            Chunk cached = MinecraftClient.getInstance().world.getChunk(getPos1());
            // there's great cache locality in block state lookups
            // generally it's within each movement
            // if it's the same chunk as last time
            // we can just skip the mc.world.getChunk lookup
            // which is a Long2ObjectOpenHashMap.get
            // see issue #113
            if (cached != null && cached.getPos().x == x >> 4 && cached.getPos().z == z >> 4) {
                return getFromChunk(cached, x, y, z);
            }
            Chunk chunk = MinecraftClient.getInstance().world.getChunk(x >> 4, z >> 4, ChunkStatus.FULL, false);
            if (chunk != null && chunk.getStatus() != ChunkStatus.EMPTY) {
                cachedRegion = BaritoneWrapper.getInstance().getWorldProvider().getCurrentWorld().getCachedWorld().getRegion(pos1.getX() >> 9, pos1.getZ() >> 9);
                return getFromChunk(chunk, x, y, z);
            }
        }
        // same idea here, skip the Long2ObjectOpenHashMap.get if at all possible
        // except here, it's 512x512 tiles instead of 16x16, so even better repetition
        ICachedRegion cached = cachedRegion;
        if (cached == null || cached.getX() != x >> 9 || cached.getZ() != z >> 9) {
            if (MinecraftClient.getInstance().world == null) {
                return Blocks.AIR.getDefaultState();
            }
            ICachedRegion region = BaritoneWrapper.getInstance().getWorldProvider().getCurrentWorld().getCachedWorld().getRegion(x>>9,z>>9); // world.getChunk(getPos1()).(x >> 9, z >> 9);

            if (region == null) {
                return Blocks.AIR.getDefaultState();
            }
            cachedRegion = BaritoneWrapper.getInstance().getWorldProvider().getCurrentWorld().getCachedWorld().getRegion(pos1.getX() >> 9, pos1.getZ() >> 9);
            cached = region;
        }
        BlockState type = cached.getBlock(x & 511, y, z & 511);
        if (type == null) {
            return Blocks.AIR.getDefaultState();
        }
        return type;
    }

    public boolean isValid(){
        return pos1 != null && pos2 != null;
    }

    public ISelection addToBaritone(){
        BetterBlockPos b1 = BetterBlockPos.from(getPos1()), b2 = BetterBlockPos.from(getPos2());
        return BaritoneWrapper.getInstance().getSelectionManager().addSelection(b1, b2);
    }

    public ISelection getFromBaritone(){
        for(ISelection i : BaritoneWrapper.getInstance().getSelectionManager().getSelections()){
            ISelection sel = findInBaritone(getPos1(), getPos2());
            if (sel != null) return sel;
        }
        return null;
    }

    public static ISelection findInBaritone(BlockPos pos1, BlockPos pos2){
        for(ISelection sel : BaritoneWrapper.getInstance().getSelectionManager().getSelections()){
            if(
                (
                    (sel.pos1().getX() == pos1.getX() && sel.pos1().getY() == pos1.getY() && sel.pos1().getZ() == pos1.getZ()) && (sel.pos2().getX() == pos2.getX() && sel.pos2().getY() == pos2.getY() && sel.pos2().getZ() == pos2.getZ())
                ) ||
                (
                    (sel.pos2().getX() == pos1.getX() && sel.pos2().getY() == pos1.getY() && sel.pos2().getZ() == pos1.getZ()) && (sel.pos1().getX() == pos2.getX() && sel.pos1().getY() == pos2.getY() && sel.pos1().getZ() == pos2.getZ())
                ) ||
                (
                    (sel.pos1().getX() == pos2.getX() && sel.pos2().getY() == pos1.getY() && sel.pos2().getZ() == pos1.getZ()) && (sel.pos2().getX() == pos1.getX() && sel.pos2().getY() == pos1.getY() && sel.pos2().getZ() == pos1.getZ())
                )
            ){
                return sel;
            }
        }
        return null;
    }
}
