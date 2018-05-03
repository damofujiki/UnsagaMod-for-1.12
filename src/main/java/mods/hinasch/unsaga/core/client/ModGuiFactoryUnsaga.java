package mods.hinasch.unsaga.core.client;

import java.util.Set;

import mods.hinasch.unsaga.core.client.gui.GuiConfigUnsaga;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;


public class ModGuiFactoryUnsaga implements IModGuiFactory{

	@Override
	public void initialize(Minecraft minecraftInstance) {
		// TODO 自動生成されたメソッド・スタブ

	}



	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}



	@Override
	public boolean hasConfigGui() {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		// TODO 自動生成されたメソッド・スタブ
		return new GuiConfigUnsaga(parentScreen);
	}

}
