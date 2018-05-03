package mods.hinasch.unsaga.core.item.weapon;

import java.util.Set;

import com.google.common.collect.Sets;

import mods.hinasch.unsaga.common.tool.ItemWeaponUnsaga;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ItemAxeUnsaga extends ItemWeaponUnsaga{

//	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE});

	private final Set<Block> blocks;

	public ItemAxeUnsaga() {
		super(ToolCategory.AXE);
	    blocks = ReflectionHelper.getPrivateValue(ItemAxe.class, null, 0);
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
		return 3.5F;
	}

	@Override
	public double getBaseAttackSpeed() {
		// TODO 自動生成されたメソッド・スタブ
		return -3.05D;
	}

	@Override
	public Set<Block> getEffectiveBlockSet() {
		// TODO 自動生成されたメソッド・スタブ
		return blocks;
	}

	@Override
	public int getHarvestLevelModifier() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getItemDamageOnBlockDestroyed() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public int getItemDamageOnHitEntity() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public Set<String> getToolClassStrings() {
		// TODO 自動生成されたメソッド・スタブ
		return Sets.newHashSet("axe");
	}

	@Override
	public String getUnlocalizedCategoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "axe";
	}

	@Override
	public boolean isEffectiveOn(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? false : true;
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.AXE;
	}




}
