package me.omegaweapondev.joinprivileges;

import me.omegaweapondev.joinprivileges.commands.*;
import me.omegaweapondev.joinprivileges.events.JoinsListener;
import me.omegaweapondev.joinprivileges.events.MenuListener;
import me.omegaweapondev.joinprivileges.events.PlayerListener;
import me.omegaweapondev.joinprivileges.utilities.MessageHandler;
import me.omegaweapondev.joinprivileges.utilities.Placeholders;
import me.omegaweapondev.joinprivileges.utilities.StorageManager;
import me.omegaweapondev.joinprivileges.utilities.UserDataHandler;
import me.ou.library.SpigotUpdater;
import me.ou.library.Utilities;
import net.milkbowl.vault.chat.Chat;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * The core class for JoinPrivileges
 *
 * @author OmegaWeaponDev
 *
 */
public class JoinPrivileges extends JavaPlugin {
  private JoinPrivileges pluginInstance;
  private StorageManager storageManager;
  private MessageHandler messageHandler;
  private UserDataHandler userDataHandler;

  private static Chat chat = null;

  /**
   *
   * Allows for enabling of the plugin once the server has started
   *
   */
  @Override
  public void onEnable() {
    // Set the plugin's instance and the Libraries instance to this instance.
    pluginInstance = this;
    Utilities.setInstance(this);

    // Setup the storage manager and messages handler
    storageManager = new StorageManager(pluginInstance);
    messageHandler = new MessageHandler(pluginInstance, storageManager.getMessagesFile().getConfig());

    // Checks if Vault is currently installed on the server.
    if(!Bukkit.getPluginManager().getPlugin("Vault").isEnabled()) {
      Utilities.logWarning(true,
        "JoinPrivileges has detected that you currently don't have Vault",
        "Installed on the server. If you wish for player prefixes/suffixes to be",
        "displayed in the join/leave messages than you should installed Vault.",
        "You can install Vault here: https://www.spigotmc.org/resources/vault.34315/"
      );
    }

    // Checks if PlaceholderAPI is currently installed on the server.
    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
      // Register the plugin's placeholders with PlaceholderAPI
      new Placeholders(pluginInstance).register();
    } else {
      // Warn the user that they will be unable to use the placeholders
      Utilities.logWarning(true,
        "JoinPrivileges has detected that you currently don't have Vault",
        "Installed on the server. If you wish to use the JoinPrivileges Placeholders",
        "in other plugins than you should installed Vault.",
        "You can install Vault here: https://www.spigotmc.org/resources/placeholderapi.6245/"
      );
    }

    // Setup Files
    storageManager.setupConfigs();
    storageManager.configUpdater();

    // Setup UserData
    userDataHandler = new UserDataHandler(pluginInstance);

    // Setup VaultHook
    setupChat();

    // Setup Commands
    registerCommands();

    // Setup Events
    registerEvents();

    // Setup Menus


    // Setup bStats
    final int bstatsPluginId = 8969;
    Metrics metrics = new Metrics(pluginInstance, bstatsPluginId);

    // Send a confirmation message in the console that JoinPrivileges has successfully enabled.
    Utilities.logInfo(false,
      "   _________",
      "  |_  | ___ \\",
      "    | | |_/ /  JoinPrivileges v" + pluginInstance.getDescription().getVersion() + " By OmegaWeaponDev.",
      "    | |  __/   Running on version: " + Bukkit.getVersion(),
      "/\\__/ / |",
      "\\____/\\_|",
      ""
    );

    // Populate the user data map with entries from the user data file
    getUserDataHandler().populateUserDataMap();

    // Send a message in console if there is a new version of the plugin
    if(getStorageManager().getConfigFile().getConfig().getBoolean("Update_Notify")) {
      new SpigotUpdater(pluginInstance, 84563).getVersion(version -> {
        int spigotVersion = Integer.parseInt(version.replace(".", ""));
        int pluginVersion = Integer.parseInt(pluginInstance.getDescription().getVersion().replace(".", ""));

        if(pluginVersion >= spigotVersion) {
          Utilities.logInfo(true, "You are already running the latest version");
          return;
        }

        PluginDescriptionFile pdf = pluginInstance.getDescription();
        Utilities.logWarning(true,
          "A new version of " + pdf.getName() + " is avaliable!",
          "Current Version: " + pdf.getVersion() + " > New Version: " + version,
          "Grab it here: https://www.spigotmc.org/resources/joinprivileges.84563/"
        );
      });
    }
  }

  /**
   *
   * Handles disabling the plugin and saving the user data to the files
   *
   */
  @Override
  public void onDisable() {
    getUserDataHandler().saveUserDataToFile();
  }

  /**
   *
   * Handles reloading the plugin files
   *
   */
  public void onReload() {
    getStorageManager().reloadFiles();
  }

  /**
   *
   * Handles registering the commands for the plugin
   *
   */
  private void registerCommands() {
    Utilities.logInfo(true, "JoinPrivileges is now attempting to register it's commands...");

    Utilities.setCommand().put("joinprivileges", new CoreCommand(pluginInstance));
    Utilities.setCommand().put("joineffects", new JoinEffectsCommand(pluginInstance));
    Utilities.setCommand().put("maintenance", new MaintenanceCommand(pluginInstance));
    Utilities.setCommand().put("playtime", new PlayTimeCommand(pluginInstance));
    Utilities.setCommand().put("rewards", new RewardsCommand(pluginInstance));
    Utilities.setCommand().put("joinstats", new JoinStatsCommand());
    Utilities.setCommand().put("seen", new SeenCommand(pluginInstance));

    Utilities.registerCommands();
    Utilities.logInfo(true, "JoinPrivileges has successfully registered all of it's commands.");
  }

  /**
   *
   * Handles registering the events that the plugin listens to
   *
   */
  private void registerEvents() {
    Utilities.registerEvents(new JoinsListener(pluginInstance), new MenuListener(), new PlayerListener(pluginInstance));
  }

  private boolean setupChat() {
    RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
    chat = rsp.getProvider();
    return chat != null;
  }

  public static Chat getChat() {
    return chat;
  }

  public StorageManager getStorageManager() {
    return storageManager;
  }

  public MessageHandler getMessageHandler() {
    return messageHandler;
  }

  public UserDataHandler getUserDataHandler() {
    return userDataHandler;
  }
}
