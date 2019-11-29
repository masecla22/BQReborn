package masecla.bqreborn.listeners;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import masecla.bqreborn.managers.OverallDataManager;
import net.md_5.bungee.api.ChatColor;

public class MobLevelCreator implements Listener {
	private EntityType[] mobs = { EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER };

	private HashMap<Integer, UUID> lastDamager = new HashMap<>();

	private OverallDataManager manager;
	private LevelManipulator manip;

	public MobLevelCreator(OverallDataManager manager, LevelManipulator manip) {
		super();
		this.manager = manager;
		this.manip = manip;
	}

	@EventHandler
	public void onSpawn(EntitySpawnEvent ev) {
		if (isManipulable(ev.getEntityType())) {
			Location location = ev.getLocation();
			double levelThreshHold = Math.sqrt((location.getX() + location.getZ()) / 2) / 2;
			int level = ThreadLocalRandom.current().nextInt(1, (int) (levelThreshHold + 2));
			int hp = level * 20;
			Entity ent = ev.getEntity();
			ent.setCustomName(createName(ev.getEntityType(), level, hp, hp));
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent ev) {
		if (!isManipulable(ev.getEntityType()))
			return;
		double dmg = ev.getDamage();
		ev.setDamage(0);
		int currentHealth = getHP(ev.getEntity().getCustomName());
		String name = ev.getEntity().getCustomName();
		if (currentHealth - dmg <= 0) {
			OfflinePlayer killer = null;
			try {
				if (((LivingEntity) ev.getEntity()).getKiller() != null) {
					killer = ((LivingEntity) ev.getEntity()).getKiller();
				} else
					killer = Bukkit.getOfflinePlayer(this.lastDamager.get(ev.getEntity().getEntityId()));
			} catch (Exception e) {

			}
			((LivingEntity) ev.getEntity()).setHealth(0);
			if (killer != null) {
				int level = getLevel(ev.getEntity().getCustomName()) + 1;
				int xp = 0;
				xp = (int) Math.pow(ThreadLocalRandom.current().nextInt(1, level * 4), 1.3);
				this.manager.getManagerFor(killer).incrementXP(xp);
				this.manip.updateXP(killer);
				int loot = ThreadLocalRandom.current().nextInt(1, level * 5);
				if (loot <= manager.getServerBalance() && ThreadLocalRandom.current().nextInt(1000) < 100) {
					manager.deltaServerBalance(-loot);
					manager.getManagerFor(killer).incrementBalance(loot);
					Player pl = (Player) killer;
					pl.sendMessage(
							ChatColor.translateAlternateColorCodes('&', "&aYou got &a&l" + loot + " &acoins of loot!"));
				}
			}
		} else {
			ev.getEntity().setCustomName(createName(ev.getEntityType(), this.getLevel(name),
					(int) (currentHealth - dmg), this.getMaxHP(name)));
		}

	}

	@EventHandler
	public void lastDamager(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType().equals(EntityType.PLAYER)) {
			Player p = (Player) event.getDamager();
			this.lastDamager.put(event.getEntity().getEntityId(), p.getUniqueId());
		} else if (this.isManipulable(event.getDamager().getType())
				&& event.getEntityType().equals(EntityType.PLAYER)) {
			event.setDamage(event.getDamage()
					* ThreadLocalRandom.current().nextInt(1, getLevel(event.getDamager().getCustomName())) + 2);
		}
	}

	private boolean isManipulable(EntityType type) {
		for (EntityType t : mobs)
			if (type.equals(t))
				return true;
		return false;
	}

	private String createName(EntityType type, int level, int hp, int maxHP) {
		return ChatColor.translateAlternateColorCodes('&', "&8[Lvl " + level + "] &e" + type.toString().charAt(0)
				+ type.toString().toLowerCase().substring(1) + "&a " + hp + "/" + maxHP);
	}

	private int getHP(String name) {
		name = ChatColor.stripColor(name);
		name = name.split(" ")[name.split(" ").length - 1];
		return Integer.parseInt(name.split("\\/")[0]);
	}

	public int getLevel(String name) {
		return Integer.parseInt(name.split("Lvl ")[1].split("\\]")[0]);
	}

	private int getMaxHP(String name) {
		name = ChatColor.stripColor(name);
		name = name.split(" ")[name.split(" ").length - 1];
		return Integer.parseInt(name.split("\\/")[1]);
	}

}
/*
 * 
 * sqrt(3000000)/2
 * 
 */