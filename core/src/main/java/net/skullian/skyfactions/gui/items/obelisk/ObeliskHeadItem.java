package net.skullian.skyfactions.gui.items.obelisk;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.api.GemsAPI;
import net.skullian.skyfactions.api.IslandAPI;
import net.skullian.skyfactions.api.RunesAPI;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.AsyncSkyItem;
import net.skullian.skyfactions.island.impl.PlayerIsland;

public class ObeliskHeadItem extends AsyncSkyItem {

    public ObeliskHeadItem(ItemData data, ItemStack stack, Player player) {
        super(data, stack, player, null);
    }

    @Override
    public Object[] replacements() {
        
        PlayerIsland island = IslandAPI.getPlayerIsland(getPLAYER().getUniqueId()).join();

        return List.of(
            "player_name", getPLAYER().getName(),
            "level", island == null ? "N/A" : SkyFactionsReborn.getDatabaseManager().getPlayerIslandManager().getIslandLevel(island).join(),
            "rune_count", RunesAPI.getRunes(getPLAYER().getUniqueId()).join(),
            "gem_count", GemsAPI.getGems(getPLAYER().getUniqueId()).join()
        ).toArray();
    }
}
