package com.djcri.combatenhanced;

import com.djcri.combatenhanced.util.Reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid=Reference.MOD_ID)
public class CRConfig {
	@Comment("Disables weak attacks.")
	public static boolean waDisabled=true;
	@Comment("Defines the maximum attack cooldown (in terms of percentage of the cooldown bar) below which the attack is considered weak. Values above 100 will prevent you from attacking")
	public static float waPower=1.0f; 
	@Comment("Removes shield delay.")
	public static boolean shieldFix=true;
	@Comment("Swing through grass.")
	public static boolean stgEnabled=true;

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	private static class EventHandler {

		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
