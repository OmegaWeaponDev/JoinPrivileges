package me.omegaweapondev.joinprivileges.commands;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import me.omegaweapondev.joinprivileges.utilities.MessageHandler;
import me.omegaweapondev.joinprivileges.utilities.UserDataHandler;
import me.ou.library.DateTimeUtils;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeenCommand extends GlobalCommand implements TabCompleter {
  private final JoinPrivileges pluginInstance;
  private final MessageHandler messageHandler;
  private final UserDataHandler userDataHandler;

  public SeenCommand(final JoinPrivileges pluginInstance) {
    this.pluginInstance = pluginInstance;
    messageHandler = pluginInstance.getMessageHandler();
    userDataHandler = pluginInstance.getUserDataHandler();
  }

  @Override
  protected void execute(CommandSender commandSender, String[] strings) {
    if(commandSender instanceof Player) {
      final Player player = (Player) commandSender;
      OfflinePlayer target = null;

      if(!Utilities.checkPermissions(player, true, "joinprivileges.seen", "joinprivileges.admin")) {
        Utilities.message(player, messageHandler.string("No_Permission", "#F63E3ESorry, You do not have the correct privileges for that command."));
        return;
      }

      if(strings.length < 1) {
        Utilities.message(player, messageHandler.string("Invalid_Player", "#F63E3ESorry, that player cannot be found."));
        return;
      }

      for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
        if(offlinePlayer.getName().equals(strings[0])) {
          target = offlinePlayer;
        }
      }

      if(target == null) {
        Utilities.message(player, messageHandler.string("Invalid_Player", "#F63E3ESorry, that player cannot be found."));
        return;
      }

      long useDataLastSeen = (long) userDataHandler.getUserDataMap().get(target.getUniqueId()).get(UserDataHandler.LAST_SEEN);
      LocalDate lastSeenDate = DateTimeUtils.convertLongToLocalDate(useDataLastSeen);
      LocalTime lastSeenTime = DateTimeUtils.convertLongToLocalTime(useDataLastSeen);

      DateTimeUtils lastSeenFormatted = new DateTimeUtils(lastSeenDate, lastSeenTime, "dd/MM/yyyy", "HH:mm:ss");

      Utilities.message(player,
        messageHandler.string("Last_Seen", "#FAA647" + target.getName() + " #5A47FAwas last seen: #FAA647%last_seen%")
          .replace("%last_seen%", lastSeenFormatted.getDateTime())
      );
    }
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
