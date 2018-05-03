package mods.hinasch.unsaga.core.item.weapon;

import java.util.Set;

import com.google.common.collect.Sets;

import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.unsaga.common.tool.ItemWeaponUnsaga;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;


/** 剣より軽くて弱い。強度は剣より２０％低い
 * ハサミと同じ効果も持つ（攻撃時）*/
public class ItemKnifeUnsaga extends ItemWeaponUnsaga{

	public ItemKnifeUnsaga() {
		super(ToolCategory.KNIFE);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public boolean canHarvest(IBlockState blockIn, ItemStack stack) {
		return Items.STONE_SWORD.canHarvestBlock(blockIn, stack);
	}



	@Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		if(entity instanceof IShearable && player.isSneaking() && entity instanceof EntityLivingBase){
			boolean flag = Items.SHEARS.itemInteractionForEntity(stack, player, (EntityLivingBase) entity, EnumHand.MAIN_HAND);
			if(flag){
				stack.damageItem(1, player);
				return true;
			}
		}
        return false;
    }

	@Override
	public float getBaseAttackDamage() {
		// TODO 自動生成されたメソッド・スタブ
		return 2.0F;
	}

	@Override
	public double getBaseAttackSpeed() {
		// TODO 自動生成されたメソッド・スタブ
		return -1.4D;
	}

	@Override
	public float getMaxDamageMultiply(){
		return 0.8F;
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
		return Sets.newHashSet("sword");
	}

	@Override
	public String getUnlocalizedCategoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "knife";
	}


	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack stack = player.getHeldItem(hand);
		//サトウキビ収穫機能
		IBlockState state = worldIn.getBlockState(pos);
		if(state.getBlock()==Blocks.REEDS){
			BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos(pos);
			int len =0;
			while(worldIn.getBlockState(p).getBlock()==Blocks.REEDS){
				p.setPos(p.getX(), p.getY()-1, p.getZ());
			}
			SoundAndSFX.playBlockBreakSFX(worldIn, p.up(2), state,true);
			stack.damageItem(1, player);
			return EnumActionResult.SUCCESS;
		}

		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	@Override
	public boolean isEffectiveOn(ItemStack stack, IBlockState state) {
		return Items.STONE_SWORD.getDestroySpeed(stack, state)>1.0F;
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.KNIFE;
	}


}
