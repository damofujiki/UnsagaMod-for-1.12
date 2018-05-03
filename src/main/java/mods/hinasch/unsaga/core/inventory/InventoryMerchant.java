/**
 *
 */
package mods.hinasch.unsaga.core.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;

/**
 *
 */
public class InventoryMerchant extends InventoryBasic{

	EntityPlayer customer;
	IMerchant merchant;
	public InventoryMerchant(int size,EntityPlayer ep,IMerchant merchant) {
		super("bartering",false,size);
		this.customer = ep;
		this.merchant = merchant;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ

		return this.merchant!=null && this.merchant.getCustomer() == entityplayer;
	}
}
