package com.westosia.civilians.listeners;

import com.westosia.civilians.Main;
import com.westosia.civilians.api.NPC;
import com.westosia.civilians.api.NPCMananger;
import com.westosia.civilians.api.events.NPCLeftClickEvent;
import com.westosia.civilians.api.events.NPCRightClickEvent;
import com.westosia.westosiaapi.utils.Text;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        NPCMananger.getAllNPCs().values().forEach(npc -> {
            NPCMananger.renderNPC(npc, player);
        });

        packetListener(player);
    }

    private void packetListener(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                if (packet instanceof PacketPlayInUseEntity) {
                    PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;

                    int id = packetPlayInUseEntity.getEntityId();

                    if (NPCMananger.getAllNPCs().containsKey(id)) {
                        NPC npc = NPCMananger.getAllNPCs().get(id);

                        if (player.hasMetadata("npc-debug")) {
                            player.sendMessage(Text.colour("&fNPC: " + npc.getName() + " &f(&e" + npc.getID() + "&f)"));
                        }

                        if (packetPlayInUseEntity.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
                            // right-click
                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                NPCRightClickEvent npcRightClickEvent = new NPCRightClickEvent(
                                        npc,
                                        player
                                );
                                Bukkit.getPluginManager().callEvent(npcRightClickEvent);
                            });
                        } else if (packetPlayInUseEntity.b() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                            // left-click
                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                NPCLeftClickEvent npcLeftClickEvent = new NPCLeftClickEvent(
                                        npc,
                                        player
                                );
                                Bukkit.getPluginManager().callEvent(npcLeftClickEvent);
                            });
                        }
                    }
                }
                super.channelRead(ctx, packet);
            }
        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

}
