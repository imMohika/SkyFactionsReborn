package net.skullian.skyfactions.gui.items.obelisk.member_manage.rank;

import lombok.Getter;
import lombok.Setter;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.event.PlayerHandler;
import net.skullian.skyfactions.faction.Faction;
import net.skullian.skyfactions.faction.RankType;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.SkyItem;
import net.skullian.skyfactions.gui.screens.obelisk.member.MemberManageRankUI;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.List;

public class MemberRankChangeItem extends SkyItem {
    @Setter
    private RankType TYPE;

    private MemberManageRankUI UI;

    public MemberRankChangeItem(ItemData data, ItemStack stack, Player player, RankType thisType, Faction faction, OfflinePlayer subject, MemberManageRankUI ui) {
        super(data, stack, player, List.of(thisType, faction, subject).toArray());

        this.UI = ui;
    }

    @Override
    public ItemBuilder process(ItemBuilder builder) {
        OfflinePlayer subject = (OfflinePlayer) getOptionals()[2];
        Faction faction = (Faction) getOptionals()[1];
        RankType thisType = (RankType) getOptionals()[0];
        RankType selectedType = TYPE != null ? TYPE : thisType;

        if (selectedType.equals(thisType) || (TYPE == null && faction.getRankType(subject.getUniqueId()).equals(thisType))) {
            builder.addEnchantment(Enchantment.LURE, 1, false);
            builder.getItemFlags().add(ItemFlag.HIDE_ENCHANTS);
        }

        return builder;
    }

    @Override
    public Object[] replacements() {
        String type = (String) getOptionals()[0];
        Faction faction = (Faction) getOptionals()[1];
        return List.of(
                "is_selected", faction.getRankType(getPLAYER().getUniqueId()).getRankValue().equalsIgnoreCase(type) ? Messages.FACTION_MANAGE_RANK_SELECTED.getString(PlayerHandler.getLocale(getPLAYER().getUniqueId()))
                        : ""
        ).toArray();
    }

    @Override
    public void onClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        player.getInventory().close();
        RankType type = (RankType) getOptionals()[0];

        UI.onSelect(type);
        notifyWindows();
    }
}