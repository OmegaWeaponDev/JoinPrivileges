package me.omegaweapondev.joinprivileges.utilities;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import me.ou.library.Utilities;
import me.ou.library.configs.ConfigCreator;
import me.ou.library.configs.ConfigUpdater;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 * The Storage Manager class that handles all the plugins files
 *
 * @author OmegaWeaponDev
 */
public class StorageManager {
  private final JoinPrivileges pluginInstance;

  private final ConfigCreator configFile = new ConfigCreator("config.yml");
  private final ConfigCreator messagesFile = new ConfigCreator("messages.yml");
  private final ConfigCreator userDataFile = new ConfigCreator("userData.yml");
  private final ConfigCreator joinEffectsFile = new ConfigCreator("joinEffects.yml");
  private final ConfigCreator rewardsFile = new ConfigCreator("rewards.yml");
  private final ConfigCreator totalJoinsLogFile = new ConfigCreator("totalJoinsLog.yml");

  /**
   *
   * The public constructor for the Storage Manager class
   *
   * @param pluginInstance (The plugin's instance)
   */
  public StorageManager(final JoinPrivileges pluginInstance) {
    this.pluginInstance = pluginInstance;
  }


  /**
   *
   * Handles creating all the files and the data folder for OmegaVision
   *
   */
  public void setupConfigs() {
    getConfigFile().createConfig();
    getMessagesFile().createConfig();
    getJoinEffectsFile().createConfig();
    getRewardsFile().createConfig();
    getUserDataFile().createConfig();
  }

  /**
   *
   * Handles making sure all the files are up-to-date against the default in the resources folder
   *
   */
  public void configUpdater() {
    Utilities.logInfo(true, "Attempting to update the config files....");

    try {
      if(getConfigFile().getConfig().getDouble("Config_Version") != 2.0) {
        getConfigFile().getConfig().set("Config_Version", 2.0);
        getConfigFile().saveConfig();
        ConfigUpdater.update(pluginInstance, "config.yml", getConfigFile().getFile(), Arrays.asList("none"));
        Utilities.logInfo(true, "The config.yml has successfully been updated!");
      }

      if(getMessagesFile().getConfig().getDouble("Config_Version") != 2.0) {
        getMessagesFile().getConfig().set("Config_Version", 2.0);
        getMessagesFile().saveConfig();
        ConfigUpdater.update(pluginInstance, "messages.yml", getMessagesFile().getFile(), Arrays.asList("none"));
        Utilities.logInfo(true, "The messages.yml has successfully been updated!");
      }
      pluginInstance.onReload();
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   *
   * Handles reloading all the files
   *
   */
  public void reloadFiles() {
    getConfigFile().reloadConfig();
    getMessagesFile().reloadConfig();
    getRewardsFile().reloadConfig();
    getJoinEffectsFile().reloadConfig();
    getTotalJoinsLogFile().reloadConfig();
  }

  public ConfigCreator getConfigFile() {
    return configFile;
  }

  public ConfigCreator getMessagesFile() {
    return messagesFile;
  }

  public ConfigCreator getJoinEffectsFile() {
    return joinEffectsFile;
  }

  public ConfigCreator getRewardsFile() {
    return rewardsFile;
  }

  public ConfigCreator getTotalJoinsLogFile() {
    return totalJoinsLogFile;
  }

  public ConfigCreator getUserDataFile() {
    return userDataFile;
  }
}
