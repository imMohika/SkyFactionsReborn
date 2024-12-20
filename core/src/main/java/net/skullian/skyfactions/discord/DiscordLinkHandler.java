package net.skullian.skyfactions.discord;

import java.awt.Color;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.api.PlayerAPI;

public class DiscordLinkHandler extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("link-mc")) {
            String code = event.getOption("code").getAsString();
            UUID playerUUID = SkyFactionsReborn.getDiscordHandler().codes.get(code);
            if (playerUUID != null) {

                OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);

                SkyFactionsReborn.getCacheService().getEntry(playerUUID).setNewDiscordID(playerUUID, event.getUser().getId());
                if (player.isOnline()) {
                    Messages.DISCORD_LINK_SUCCESS.send(player.getPlayer(), PlayerAPI.getLocale(player.getUniqueId()), "discord_name", event.getUser().getName());
                }

                event.reply("").setEmbeds(buildEmbed(Color.GREEN, Messages.DISCORD_APP_LINK_SUCCESS.getString(Messages.getDefaulLocale()).replace("player_name", player.getName())).build()).queue();
                SkyFactionsReborn.getDiscordHandler().codes.remove(code);
            } else {
                event.reply("").setEmbeds(buildEmbed(Color.RED, Messages.DISCORD_APP_LINK_FAILED.getString(Messages.getDefaulLocale())).build()).queue();
            }
        }
    }

    private EmbedBuilder buildEmbed(Color color, String body) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(color)
                .setDescription(body);

        return embedBuilder;
    }
}
