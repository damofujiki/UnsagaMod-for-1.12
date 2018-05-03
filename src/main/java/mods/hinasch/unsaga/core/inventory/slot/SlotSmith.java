package mods.hinasch.unsaga.core.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotSmith{


	public static class SlotMaterial extends Slot{

		public SlotMaterial(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
	    public int getSlotStackLimit()
	    {
	        return 2;
	    }
	}
	public static class SlotPayment extends Slot{

		public SlotPayment(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
	    public int getSlotStackLimit()
	    {
	        return 64;
	    }
	}
}
