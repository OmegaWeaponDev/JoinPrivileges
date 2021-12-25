package me.omegaweapondev.joinprivileges.events;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import me.omegaweapondev.joinprivileges.utilities.MessageHandler;
import me.omegaweapondev.joinprivileges.utilities.StorageManager;
import me.omegaweapondev.joinprivileges.utilities.UserDataHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinsListener implements Listener {
  private final JoinPrivileges pluginInstance;
  private final UserDataHandler userDataHandler;
  private final StorageManager storageManager;
  private final MessageHandler messageHandler;
  private final FileConfiguration totalJoinsLog;

  public JoinsListener(final JoinPrivileges pluginInstance) {
    this.pluginInstance = pluginInstance;
    storageManager = pluginInstance.getStorageManager();
    userDataHandler = pluginInstance.getUserDataHandler();
    messageHandler = pluginInstance.getMessageHandler();

    totalJoinsLog = storageManager.getTotalJoinsLogFile().getConfig();
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerFirstJoin(PlayerJoinEvent playerJoinEvent) {
    final Player player = playerJoinEvent.getPlayer();
    final int currentUniqueJoins = totalJoinsLog.getInt("Unique_Player_Joins");

    if(player.getFirstPlayed() != System.currentTimeMillis()) {
      return;
    }

    userDataHandler.addUserToMap(player.getUniqueId());
    userDataHandler.setJoinStatus(player.getUniqueId(), UserDataHandler.FIRST_JOINED, System.currentTimeMillis());

    totalJoinsLog.set("Unique_Player_Joins", currentUniqueJoins + 1);
    storageManager.getTotalJoinsLogFile().saveConfig();
  }

}
