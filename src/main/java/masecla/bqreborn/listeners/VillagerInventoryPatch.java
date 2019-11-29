package masecla.bqreborn.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

public class VillagerInventoryPatch implements Listener {
	private MLib lib;

	public VillagerInventoryPatch(MLib lib) {
		super();
		this.lib = lib;
	}

	@EventHandler
	public void onClick(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked().getType().equals(EntityType.VILLAGER)) {
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cComing soon!"));
			event.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(lib.getPlugin(), () -> {
				event.getPlayer().closeInventory();
			}, 1);
		}
	}

	@EventHandler
	public void onTrade(InventoryClickEvent evnt) {
		if (evnt.getInventory().getType().equals(InventoryType.MERCHANT))
			evnt.setCancelled(true);
	}

}
