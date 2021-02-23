package me.omegaweapondev.joinprivileges.utilities;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinLeaveUtil {
  private final JoinPrivileges plugin;
  private final Player player;

  private SettingsHandler settingsHandler;

  public JoinLeaveUtil(final JoinPrivileges plugin, final Player player) {
    this.plugin = plugin;
    this.player = player;

    settingsHandler = new SettingsHandler(plugin);
  }

  public void playerMessage(final String key) {
    if(player.getFirstPlayed() == System.currentTimeMillis()) {
      Utilities.broadcast(firstJoinMessage());
      return;
    }

    Utilities.broadcast(message(key));
  }

  public void commandExecutor(final List<String> commands) {
    if(commands.size() < 1) {
      return;
    }

    for(String command : commands) {
      Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("%player%", player.getName()));
    }
  }

  private String message(final String key) {
    String message = settingsHandler.getConfigFile().getConfig().getString(key);

    if(message.contains("%player%")) {
      message = message.replace("%player%", player.getName());
    }

    if(message.contains("%displayname%")) {
      message = message.replace("%displayname%", player.getDisplayName());
    }

    if(message.contains("%prefix%")) {
      message = message.replace("%prefix%", plugin.getChat().getPlayerPrefix(player));
    }

    if(message.contains("%suffix%")) {
      message = message.replace("%suffix%", plugin.getChat().getPlayerSuffix(player));
    }

    if(message.contains("%maxPlayers%")) {
      message = message.replace("%maxPlayers%", String.valueOf(Bukkit.getServer().getOfflinePlayers().length));
    }

    if(message.contains("%playerCount%")) {
      message = message.replace("%playerCount%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
    }

    return message;
  }

  private String firstJoinMessage() {
    return message("First_Join_Settings.Format");
  }

  public String defaultJoinMessage() {
    return message("Join_Settings.Default_Format");
  }

  public String defaultQuitMessage() {
    return message("Quit_Settings.Default_Format");
  }
}
