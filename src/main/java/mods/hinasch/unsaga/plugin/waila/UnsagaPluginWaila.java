package mods.hinasch.unsaga.plugin.waila;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class UnsagaPluginWaila implements IWailaPlugin{


	@Override
	public void register(IWailaRegistrar registrar) {
		// TODO 自動生成されたメソッド・スタブ
//		System.out.println("Wailaへ登録します");
		HUDHandlerLP.register(registrar);
	}
}
