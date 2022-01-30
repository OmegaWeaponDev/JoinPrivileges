package me.omegaweapondev.joinprivileges.commands;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import me.omegaweapondev.joinprivileges.utilities.MessageHandler;
import me.omegaweapondev.joinprivileges.utilities.StorageManager;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 *
 * The core command used by the JoinPrivileges plugin
 *
 */
public class CoreCommand extends GlobalCommand implements TabCompleter {
  private final JoinPrivileges pluginInstance;
  private final StorageManager storageManager;
  private final MessageHandler messageHandler;
  private final FileConfiguration configFile;

  /**
   *
   * Public constructor for the core command class
   *
   * @param pluginInstance (The plugins instance)
   */
  public CoreCommand(final JoinPrivileges pluginInstance) {
    this.pluginInstance = pluginInstance;
    storageManager = pluginInstance.getStorageManager();
    messageHandler = pluginInstance.getMessageHandler();
    configFile = storageManager.getConfigFile().getConfig();
  }

  /**
   *
   * Handles the execution of the core command.
   *
   * @param sender (The player or console that executes the command)
   * @param strings (The arguments passed into the command)
   */
  @Override
  protected void execute(final CommandSender sender, final String[] strings) {
    switch (strings[0]) {
      case "version" -> versionMessage(sender);
      case "debug" -> debugCommand(sender);
      case "reload" -> reloadCommand(sender);
      default -> invalidArgs(sender);
    }
  }

  /**
   *
   * Sends a message to the player or console that lists the valid commands that can be used.
   *
   * @param sender (The player or console that executed the command)
   */
  private void invalidArgs(final CommandSender sender) {
    if(sender instanceof final Player player) {

      versionMessage(player);
      Utilities.message(player,
        messageHandler.getPrefix() + "#86DE0FReload Command: #CA002E/joinprivileges reload",
        messageHandler.getPrefix() + "#86DE0FVersion Command: #CA002E/joinprivileges version",
        messageHandler.getPrefix() + "#86DE0FHelp Command: #CA002E/joinprivileges help",
        messageHandler.getPrefix() + "#86DE0FMaintenance Enable Command: #CA002E/maintenance enable",
        messageHandler.getPrefix() + "#86DE0FMaintenance Disable Command: #CA002E/maintenance disable",
        messageHandler.getPrefix() + "#86DE0FJoin Effects Command: #CA002E/joineffects",
        messageHandler.getPrefix() + "#86DE0FPlaytime Command: #CA002E/playtime",
        messageHandler.getPrefix() + "#86DE0FPlaytime Others Command: #CA002E/playtime <player>",
        messageHandler.getPrefix() + "#86DE0FRewards Command: #CA002E/rewards",
        messageHandler.getPrefix() + "#86DE0FJoin Stats Command: #CA002E/joinstats",
        messageHandler.getPrefix() + "#86DE0FSeen Command: #CA002E/seen <player>"
      );
      return;
    }

    if(sender instanceof ConsoleCommandSender) {
      versionMessage(sender);
      Utilities.logInfo(true,
        "Reload Command: /joinprivileges reload",
        "Version Command: /joinprivileges version",
        "Help Command: /joinprivileges help",
        "Maintenance Enable Command: /maintenance enable",
        "Maintenance Disable Command: /maintenance disable",
        "Playtime Others Command: /playtime <player>",
        "Join Stats Command: /joinstats",
        "Seen Command: /seen <player>"
      );
    }
  }

  /**
   *
   * Sends a message to either the player or console with the plugins version
   *
   * @param sender (The player or console that executed the command)
   */
  private void versionMessage(final CommandSender sender) {
    if(sender instanceof final Player player) {
      Utilities.message(player, messageHandler.getPrefix() + "#14abc9JoinPrivileges #ff4a4av" + pluginInstance.getDescription().getVersion() + " #14abc9By OmegaWeaponDev");
      return;
    }

    Utilities.logInfo(true, "JoinPrivileges v" + pluginInstance.getDescription().getVersion() + " By OmegaWeaponDev");
  }

