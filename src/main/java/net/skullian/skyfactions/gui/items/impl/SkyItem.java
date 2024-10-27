package net.skullian.skyfactions.gui.items.impl;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.util.text.TextUtility;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public abstract class SkyItem extends AbstractItem {

    private ItemData DATA;
    private ItemStack STACK;
    private Player PLAYER;

    public SkyItem(ItemData data, ItemStack stack, Player player) {
        this.DATA = data;
        this.STACK = stack;
        this.PLAYER = player;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder builder = new ItemBuilder(STACK)
            .setDisplayName(replace(TextUtility.color(DATA.getNAME())));

        for (String loreLine : DATA.getLORE()) {
            builder.addLoreLines(replace(TextUtility.color(loreLine), replacements()));
        }

        return process(builder);
    }

    public abstract ItemBuilder process(ItemBuilder builder);

    public abstract Object[] replacements();

    public static String replace(String message, Object... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            message = message.replace(String.valueOf(replacements[i]), String.valueOf(replacements[i + 1]));
        }

        return message;
    }



}