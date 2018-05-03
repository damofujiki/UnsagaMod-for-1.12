package mods.hinasch.unsaga.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;

public class InventoryEquipment extends InventoryBasic{


	protected final EntityPlayer thePlayer;

//	protected ItemStack[] skillsButton;

	public InventoryEquipment(EntityPlayer ep){
		super("equip",false,3);
		this.thePlayer = ep;
	}


	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ

		return entityplayer.openContainer != entityplayer.inventoryContainer;
	}

	@Override
	public void closeInventory(EntityPlayer entityplayer) {


		if(AccessorySlotCapability.adapter.hasCapability(thePlayer)){
			AccessorySlotCapability.adapter.getCapability(thePlayer).getEquippedList().set(0,this.getStackInSlot(0));
			AccessorySlotCapability.adapter.getCapability(thePlayer).getEquippedList().set(1,this.getStackInSlot(1));
//			AccessoryHelper.getCapability(thePlayer).setTablet(this.getStackInSlot(2));
		}

	}


	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return "unsaga.inventory.equipment";
	}




}
