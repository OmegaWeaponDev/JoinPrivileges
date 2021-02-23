package me.omegaweapondev.joinprivileges.utilities;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import me.ou.library.Utilities;
import me.ou.library.configs.ConfigCreator;
import me.ou.library.configs.ConfigUpdater;

import java.io.IOException;
import java.util.Arrays;

public class SettingsHandler {
  private final JoinPrivileges plugin;
  private final ConfigCreator configFile = new ConfigCreator("config.yml");
  private final ConfigCreator messagesFile = new ConfigCreator("messages.yml");

  
  public SettingsHandler(final JoinPrivileges plugin) {
    this.plugin = plugin;
  }

  public void setupConfigs() {
    // Setup the files
    getConfigFile().createConfig();
    getMessagesFile().createConfig();
  }

  public void configUpdater() {
    Utilities.logInfo(true, "Attempting to update the config files....");

    try {
      if(getConfigFile().getConfig().getDouble("Config_Version") != 1.0) {
        getConfigFile().getConfig().set("Config_Version", 1.0);
        getConfigFile().saveConfig();
        ConfigUpdater.update(plugin, "config.yml", getConfigFile().getFile(), Arrays.asList("Join_Settings.Group_Join_Settings.Groups", "Quit_Settings.Group_Quit_Settings.Groups"));
        Utilities.logInfo(true, "The config.yml has successfully been updated!");
      }

      if(getMessagesFile().getConfig().getDouble("Config_Version") != 1.0) {
        getMessagesFile().getConfig().set("Config_Version", 1.0);
        getMessagesFile().saveConfig();
        ConfigUpdater.update(plugin, "messages.yml", getMessagesFile().getFile(), Arrays.asList("none"));
        Utilities.logInfo(true, "The messages.yml has successfully been updated!");
      }
      plugin.onReload();
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  public void reloadFiles() {
    getConfigFile().reloadConfig();
    getMessagesFile().reloadConfig();
  }

  public ConfigCreator getConfigFile() {
    return configFile;
  }

  public ConfigCreator getMessagesFile() {
    return messagesFile;
  }
}
