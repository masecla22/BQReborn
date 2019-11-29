package masecla.bqreborn.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;

public class ChatFilterManipulator implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player p = event.getPlayer();
		if (event.getMessage().startsWith("!")) {
			String finalMessage = "&9";
			finalMessage += p.getLevel() + " &e";
			finalMessage += p.getName() + " &r" + event.getMessage().substring(1);
			finalMessage = ChatColor.translateAlternateColorCodes('&', finalMessage);
			for (Player h : Bukkit.getOnlinePlayers())
				h.sendMessage(finalMessage);
		} else {
			String finalMessage = "&9&lLocal>&e ";
			finalMessage += p.getName() + " &e" + event.getMessage().substring(1);
			boolean b = false;
			finalMessage = ChatColor.translateAlternateColorCodes('&', finalMessage);
			for (Entity e : p.getNearbyEntities(100, 100, 100)) {
				if (e.getType().equals(EntityType.PLAYER)) {
					Player g = (Player) e;
					g.sendMessage(finalMessage);
					b = true;
				}
			}
			if (b) {
				p.sendMessage(finalMessage);
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&9&lLocal> &cNobody is within earshot! Try shouting"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&9&lLocal> &cShout by placing a ! before messages"));
			}
		}
	}

}
