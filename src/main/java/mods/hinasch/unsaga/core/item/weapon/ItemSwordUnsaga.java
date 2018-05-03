package mods.hinasch.unsaga.core.item.weapon;

import java.util.Set;

import com.google.common.collect.Sets;

import mods.hinasch.unsaga.common.tool.ItemWeaponUnsaga;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/** ツルハシと武器のハイブリッドな感じ*/
public class ItemSwordUnsaga extends ItemWeaponUnsaga{

	public ItemSwordUnsaga() {
		super(ToolCategory.SWORD);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public float getBaseAttackDamage() {
		// TODO 自動生成されたメソッド・スタブ
		return 3.0F;
	}

	@Override
	public double getBaseAttackSpeed() {
		// TODO 自動生成されたメソッド・スタブ
		return -2.4000000953674316D;
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
	public Set<String> getToolClassStrings() {
		// TODO 自動生成されたメソッド・スタブ
		return Sets.newHashSet();
	}

	@Override
	public String getUnlocalizedCategoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "sword";
	}

	@Override
	public boolean isEffectiveOn(ItemStack stack, IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return Items.STONE_SWORD.getDestroySpeed(stack, state)>1.0F;
	}

	@Override
	public int getItemDamageOnHitEntity() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public int getItemDamageOnBlockDestroyed() {
		// TODO 自動生成されたメソッド・スタブ
		return 2;
	}

	@Override
	public boolean canHarvest(IBlockState blockIn, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		return Items.STONE_SWORD.canHarvestBlock(blockIn, stack);
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.SWORD;
	}


}
