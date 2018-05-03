package mods.hinasch.unsaga.plugin;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.unsaga.plugin.hac.UnsagaPluginHAC;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class UnsagaPluginIntegration {


	public UnsagaPluginHAC hac;


	boolean isLoadedHAC = false;
	public boolean isLoadedHAC() {
		return isLoadedHAC;
	}
	/**
	 * WailaなどのInitで登録しないといけないやつ
	 */
	public void checkLoadedModsInit(){
		if(Loader.isModLoaded("Waila")){
			System.out.println("WAILAを発見しました");
			FMLInterModComms.sendMessage("Waila", "register", "mods.hinasch.unsaga.plugin.waila.UnsagaPluginWaila.callbackRegister");
		}
	}
	public void checkLoadedMods(){
		if(HSLib.plugin().isLoadedHAC()){
			System.out.println("HACを発見しました");
			hac = new UnsagaPluginHAC();
			hac.registerBlocks();
			hac.registerEvents();
			this.isLoadedHAC = true;
		}

	}
}
