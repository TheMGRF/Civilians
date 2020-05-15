package com.westosia.civilians.api.events;

import com.westosia.civilians.api.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCRightClickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final NPC npc;
    private final Player player;

    public NPCRightClickEvent(NPC npc, Player player) {
        this.npc = npc;
        this.player = player;
    }

    public NPC getNPC() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}