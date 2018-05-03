package mods.hinasch.unsaga.core.item;

import mods.hinasch.lib.item.SimpleCreativeTab;

public class UnsagaCreativeTabs {

	public static final SimpleCreativeTab TOOLS;
	public static final SimpleCreativeTab MISC;
	public static final SimpleCreativeTab PANEL_GROWTH ;

	static{
		TOOLS = SimpleCreativeTab.createSimpleTab("unsaga.tools");
		MISC = SimpleCreativeTab.createSimpleTab("unsaga.misc");
		PANEL_GROWTH = SimpleCreativeTab.createSimpleTab("unsaga.skill_panels");
	}
}
