package mods.hinasch.unsaga.core.client.gui;

import mods.hinasch.lib.config.ConfigGuiSettings;
import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;


public class GuiConfigUnsaga extends GuiConfig{

	public GuiConfigUnsaga(GuiScreen parentScreen) {
		super(parentScreen, ConfigGuiSettings.getConfigElements(UnsagaMod.configFile, Configuration.CATEGORY_GENERAL), UnsagaMod.MODID, "config.unsaga", false,
				false, "Unsaga Mod Configuration", ConfigGuiSettings.getTitleLine2(UnsagaMod.configFile));
		// TODO 自動生成されたコンストラクター・スタブ
	}

}
