package me.omegaweapondev.joinprivileges.utilities;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * The UserDataHandler class that handles all the user data for the plugin
 *
 * @author OmegaWeaponDev
 */
public class UserDataHandler {
  private final JoinPrivileges pluginInstance;
  private final FileConfiguration userDataFile;
  private final Map<UUID, Map<String, Object>> userDataMap = new ConcurrentHashMap<>();

  public static final String FIRST_JOINED = "First_Joined";
  public static final String LAST_SEEN = "Last_Seen";
  public static final String PLAY_TIME = "Play_Time";

  public static final String DAILY_REWARD_CLAIMED = "Daily_Reward_Claimed";
  public static final String MONTHLY_REWARD_CLAIMED = "Monthly_Reward_Claimed";
  public static final String YEARLY_REWARD_CLAIMED = "Yearly_Reward_Claimed";

  /**
   *
   * The public constructor for the User Data Handler class
   *
   * @param pluginInstance (The plugin's instance)
   */
  public UserDataHandler(final JoinPrivileges pluginInstance) {
    this.pluginInstance = pluginInstance;
    userDataFile = pluginInstance.getStorageManager().getUserDataFile().getConfig();
  }

  /**
   *
   * Grabs all the entries from the userData.yml file and
   * adds the players into the map
   *
   */
  public void populateUserDataMap() {
    Bukkit.getScheduler().runTaskAsynchronously(pluginInstance, () -> {
      if(userDataFile.getConfigurationSection("Users.").getKeys(false).isEmpty()) {
        return;
      }

      getUserDataMap().clear();
      for(String user : userDataFile.getConfigurationSection("Users.").getKeys(false)) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(user));

        if(player.isOnline()) {
          getUserDataMap().put(UUID.fromString(user), new ConcurrentHashMap<>());
        }
      }
    });
  }

  /**
   *
   * Adds a specific user's data from the userData.yml and populates their map entry
   *
   */
  public void addUserToMap(@NotNull final UUID playerUUID) {
    getUserDataMap().putIfAbsent(playerUUID, new ConcurrentHashMap<>());

    setJoinStatus(playerUUID, FIRST_JOINED, userDataFile.getLong("Users." + playerUUID + "." + FIRST_JOINED, System.currentTimeMillis()));
    setJoinStatus(playerUUID, LAST_SEEN, userDataFile.getLong("Users." + playerUUID + "." + LAST_SEEN, 0));
    setJoinStatus(playerUUID, PLAY_TIME, userDataFile.getLong("Users." + playerUUID + "." + PLAY_TIME, 0));

    setJoinStatus(playerUUID, DAILY_REWARD_CLAIMED, userDataFile.getBoolean("Users." + playerUUID + "." + DAILY_REWARD_CLAIMED, false));
    setJoinStatus(playerUUID, MONTHLY_REWARD_CLAIMED, userDataFile.getBoolean("Users." + playerUUID + "." + MONTHLY_REWARD_CLAIMED, false));
    setJoinStatus(playerUUID, YEARLY_REWARD_CLAIMED, userDataFile.getBoolean("Users." + playerUUID + "." + YEARLY_REWARD_CLAIMED, false));
  }

  /**
   *
   * Saves all the current entries from the user data map into the userData.yml
   *
   */
  public void saveUserDataToFile() {
    for (UUID userUUID : getUserDataMap().keySet()) {
      userDataFile.set("Users." + userUUID + "." + FIRST_JOINED, getUserDataMap().get(userUUID).getOrDefault(FIRST_JOINED, Bukkit.getOfflinePlayer(userUUID).getFirstPlayed()));
      userDataFile.set("Users." + userUUID + "." + LAST_SEEN, getUserDataMap().get(userUUID).getOrDefault(LAST_SEEN, 0));
      userDataFile.set("Users." + userUUID + "." + PLAY_TIME, getUserDataMap().get(userUUID).getOrDefault(PLAY_TIME, 0));

      userDataFile.set("Users." + userUUID + "." + DAILY_REWARD_CLAIMED, getUserDataMap().get(userUUID).getOrDefault(DAILY_REWARD_CLAIMED, false));
      userDataFile.set("Users." + userUUID + "." + MONTHLY_REWARD_CLAIMED, getUserDataMap().get(userUUID).getOrDefault(MONTHLY_REWARD_CLAIMED, false));
      userDataFile.set("Users." + userUUID + "." + YEARLY_REWARD_CLAIMED, getUserDataMap().get(userUUID).getOrDefault(YEARLY_REWARD_CLAIMED, false));
    }
    pluginInstance.getStorageManager().getUserDataFile().saveConfig();
    userDataMap.clear();
  }

  /**
   *
   * Saves a specific players entry in the user data map into the userData.yml
   *
   * @param playerUUID (The player that has their user data saved)
   */
  public void saveUserDataToFile(@NotNull final UUID playerUUID) {
    Bukkit.getScheduler().runTaskAsynchronously(pluginInstance, () -> {
      userDataFile.set("Users." + playerUUID + "." + FIRST_JOINED, getUserDataMap().get(playerUUID).getOrDefault(FIRST_JOINED, Bukkit.getOfflinePlayer(playerUUID).getFirstPlayed()));
      userDataFile.set("Users." + playerUUID + "." + LAST_SEEN, getUserDataMap().get(playerUUID).getOrDefault(LAST_SEEN, 0));
      userDataFile.set("Users." + playerUUID + "." + PLAY_TIME, getUserDataMap().get(playerUUID).getOrDefault(PLAY_TIME, 0));

      userDataFile.set("Users." + playerUUID + "." + DAILY_REWARD_CLAIMED, getUserDataMap().get(playerUUID).getOrDefault(DAILY_REWARD_CLAIMED, false));
      userDataFile.set("Users." + playerUUID + "." + MONTHLY_REWARD_CLAIMED, getUserDataMap().get(playerUUID).getOrDefault(MONTHLY_REWARD_CLAIMED, false));
      userDataFile.set("Users." + playerUUID + "." + YEARLY_REWARD_CLAIMED, getUserDataMap().get(playerUUID).getOrDefault(YEARLY_REWARD_CLAIMED, false));
      pluginInstance.getStorageManager().getUserDataFile().saveConfig();
      userDataMap.remove(playerUUID);
    });
  }

  /**
   *
   * Sets the player's stats in the map
   *
   * @param uuid (The player whose entry in the map is to be modified)
   * @param stat (The status effect to modify the value for)
   * @param value (The status effect value)
   */
  public void setJoinStatus(@NotNull UUID uuid, String stat, Object value) {
    getUserDataMap().get(uuid).put(stat, value);
  }

  /**
   *
   * Gets a specific player's stats
   *
   * @param uuid (The player whose data needs to be retrieved from the user data map)
   * @return (The value of the stat)
   */
  public Object getJoinStatus(@NotNull UUID uuid, String stat) {
    return getUserDataMap().get(uuid).get(stat);
  }

  /**
   *
   * Getter for the userDataMap
   *
   * @return userDataMap
   */
  public Map<UUID, Map<String, Object>> getUserDataMap() {
    return userDataMap;
  }
}
