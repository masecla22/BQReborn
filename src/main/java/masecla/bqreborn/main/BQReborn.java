package masecla.bqreborn.main;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import masecla.bqreborn.commands.MakeMod;
import masecla.bqreborn.listeners.ChatFilterManipulator;
import masecla.bqreborn.listeners.LevelManipulator;
import masecla.bqreborn.listeners.MobLevelCreator;
import masecla.bqreborn.listeners.VillagerInventoryPatch;
import masecla.bqreborn.managers.OverallDataManager;
import masecla.bqreborn.tasks.ScoreboardApplier;
import masecla.mlib.main.MLib;

public class BQReborn extends JavaPlugin {

	private MLib lib;
	public static File f; // Don't kill me it's the only static ever used.

	@Override
	public void onEnable() {
		this.lib = new MLib(this);

		lib.getConfigurationAPI().requiresConfiguration("data");
		lib.getConfigurationAPI().requiresConfiguration("messages");
		OverallDataManager mng = new OverallDataManager(lib);
		lib.registerListener(mng);
		LevelManipulator manip = new LevelManipulator(mng);
		lib.registerListener(manip);
		f = new File(this.getDataFolder().getPath() + File.separator + "player_data");
		if (!f.exists()) {
			f.mkdirs();
		}

		lib.registerListener(new VillagerInventoryPatch(lib));
		lib.registerListener(new ChatFilterManipulator());
		lib.registerListener(new MobLevelCreator(mng, manip));
		lib.registerListener(new ScoreboardApplier(mng, lib));

		lib.setCommandExecutor("makemod", new MakeMod(lib, mng));
	}

}
