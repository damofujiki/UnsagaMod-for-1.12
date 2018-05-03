package mods.hinasch.unsaga.core.net;

import mods.hinasch.lib.network.ProxyBase;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.core.client.KeyBindingUnsaga;
import mods.hinasch.unsaga.init.UnsagaGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy extends ProxyBase implements IGuiHandler{

	@Override
	public void registerRenderers() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void registerKeyHandlers() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void registerEntityRenderers() {
		// TODO 自動生成されたメソッド・スタブ

	}
	public XYZPos getDebugPos(int bank){
		return null;
	}
	public void setDebugPos(int bank,XYZPos pos){

	}
	public void registerEvents(){

	}
	public KeyBindingUnsaga getKeyBindings(){
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return UnsagaGui.Type.fromMeta(ID).getContainer(world, player,new XYZPos(x,y,z));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return UnsagaGui.Type.fromMeta(ID).getGui(world, player, new XYZPos(x,y,z));
	}
}
