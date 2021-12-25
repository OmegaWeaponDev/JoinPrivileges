package me.omegaweapondev.joinprivileges.commands;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import me.omegaweapondev.joinprivileges.utilities.MessageHandler;
import me.omegaweapondev.joinprivileges.utilities.StorageManager;
import me.omegaweapondev.joinprivileges.utilities.UserDataHandler;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayTimeCommand extends GlobalCommand implements TabCompleter {
  private final JoinPrivileges pluginInstance;
  private final StorageManager storageManager;
  private final MessageHandler messageHandler;
  private final UserDataHandler userDataHandler;
  private final FileConfiguration configFile;

  public PlayTimeCommand(final JoinPrivileges pluginInstance) {
    this.pluginInstance = pluginInstance;
    storageManager = pluginInstance.getStorageManager();
    messageHandler = pluginInstance.getMessageHandler();
    userDataHandler = pluginInstance.getUserDataHandler();
    configFile = storageManager.getConfigFile().getConfig();
  }

  @Override
  protected void execute(CommandSender commandSender, String[] strings) {
    if(!configFile.getBoolean("PlayTime_Settings.Enabled")) {
      return;
    }

    if (commandSender instanceof final Player player) {
      if(strings.length == 0) {
        if (!Utilities.checkPermissions(player, true, "joinprivileges.playtime.self", "joinprivileges.admin")) {
          Utilities.message(player, messageHandler.string("No_Permission", "#f63e3eSorry, You do not have the correct privileges for that command."));
          return;
        }

        Utilities.message(player,
          messageHandler.string("PlayTime_Messages.PlayTime_Self", "#5A47FAYour current play time is: #FAA647%playTime%")
            .replace("%playTime%", formatPlayTime(calculateNewPlayTime(player)))
        );
        return;
      }

      if(strings.length == 1) {
        if (!Utilities.checkPermissions(player, true, "joinprivileges.playtime.others", "joinprivileges.admin")) {
          Utilities.message(player, messageHandler.string("No_Permission", "#f63e3eSorry, You do not have the correct privileges for that command."));
          return;
        }

        final Player target = Bukkit.getPlayer(strings[0]);
        if(target == null) {
          Utilities.message(player, messageHandler.string("Invalid_Player", "#f63e3eSorry, that player cannot be found."));
          return;
        }

        Utilities.message(player,
          messageHandler.string("PlayTime_Messages.PlayTime_Others", "#5A47FAThe playtime for #FAA647%player% #5A47FAis: #FAA647%playTime%")
            .replace("%player%", target.getName())
            .replace("%playTime%", formatPlayTime(calculateNewPlayTime(target)))
        );
        return;
      }
    }

    if(strings.length != 1) {
      // Add message to tell console to add a player to the command
      return;
    }

    final Player target = Bukkit.getPlayer(strings[0]);
    if(target == null) {
      Utilities.logInfo(true, messageHandler.console("Invalid_Player", "#f63e3eSorry, that player cannot be found."));
      return;
    }

    Utilities.logInfo(true,
      messageHandler.console("PlayTime_Messages.PlayTime_Others", "#5A47FAThe playtime for #FAA647%player% #5A47FAis: #FAA647%playTime%")
        .replace("%player%", target.getName())
        .replace("%playTime%", formatPlayTime(calculateNewPlayTime(target)))
    );
  }

  private long calculateNewPlayTime(@NotNull final Player player) {
    final long PLAYER_FIRST_JOIN = (long) userDataHandler.getUserDataMap().get(player.getUniqueId()).get(UserDataHandler.FIRST_JOINED);

    return System.currentTimeMillis() - PLAYER_FIRST_JOIN;
  }

  private String formatPlayTime(final long currentPlayTime) {
    return String.format("%d days, %d hours, %d minutes, %d seconds",
      currentPlayTime / (24 * 60 * 60 * 1000),
      currentPlayTime / (60 * 60 * 1000) % 24,
      currentPlayTime / (60 * 1000) % 60,
      currentPlayTime / 1000 % 60
    );
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if(strings.length <= 1) {
      List<String> onlinePlayers = new ArrayList<>();

      for(Player player : Bukkit.getOnlinePlayers()) {
        onlinePlayers.add(player.getName());
      }

      return new TabCompleteBuilder(commandSender).addCommand(onlinePlayers).build(strings[0]);
    }
    return Collections.emptyList();
  }
}
