package masecla.bqreborn.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import masecla.bqreborn.managers.OverallDataManager;
import masecla.mlib.classes.Replaceable;
import masecla.mlib.main.MLib;

public class Ban implements CommandExecutor, Listener {
	private MLib lib;
	private OverallDataManager manager;

	public Ban(MLib lib, OverallDataManager manager) {
		super();
		this.lib = lib;
		this.manager = manager;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!manager.getManagerFor((OfflinePlayer) sender).getMod()) {
			lib.getMessagesAPI().sendMessage("no-permission", sender);
			return true;
		}
		OfflinePlayer p = null;
		try {
			p = Bukkit.getOfflinePlayer(args[0]);
		} catch (Exception e) {

		}
		if (p == null) {
			lib.getMessagesAPI().sendMessage("player-inexistent", sender);
			return true;
		}
		if (p.isOnline()) {
			Player pg = (Player) p;
			pg.kickPlayer("You have been banned from this server!");
		}
		manager.banPlayer(p);
		lib.getMessagesAPI().sendMessage("mod-player-ban", sender, new Replaceable("%player%", p.getName()));
		return true;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		if (this.manager.isBanned(event.getPlayer())) {
			event.getPlayer().kickPlayer("You have been banned from this server!");
		}
	}

}
