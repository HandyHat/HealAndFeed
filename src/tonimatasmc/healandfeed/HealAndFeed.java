package tonimatasmc.healandfeed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tonimatasmc.healandfeed.commands.FeedCommand;
import tonimatasmc.healandfeed.commands.HealCommand;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HealAndFeed extends JavaPlugin {
    private FileConfiguration messages = null;
    File configFile = new File(this.getDataFolder(), "config.yml");
    private File messagesFile = null;
    private FileConfiguration permissions = null;
    private File permissionsFile = null;
    public String rutaConfig;
    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();
    public String latestversion;
    public String nombre = ChatColor.GOLD + "[" + ChatColor.BLUE + pdffile.getName() + ChatColor.GOLD + "]";

    public static void getPlugin() {
    }

    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "<---------------------------------------->");
        Bukkit.getConsoleSender().sendMessage(nombre + ChatColor.DARK_RED + " The plugin was activated (Version: " + version + ")");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Visit: https://tonimatasmc.com");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "<---------------------------------------->");
        registerEvents();
        registerConfig();
        registerMessages();
        registerPermissions();
        registerCommand();
        updateChecker();
        this.setupConfig();
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "<---------------------------------------->");
        Bukkit.getConsoleSender().sendMessage(nombre + ChatColor.DARK_RED + " The plugin was disabled (Version: " + version + ")");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Visit: https://tonimatasmc.com");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "<---------------------------------------->");
    }

    //--------------------------------------Setup Config---------------------------------------------
    public void setupConfig() {
        if (this.configFile.exists()) {
            LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Permissions.yml has been initialised.");
            LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Messages.yml has been initialised.");
            LoggerMessage.log(LoggerMessage.LogLevel.SUCCESS, "Config.yml has been initialised.");

        }
    }

    //--------------------------------------Register Commands---------------------------------------------
    public void registerCommand() {
        this.getCommand("heal").setExecutor(new HealCommand(this));
        this.getCommand("feed").setExecutor(new FeedCommand(this));
    }

    //--------------------------------------Register Config---------------------------------------------
    public void registerConfig() {
        File config = new File(this.getDataFolder(), "config.yml");
        rutaConfig = config.getPath();
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    //--------------------------------------Register Events---------------------------------------------
    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
    }

    //--------------------------------------Messages File Config----------------------------------------
    public FileConfiguration getMessages(){
        if(messages == null){
            reloadMessages();
        }
        return messages;
    }

    public void reloadMessages(){
        if(messages == null){
            messagesFile = new File(getDataFolder(),"messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        Reader defConfigStream;
        defConfigStream = new InputStreamReader(this.getResource("messages.yml"), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        messages.setDefaults(defConfig);
    }

    public void saveMessages(){
        try{
            messages.save(messagesFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void registerMessages(){
        messagesFile = new File(this.getDataFolder(),"messages.yml");
        if(!messagesFile.exists()){
            this.getMessages().options().copyDefaults(true);
            saveMessages();
        }
    }
    //--------------------------------------Permissions File Config---------------------------------------------
    public FileConfiguration getPermissions() {
        if (permissions == null) {
            reloadPermissions();
        }
        return permissions;
    }

    public void reloadPermissions() {
        if (permissions == null) {
            permissionsFile = new File(getDataFolder(), "permissions.yml");
        }
        permissions = YamlConfiguration.loadConfiguration(permissionsFile);
        Reader defConfigStream;
        defConfigStream = new InputStreamReader(this.getResource("permissions.yml"), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        permissions.setDefaults(defConfig);
    }

    public void savePermissions() {
        try {
            permissions.save(permissionsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerPermissions() {
        permissionsFile = new File(this.getDataFolder(), "permissions.yml");
        if (!permissionsFile.exists()) {
            this.getPermissions().options().copyDefaults(true);
            savePermissions();
        }

    }
//--------------------------------------UpdateChecker---------------------------------------------
    public void updateChecker(){
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=93679").openConnection();
            int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (latestversion.length() <= 7) {
                if(!version.equals(latestversion)){
                    Bukkit.getConsoleSender().sendMessage(nombre + ChatColor.RED +" There is a new version available. "+ChatColor.YELLOW+
                            "("+ChatColor.GRAY+latestversion+ChatColor.YELLOW+")");
                    Bukkit.getConsoleSender().sendMessage(nombre + ChatColor.RED+" You can download it at: "+ChatColor.WHITE+"https://www.spigotmc.org/resources/simple-feed-and-heal-plugin-reworked.93679/");
                }
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(nombre + ChatColor.RED +" Error while checking update.");
        }
    }
}