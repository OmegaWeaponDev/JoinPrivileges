package me.omegaweapondev.joinprivileges.commands;

import me.omegaweapondev.joinprivileges.JoinPrivileges;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CoreCommand extends GlobalCommand implements TabCompleter {


  public CoreCommand(final JoinPrivileges plugin) {

  }

  @Override
  protected void execute(final CommandSender sender, final String[] strings) {

  }


  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
    return Collections.emptyList();
  }

}
