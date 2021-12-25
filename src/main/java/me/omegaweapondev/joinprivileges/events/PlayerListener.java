package me.omegaweapondev.joinprivileges.events;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import me.omegaweapondev.joinprivileges.utilities.MessageHandler;
import me.omegaweapondev.joinprivileges.utilities.StorageManager;
import me.omegaweapondev.joinprivileges.utilities.UserDataHandler;
import me.ou.library.Utilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
  private final JoinPrivileges pluginInstance;
  private final UserDataHandler userDataHandler;
  private final StorageManager storageManager;
  private final MessageHandler messageHandler;
  private final FileConfiguration configFile;

  public PlayerListener(final JoinPrivileges pluginInstance) {
    this.pluginInstance = pluginInstance;
    userDataHandler = pluginInstance.getUserDataHandler();
    storageManager = pluginInstance.getStorageManager();
    messageHandler = pluginInstance.getMessageHandler();
    configFile = storageManager.getConfigFile().getConfig();
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
    final Player player = playerQuitEvent.getPlayer();

    userDataHandler.saveUserDataToFile(player.getUniqueId());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onMaintenanceEnable(PlayerLoginEvent playerLoginEvent) {
    final Player player = playerLoginEvent.getPlayer();

    if(!configFile.getBoolean("Maintenance_Mode.Enabled")) {
      playerLoginEvent.allow();
      return;
    }

    if (!Utilities.checkPermissions(player, true, "joinprivileges.maintenance.bypass", "joinprivileges.admin")) {
      playerLoginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER, messageHandler.string("Maintenance_Mode.Player_Kicked", "#FAA647You have been kicked because you don't have permission to be on while maintenance mode is active."));
    }
  }

}
