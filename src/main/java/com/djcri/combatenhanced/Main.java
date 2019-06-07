package com.djcri.combatenhanced;

import com.djcri.combatenhanced.util.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid=Reference.MOD_ID,name=Reference.NAME,version=Reference.VERSION)
public class Main {
	@Instance
	public static Main instance;
	
	@Mod.EventHandler
	public void onInitialization(FMLInitializationEvent event)
	{
		
	}
	
	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new Handler());
	}
}