package masecla.bqreborn.listeners;

import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import masecla.bqreborn.managers.OverallDataManager;

public class LevelManipulator implements Listener {

	OverallDataManager mng;

	public LevelManipulator(OverallDataManager mng) {
		super();
		this.mng = mng;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent ev) {
		setTotalExperience(ev.getPlayer());
	}

	public void updateXP(OfflinePlayer p) {
		this.setTotalExperience((Player) p);
	}

	public int getExpForLevel(int level) {
		return (int) Math.pow(level, 2) * 256;
	}

	public float getExpProgress(int exp) {
		int level = getLevel(exp);
		int nextlevel = getExpForLevel(level + 1);
		int prevlevel = 0;
		if (level > 0) {
			prevlevel = getExpForLevel(level);
		}
		float progress = ((exp - prevlevel) / (float) (nextlevel - prevlevel));
		return progress;
	}

	public int getLevel(int exp) {
		return (int) Math.floor(Math.sqrt(exp / (float) 256));
	}

	public void setTotalExperience(Player player) {
		int rawxp = this.mng.getManagerFor(player).getRawXP();
		int level = getLevel(rawxp);
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0 + ((double) ((int) (level / 5))) * 0.5);
		float progress = getExpProgress(rawxp);
		player.setLevel(level);
		player.setExp(progress);
	}

}
