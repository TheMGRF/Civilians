package com.westosia.civilians.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import java.util.UUID;

public class NPC {

    private final int id;
    private final Location location;
    private final String name, texture, signature;
    private final GameProfile gameProfile;
    private final EntityPlayer entityPlayer;

    public NPC(Location location, String name, String texture, String signature) {
        this.location = location;
        this.name = name;
        this.texture = texture;
        this.signature = signature;

        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

        gameProfile = new GameProfile(UUID.randomUUID(), name);
        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        entityPlayer = new EntityPlayer(minecraftServer, worldServer, gameProfile, new PlayerInteractManager(worldServer));
        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        id = entityPlayer.getId();
    }

    public int getID() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getTexture() {
        return texture;
    }

    public String getSignature() {
        return signature;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }
}
