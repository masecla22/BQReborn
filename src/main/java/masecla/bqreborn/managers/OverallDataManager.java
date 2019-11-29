package masecla.bqreborn.managers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import masecla.mlib.main.MLib;

public class OverallDataManager implements Listener {
	private HashMap<UUID, PlayerDataManager> loaded = new HashMap<>();
	private MLib lib;

	public OverallDataManager(MLib lib) {
		super();
		this.lib = lib;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.loaded.put(event.getPlayer().getUniqueId(), new PlayerDataManager(event.getPlayer()));
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		this.loaded.remove(event.getPlayer().getUniqueId());
	}

	public PlayerDataManager getManagerFor(OfflinePlayer p) {
		return this.loaded.get(p.getUniqueId());
	}

	public int getServerBalance() {
		return lib.getConfigurationAPI().getConfig("data").getInt("server-balance", 0);
	}

	public void setServerBalance(int amount) {
		lib.getConfigurationAPI().getConfig("data").set("server-balance", amount);
		lib.getConfigurationAPI().saveConfig("data");
	}

	public void deltaServerBalance(int amount) {
		setServerBalance(getServerBalance() + amount);
	}

	public List<String> getPlayersBanned() {
		return lib.getConfigurationAPI().getConfig("data").getStringList("banned-players");
	}

	public boolean isBanned(OfflinePlayer p) {
		try {
			return getPlayersBanned().contains(p.getUniqueId().toString());
		} catch (Exception e) {
			return false;
		}
	}

	public void banPlayer(OfflinePlayer p) {
		List<String> banned = this.getPlayersBanned();
		banned.add(p.getUniqueId().toString());
		lib.getConfigurationAPI().getConfig("data").set("banned-players", banned);
		lib.getConfigurationAPI().saveConfig("data");
	}

	public void unbanPlayer(OfflinePlayer p) {
		List<String> banned = this.getPlayersBanned();
		banned.remove(p.getUniqueId().toString());
		lib.getConfigurationAPI().getConfig("data").set("banned-players", banned);
		lib.getConfigurationAPI().saveConfig("data");
	}

}
