package com.westosia.civilians;

import co.aikar.commands.PaperCommandManager;
import com.westosia.civilians.api.NPCMananger;
import com.westosia.civilians.commands.NPCCommand;
import com.westosia.civilians.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new NPCCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        NPCMananger.getAllNPCs().values().forEach(NPCMananger::removeNPC);
        NPCMananger.getAllNPCs().clear();
    }

    public static Main getInstance() {
        return instance;
    }
}
