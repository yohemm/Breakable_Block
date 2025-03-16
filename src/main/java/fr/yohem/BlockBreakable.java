package fr.yohem;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockBreakable implements ConfigurationSerializable {
    static List<BlockBreakable> blocks = new ArrayList<BlockBreakable>();

    private Block block;
    private MaterialData intialMaterial;
    private MaterialData backBlock;
    private ItemStack tool;
    private MaterialData treasure;
    private int[] rangeDrop = new int[2];
    private int coulDown;
    private long lastCDStart = 0;

    public int getCoulDown() {
        return coulDown;
    }

    public void startCouldDown() {
        lastCDStart = System.currentTimeMillis();
    }

    private boolean isBreaked = false;

    public boolean isBreaked() {
        return isBreaked;
    }

    BlockBreakable(Location location, MaterialData intialMaterial, MaterialData backBlock, ItemStack tool,
            MaterialData treasure, int[] rangeDrop, int coulDown) {
        BlockBreakable.blocks.add(this);
        this.intialMaterial = intialMaterial;
        this.coulDown = coulDown;
        this.backBlock = backBlock;
        this.rangeDrop = rangeDrop;
        this.tool = tool;
        this.block = location.getBlock();
        this.treasure = treasure;

    }

    BlockBreakable(Block block, int coulDown, MaterialData treasure, int[] rangeDrop, ItemStack tool,
            MaterialData backBlock) {
        this(block.getLocation(), new MaterialData(block.getType(), block.getData()), backBlock, tool, treasure,
                rangeDrop, coulDown);
    }

    static public BlockBreakable blockToBreakableBlock(Block blockToVerif) {
        for (BlockBreakable blockBreakable : blocks)
            if (blockBreakable.block.getLocation().equals(blockToVerif.getLocation()))
                return blockBreakable;
        return null;
    }

    public boolean breakBlock() {
        if (isBreaked)
            return false;
        block.setType(backBlock.getItemType());
        block.setData(backBlock.getData());
        startCouldDown();
        drop();
        isBreaked = true;
        return true;
    }

    public boolean isCouldDownFinish() {
        return System.currentTimeMillis() >= lastCDStart + (coulDown * 1000);
    }

    public void verifyCouldDown() {
        if (isCouldDownFinish()) {
            regeneBlock();
        }
    }

    public boolean regeneBlock() {
        if (!isBreaked)
            return false;
        block.setType(intialMaterial.getItemType());
        block.setData(intialMaterial.getData());
        isBreaked = false;
        lastCDStart = 0;
        return true;
    }

    private void drop() {
        block.getWorld().dropItemNaturally(block.getLocation().add(0, 1, 0), new ItemStack(treasure.getItemType(),
                (rangeDrop[0] + (int) ((rangeDrop[1] - rangeDrop[0]) * Math.random())), treasure.getData()));
    }

    @Override
    public String toString() {
        return "BlockBreakable [block=" + block.getLocation() + ", intialMaterial=" + intialMaterial + ", backBlock="
                + backBlock
                + ", tool=" + tool + ", treasure=" + treasure + ", rangeDrop=" + Arrays.toString(rangeDrop)
                + ", coulDown=" + coulDown + ", isBreaked=" + isBreaked + "]";
    }

    public void destroy() {
        this.regeneBlock();
        BlockBreakable.blocks.remove(this);
    }

    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> serial = new HashMap<String, Object>();

        serial.put("block", block.getLocation().serialize());

        serial.put("intialMaterial", intialMaterial.toItemStack(1).serialize());

        serial.put("backBlock", backBlock.toItemStack(1).serialize());

        serial.put("tool", tool.serialize());

        serial.put("treasure", treasure.toItemStack(1).serialize());

        serial.put("rangeDrop", rangeDrop);

        serial.put("couldownDuration", coulDown);

        return serial;

    }

    public static BlockBreakable deserialize(Map<String, Object> serial) {
        List<Integer> rangeDropList = (List<Integer>) serial.get("rangeDrop");
        int[] rangeDrop = new int[] { rangeDropList.get(0), rangeDropList.get(1) };
        return new BlockBreakable(Location.deserialize((Map) serial.get("block")),
                (ItemStack.deserialize((Map<String, Object>) serial.get("intialMaterial"))).getData(),
                (ItemStack.deserialize((Map<String, Object>) serial.get("backBlock"))).getData(),
                ItemStack.deserialize((Map) serial.get("tool")),
                (ItemStack.deserialize((Map<String, Object>) serial.get("treasure"))).getData(),
                rangeDrop,
                (Integer) serial.get("couldownDuration"));
    }

    public static void export(MyPlugin plugin) {
        final File file = new File(plugin.getDataFolder() + "/blocks.yml");
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        configuration.set("blocks", blocks);
        try {
            configuration.save(file);
        } catch (IOException e) {
            System.err.println("Blocks-breackables NOT EXPORT");
            throw new RuntimeException(e);
        }

    }

    public static void importsBlockBreakables(MyPlugin plugin) {
        final File file = new File(plugin.getDataFolder() + "/blocks.yml");
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        BlockBreakable.blocks = (List<BlockBreakable>) configuration.get("blocks");
        if (blocks == null)
            blocks = new ArrayList<BlockBreakable>();
    }

    public static void regeneAllBlocks() {
        for (BlockBreakable blockBreakable : blocks)
            blockBreakable.regeneBlock();
    }
}