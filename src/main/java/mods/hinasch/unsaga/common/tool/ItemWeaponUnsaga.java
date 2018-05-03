package mods.hinasch.unsaga.common.tool;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.material.IUnsagaMaterialSelector;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * このmodの武器はこれを継承（斧もめんどくさいのでItemSwordを継承してるので
 * エンチャントは全て武器のものになる）
 * */
public abstract class ItemWeaponUnsaga extends ItemHasSpecialMove implements IUnsagaMaterialSelector,IItemColor{

	public static final ToolMaterial FAILED = EnumHelper.addToolMaterial("failed", 1,60, 1.0F,0.0F, 2);
	ComponentUnsagaWeapon component;
	boolean isFailed = false;

	public ItemWeaponUnsaga(ToolCategory cate) {
		super(ToolMaterial.STONE);
		this.component = new ComponentUnsagaWeapon(cate);
		//		UnsagaMaterials materialsNew = UnsagaMod.core.materialsNew;
		//		testList.addAll(Lists.newArrayList(materialsNew.iron,materialsNew.birch,materialsNew.acacia));


		this.component.addPropertyOverrides(this);
	}

	@Override
	public ComponentUnsagaWeapon getComponent(){
		return this.component;
	}
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		this.component.addInformations(stack, tooltip, flagIn);
	}
	/** 採掘できるかどうか。ブロックに採掘可能ツールが設定されている場合は
	 * このメソッドは無視されて採掘レベルの比較になる（forgeのhook)
	 * ０以下ならこのメソッドは使われる（つまり常に採掘できるもの）*/
	public abstract boolean canHarvest(IBlockState blockIn,ItemStack stack);
	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		return this.canHarvest(state,stack);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		float at = this.component.getAppliedAttackDamage(this.getBaseAttackDamage(), stack);
		if(this.isFailed){
			at = FAILED.getAttackDamage();
		}
		UUID id1 = this.ATTACK_DAMAGE_MODIFIER;
		UUID id2 = this.ATTACK_SPEED_MODIFIER;
		HashMultimap<String, AttributeModifier> map = HashMultimap.<String, AttributeModifier>create();
		return this.component.getItemAttributeModifiers(map, new Tuple(id1,id2), at, this.getBaseAttackSpeed(),stack,slot);

	}

	/** 素材の攻撃力＋ベース攻撃力
	 * ちなみにバニラの剣はベース攻撃力３*/
	public abstract float getBaseAttackDamage();
	/** 値はバニラソースにベタ書きされてるのでそっから持ってくる*/
	public abstract double getBaseAttackSpeed();
	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		// TODO 自動生成されたメソッド・スタブ
		return this.component.getColorFromItemstack(stack, tintIndex);
	}

	/** 破壊しやすいブロックリスト*/
	public abstract Set<Block> getEffectiveBlockSet();

	@Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
    {
		if(this.isFailed){
			return FAILED.getHarvestLevel();
		}
		if (toolClass != null && this.getToolClasses(stack).stream().anyMatch(cls -> toolClass.equals(cls)))
		{
			int hl = UnsagaToolUtil.getMaterial(stack).getToolMaterial().getHarvestLevel() + this.getHarvestLevelModifier();
			hl = MathHelper.clamp(hl, 0, 255);
			return hl;
		}
		else
		{
			return -1;
		}
	}

	/** 棍棒の場合採掘レベルが１下がるので-1
	 * 特に何もなければ０、採掘レベルは-1以下にならない*/
	public abstract int getHarvestLevelModifier();

	/** 棍棒だと２*/
	public abstract int getItemDamageOnBlockDestroyed();

	/** 基本的に１*/
	public abstract int getItemDamageOnHitEntity();

	@Override
	public int getItemEnchantability(ItemStack is)
	{
		return this.component.getItemEnchantability(is);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		if(this.hasSpecialMoveType(stack, InvokeType.CHARGE)){
			return EnumAction.BOW;
		}
		return EnumAction.NONE;
	}
	@Override
	public int getMaxAbilitySize(){
		return 4;
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return (int)((float)this.component.getMaxDamage(stack)*this.getMaxDamageMultiply());
	}

	/** MaxDamage * これ*/
	public float getMaxDamageMultiply(){
		return 1.0F;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		return !this.isEffectiveOn(stack, state)? this.getStrVsBlockByTool(stack, state) : UnsagaToolUtil.getEfficiencyOnProperMaterial(stack);
	}

	protected float getStrVsBlockByTool(ItemStack stack, IBlockState state)
	{
		for (String type : getToolClasses(stack))
		{
			if (state.getBlock().isToolEffective(type, state))
				return UnsagaToolUtil.getEfficiencyOnProperMaterial(stack);
		}
		return this.getEffectiveBlockSet().contains(state.getBlock()) ? UnsagaToolUtil.getEfficiencyOnProperMaterial(stack) : 1.0F;
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
	public Set<String> getToolClasses(ItemStack stack)
	{
		return this.getToolClassStrings();
	}
	/** このツールの持つクラス*/
	public abstract Set<String> getToolClassStrings();

	/** unlocalized name用。剣="sword"*/
	public abstract String getUnlocalizedCategoryName();
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return this.component.getUnlocalizedName(par1ItemStack);
	}


	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(this.getItemDamageOnHitEntity(), attacker);
		return true;
	}

	//	@Override
	//	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	//	{
	//		return this.component.initCapabilities(stack, nbt);
	//	}
	/** 破壊しやすいブロックか（このメソッド）→ツールクラスの合うブロックか
	 * →破壊しやすいブロックセットに含まれているか
	 * の順番。全部falseなら１．０F*/
	public abstract boolean isEffectiveOn(ItemStack stack, IBlockState state);
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if ((double)state.getBlockHardness(worldIn, pos) != 0.0D)
		{
			stack.damageItem(this.getItemDamageOnBlockDestroyed(), entityLiving);
		}

		return true;
	}

	@Override
    public boolean onEntitySwing(EntityLivingBase playerIn, ItemStack itemStackIn)
    {
		World worldIn = playerIn.getEntityWorld();

        return false;
    }

}