  /**
   *
   * Handles reloading the plugin
   * Sends a message to either the player or console with saying that the plugin has successfully reloaded.
   *
   * @param sender (The player or console who executed the command)
   */
  private void reloadCommand(final CommandSender sender) {
    if(sender instanceof final Player player) {
      if(!Utilities.checkPermissions(player, true, "joinprivileges.reload", "joinprivileges.admin")) {
        Utilities.message(player, messageHandler.string("No_Permission", "#f63e3eSorry, You do not have the correct privileges for that command."));
        return;
      }

      pluginInstance.onReload();
      Utilities.message(player, messageHandler.string("Plugin_Reload", "#f63e3eJoinPrivileges has successfully been reloaded."));
      return;
    }

    pluginInstance.onReload();
    Utilities.logInfo(true, messageHandler.console("Plugin_Reload", "JoinPrivileges has successfully been reloaded."));
  }

  /**
   *
   * The debug message that is sent to either the player or console that executed the command
   *
   * @param sender (The player or console that executed the command)
   */
  private void debugCommand(final CommandSender sender) {
    StringBuilder plugins = new StringBuilder();

    if(sender instanceof Player player) {
      if(!Utilities.checkPermissions(player, true, "joinprivileges.debug", "joinprivileges.admin")) {
        Utilities.message(player, messageHandler.string("No_Permission", "#f63e3eSorry, You do not have the correct privileges for that command."));
        return;
      }

      for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
        plugins.append("#ff4a4a").append(plugin.getName()).append(" ").append(plugin.getDescription().getVersion()).append("#14abc9, ");
      }

      Utilities.message(player,
        "#14abc9===========================================",
        " #6928f7JoinPrivileges #ff4a4av" + pluginInstance.getDescription().getVersion() + " #14abc9By OmegaWeaponDev",
        "#14abc9===========================================",
        " #14abc9Server Brand: #ff4a4a" + Bukkit.getName(),
        " #14abc9Server Version: #ff4a4a" + Bukkit.getServer().getVersion(),
        " #14abc9Online Mode: #ff4a4a" + Bukkit.getOnlineMode(),
        " #14abc9Players Online: #ff4a4a" + Bukkit.getOnlinePlayers().size() + " / " + Bukkit.getMaxPlayers(),
        " #14abc9JoinPrivileges Commands: #ff4a4a" + Utilities.setCommand().size() + " / 6 #14abc9registered",
        " #14abc9Currently Installed Plugins...",
        " " + plugins,
        "#14abc9==========================================="
      );
      return;
    }

    for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
      plugins.append(plugin.getName()).append(" ").append(plugin.getDescription().getVersion()).append(", ");
    }

    Utilities.logInfo(true,
      "===========================================",
      " JoinPrivileges v" + pluginInstance.getDescription().getVersion() + " By OmegaWeaponDev",
      "===========================================",
      " Server Brand: " + Bukkit.getName(),
      " Server Version: " + Bukkit.getServer().getVersion(),
      " Online Mode: " + Bukkit.getOnlineMode(),
      " Players Online: " + Bukkit.getOnlinePlayers().size() + " / " + Bukkit.getMaxPlayers(),
      " JoinPrivileges Commands: " + Utilities.setCommand().size() + " / 6 registered",
      " Currently Installed Plugins...",
      " " + plugins,
      "==========================================="
    );
  }

  /**
   *
   * Handles the tab completion for the core command for JoinPrivileges
   *
   * @param commandSender (The console or player who sent the command)
   * @param command (The command that was used)
   * @param s (The name of the command)
   * @param strings (The args that was used in the command.)
   * @return (The tab completion)
   */
  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
    if(strings.length <= 1) {
      return new TabCompleteBuilder(commandSender)
        .addCommand("version")
        .addCommand("help")
        .checkCommand("debug", true, "joinprivileges.debug", "joinprivileges.admin")
        .checkCommand("reload", true, "joinprivileges.reload", "joinprivileges.admin")
        .build(strings[0]);
    }
    return Collections.emptyList();
  }
}
