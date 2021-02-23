package me.omegaweapondev.joinprivileges;

import me.omegaweapondev.joinprivileges.commands.CoreCommand;
import me.omegaweapondev.joinprivileges.events.PlayerListener;
import me.omegaweapondev.joinprivileges.utilities.Placeholders;
import me.omegaweapondev.joinprivileges.utilities.SettingsHandler;
import me.ou.library.SpigotUpdater;
import me.ou.library.Utilities;
import net.milkbowl.vault.chat.Chat;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinPrivileges extends JavaPlugin {
  public JoinPrivileges plugin;

  private SettingsHandler settingsHandler;
  private Chat chat = null;

  @Override
  public void onEnable() {
    plugin = this;
    settingsHandler = new SettingsHandler(plugin);

    initialSetup();
    getSettingsHandler().setupConfigs();
    getSettingsHandler().configUpdater();
    setupChat();
    setupCommands();
    setupEvents();
    spigotUpdater();
  }

  @Override
  public void onDisable() {
    plugin = null;
    super.onDisable();
  }

  public void onReload() {
    getSettingsHandler().reloadFiles();
  }

  private void initialSetup() {

    // Setup the instance for plugin and OU Library
    plugin = this;
    Utilities.setInstance(this);

    // Make sure vault is installed
    if(Bukkit.getPluginManager().getPlugin("Vault") == null) {
      Utilities.logWarning(true,
        "JoinPrivileges has detected that Vault is not installed.",
        "Without it, the prefixes and suffixes for players will not work.",
        "To install vault, please go to https://www.spigotmc.org/resources/vault.34315/ and download it.",
        "Once vault is installed, restart the server and JoinPrivileges will work."
      );
    }

    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      new Placeholders(this).register();
    }

    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
      Utilities.logWarning(true,
        "DeathWarden requires PlaceholderAPI to be installed if you are wanting to use the placeholders",
        "You can install PlaceholderAPI here: https://www.spigotmc.org/resources/placeholderapi.6245/ "
      );
    }

    // Setup bStats
    final int bstatsPluginId = 8969;
    Metrics metrics = new Metrics(plugin, bstatsPluginId);

    // Logs a message to console, saying that the plugin has enabled correctly.
    Utilities.logInfo(false,
      "   _________",
        "  |_  | ___ \\",
        "    | | |_/ /  JoinPrivileges v" + plugin.getDescription().getVersion() + " By OmegaWeaponDev.",
        "    | |  __/   Handle the way players join and leave your server!",
        "/\\__/ / |      Currently supporting spigot 1.13 upto 1.16.5",
        "\\____/\\_|",
      ""
    );
  }

  private void setupCommands() {
    Utilities.logInfo(true, "Registering the commands...");

    Utilities.setCommand().put("joinprivileges", new CoreCommand(plugin));

    Utilities.registerCommands();
    Utilities.logInfo(true, "Commands Registered: " + Utilities.setCommand().size());
  }

  private void setupEvents() {
    Utilities.registerEvents(new PlayerListener(plugin));
  }

  private void spigotUpdater() {
    new SpigotUpdater(this, 84563).getVersion(version -> {
      int spigotVersion = Integer.parseInt(version.replace(".", ""));
      int pluginVersion = Integer.parseInt(this.getDescription().getVersion().replace(".", ""));

      if(pluginVersion >= spigotVersion) {
        Utilities.logInfo(true, "There are no new updates for the plugin. Enjoy!");
        return;
      }

      PluginDescriptionFile pdf = this.getDescription();
      Utilities.logWarning(true,
        "A new version of " + pdf.getName() + " is avaliable!",
        "Current Version: " + pdf.getVersion() + " > New Version: " + version,
        "Grab it here: https://github.com/OmegaWeaponDev/JoinPrivileges"
      );
    });
  }

  private boolean setupChat() {
    RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
    chat = rsp.getProvider();
    return chat != null;
  }

  public Chat getChat() {
    return chat;
  }

  public SettingsHandler getSettingsHandler() {
    return settingsHandler;
  }
}
