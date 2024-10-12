package net.skullian.skyfactions.config;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.config.types.*;
import net.skullian.skyfactions.defence.DefencesFactory;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigFileHandler {

    private Map<ConfigTypes, ConfigHandler> configs;

    public ConfigFileHandler() {
        configs = new HashMap<>();
    }

    public FileConfiguration MESSAGES_CONFIG;
    public FileConfiguration DISCORD_CONFIG;

    public void loadFiles(SkyFactionsReborn plugin) {
        new File(plugin.getDataFolder(), "/schematics").mkdirs();
        new File(plugin.getDataFolder(), "/songs").mkdir();

        registerFile(ConfigTypes.SETTINGS, new ConfigHandler(plugin, "config"));
        registerFile(ConfigTypes.MESSAGES, new ConfigHandler(plugin, "messages"));
        registerFile(ConfigTypes.DISCORD, new ConfigHandler(plugin, "discord"));
        registerFile(ConfigTypes.OBELISK, new ConfigHandler(plugin, "obelisk"));
        registerFile(ConfigTypes.RUNES, new ConfigHandler(plugin, "runes"));
        registerFile(ConfigTypes.DEFENCES, new ConfigHandler(plugin, "defences"));

        loadGUIs();
        if (!Files.exists(Paths.get(plugin.getDataFolder() + "/defences"))) {
            DefencesFactory.registerDefaultDefences();
        }
        DefencesFactory.register();

        configs.values().forEach(ConfigHandler::saveDefaultConfig);
        Messages.setConfig(getFile(ConfigTypes.MESSAGES).getConfig());
        Settings.setConfig(getFile(ConfigTypes.SETTINGS).getConfig());
        ObeliskConfig.setConfig(getFile(ConfigTypes.OBELISK).getConfig());
        Runes.setConfig(getFile(ConfigTypes.RUNES).getConfig());
        DefencesConfig.setConfig(getFile(ConfigTypes.DEFENCES).getConfig());

        MESSAGES_CONFIG = getFile(ConfigTypes.MESSAGES).getConfig();
        DISCORD_CONFIG = getFile(ConfigTypes.DISCORD).getConfig();
    }

    public ConfigHandler getFile(ConfigTypes type) {
        return configs.get(type);
    }

    public void reloadFiles() {
        configs.values().forEach(ConfigHandler::reload);
        Messages.setConfig(getFile(ConfigTypes.MESSAGES).getConfig());
        Settings.setConfig(getFile(ConfigTypes.SETTINGS).getConfig());
        ObeliskConfig.setConfig(getFile(ConfigTypes.OBELISK).getConfig());
        Runes.setConfig(getFile(ConfigTypes.RUNES).getConfig());
        DefencesConfig.setConfig(getFile(ConfigTypes.DEFENCES).getConfig());

        loadGUIs();
        DefencesFactory.register();

        MESSAGES_CONFIG = getFile(ConfigTypes.MESSAGES).getConfig();
        DISCORD_CONFIG = getFile(ConfigTypes.DISCORD).getConfig();
    }

    private void loadGUIs() {
        for (GUIEnums enumEntry : GUIEnums.values()) {
            ConfigHandler handler = new ConfigHandler(SkyFactionsReborn.getInstance(), enumEntry.getConfigPath());
            handler.saveDefaultConfig();
            GUIEnums.configs.put(enumEntry.getConfigPath(), handler.getConfig());
        }
    }

    public void registerFile(ConfigTypes type, ConfigHandler handler) {
        configs.put(type, handler);
    }
}