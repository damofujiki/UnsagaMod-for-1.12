package mods.hinasch.unsaga.common.tool;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.unsaga.ability.AbilityAPI;
import mods.hinasch.unsaga.ability.AbilityCapability;
import mods.hinasch.unsaga.core.client.event.UnsagaTooltips;
import mods.hinasch.unsaga.core.item.weapon.ItemShieldUnsaga;
import mods.hinasch.unsaga.init.UnsagaItemRegistry;
import mods.hinasch.unsaga.material.IUnsagaForgeableTool;
import mods.hinasch.unsaga.material.SuitableLists;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.material.UnsagaMaterialRegistry;
import mods.hinasch.unsaga.material.UnsagaMaterials;
import mods.hinasch.unsaga.material.UnsagaWeightType;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.EnumHelper;

public class ComponentUnsagaWeapon {

	public class PropertyGetter implements IItemPropertyGetter{

		String sub;
		public PropertyGetter(String sub){

			this.sub = sub;
		}
		@Override
		public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {

			if(UnsagaMaterialCapability.adapter.hasCapability(stack)){
				UnsagaMaterial mat = UnsagaMaterialCapability.adapter.getCapability(stack).getMaterial();
				if(mat.getSubIconGetter().isPresent()){
					if(mat.getSubIconGetter().get().equals(this.sub)){
						return 1.0F;
					}
				}
			}
			return 0;
		}

	}
	private static final int[] MAX_DAMAGE_ARRAY = new int[] {13, 15, 16, 11};
	/** 適合しない素材のツールマテリアル*/
	public static final ToolMaterial FAILED = EnumHelper.addToolMaterial("failed", 1,60, 1.0F,0.0F, 2);

	public static final ArmorMaterial FAILED_ARMOR = EnumHelper.addArmorMaterial("failed","", 1, new int[]{1, 1, 1, 1}, 1, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	public static final UUID WEIGHT_EFFECTED_SPEED = UUID.fromString("a46c987d-cb19-4f36-bb57-25098ce49450");
	/**これを境に軽重が分岐する*/
	public static final int THRESHOLD_WEIGHT = 5;
	public static final double SPEED_MODIFIER = 20.0D;

	public static double getSpeedModifierFromWeight(int weight){
		double w = (double)weight;
		if(w<0){
			w = 0.001D;
		}
		return  -weight/SPEED_MODIFIER;
	}

	public static void refleshWeightModifier(ItemStack newstack,int weight){
    		double mod = ComponentUnsagaWeapon.getSpeedModifierFromWeight(weight);
    		newstack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).put(SharedMonsterAttributes.ATTACK_SPEED.getName()
    				, new AttributeModifier(ComponentUnsagaWeapon.WEIGHT_EFFECTED_SPEED, "Weapon modifier", mod, 0));
      }
	public List<IComponentDisplayInfo> displayInfoComponents = Lists.newArrayList();
	/** 重量の表示*/
	public final IComponentDisplayInfo displayMaterialWeight = new ComponentDisplayInfo(3,(is,ep,list,sw)-> !is.isEmpty() && UnsagaMaterialCapability.adapter.hasCapability(is)){


		@Override
		public void addInfo(ItemStack is, EntityPlayer ep, List dispList, boolean par4) {
			UnsagaMaterial m = UnsagaMaterialCapability.adapter.getCapability(is).getMaterial();
			int weight = UnsagaMaterialCapability.adapter.getCapability(is).getWeight();
			UnsagaWeightType weightType = UnsagaWeightType.fromWeight(weight);
			String str = HSLibs.translateKey("tooltip.unsaga.material")+":"+HSLibs.translateKey("material."+m.getPropertyName());
			dispList.add(str);

			String weightString = HSLibs.translateKey("word.weight") + ":" + weight;
			weightString += "(" + weightType.getName() + ")";

			if(weightString!=null){
				dispList.add(weightString);
			}
		}

		//		public String getDisplayMessage(ItemStack input,int weight) {
		//			if(!HSLib.configHandler.isDebug()){
		//				return weight>THRESHOLD_WEIGHT ? "Heavy" : "Light";
		//			}
		//			return null;
		//		}

		public Tuple<UnsagaMaterial,Integer> getMaterialFromCapability(ItemStack is) {
			if(UnsagaMaterialCapability.adapter.hasCapability(is)){
				return new  Tuple(UnsagaMaterialCapability.adapter.getCapability(is).getMaterial(),UnsagaMaterialCapability.adapter.getCapability(is).getWeight());
			}
			return new Tuple(UnsagaMaterials.STONE,1);

		}
	};

