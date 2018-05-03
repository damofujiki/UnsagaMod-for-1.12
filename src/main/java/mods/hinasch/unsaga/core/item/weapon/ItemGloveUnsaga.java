package mods.hinasch.unsaga.core.item.weapon;

import java.util.Set;

import com.google.common.collect.Sets;

import mods.hinasch.unsaga.common.tool.ItemWeaponUnsaga;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ItemGloveUnsaga extends ItemWeaponUnsaga{

	public ItemGloveUnsaga() {
		super(ToolCategory.GLOVES);
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
	}

	@Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

	@Override
	public int getMaxAbilitySize() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
    public float getAttackDamage()
    {
        return 0;
    }
	@Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        return 1.0F;
    }

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.GLOVES;
	}

	@Override
	public boolean canHarvest(IBlockState blockIn, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public float getBaseAttackDamage() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public double getBaseAttackSpeed() {
		// TODO 自動生成されたメソッド・スタブ
		return -1.3D;
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
		return 0;
	}

	@Override
	public int getItemDamageOnHitEntity() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public Set<String> getToolClassStrings() {
		// TODO 自動生成されたメソッド・スタブ
		return Sets.newHashSet("glove");
	}

	@Override
	public String getUnlocalizedCategoryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "glove";
	}

	@Override
	public boolean isEffectiveOn(ItemStack stack, IBlockState state) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public static void onLivingAttack(LivingHurtEvent e){
		if(e.getSource().getTrueSource() instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer) e.getSource().getTrueSource();
			ItemStack held = ep.getHeldItemMainhand();
			if(!held.isEmpty() && held.getItem() instanceof ItemGloveUnsaga){

				if(SkillPanelAPI.hasPanel(ep, SkillPanels.PUNCH)){
					float damage = 4.0F + (0.5F*SkillPanelAPI.getHighestPanelLevel(ep, SkillPanels.PUNCH).getAsInt());

					e.setAmount(damage);
				}else{
					e.setAmount(1.0F);
				}
			}
		}

	}
}
