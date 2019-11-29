package masecla.bqreborn.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import masecla.bqreborn.main.BQReborn;

public class PlayerDataManager {
	private OfflinePlayer p;
	private YamlConfiguration config;
	private File f;

	private void initalizeFile() {
		this.config = new YamlConfiguration();
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			this.config.load(f);
		} catch (Exception e) {
		}
	}

	public PlayerDataManager(OfflinePlayer p) {
		super();
		this.p = p;
		this.f = new File(BQReborn.f.getPath() + File.separator + p.getUniqueId().toString() + ".yml");
		this.initalizeFile();
	}

	public int getRawXP() {
		return config.getInt("raw_xp", 0);
	}

	public OfflinePlayer getPlayer() {
		return p;
	}

	public void incrementXP(int amount) {
		this.setRawXP(this.getRawXP() + amount);
	}

	public void setRawXP(int xp) {
		config.set("raw_xp", xp);
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMod(boolean ismod) {
		config.set("ismod", ismod);
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean getMod() {
		return config.getBoolean("ismod", false);
	}

	public int getBalance() {
		return config.getInt("balance", 0);
	}

	public void incrementBalance(int amount) {
		this.setBalance(this.getBalance() + amount);
	}

	public void setBalance(int balance) {
		config.set("balance", balance);
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
