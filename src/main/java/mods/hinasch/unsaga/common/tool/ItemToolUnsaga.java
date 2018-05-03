package mods.hinasch.unsaga.common.tool;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import mods.hinasch.unsaga.ability.IAbilitySelector;
import mods.hinasch.unsaga.material.IUnsagaMaterialSelector;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemToolUnsaga extends ItemTool implements IItemColor,IUnsagaMaterialSelector,IAbilitySelector{

	protected ItemToolUnsaga(float attack,float speed, Set<Block> effectiveBlocksIn,ToolCategory cate) {
		super(attack,speed,ToolMaterial.STONE, effectiveBlocksIn);
		this.component = new ComponentUnsagaWeapon(cate);
		this.component.addPropertyOverrides(this);

	}

	ComponentUnsagaWeapon component;

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		// TODO 自動生成されたメソッド・スタブ
		return this.component.getColorFromItemstack(stack, tintIndex);
	}
	@Override
	public int getMaxAbilitySize() {
		// TODO 自動生成されたメソッド・スタブ
		return 4;
	}

	@Override
	public ComponentUnsagaWeapon getComponent(){
		return this.component;
	}
	/** 攻撃力は基本なし*/
	@Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
		return HashMultimap.<String, AttributeModifier>create();
    }

	@Override
	public int getItemEnchantability(ItemStack is)
	{
		return this.component.getItemEnchantability(is);
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return (int)this.component.getMaxDamage(stack);
	}

	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		this.component.addInformations(stack, tooltip, flagIn);
	}

	@Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
		if(!this.isInCreativeTab(tab)){
			return;
		}
		this.component.getSubItems(this, tab, items);


	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return this.component.getUnlocalizedName(par1ItemStack);
	}
}
