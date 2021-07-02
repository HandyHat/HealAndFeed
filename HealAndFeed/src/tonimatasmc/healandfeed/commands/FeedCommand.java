package tonimatasmc.healandfeed.commands;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tonimatasmc.healandfeed.HealAndFeed;

public class FeedCommand implements CommandExecutor {
    private HealAndFeed plugin;
    HashMap<String, Long> cooldowns = new HashMap();

    public FeedCommand(HealAndFeed plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = this.plugin.nombre;
        Player jugador;
        if (args.length != 1) {
            if (sender instanceof Player) {
                if (sender.hasPermission(this.plugin.getPermissions().getString("Feed.Feedperm"))) {
                    if (cmd.getName().equalsIgnoreCase("feed")) {
                        Player jugadors = (Player) sender;
                        if (jugadors.hasPermission(this.plugin.getPermissions().getString("Feed.Feedcooldownbypassperm"))) {
                            jugadors.setFoodLevel(20);
                            sender.sendMessage(prefix + this.plugin.getMessages().getString("Feed.Feed").replace('&', '§'));
                        }
                        int seconds = this.plugin.getConfig().getInt("feed-cooldown-time");
                        if (this.cooldowns.containsKey(jugadors.getName())) {
                            long secondsLeft = (Long)this.cooldowns.get(jugadors.getName()) / 1000L + (long)seconds - System.currentTimeMillis() / 1000L;
                            if (secondsLeft > 0L) {
                                jugadors.sendMessage(prefix + this.plugin.getMessages().getString("Feed.Feedcooldownmsg").replace('&', '§').replace("%timeleft%", "" + secondsLeft));
                            }
                            this.cooldowns.remove(jugadors.getName());
                            jugadors.setFoodLevel(20);
                            sender.sendMessage(prefix + this.plugin.getMessages().getString("Feed.Feed").replace('&', '§'));
                            this.cooldowns.put(jugadors.getName(), System.currentTimeMillis());
                        } else {
                            this.cooldowns.put(jugadors.getName(), System.currentTimeMillis());
                            jugadors.setFoodLevel(20);
                            sender.sendMessage(prefix + this.plugin.getMessages().getString("Feed.Feed").replace('&', '§'));
                        }
                    }
                } else {
                    sender.sendMessage(prefix + this.plugin.getMessages().getString("Permissions.no-permission-message").replace('&', '§'));
                }
            } else {
                sender.sendMessage(prefix + this.plugin.getMessages().getString("Other.Mustbeaplayer").replace('&', '§'));
            }
        } else if (sender.hasPermission(this.plugin.getPermissions().getString("Feed.Feedpermothers"))) {
            jugador = Bukkit.getPlayer(args[0]);
            if (jugador == null) {
                sender.sendMessage(prefix + this.plugin.getConfig().getString("Other.unknownplayer").replace('&', '§').replace("%target%", args[0]));
            } else {
                jugador.setFoodLevel(20);
                sender.sendMessage(prefix + this.plugin.getMessages().getString("Feed.Feedother").replace('&', '§').replace("%target%", jugador.getName()));
                jugador.sendMessage(prefix + this.plugin.getMessages().getString("Feed.Fedby").replace('&', '§').replace("%player%", sender.getName()));
            }
        } else {
            sender.sendMessage(this.plugin.getConfig().getString("Permissions.no-permission-message").replace('&', '§'));
        }
        return true;
    }
}