	public final IComponentDisplayInfo abilityDisplay = new ComponentDisplayAbility();

	/** 引き出す事の出来るアビリティを表示（クリエイティブオンリー）*/
	public final IComponentDisplayInfo attachableAbilityDisplay = new ComponentDisplayInfo(5,(is,ep,list,sw)->!is.isEmpty() && ep!=null && ep.isCreative() &&AbilityCapability.adapter.hasCapability(is)){

		@Override
		public void addInfo(ItemStack is, EntityPlayer ep, List dispList, boolean par4) {
			UnsagaMaterial m = UnsagaMaterialCapability.adapter.getCapability(is).getMaterial();
			String abilityNames  = AbilityAPI.getAbilityTableString(is);

			dispList.add(UnsagaTextFormatting.PROPERTY_LOCKED+HSLibs.translateKey("tooltip.unsaga.ability.learnable")+":"+abilityNames);

		}

	};
	/** デバッグ用途*/
	public final IComponentDisplayInfo debugDisplay = new ComponentDisplayInfo(6,(is,ep,list,sw)->!is.isEmpty() && HSLib.configHandler.isDebug() &&UnsagaMaterialCapability.adapter.hasCapability(is)){

		@Override
		public void addInfo(ItemStack is, EntityPlayer ep, List dispList, boolean par4) {
			UnsagaMaterial m = UnsagaMaterialCapability.adapter.getCapability(is).getMaterial();
			dispList.add("Tool Material:"+m.getToolMaterial().name());
			dispList.add("Armor Material:"+m.getArmorMaterial());
			float multiply = 1.0F;
			if(is.getItem() instanceof ItemWeaponUnsaga){
				multiply = ((ItemWeaponUnsaga)is.getItem()).getMaxDamageMultiply();
			}
			dispList.add("Max Uses:"+getMaxDamage(is) * multiply);
		}
	};


	/** 盾の能力*/
	public final IComponentDisplayInfo shieldDisplay = new ComponentDisplayInfo(1,(is,ep,list,sw)->!is.isEmpty()&& is.getItem() instanceof ItemShieldUnsaga &&UnsagaMaterialCapability.adapter.hasCapability(is)){

		@Override
		public void addInfo(ItemStack is, EntityPlayer ep, List dispList, boolean par4) {
			UnsagaMaterial m = UnsagaMaterialCapability.adapter.getCapability(is).getMaterial();
			dispList.add(UnsagaTextFormatting.POSITIVE+"Blocking Power +"+String.valueOf(m.getShieldPower())+"%");
		}
	};

	ToolCategory toolCategory;

	public ComponentUnsagaWeapon(ToolCategory cate){
		this.toolCategory = cate;

		displayInfoComponents.add(displayMaterialWeight);
		displayInfoComponents.add(debugDisplay);
		displayInfoComponents.add(abilityDisplay);
		displayInfoComponents.add(attachableAbilityDisplay);
		displayInfoComponents.add(shieldDisplay);
	}

