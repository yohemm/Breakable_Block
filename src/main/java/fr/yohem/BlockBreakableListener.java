package fr.yohem;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockBreakableListener implements Listener{
    MyPlugin plugin;
    BlockBreakableListener(MyPlugin plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        BlockBreakable breakable = BlockBreakable.blockToBreakableBlock(event.getBlock());  
        if (breakable != null) {
            event.setCancelled(true);
            if (!breakable.breakBlock()) {
                return;
            }
            plugin.coulDowns.put(breakable, System.currentTimeMillis());
            breakable.drop();
        }
        return;
    }
}
