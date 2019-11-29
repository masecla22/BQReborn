package masecla.bqreborn.tasks;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import masecla.bqreborn.managers.OverallDataManager;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

public class ScoreboardApplier implements Listener {

	private OverallDataManager manager;
	private MLib lib;
	private ScoreboardManager mng;

	private HashMap<UUID, Scoreboard> hsm = new HashMap<>();

	public ScoreboardApplier(OverallDataManager manager, MLib lib) {
		super();
		this.manager = manager;
		this.lib = lib;
		this.mng = Bukkit.getScoreboardManager();
		this.startTask();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		updateScoreboard(event.getPlayer().getUniqueId());
	}

	private void updateScoreboard(UUID uuid) {
		Scoreboard brd = this.hsm.getOrDefault(uuid, mng.getNewScoreboard());
		Bukkit.getPlayer(uuid).setScoreboard(brd);
		this.hsm.put(uuid, brd);
		Objective obj = null;
		if ((obj = brd.getObjective("balance")) == null)
			obj = brd.registerNewObjective("balance", "dummy");
		obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lCoin&7&lQuest"));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.getScore(ChatColor.translateAlternateColorCodes('&', "&aBalance:"))
				.setScore(manager.getManagerFor(Bukkit.getOfflinePlayer(uuid)).getBalance());
	}

	private void startTask() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(lib.getPlugin(), () -> {
			for (Player p : Bukkit.getOnlinePlayers())
				updateScoreboard(p.getUniqueId());
		}, 0, 5);
	}

}
