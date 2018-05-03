package mods.hinasch.unsaga.core.item.wearable;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.iface.ICustomModel;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.IAbilitySelector;
import mods.hinasch.unsaga.common.tool.ComponentUnsagaWeapon;
import mods.hinasch.unsaga.core.client.model.ModelArmorColored;
import mods.hinasch.unsaga.core.item.wearable.MaterialArmorTextureSetting.RenderSize;
import mods.hinasch.unsaga.material.IUnsagaMaterialSelector;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorUnsaga extends ItemArmor implements IItemColor,IAbilitySelector,IUnsagaMaterialSelector,ICustomModel{

	private static final int[] MAX_DAMAGE_ARRAY = new int[] {13, 15, 16, 11};
	protected static final String PATH = UnsagaMod.MODID+":textures/models/armor/";
	protected static ArmorTexture TEXTURE_DEFAULT = new ArmorTexture("armor", "armor2");
	public static final ArmorMaterial FAILED = EnumHelper.addArmorMaterial("failed", "", 2, new int[]{1,1,1,1}, 1, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	private static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
	final ComponentUnsagaWeapon component;
	final ToolCategory category;
//	String categoryName;
//	boolean isFailed = false;
	final public ModelArmorColored modelArmor = new ModelArmorColored(1.0F);
	final public ModelArmorColored modelArmor2 = new ModelArmorColored(0.65F);

	public ItemArmorUnsaga(ToolCategory category) {
		super(ArmorMaterial.IRON, 0, category.getEquipmentSlot());
		this.component = new ComponentUnsagaWeapon(category);
		this.component.addPropertyOverrides(this);
		this.category = category;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default)
	{
		ModelArmorColored model = modelArmor;
		if(UnsagaMaterialCapability.adapter.hasCapability(itemStack)){
			ArmorTexture textures = UnsagaMaterialCapability.adapter.getCapability(itemStack).getMaterial().getSpecialArmorTexture(getCategory());
			if(textures!=null){
				if(textures.getRenderSize()==RenderSize.THIN){
					model = modelArmor2;
				}

			}
		}
		model.setStack(itemStack);

//		modelArmor.setParent(this.prepareArmorModel(_default,entityLiving, itemStack, armorSlot));
//		modelArmor2.setStack(itemStack);
//		modelArmor2.setParent(_default);

		return this.prepareArmorModel(model, entityLiving, itemStack, armorSlot);
	}
	//	@Override
	//	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	//	{
	//		return this.component.initCapabilities(stack, nbt);
	//	}

	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		this.component.addInformations(stack, tooltip, flagIn);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();

		double at = this.component.isFailed(stack) ? FAILED.getDamageReductionAmount(equipmentSlot) : this.getDamageReductionAmount(stack);
		double tough = this.component.isFailed(stack) ? FAILED.getToughness() : this.getToughness(stack);
		if (equipmentSlot == this.armorType)
		{
			multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double)at, 0));
			multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double)tough, 0));
		}

		return multimap;
	}

	public int getDamageReductionAmount(ItemStack is){
		return this.component.getDamageReductionAmount(is, armorType);
	}

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		return this.component.getColorFromItemstack(stack, tintIndex);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
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
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
		if(!this.isInCreativeTab(tab)){
			return;
		}
		this.component.getSubItems(this, tab, items);


	}

	@Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity)
    {
		//TODO:ここコンフィグで全身鎧OKにするとどこでも装備できるように
        return net.minecraft.entity.EntityLiving.getSlotForItemStack(stack) == armorType;
    }

	public float getToughness(ItemStack is){
		return this.component.getToughness(is);
	}

//	@Override
//	public ActionResult<ItemStack> onItemRightClick(ItemStack armorStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
//	{
//		InventoryHandler inv = new InventoryHandler(playerIn.inventory);
//
//		EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(armorStackIn);
//		if(playerIn.isSneaking()){
//			if(inv.getFirstEmptyArmorSlot().isPresent()){
//				entityequipmentslot = inv.getFirstEmptyArmorSlot().get().getEquipmentSlot();
//			}
//		}
//		ItemStack itemstack = playerIn.getItemStackFromSlot(entityequipmentslot);
//
//		if (itemstack != null)
//		{
//			playerIn.setItemStackToSlot(entityequipmentslot, armorStackIn.copy());
//			ItemUtil.setStackSize(itemstack,0);
//			return new ActionResult(EnumActionResult.SUCCESS, armorStackIn);
//		}
//		else
//		{
//			return new ActionResult(EnumActionResult.FAIL, armorStackIn);
//		}
//	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return this.component.getUnlocalizedName(par1ItemStack);
	}

	private ArmorTexture getTexturePair(UnsagaMaterial mate,ToolCategory unsagatype){
		if(mate!=null && mate.getSpecialArmorTexture(unsagatype)!=null){
			return mate.getSpecialArmorTexture(unsagatype);

		}
		return TEXTURE_DEFAULT;
	}
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		String suffix = ".png";//type!= null ? "_overlay.png" : ".png";
		ItemArmorUnsaga armorunsaga = (ItemArmorUnsaga)stack.getItem();
		final UnsagaMaterial mate = UnsagaMaterialCapability.adapter.hasCapability(stack) ? UnsagaMaterialCapability.adapter.getCapability(stack).getMaterial() : null;

		final ArmorTexture pairTexture = this.getTexturePair(mate, this.getCategory());




		if(this.getCategory()==ToolCategory.HELMET || this.getCategory()==ToolCategory.BOOTS || this.getCategory()==ToolCategory.ARMOR)
		{
			return PATH + pairTexture.first() + suffix;
		}
		if(this.getCategory()==ToolCategory.LEGGINS)
		{
			return PATH +pairTexture.second() + suffix;
		}
		UnsagaMod.logger.trace("Unknown ArmorType???");
		return pairTexture.first() + suffix;
	}
	@Override
	public int getMaxAbilitySize() {
		// TODO 自動生成されたメソッド・スタブ
		return 4;
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return this.category;
	}

	public static class ArmorTexture extends Pair<String,String>{



		RenderSize size = RenderSize.NORMAL;
		protected ArmorTexture(String first, String second) {
			super(first, second);
			// TODO 自動生成されたコンストラクター・スタブ
		}


		public String upper(){
			return this.first();
		}


		public String lower(){
			return this.second();
		}

		public RenderSize getRenderSize(){
			return this.size;
		}

		public ArmorTexture setRenderSize(RenderSize size){
			this.size = size;
			return this;
		}
	}

	@Override
	public ComponentUnsagaWeapon getComponent() {
		// TODO 自動生成されたメソッド・スタブ
		return this.component;
	}
}
