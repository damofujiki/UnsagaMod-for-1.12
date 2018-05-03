package mods.hinasch.unsaga.core.item.weapon;

import java.util.Set;

import com.google.common.collect.Sets;

import mods.hinasch.lib.iface.IExtendedReach;
import mods.hinasch.unsaga.common.tool.ItemWeaponUnsaga;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.util.LivingHelper;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemSpearUnsaga extends ItemWeaponUnsaga implements IExtendedReach{

	public ItemSpearUnsaga() {
		super(ToolCategory.SPEAR);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public boolean canHarvest(IBlockState blockIn, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public float getBaseAttackDamage() {
		// TODO 自動生成されたメソッド・スタブ
		return 2.0F;
	}

	@Override
	public float getMaxDamageMultiply(){
		return 0.8F;
	}

	@Override
	public double getBaseAttackSpeed() {
		// TODO 自動生成されたメソッド・スタブ
		return -3.2F;
	}

	@Override
	public Set<Block> getEffectiveBlockSet() {
		// TODO 自動生成されたメソッド・スタブ
		return Sets.newHashSet();
	}

	@Override
	public int getHarvestLevelModifier() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getItemDamageOnBlockDestroyed() {
		// TODO 自動生成されたメソッド・スタブ
		return 2;
	}

	@Override
	public int getItemDamageOnHitEntity() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public Set<String> getToolClassStrings() {
		// TODO 自動生成されたメソッド・スタブ
		return Sets.newHashSet();
	}

	@Override
	public String getUnlocalizedCategoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "spear";
	}

	@Override
	public boolean isEffectiveOn(ItemStack stack, IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack is)
	{
		if(!entityLiving.isSwingInProgress && entityLiving instanceof EntityPlayer){
			if(LivingHelper.ADAPTER.hasCapability(entityLiving)){
				if(!LivingHelper.ADAPTER.getCapability(entityLiving).isStateActive(UnsagaPotions.ACTION_PROGRESS)){
					float reach = ((IExtendedReach) is.getItem()).getReach();

					return IExtendedReach.checkSpearLength(is, (EntityPlayer) entityLiving, entityLiving.getEntityWorld());
				}
			}

		}


		return false;
	}


	@Override
	public float getReach() {
		// TODO 自動生成されたメソッド・スタブ
		return 8.0F;
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.SPEAR;
	}

}
