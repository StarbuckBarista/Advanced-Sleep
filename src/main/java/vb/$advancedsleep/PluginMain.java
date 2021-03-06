package vb.$advancedsleep;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.java.*;
import org.bukkit.util.*;
import com.gmail.visualbukkit.stdlib.VariableManager;

public class PluginMain extends JavaPlugin implements Listener {

	private static PluginMain instance;
	private static Object localVariableScope = new Object();

	public void onEnable() {
		VariableManager.loadVariables(this);
		instance = this;
		getDataFolder().mkdir();
		getServer().getPluginManager().registerEvents(this, this);
		Object localVariableScope = new Object();
		saveDefaultConfig();
	}

	public void onDisable() {
		VariableManager.saveVariables();
	}

	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] commandArgs) {
		if (command.getName().equalsIgnoreCase("reload")) {
			try {
				Object localVariableScope = new Object();
				PluginMain.getInstance().reloadConfig();
				commandSender.sendMessage(
						PluginMain.color("&f[&e&lAdvanced&6&lSleep&f] >> &aConfiguration File Successfully Reloaded"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static void procedure(String procedure, List<?> procedureArgs) throws Exception {
	}

	public static Object function(String function, List<?> functionArgs) throws Exception {
		return null;
	}

	public static List<Object> createList(Object obj) {
		List<Object> list = new ArrayList<>();
		if (obj.getClass().isArray()) {
			int length = java.lang.reflect.Array.getLength(obj);
			for (int i = 0; i < length; i++) {
				list.add(java.lang.reflect.Array.get(obj, i));
			}
		} else if (obj instanceof Collection<?>) {
			list.addAll((Collection<?>) obj);
		} else {
			list.add(obj);
		}
		return list;
	}

	public static String color(String string) {
		return string != null ? ChatColor.translateAlternateColorCodes('&', string) : null;
	}

	public static void createResourceFile(String path) {
		Path file = getInstance().getDataFolder().toPath().resolve(path);
		if (Files.notExists(file)) {
			try (InputStream inputStream = PluginMain.class.getResourceAsStream("/" + path)) {
				Files.createDirectories(file.getParent());
				Files.copy(inputStream, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static PluginMain getInstance() {
		return instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPluginEnableEvent1(org.bukkit.event.server.PluginEnableEvent event) throws Exception {
		Bukkit.getConsoleSender().sendMessage(
				PluginMain.color(String.valueOf(PluginMain.getInstance().getConfig().get("enable_message"))));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPluginDisableEvent2(org.bukkit.event.server.PluginDisableEvent event) throws Exception {
		Bukkit.getConsoleSender().sendMessage(
				PluginMain.color(String.valueOf(PluginMain.getInstance().getConfig().get("disable_message"))));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBedEnterEvent3(org.bukkit.event.player.PlayerBedEnterEvent event) throws Exception {
		if ((event.getPlayer().hasPermission(String.valueOf(PluginMain.getInstance().getConfig().get("permission")))
				&& ((java.util.List) PluginMain.getInstance().getConfig().get("worlds"))
						.contains(event.getPlayer().getWorld().getName()))) {
			new org.bukkit.scheduler.BukkitRunnable() {
				public void run() {
					try {
						event.getPlayer().getWorld().setTime(((long) 1000d));
						if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("broadcast_sleep"))
								.booleanValue()) {
							VariableManager.setLocalVariable(localVariableScope,
									PluginMain.getInstance().getConfig().get("broadcast_message"), "broadcast_message");
							VariableManager.setLocalVariable(localVariableScope,
									String.valueOf(
											VariableManager.getLocalVariable(localVariableScope, "broadcast_message"))
											.replace("{player}", event.getPlayer().getName()),
									"broadcast_message");
							VariableManager.setLocalVariable(localVariableScope, String
									.valueOf(VariableManager.getLocalVariable(localVariableScope, "broadcast_message"))
									.replace("{world}", event.getPlayer().getWorld().getName()), "broadcast_message");
							for (Object loopValue0 : PluginMain.createList(Bukkit.getOnlinePlayers())) {
								((org.bukkit.command.CommandSender) loopValue0)
										.sendMessage(PluginMain.color(String.valueOf(VariableManager
												.getLocalVariable(localVariableScope, "broadcast_message"))));
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}.runTaskLater(PluginMain.getInstance(),
					((long) (((Number) PluginMain.getInstance().getConfig().get("delay")).doubleValue() * 20d)));
		}
	}
}
