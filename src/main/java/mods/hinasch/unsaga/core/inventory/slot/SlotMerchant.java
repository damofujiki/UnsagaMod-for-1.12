package mods.hinasch.unsaga.core.inventory.slot;

import mods.hinasch.unsaga.core.inventory.InventoryMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMerchant{



	public static class MerchantSell extends Slot{
		public boolean slotLocked = true;
		public boolean isInit = true;
//		public BarteringProcessor info;
		public InventoryMerchant invMerhcant;
		public int SlotID;
		public MerchantSell(IInventory par1iInventory, int par2, int par3,
				int par4) {
			super(par1iInventory, par2, par3, par4);
			this.invMerhcant = (InventoryMerchant)par1iInventory;
			this.SlotID = par2;
//			if(this.invMerhcant.getMerchandise(par2-10)!=null){
//				this.info = new BarteringProcessor(this.invMerhcant.getMerchandise(par2-10));
//			}
			// TODO 自動生成されたコンストラクター・スタブ
		}
//		@Override
//	    public void putStack(ItemStack par1ItemStack)
//	    {
	//
//
//	    }
	//
//		@Override
//	    public ItemStack decrStackSize(int par1)
//	    {
//			if(canBuy(this.invMerhcant.getMerchandise(this.SlotID))){
//				super.decrStackSize(1);
//			}
//	    	return null;
//	    }
	//
//		public boolean canBuy(ItemStack is){
//			if(is==null)return false;
//			MerchandiseInfo info = new MerchandiseInfo(is);
//			int priceBuy = info.getBuyPriceTag();
//			int priceSell = this.invMerhcant.getCurrentPriceToSell();
//			if(priceBuy<=priceSell){
//				return true;
//			}
//			return false;
//		}

		@Override
	    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
	    {
			return false;
//	    	if(this.info==null)return false;
//	    	int priceBuy = info.getBuyPriceTag();
//	    	int priceSell = this.invMerhcant.getCurrentPriceToSell();
//	    	if(priceSell>=priceBuy){
//	    		this.slotLocked = false;
//	    	}
//	    	Unsaga.debug(this.slotLocked);
//	        return slotLocked ? false : true;
	    }
	}
	public static class MerchantSecret extends MerchantSell{

		protected boolean isSecret;

		public MerchantSecret(IInventory par1iInventory, int par2, int par3,
				int par4) {
			super(par1iInventory, par2, par3, par4);
			// TODO 自動生成されたコンストラクター・スタブ
			this.isSecret = true;
		}

		@Override
	    public ItemStack getStack()
	    {
	        if(this.isSecret){
	        	return null;
	        }
	        return super.getStack();
	    }

		public void setSecret(boolean par1){
			this.isSecret = par1;
		}
	}
	public static class PlayerSell extends Slot{

		public PlayerSell(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_,
				int p_i1824_4_) {
			super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
			// TODO 自動生成されたコンストラクター・スタブ
		}

	}

	public static class Result extends Slot{

		public Result(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_,
				int p_i1824_4_) {
			super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
			// TODO 自動生成されたコンストラクター・スタブ
		}

	}
}
