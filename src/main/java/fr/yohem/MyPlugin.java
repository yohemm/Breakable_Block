package fr.yohem;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

public class MyPlugin extends JavaPlugin {

    HashMap<BlockBreakable, Long> coulDowns = new HashMap<BlockBreakable,Long>();
    // This code is called after the server starts and after the /reload command
    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "{0}.onEnable()", this.getClass().getName());
        getCommand("block-breakable").setExecutor(new BreakableCommands());
        getCommand("no-block-breakable").setExecutor(new BreakableCommands());
        getServer().getPluginManager().registerEvents(new BlockBreakableListener(this), this);

        new BukkitRunnable() {

			@Override
			public void run() {
                Iterator<Entry<BlockBreakable, Long>> iterator = coulDowns.entrySet().iterator(); 

                // Iterate over the HashMap 
                while (iterator.hasNext()) { 

                    // Get the entry at this iteration 
                    Entry<BlockBreakable, Long> entry = iterator.next(); 

                    // Check if this key is the required key 
                    if (!entry.getKey().verifyCouldDown(entry.getValue())) { 
                        entry.getKey().regeneBlock();

                        // Remove this entry from HashMap 
                        iterator.remove(); 
                    } 
                } 
			}
            
        }.runTaskTimer(this, 0, 40L);
    }

    // This code is called before the server stops and after the /reload command
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }
}
