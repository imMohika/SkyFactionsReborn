package net.skullian.skyfactions.event.defence;

import java.util.Optional;
import java.util.UUID;

import net.skullian.skyfactions.api.FactionAPI;
import net.skullian.skyfactions.config.types.DefencesConfig;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.api.PlayerAPI;
import net.skullian.skyfactions.event.armor.ArmorEquipEvent;
import net.skullian.skyfactions.gui.screens.defence.DefenceManageUI;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;

import net.skullian.skyfactions.api.DefenceAPI;
import net.skullian.skyfactions.defence.Defence;
import org.bukkit.event.player.PlayerInteractEvent;

public class DefenceInteractionHandler implements Listener {

    @EventHandler
    public void onDefenceExplode(BlockExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (DefenceAPI.isDefenceMaterial(block) && DefenceAPI.isDefence(block.getLocation())) {

                Defence defence = DefenceAPI.getLoadedDefence(block.getLocation());
                if (defence != null) defence.damage(Optional.empty());

                event.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onDefenceBreak(BlockBreakEvent event) {
        if (DefenceAPI.isDefence(event.getBlock().getLocation())) {
            event.setCancelled(true);
            Messages.DEFENCE_DESTROY_DENY.send(event.getPlayer(), event.getPlayer().locale().getLanguage());
        }
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        if (DefenceAPI.isDefence(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDefenceInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !event.hasBlock()) return;
        if (!DefenceAPI.isDefenceMaterial(event.getClickedBlock()) || !DefenceAPI.isDefence(event.getClickedBlock().getLocation())) return;

        Player player = event.getPlayer();
        Defence defence = DefenceAPI.getLoadedDefence(event.getClickedBlock().getLocation());

        if (defence != null) {
            if (defence.getData().isIS_FACTION()) {
                FactionAPI.getFaction(player.getUniqueId()).whenComplete((faction, ex) -> {
                    if (ex != null) {
                        ex.printStackTrace();
                        return;
                    } else if (faction == null) return;
                        else if (!faction.getName().equals(defence.getData().getUUIDFactionName())) return;
                        else if (DefenceAPI.hasPermissions(DefencesConfig.PERMISSION_ACCESS_DEFENCE.getList(), player, faction))
                            Messages.DEFENCE_INSUFFICIENT_PERMISSIONS.send(player, PlayerAPI.getLocale(player.getUniqueId()));

                    DefenceManageUI.promptPlayer(player, defence.getData(), defence.getStruct(), faction);
                });
            } else {
                if (UUID.fromString(defence.getData().getUUIDFactionName()).equals(player.getUniqueId())) {
                    DefenceManageUI.promptPlayer(player, defence.getData(), defence.getStruct(), null);
                }
            }
        }
    }

    /*private static HashSet<Material> transparentBlocks = new HashSet<>();

    static {
        transparentBlocks.add(Material.WATER);
        transparentBlocks.add(Material.AIR);
    }

    @EventHandler
    public void onBlockBreak(BlockDamageEvent event){

        SkyFactionsReborn.blockService.createBrokenBlock(event.getBlock(), 30);
    }

    @EventHandler
    public void onBreakingBlock(PlayerAnimationEvent event) {
        Player player = event.getPlayer();

        Block block = player.getTargetBlock(transparentBlocks, 5);
        Location blockPos = block.getLocation();

        if (!SkyFactionsReborn.blockService.isBrokenBlock(blockPos)) return;

        double distanceX = blockPos.getX() - player.getLocation().getX();
        double distanceY = blockPos.getY() - player.getLocation().getY();
        double distanceZ = blockPos.getZ() - player.getLocation().getZ();

        if(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ >= 1024.0D) return;
        DefenceDestructionManager.addSlowDig(event.getPlayer(), 200);
        SkyFactionsReborn.blockService.getBrokenBlock(blockPos).incrementDamage(player, 1);
    }*/


}
