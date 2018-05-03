package mods.hinasch.unsaga.core.inventory;

import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class InventorySmithMinsaga extends InventoryBasic{

	public static final int INV_BASE = 0;
	public static final int INV_MATERIAL = 1;
	public static final int INV_FORGED = 2;
	public InventorySmithMinsaga() {
		super("",false,3);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return "unsaga.smithMinsaga.inventory";
	}
	public ItemStack getBaseItem(){
		return this.getStackInSlot(INV_BASE);
	}

	public void consumerItems(){
		this.decrStackSize(INV_BASE, 1);
		this.decrStackSize(INV_MATERIAL, 1);
	}
	public void setForged(ItemStack stack){
		this.setInventorySlotContents(INV_FORGED, stack);
	}
	public ItemStack getMaterial(){
		return this.getStackInSlot(INV_MATERIAL);
	}

	public ItemStack getForged(){
		return this.getStackInSlot(INV_FORGED);
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 64;
	}


	@Override
	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ

		return !WorldHelper.findNear(entityplayer.getEntityWorld(), XYZPos.createFrom(entityplayer), 6, 3, (w,pos,scan)->
		scan.getBlock()==Blocks.ANVIL).isEmpty();
	}
}
