package net.skullian.skyfactions.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.config.types.Settings;
import net.skullian.skyfactions.faction.Faction;
import net.skullian.skyfactions.npc.factory.CitizensFactory;
import net.skullian.skyfactions.npc.factory.FancyNPCsFactory;
import net.skullian.skyfactions.npc.factory.SkyNPCFactory;
import net.skullian.skyfactions.npc.factory.ZNPCsPlusFactory;
import net.skullian.skyfactions.util.DependencyHandler;
import net.skullian.skyfactions.util.SLogger;

public class NPCManager {

    private final Map<SkyNPC, UUID> playerNPCs = new HashMap<>();
    private final Map<SkyNPC, Faction> factionNPCs = new HashMap<>();

    public final SkyNPCFactory factory;

    public NPCManager() {
        this.factory = getFactory();
    }
    
    public void onClick(SkyNPC npc, Player player) {
        boolean isFaction = playerNPCs.containsKey(npc); // sketchy, but it works (i hope 😭)

        if (isFaction) {
            Faction faction = Objects.requireNonNull(factionNPCs.get(npc));

            if (!faction.isInFaction(player.getUniqueId())) {
                Messages.NPC_ACCESS_DENY.send(player);
                return;
            }

            process(Settings.NPC_FACTION_ISLANDS_ACTIONS.getList());
        } else {
            UUID owner = Objects.requireNonNull(playerNPCs.get(npc));

            if (!owner.equals(player.getUniqueId())) {
                Messages.NPC_ACCESS_DENY.send(player);
                return;
            }

            process(Settings.NPC_PLAYER_ISLANDS_ACTIONS.getList());
        }
    }

    public void spawnNPC(UUID playerUUID) {
        
    }

    private void process(List<String> actions) {
        for (String action : actions) {

            switch(action) {

            }

        }
    }

    private SkyNPCFactory getFactory() {
        switch (Settings.NPC_FACTORY.getString().toLowerCase()) {

            case "znpcsplus":
                if (DependencyHandler.isEnabled("FancyNPCs")) {
                    return new ZNPCsPlusFactory();
                } else alert("FancyNPCs");

                break;

            case "citizens":
                if (DependencyHandler.isEnabled("Citizens")) {
                    return new CitizensFactory();
                } else alert("Citizens");

                break;

            case "fancynpcs":
                if (DependencyHandler.isEnabled("FancyNPCs")) {
                    return new FancyNPCsFactory();
                } else alert("FancyNPCs");

                break;
            
            default:
                new Exception("Unknown NPC Factory: " + Settings.NPC_FACTORY.getString()).printStackTrace();
                Bukkit.getPluginManager().disablePlugin(SkyFactionsReborn.getInstance());
        }

        return null;
    }

    private void alert(String plugin) {
        SLogger.fatal("----------------------- NPC EXCEPTION -----------------------");
        SLogger.fatal("There was an error initialising the NPC integration.");
        SLogger.fatal("Plugin will now disable.");
        SLogger.fatal("----------------------- NPC EXCEPTION -----------------------");
        new Exception(String.format("Attempted to use the %s NPC factory when %s was not present on the server!", plugin, plugin)).printStackTrace();
        Bukkit.getPluginManager().disablePlugin(SkyFactionsReborn.getInstance());
    }
}