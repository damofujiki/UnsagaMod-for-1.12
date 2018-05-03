package mods.hinasch.unsaga.core.client.gui;

import mods.hinasch.lib.client.GuiContainerBase;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.inventory.container.ContainerToolCustomizeMinsaga;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiToolCustomizeMinsaga extends GuiContainerBase{

	public static final int ID_FORGE = 3;
	public GuiToolCustomizeMinsaga(World world,EntityPlayer ep) {
		super(new ContainerToolCustomizeMinsaga(ep));
		// TODO 自動生成されたコンストラクター・スタブ
	}
	@Override
	public String getGuiName(){
		return "Tool Custom";
	}
	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;

//		UnsagaMod.packetDispatcher.sendToServer(PacketSyncGui.create(Type.SMITH_PROFESSIONALITY	,null));
		// ボタンを追加
		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
		this.addButton(ID_FORGE, i + (18*5)+2, j + 16 +(18*2), 30, 19 , "Forge!");

	}
	@Override
	public String getGuiTextureName(){
		return UnsagaMod.MODID + ":textures/gui/container/smith2.png";
	}
}
