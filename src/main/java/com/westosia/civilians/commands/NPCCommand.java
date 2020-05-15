package com.westosia.civilians.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.westosia.civilians.Main;
import com.westosia.civilians.api.NPC;
import com.westosia.civilians.api.NPCMananger;
import com.westosia.civilians.utils.PlayerSkin;
import com.westosia.westosiaapi.WestosiaAPI;
import com.westosia.westosiaapi.api.Notifier;
import com.westosia.westosiaapi.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

@CommandAlias("npc")
public class NPCCommand extends BaseCommand {

    @Default
    @Subcommand("help")
    @Description("Base help command for Civilians")
    public void help(Player player) {
        // TODO: Help
        player.sendMessage("help");
    }

    @Subcommand("create")
    public void create(Player player, String npcName) {
        String finalNpcName = Text.colour(npcName);
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            String[] values = PlayerSkin.getFromName(finalNpcName);
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                NPCMananger.createNPC(
                        new NPC(
                                player.getLocation(),
                                finalNpcName,
                                values[0],
                                values[1]
                        )
                );
                WestosiaAPI.getNotifier().sendChatMessage(player, Notifier.NotifyStatus.SUCCESS, "Created NPC!");
            });
        });
    }

    @Subcommand("skin")
    public void skin(Player player, String npcName) {
        // TODO: Set the skin of the NPC
    }

    @Subcommand("delete")
    public void delete(Player player, int npcID) {
        if (NPCMananger.getAllNPCs().containsKey(npcID)) {
            NPCMananger.removeNPC(NPCMananger.getAllNPCs().get(npcID));
            WestosiaAPI.getNotifier().sendChatMessage(player, Notifier.NotifyStatus.SUCCESS, "NPC deleted!");
        } else {
            WestosiaAPI.getNotifier().sendChatMessage(player, Notifier.NotifyStatus.ERROR, "Could not find an NPC with the id: " + npcID);
        }
    }

    @Subcommand("debug")
    public void debug(Player player) {
        if (player.hasMetadata("npc-debug")) {
            player.removeMetadata("npc-debug", Main.getInstance());
            WestosiaAPI.getNotifier().sendChatMessage(player, Notifier.NotifyStatus.ERROR, "Disabled NPC debugger!");
        } else {
            player.setMetadata("npc-debug", new FixedMetadataValue(Main.getInstance(), true));
            WestosiaAPI.getNotifier().sendChatMessage(player, Notifier.NotifyStatus.SUCCESS, "Enabled NPC debugger!");
        }
    }

}
