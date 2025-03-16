package fr.yohem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.BlockIterator;

public class BreakableCommands implements CommandExecutor {
    public int[] argToMaterialWithData(String agr, String separator) {
        String[] speparated = agr.split(separator);
        int[] res = new int[2];
        try {
            if (speparated.length >= 2) {
                res[1] = Integer.parseInt(speparated[1]);
            } else {
                res[1] = Integer.parseInt(speparated[0]);

            }
            res[0] = Integer.parseInt(speparated[0]);
        } catch (NumberFormatException err) {
            return null;
        }
        return res;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // TODO Auto-generated method stub
        if ("no-block-breakable".equalsIgnoreCase(command.getName())) {
            if (sender instanceof Player) {

                Player p = (Player) sender;
                BlockIterator iter = new BlockIterator(p, 10);
                Block lastBlock;
                do {
                    lastBlock = iter.next();
                    if (lastBlock.getType() != Material.AIR && lastBlock.getType() != Material.STATIONARY_LAVA
                            && lastBlock.getType() != Material.STATIONARY_WATER)
                        break;
                    else
                        lastBlock = null;
                } while (iter.hasNext());
                if (lastBlock == null) {
                    lastBlock = null;
                    p.sendMessage("Le block que tu vise n'est pas a porté");
                    return true;
                }

                if (BlockBreakable.blockToBreakableBlock(lastBlock) != null) {
                    BlockBreakable.blockToBreakableBlock(lastBlock).destroy();
                }
                return true;
            }

        } else if ("block-breakable".equalsIgnoreCase(command.getName()) && args.length >= 3) {
            if (sender instanceof Player) {
                int coulDown;
                int[] range = new int[2];
                MaterialData idTreasure;
                MaterialData blockAfterBreak;

                range = argToMaterialWithData(args[2], "-");
                if (range == null) {
                    sender.sendMessage(
                            "impossible de selection cette ranger veuillez utilser se format : \\\"min-max\" ou \"nb\"");
                    return true;
                }
                if (range[0] > range[1])
                    range[1] = range[0];

                int[] treasureTemp = argToMaterialWithData(args[1], ":");
                if (treasureTemp == null) {
                    sender.sendMessage(
                            "impossible de selection cette item veuillez utilser se format : \\\"type:data\"");
                    return true;
                }
                idTreasure = new MaterialData(treasureTemp[0], (byte) treasureTemp[1]);

                if (args.length >= 4) {
                    int[] blockTemp = argToMaterialWithData(args[3], ":");

                    if (blockTemp == null) {
                        sender.sendMessage(
                                "impossible de selection ce block veuillez utilser se format : \\\"type:data\"");
                        return true;
                    }
                    blockAfterBreak = new MaterialData(blockTemp[0], (byte) blockTemp[1]);
                } else {
                    blockAfterBreak = new MaterialData(Material.BEDROCK);
                }
                if (!blockAfterBreak.getItemType().isBlock()) {
                    sender.sendMessage(
                            "l'id de lors de l'état \"casser\" ne correspondait pas à un block, remplacement par bedRock....");
                    blockAfterBreak = new MaterialData(Material.BEDROCK);
                }

                try {
                    coulDown = Integer.parseInt(args[0]);
                } catch (NumberFormatException err) {
                    sender.sendMessage("le couldDown(en secondes) doit être au format numérique");
                    return true;
                }

                Player p = (Player) sender;
                BlockIterator iter = new BlockIterator(p, 10);
                Block lastBlock;

                do {
                    lastBlock = iter.next();
                    if (lastBlock.getType() != Material.AIR && lastBlock.getType() != Material.STATIONARY_LAVA
                            && lastBlock.getType() != Material.STATIONARY_WATER)
                        break;
                    else
                        lastBlock = null;
                } while (iter.hasNext());
                if (lastBlock == null) {
                    p.sendMessage("Le block que tu vise n'est pas a porté");
                    return true;
                }

                if (BlockBreakable.blockToBreakableBlock(lastBlock) != null) {
                    BlockBreakable.blockToBreakableBlock(lastBlock).destroy();
                }

                Material tool = null;
                if (p.getInventory().getItemInMainHand() != null) {
                    tool = p.getInventory().getItemInMainHand().getType();
                    if (!tool.isItem())
                        tool = null;
                }

                BlockBreakable blockBreakable = new BlockBreakable(lastBlock, coulDown, idTreasure,
                        range, new ItemStack(tool), blockAfterBreak);
                System.out.println(blockBreakable);
                sender.sendMessage("Le Block cassable a était créé avec success!");
                return true;
            }
        }
        return false;
    }

}
