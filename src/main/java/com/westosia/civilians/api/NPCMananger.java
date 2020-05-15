package com.westosia.civilians.api;

import com.westosia.civilians.Main;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class NPCMananger {

    private static final HashMap<Integer, NPC> npcs = new HashMap<>();

    public static void createNPC(NPC npc) {
        npcs.put(npc.getEntityPlayer().getBukkitEntity().getEntityId(), npc);
        spawnNPC(npc);
    }

    public static void spawnNPC(NPC npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            renderNPC(npc, player);
        }
    }

    public static void renderNPC(NPC npc, Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        EntityPlayer entityPlayer = npc.getEntityPlayer();
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (entityPlayer.yaw * 256 / 360)));

        DataWatcher dw = entityPlayer.getDataWatcher();
        dw.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 0xFF);
        connection.sendPacket(new PacketPlayOutEntityMetadata(entityPlayer.getId(), dw, false));

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
        }, 1);
    }

    public static void removeNPC(NPC npc) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

            EntityPlayer entityPlayer = npc.getEntityPlayer();
            connection.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));
        });
        npcs.remove(npc.getID());
    }

    public static HashMap<Integer, NPC> getAllNPCs() {
        return npcs;
    }
}
