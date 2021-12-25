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
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MaintenanceCommand extends GlobalCommand implements TabCompleter {
  private final JoinPrivileges pluginInstance;
  private final MessageHandler messageHandler;
  private final StorageManager storageManager;
  private final FileConfiguration configFile;

  public MaintenanceCommand(final JoinPrivileges pluginInstance) {
    this.pluginInstance = pluginInstance;
    storageManager = pluginInstance.getStorageManager();
    messageHandler = pluginInstance.getMessageHandler();
    configFile = storageManager.getConfigFile().getConfig();
  }

  @Override
  protected void execute(CommandSender commandSender, String[] strings) {

  }

  private void maintenanceModeEnable(@NotNull CommandSender commandSender) {
    if (commandSender instanceof final Player player) {
      if (!Utilities.checkPermissions(player, true, "joinprivileges.maintenance.toggle", "joinprivileges.admin")) {
        Utilities.message(player, messageHandler.string("No_Permission", "#f63e3eSorry, You do not have the correct privileges for that command."));
        return;
      }
    }

    configFile.set("Maintenance_Mode.Enabled", true);
    storageManager.getConfigFile().saveConfig();

    Utilities.broadcast(true, messageHandler.string("Maintenance_Mode.Enabled", "#5A47FAMaintenance Mode has been enabled!"));
    for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if (!Utilities.checkPermissions(onlinePlayer, true, "joinprivileges.maintenance.bypass", "joinprivileges.admin")) {
        onlinePlayer.kickPlayer(messageHandler.string("Maintenance_Mode.Player_Kicked", "#FAA647You have been kicked because you don't have permission to be on while maintenance mode is active."));
      }
    }
  }

  private void maintenanceModeDisable(@NotNull CommandSender commandSender) {

  }


  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if(strings.length <= 1) {
      return new TabCompleteBuilder(commandSender)
        .checkCommand("enable", true, "joinprivileges.maintenance.toggle", "joinprivileges.admin")
        .checkCommand("disable", true, "joinprivileges.maintenance.toggle", "joinprivileges.admin")
        .build(strings[0]);
    }
    return Collections.emptyList();
  }
}