	public void addDisplayInfoComponents(IComponentDisplayInfo... components){
		this.displayInfoComponents.addAll(Lists.newArrayList(components));
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List infoList, ITooltipFlag par4) {
		//    	String lang = Minecraft.getMinecraft().gameSettings.language;

//		if(!this.displayInfoComponents.isEmpty()){
//			for(IComponentDisplayInfo component:this.displayInfoComponents){
//				if(component.predicate(par1ItemStack, par2EntityPlayer, infoList, par4.isAdvanced())){
//					component.addInfo(par1ItemStack, par2EntityPlayer, infoList, par4.isAdvanced());
//				}
//
//			}
//		}
	}
	public void addInformations(ItemStack par1ItemStack, List<String> infoList, ITooltipFlag par4,UnsagaTooltips.Type... types) {
		//    	String lang = Minecraft.getMinecraft().gameSettings.language;

//		if(!this.displayInfoComponents.isEmpty()){
//			for(IComponentDisplayInfo component:this.displayInfoComponents){
//				if(component.predicate(par1ItemStack, par2EntityPlayer, infoList, par4.isAdvanced())){
//					component.addInfo(par1ItemStack, par2EntityPlayer, infoList, par4.isAdvanced());
//				}
//
//			}
//		}
		List<UnsagaTooltips.Type> list = Lists.newArrayList(UnsagaTooltips.Type.DEBUG,UnsagaTooltips.Type.ABILITY_TO_LEARN,UnsagaTooltips.Type.ABILITY,UnsagaTooltips.Type.MATERIAL_AND_WEIGHT);
		if(types!=null)list.addAll(Lists.newArrayList(types));
		UnsagaTooltips.addTooltips(par1ItemStack, infoList, par4,list);
	}
	public void addPropertyOverrides(Item item){
		for(UnsagaMaterial in :UnsagaMaterialRegistry.instance().getProperties()){
			item.addPropertyOverride(in.getKey(), in.getPropertyGetter());
		}
		//		for(UnsagaMaterials.Category category:UnsagaMod.core.materialsNew.categories){
		//			item.addPropertyOverride(new ResourceLocation(category.getName()),category.getPropertyGetter());
		//		}
		for(String name:UnsagaMaterialRegistry.instance().getSubIconGetterNames()){
			item.addPropertyOverride(new ResourceLocation(name),new PropertyGetter(name));
		}
		item.addPropertyOverride(new ResourceLocation("failed"), new IItemPropertyGetter(){

			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if(UnsagaMaterialCapability.adapter.hasCapability(stack)){
					if(!SuitableLists.getSuitables(toolCategory).contains(UnsagaMaterialCapability.adapter.getCapability(stack).getMaterial())){
						return 1.0F;
					}
				}
				return 0.0F;
			}}
				);
	}

	public float getAppliedAttackDamage(float base,ItemStack is){
		if(UnsagaMaterialCapability.adapter.hasCapability(is)){
			UnsagaMaterial m = UnsagaMaterialCapability.adapter.getCapability(is).getMaterial();
			return base + m.getToolMaterial().getAttackDamage();
		}
		return base;
	}



	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		if(tintIndex==0){
			return UnsagaToolUtil.getMaterial(stack).getMaterialColor();
		}
		return Statics.COLOR_NONE;
	}

	public int getDamageReductionAmount(ItemStack is,EntityEquipmentSlot armorType){
		if(this.isFailed(is)){
			return FAILED_ARMOR.getDamageReductionAmount(armorType);
		}
		return UnsagaToolUtil.getMaterial(is).getArmorMaterial().getDamageReductionAmount(armorType);
	}

	public Multimap<String, AttributeModifier> getItemAttributeModifiers(Multimap<String, AttributeModifier> multimap,Tuple<UUID,UUID> uuids,float attackdamage,double basespeed,ItemStack is,EntityEquipmentSlot equipmentSlot)
	{

		float at = this.isFailed(is)?FAILED.getAttackDamage() : attackdamage;
		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(uuids.getFirst(), "Weapon modifier", (double)at, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(uuids.getSecond(),"Weapon modifier", basespeed, 0));

			if(UnsagaMaterialCapability.adapter.hasCapability(is)){
				int weight = UnsagaMaterialCapability.adapter.getCapability(is).getWeight();
				if(weight>0){
					double mod = getSpeedModifierFromWeight(weight);
					multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(WEIGHT_EFFECTED_SPEED, "Weapon modifier", mod, 0));

				}
			}
		}

		return multimap;
	}
	public int getItemEnchantability(ItemStack is)
	{
		if(this.isFailed(is)){
			return FAILED.getEnchantability();
		}
		return UnsagaToolUtil.getItemEnchantability(is);
	}

	public float getMaxDamage(ItemStack stack) {

		if(this.isFailed(stack)){
			return FAILED.getMaxUses();
		}
		if(UnsagaMaterialCapability.adapter.hasCapability(stack)){
			UnsagaMaterial material = UnsagaMaterialCapability.adapter.getCapability(stack).getMaterial();
			if(ToolCategory.armorSet.contains(this.toolCategory)){
				return MAX_DAMAGE_ARRAY[this.toolCategory.getEquipmentSlot().getIndex()] * material.getArmorMaterial().getDurability(this.toolCategory.getEquipmentSlot());
			}


//			if(ToolCategory.SHIELD==this.toolCategory){
//				int s = material.getShieldPower()+5;
//				return s*s+s;
//			}
			return material.getToolMaterial().getMaxUses();
		}
		return 0;
	}

    public void getSubItems(Item par1,CreativeTabs tab, NonNullList<ItemStack> items)
    {

    	Preconditions.checkNotNull(SuitableLists.getSuitables(toolCategory),toolCategory);
//    	UnsagaMod.logger.trace("getsubitems", SuitableLists.getSuitables(toolCategory));
    	Preconditions.checkArgument(!SuitableLists.getSuitables(toolCategory).isEmpty(),toolCategory);
		for (UnsagaMaterial m:SuitableLists.getSuitables(toolCategory).stream().sorted().collect(Collectors.toList()))
		{
			items.add(UnsagaItemRegistry.instance().createStack(par1, m, 0));

		}
		if(this.toolCategory!=ToolCategory.GLOVES){
			if(!SuitableLists.getSuitables(toolCategory).contains(UnsagaMaterials.FEATHER)){
				items.add(UnsagaItemRegistry.instance().createStack(par1, UnsagaMaterials.FEATHER, 0));
			}else{
				if(!SuitableLists.getSuitables(toolCategory).contains(UnsagaMaterials.COTTON)){
					items.add(UnsagaItemRegistry.instance().createStack(par1, UnsagaMaterials.COTTON, 0));
				}else{
					items.add(UnsagaItemRegistry.instance().createStack(par1, UnsagaMaterials.DARK_STONE, 0));
				}
			}
		}


	}

	public float getToughness(ItemStack is){
		if(this.isFailed(is)){
			return FAILED_ARMOR.getToughness();
		}
		return UnsagaToolUtil.getMaterial(is).getArmorMaterial().getToughness();
	}

	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		String materialName = "";
		if(this.isFailed(par1ItemStack)){
			return "item.unsaga." + this.toolCategory.getPrefix() + ".failed";
		}
		if(UnsagaMaterialCapability.adapter.hasCapability(par1ItemStack)){
			UnsagaMaterial ma = UnsagaMaterialCapability.adapter.getCapability(par1ItemStack).getMaterial();
			if(ma.getAnotherName(toolCategory).isPresent()){
				materialName = ma.getAnotherName(this.toolCategory).get();
			}else{
				materialName = ma.getPropertyName();
			}

		}

		return "item.unsaga." + this.toolCategory.getPrefix() + "." + materialName;
	}


	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		ICapabilityProvider provider = new ICapabilitySerializable<NBTBase>(){

			IUnsagaForgeableTool def = UnsagaMaterialCapability.CAPA.getDefaultInstance();
			Capability<IUnsagaForgeableTool> capa = UnsagaMaterialCapability.CAPA;
			@Override
			public void deserializeNBT(NBTBase nbt) {
				// TODO 自動生成されたメソッド・スタブ
				capa.getStorage().readNBT(capa, def, null, nbt);
			}

			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
				if(capa!=null){
					if(capa==capability){
						return (T)def;
					}
				}
				return null;
			}

			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
				// TODO 自動生成されたメソッド・スタブ
				return capa!=null && capa==capability;
			}

			@Override
			public NBTBase serializeNBT() {
				// TODO 自動生成されたメソッド・スタブ
				return capa.getStorage().writeNBT(capa, def, null);
			}

		};
		return provider;
	}





      public boolean isFailed(ItemStack is){
		UnsagaMaterial material = UnsagaToolUtil.getMaterial(is);
		return !SuitableLists.getSuitables(toolCategory).contains(material);
	}
}
