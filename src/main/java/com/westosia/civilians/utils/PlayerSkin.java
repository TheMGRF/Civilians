package com.westosia.civilians.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class PlayerSkin {

    public static String[] getFromPlayer(Player playerBukkit) {
        EntityPlayer playerNMS = ((CraftPlayer) playerBukkit).getHandle();
        GameProfile profile = playerNMS.getProfile();
        Property property = profile.getProperties().get("textures").iterator().next();
        String texture = property.getValue();
        String signature = property.getSignature();
        return new String[] {texture, signature};
    }

    public static String[] getFromName(String name) {
        try {
            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + ChatColor.stripColor(name));

            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            if (reader_0.ready()) {

                String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

                URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
                InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
                JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                String texture = textureProperty.get("value").getAsString();
                String signature = textureProperty.get("signature").getAsString();

                return new String[]{texture, signature};
            }
            return new String[]{"", ""};
        } catch (IOException e) {
            System.err.println("Could not get skin data from session servers!");
            e.printStackTrace();
            return null;
        }
    }

}
