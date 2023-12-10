package fr.yohem;

import java.lang.reflect.Array;
import java.sql.Blob;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ObjectUtils.Null;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.junit.Test.None;

public class BlockBreakable {
    static List<BlockBreakable> blocks = new ArrayList<BlockBreakable>();

    private Block block;
    private MaterialData intialMaterial;
    private MaterialData backBlock;
    private ItemStack tool;
    private List<MaterialData> treasure;
    private int[] rangeDrop = new int[2];
    private int coulDown;
    public int getCoulDown() {
		return coulDown;
	}
	private boolean isBreaked = false;
    BlockBreakable(Block block,int coulDown, List<MaterialData> treasure,int[] rangeDrop, ItemStack tool, MaterialData backBlock){
        BlockBreakable.blocks.add(this);
        intialMaterial = new MaterialData(block.getType(), block.getData());
        this.coulDown = coulDown;
        this.backBlock = backBlock;
        this.rangeDrop = rangeDrop;
        this.tool = tool;
        this.block = block;
        this.treasure = treasure;
    }
    BlockBreakable(Block block,int coulDown, MaterialData treasure,int[] rangeDrop, ItemStack tool, MaterialData backBlock){
        this(block,coulDown, Arrays.asList(treasure), rangeDrop, tool, backBlock);
    }

    static public BlockBreakable blockToBreakableBlock(Block blockToVerif){
        for (BlockBreakable blockBreakable : blocks)
            if (blockBreakable.block.equals(blockToVerif)) return blockBreakable;
        return null;
    }

    public boolean breakBlock(){
        if (isBreaked) 
            return false;
        block.setType(backBlock.getItemType());
        block.setData(backBlock.getData()); 
        isBreaked = true;
        return true;
    }
    public boolean verifyCouldDown(long startCd){
        if (System.currentTimeMillis()- startCd >=coulDown*1000) return false;
        return true;
    }
    public boolean regeneBlock(){
        if (!isBreaked)
            return false;
        block.setType(intialMaterial.getItemType());
        block.setData(intialMaterial.getData()); 
        isBreaked = false;
        return true;
    }
    public void drop(){
        for(MaterialData mat : treasure)
            block.getWorld().dropItemNaturally(block.getLocation().add(0, 1, 0), new ItemStack(mat.getItemType(), (rangeDrop[0]+ (int)((rangeDrop[1]-rangeDrop[0])* Math.random())), mat.getData()));
    }
	@Override
	public String toString() {
		return "BlockBreakable [block=" + block + ", intialMaterial=" + intialMaterial + ", backBlock=" + backBlock
				+ ", tool=" + tool + ", treasure=" + treasure + ", rangeDrop=" + Arrays.toString(rangeDrop)
				+ ", coulDown=" + coulDown + ", isBreaked=" + isBreaked + "]";
	}
	public void destroy() {
        this.regeneBlock();
        BlockBreakable.blocks.remove(this);
	}

    

    
}
