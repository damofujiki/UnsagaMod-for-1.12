package mods.hinasch.unsaga.core.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMinsagaSmith {

	public static class Base extends Slot{

		public Base(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
	    public int getSlotStackLimit()
	    {
	        return 1;
	    }

	    @Override
	    public boolean isItemValid(ItemStack stack)
	    {
	        return stack.getItem().isRepairable();
	    }
	}

	public static class Material extends Slot{

		public Material(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
	    public int getSlotStackLimit()
	    {
	        return 64;
	    }

	    @Override
	    public boolean isItemValid(ItemStack stack)
	    {
	        return true;
	    }
	}

	public static class Forged extends Slot{

		public Forged(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
	    public int getSlotStackLimit()
	    {
	        return 1;
	    }
	}
}
