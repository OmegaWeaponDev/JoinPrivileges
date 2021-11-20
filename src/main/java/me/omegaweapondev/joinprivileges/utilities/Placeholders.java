package me.omegaweapondev.joinprivileges.utilities;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.omegaweapondev.joinprivileges.JoinPrivileges;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {
  private final JoinPrivileges plugin;

  /**
   * Since we register the expansion inside our own plugin, we
   * can simply use this method here to get an instance of our
   * plugin.
   *
   * @param plugin
   *        The instance of our plugin.
   */
  public Placeholders(JoinPrivileges plugin){
    this.plugin = plugin;
  }

  /**
   * Because this is an internal class,
   * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
   * PlaceholderAPI is reloaded
   *
   * @return true to persist through reloads
   */
  @Override
  public boolean persist(){
    return true;
  }

  /**
   * Because this is a internal class, this check is not needed
   * and we can simply return {@code true}
   *
   * @return Always true since it's an internal class.
   */
  @Override
  public boolean canRegister(){
    return true;
  }

  /**
   * The name of the person who created this expansion should go here.
   * <br>For convienience do we return the author from the plugin.yml
   *
   * @return The name of the author as a String.
   */
  @Override
  public String getAuthor(){
    return plugin.getDescription().getAuthors().toString();
  }

  /**
   * The placeholder identifier should go here.
   * <br>This is what tells PlaceholderAPI to call our onRequest
   * method to obtain a value if a placeholder starts with our
   * identifier.
   * <br>The identifier has to be lowercase and can't contain _ or %
   *
   * @return The identifier in {@code %<identifier>_<value>%} as String.
   */
  @Override
  public String getIdentifier(){
    return "joinprivileges";
  }

  /**
   * This is the version of the expansion.
   * <br>You don't have to use numbers, since it is set as a String.
   *
   * For convienience do we return the version from the plugin.yml
   *
   * @return The version as a String.
   */
  @Override
  public String getVersion(){
    return plugin.getDescription().getVersion();
  }

  /**
   * This is the method called when a placeholder with our identifier
   * is found and needs a value.
   * <br>We specify the value identifier in this method.
   * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
   *
   * @param  player
   *         A {@link org.bukkit.entity.Player Player}.
   * @param  identifier
   *         A String containing the identifier/value.
   *
   * @return possibly-null String of the requested identifier.
   */
  @Override
  public String onPlaceholderRequest(Player player, String identifier){
//    FileConfiguration totalJoinsLog = plugin.getSettingsHandler().getTotalJoinsLog().getConfig();
    
    if(player == null){
      return "";
    }

//    UserDataHandler userDataHandler = new UserDataHandler(plugin, player, player.getUniqueId());
//
//    // %joinprivileges_firstjoin_date%
//    if(identifier.equals("firstjoin_date")){
//      return userDataHandler.getUserData().getString("First_Joined.Date");
//    }
//
//    // %joinprivileges_firstjoin_time%
//    if(identifier.equals("firstjoin_time")){
//      return userDataHandler.getUserData().getString("First_Joined.Time");
//    }
//
//    // %joinprivileges_lastseen_date%
//    if(identifier.equals("lastseen_date")) {
//      return userDataHandler.getUserData().getString("Last_Seen.Date");
//    }
//
//    // %joinprivileges_lastseen_time%
//    if(identifier.equals("lastseen_time")) {
//      return userDataHandler.getUserData().getString("Last_Seen.Time");
//    }
//
//    // %joinprivileges_hasjoineffects%
//    if(identifier.equals("hasjoineffects")) {
//      return String.valueOf(userDataHandler.getUserData().getBoolean("Join_Effects.Enabled"));
//    }
//
//    // joinprivileges_playerjoins_daily
//    if(identifier.equals("playerjoins_daily")) {
//      return String.valueOf(userDataHandler.getUserData().getInt("Daily_Joins"));
//    }
//
//    // joinprivileges_playerjoins_monthly
//    if(identifier.equals("playerjoins_monthly")) {
//      return String.valueOf(userDataHandler.getUserData().getInt("Monthly_Joins"));
//    }
//
//    // joinprivileges_playerjoins_yearly
//    if(identifier.equals("playerjoins_yearly")) {
//      return String.valueOf(userDataHandler.getUserData().getInt("Yearly_Joins"));
//    }
//
//    // joinprivileges_playerjoins_total
//    if(identifier.equals("playerjoins_total")) {
//      return String.valueOf(userDataHandler.getUserData().getInt("Total_Joins"));
//    }
//
//    // joinprivileges_uniquejoins
//    if(identifier.equals("uniquejoins")) {
//      return String.valueOf(totalJoinsLog.getInt("Unique_Player_Joins"));
//    }
//
//    // joinprivileges_topplayerjoins_daily
//    if(identifier.equals("topplayerjoins_daily")) {
//      String topDailyPlayer = " ";
//
//      for(String playerNames : totalJoinsLog.getConfigurationSection("Total_Player_Joins.Daily").getKeys(false)) {
//        if(totalJoinsLog.getInt("Total_Player_Joins.Daily." + playerNames) > totalJoinsLog.getInt("Total_Player_Joins.Daily." + topDailyPlayer)) {
//          topDailyPlayer = playerNames;
//        }
//      }
//
//      return topDailyPlayer + " " + totalJoinsLog.getInt("Total_Player_Joins.Daily." + topDailyPlayer);
//    }
//
//    // joinprivileges_topplayerjoins_monthly
//    if(identifier.equals("topplayerjoins_monthly")) {
//      String topMonthlyPlayer = " ";
//
//      for(String playerNames : totalJoinsLog.getConfigurationSection("Total_Player_Joins.Monthly").getKeys(false)) {
//        if(totalJoinsLog.getInt("Total_Player_Joins.Monthly." + playerNames) > totalJoinsLog.getInt("Total_Player_Joins.Monthly." + topMonthlyPlayer)) {
//          topMonthlyPlayer = playerNames;
//        }
//      }
//
//      return topMonthlyPlayer + " " + totalJoinsLog.getInt("Total_Player_Joins.Monthly." + topMonthlyPlayer);
//    }
//
//    // joinprivileges_topplayerjoins_yearly
//    if(identifier.equals("topplayerjoins_yearly")) {
//      String topYearlyPlayer = " ";
//
//      for(String playerNames : totalJoinsLog.getConfigurationSection("Total_Player_Joins.Yearly").getKeys(false)) {
//        if(totalJoinsLog.getInt("Total_Player_Joins.Yearly." + playerNames) > totalJoinsLog.getInt("Total_Player_Joins.Yearly." + topYearlyPlayer)) {
//          topYearlyPlayer = playerNames;
//        }
//      }
//
//      return topYearlyPlayer + " " + totalJoinsLog.getInt("Total_Player_Joins.Yearly." + topYearlyPlayer);
//    }

    return null;
  }
}
