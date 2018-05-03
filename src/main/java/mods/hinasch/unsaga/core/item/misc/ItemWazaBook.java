package mods.hinasch.unsaga.core.item.misc;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.ability.AbilityCapability;
import mods.hinasch.unsaga.ability.IAbility;
import mods.hinasch.unsaga.ability.IAbilitySelector;
import mods.hinasch.unsaga.common.tool.ComponentDisplayAbility;
import mods.hinasch.unsaga.common.tool.IComponentDisplayInfo;
import mods.hinasch.unsaga.init.UnsagaItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWazaBook extends Item implements IAbilitySelector{

	public ImmutableList<IComponentDisplayInfo> displayInfoComponents;

	public ItemWazaBook(){
		ImmutableList.Builder<IComponentDisplayInfo> list = ImmutableList.builder();
		list.add(new ComponentDisplayAbility());
		this.displayInfoComponents = list.build();
		this.setMaxStackSize(1);
	}


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
    	this.displayInfoComponents.forEach(in -> in.addInfo(stack, ClientHelper.getPlayer(), tooltip, flagIn.isAdvanced()));
    }
	@Override
	public int getMaxAbilitySize() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}
	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return !AbilityCapability.adapter.getCapability(stack).isAbilityEmpty();
    }
	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent e){
		if(e.getLeft()!=null && e.getRight()!=null){
			if(AbilityCapability.adapter.hasCapability(e.getLeft())){
				if(e.getRight().getItem()==UnsagaItems.WAZA_BOOK && !AbilityCapability.adapter.getCapability(e.getRight()).isAbilityEmpty()){
					ItemStack newStack = e.getLeft().copy();
					IAbility waza = AbilityCapability.adapter.getCapability(e.getRight()).getLearnedAbilities().get(0);
					AbilityCapability.adapter.getCapability(newStack).setAbility(0, waza);
					e.setOutput(newStack);
					e.setCost(5);
				}
			}
		}
	}
}
