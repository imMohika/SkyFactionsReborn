package net.skullian.skyfactions.gui.items.defence;

import net.skullian.skyfactions.api.DefenceAPI;
import net.skullian.skyfactions.config.types.DefencesConfig;
import net.skullian.skyfactions.config.types.Settings;
import net.skullian.skyfactions.defence.struct.DefenceData;
import net.skullian.skyfactions.faction.Faction;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.SkyItem;
import net.skullian.skyfactions.util.SoundUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.List;

public class DefenceAmmoItem extends SkyItem {

    private boolean HAS_PERMISSIONS = false;

    public DefenceAmmoItem(ItemData data, ItemStack stack, Player player, DefenceData defenceData, Faction faction) {
        super(data, stack, player, List.of(defenceData, faction).toArray());
    }

    @Override
    public ItemBuilder process(ItemBuilder builder) {
        if (!(getOptionals()[2] instanceof String)) {
            Faction faction = (Faction) getOptionals()[1];

            if (DefenceAPI.hasPermissions(DefencesConfig.PERMISSION_REPLENISH_AMMO.getList(), getPLAYER(), faction)) this.HAS_PERMISSIONS = true;
            else return DefenceAPI.processPermissions(builder, getPLAYER());
        } else this.HAS_PERMISSIONS = true;

        return builder;
    }

    @Override
    public Object[] replacements() {
        DefenceData data = (DefenceData) getOptionals()[0];

        return List.of(
                "ammo", data.getAMMO()
        ).toArray();
    }

    @Override
    public void onClick(ClickType clickType, Player player, InventoryClickEvent event) {
        if (!this.HAS_PERMISSIONS) {
            SoundUtil.playSound(player, Settings.ERROR_SOUND.getString(), Settings.ERROR_SOUND_PITCH.getInt(), 1);
            return;
        }
        // todo
    }
}
