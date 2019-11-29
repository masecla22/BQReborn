package masecla.bqreborn.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import masecla.bqreborn.managers.OverallDataManager;
import masecla.mlib.main.MLib;

public class MakeMod implements CommandExecutor {
	private MLib lib;
	private OverallDataManager manager;

	public MakeMod(MLib lib, OverallDataManager manager) {
		super();
		this.lib = lib;
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isOp()) {
			lib.getMessagesAPI().sendMessage("no-permission", sender);
			return true;
		}
		Player p = null;
		try {
			p = Bukkit.getPlayer(args[0]);
		} catch (Exception e) {

		}
		if (p == null) {
			lib.getMessagesAPI().sendMessage("player-inexistent", sender);
			return true;
		}
		boolean makes = true;
		if (args.length != 1 && args[1].equalsIgnoreCase("false"))
			makes = false;

		if (makes && manager.getManagerFor(p).getMod()) {
			lib.getMessagesAPI().sendMessage("player-already-mod", sender);
			return true;
		}
		if (!makes && !manager.getManagerFor(p).getMod()) {
			lib.getMessagesAPI().sendMessage("player-not-a-mod", sender);
			return true;
		}
		manager.getManagerFor(p).setMod(makes);
		if (makes) {
			lib.getMessagesAPI().sendMessage("player-modded", sender);
			lib.getMessagesAPI().sendMessage("player-self-modded", p);
		} else {
			lib.getMessagesAPI().sendMessage("player-demodded", sender);
			lib.getMessagesAPI().sendMessage("player-self-demodded", p);
		}
		return true;
	}

}
