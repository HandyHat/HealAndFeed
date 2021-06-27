package tonimatasmc.healandfeed.commands;

import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import tonimatasmc.healandfeed.HealAndFeed;

public class HealCommand implements CommandExecutor {
    private HealAndFeed plugin;
    HashMap<String, Long> cooldowns = new HashMap<>();

    public HealCommand(HealAndFeed plugin){
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = this.plugin.nombre;
        Player p;
        PotionEffect effect;
        Iterator<PotionEffect> var8;
        if (args.length != 1) {
            if (sender instanceof Player) {
                p = (Player)sender;
                if (p.hasPermission(this.plugin.getPermissions().getString("Heal.Healperm"))) {
                    if (cmd.getName().equalsIgnoreCase("heal")) {
                        if (p.hasPermission(this.plugin.getPermissions().getString("Heal.Healcooldownbypassperm"))) {
                            var8 = p.getActivePotionEffects().iterator();

                            while(var8.hasNext()) {
                                effect = var8.next();
                                p.removePotionEffect(effect.getType());
                            }

                            p.setHealth(20.0D);
                            p.setFoodLevel(20);
                            p.setFireTicks(0);
                            sender.sendMessage(prefix + this.plugin.getMessages().getString("Heal.Heal").replace('&', '§'));
                        } else {
                            int seconds = this.plugin.getConfig().getInt("heal-cooldown-time");
                            if (this.cooldowns.containsKey(p.getName())) {
                                long secondsLeft = this.cooldowns.get(p.getName()) / 1000L + (long)seconds - System.currentTimeMillis() / 1000L;
                                if (secondsLeft > 0L) {
                                    p.sendMessage(prefix + this.plugin.getMessages().getString("Heal.Healcooldownmsg").replace('&', '§').replace("%timeleft%", "" + secondsLeft));
                                }

                                this.cooldowns.remove(p.getName());

                                for (PotionEffect effectss : p.getActivePotionEffects()) {
                                    p.removePotionEffect(effectss.getType());
                                }

                            } else {
                                this.cooldowns.put(p.getName(), System.currentTimeMillis());

                                for (PotionEffect effects : p.getActivePotionEffects()) {
                                    p.removePotionEffect(effects.getType());
                                }

                            }
                            p.setHealth(20.0D);
                            p.setFoodLevel(20);
                            p.setFireTicks(0);
                            sender.sendMessage(prefix + this.plugin.getMessages().getString("Heal.Heal").replace('&', '§'));
                            this.cooldowns.put(p.getName(), System.currentTimeMillis());
                        }
                    }
                } else {
                    sender.sendMessage(prefix + this.plugin.getMessages().getString("Permissions.no-permission-message").replace('&', '§'));
                }
            } else {
                sender.sendMessage(prefix + this.plugin.getMessages().getString("Other.Mustbeaplayer").replace('&', '§'));
            }
        } else if (sender.hasPermission(this.plugin.getPermissions().getString("Heal.Healpermothers"))) {
            p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                sender.sendMessage(prefix + this.plugin.getMessages().getString("Other.unknownplayer").replace('&', '§').replace("%target%", args[0]));
            } else {
                p.setHealth(20.0D);
                p.setFoodLevel(20);
                p.setFireTicks(0);
                var8 = p.getActivePotionEffects().iterator();

                while(var8.hasNext()) {
                    effect = var8.next();
                    p.removePotionEffect(effect.getType());
                }

                sender.sendMessage(prefix + this.plugin.getMessages().getString("Heal.Healother").replace('&', '§').replace("%target%", p.getName()));
                p.sendMessage(prefix + this.plugin.getMessages().getString("Heal.Healby").replace('&', '§').replace("%player%", sender.getName()));
            }
        } else {
            sender.sendMessage(prefix + this.plugin.getMessages().getString("Permissions.no-permission-message").replace('&', '§'));
        }
        return true;
    }
}
