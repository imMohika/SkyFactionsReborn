package net.skullian.skyfactions.util;

import net.skullian.skyfactions.SkyFactionsReborn;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.api.PlayerAPI;

public class ErrorUtil {

    public static void handleError(CommandSender sender, String operation, String debug, Throwable err) {
        err.printStackTrace();
        Messages.ERROR.send(sender, sender instanceof Player ? PlayerAPI.getLocale(((Player) sender).getUniqueId()) : Messages.getDefaulLocale(), "operation", operation, "debug", debug);
    }

    public static void handleError(Exception error) {
        Bukkit.getScheduler().runTask(SkyFactionsReborn.getInstance(), () -> {
            error.printStackTrace();
            SLogger.fatal("----------------------- DATABASE EXCEPTION -----------------------");
            SLogger.fatal("There was an error while performing database actions:");
            SLogger.fatal(error.getMessage());
            SLogger.fatal("Please see https://docs.terrabytedev.com/skyfactions/errors-and-debugging for more information.");
            SLogger.fatal("Please contact the devs.");
            SLogger.fatal("----------------------- DATABASE EXCEPTION -----------------------");
        });
    }

}
