package mods.hinasch.unsaga.core.inventory.slot;


import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.unsaga.core.item.misc.skillpanel.ItemSkillPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSkillPanel extends Slot{

	protected boolean isPlayerPanel;
	public SlotSkillPanel(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
		this.isPlayerPanel = false;
		// TODO 自動生成されたコンストラクター・スタブ
	}


	public Slot setPlayerPanel(boolean par1){
		this.isPlayerPanel = par1;
		return this;
	}

	public boolean isPlayerPanel(){
		return this.isPlayerPanel;
	}
	@Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return ItemUtil.isItemStackPresent(par1ItemStack)&&(par1ItemStack.getItem() instanceof ItemSkillPanel);
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return true;
    }
}
