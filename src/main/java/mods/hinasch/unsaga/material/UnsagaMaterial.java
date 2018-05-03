package mods.hinasch.unsaga.material;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import mods.hinasch.lib.misc.JsonApplier.IJsonApply;
import mods.hinasch.lib.registry.PropertyElementBase;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.item.wearable.ItemArmorUnsaga.ArmorTexture;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class UnsagaMaterial extends PropertyElementBase implements Comparable<UnsagaMaterial>,IJsonApply<UnsagaMaterialRegistry.JsonParserMaterial>{




	public static ToolMaterial TOOLMATERIAL_DEFAULT = ToolMaterial.STONE;
	public static ArmorMaterial ARMORMATERIAL_DEFAULT = ArmorMaterial.LEATHER;
	Optional<ToolMaterial> toolMaterial = Optional.empty();
	Optional<ArmorMaterial> armorMaterial = Optional.empty();
	Optional<UnsagaMaterialRegistry.Category> category = Optional.empty();
	Map<ToolCategory,String> useAnotherNameMap = Maps.newHashMap();
	Optional<String> subIconGetter = Optional.empty();
	Map<ToolCategory,ArmorTexture> specialArmorTextureMap = Maps.newHashMap();
//	List<ToolCategory> useOriginalNameForcedList = Lists.newArrayList();
	public int shieldPower = 1;
	public int color = Statics.COLOR_NONE;
	public int weight = 0;
	public int rank = 0;
	public int price = 0;

	public int getWeight() {
		return weight;
	}

	/** unlocalizednameで使う。素材の名はカテゴリのものを使うかどうか
	 * Categoryのほうで一律設定する。*/
	public boolean isUseParentName(ToolCategory cate) {
		if(this.getParent().isPresent()){
			return this.getParent().get().isUseParentMaterial(cate);
		}
		return false;
	}

	public UnsagaMaterial(String name) {
		super(new ResourceLocation(name), name);
//		this.weight = weight;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public UnsagaMaterial(String resourceName, String name) {
		super(new ResourceLocation(resourceName), name);
//		this.weight = weight;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public ArmorMaterial getArmorMaterial() {
		if(this.armorMaterial.isPresent()){
			return this.armorMaterial.get();
		}else{
			if(this.getParent().isPresent()){
				return this.getParent().get().getDefaultMaterial().getArmorMaterial();
			}
		}


		return this.ARMORMATERIAL_DEFAULT;
	}

	public int getMaterialColor(){
//		if(this.color==Statics.COLOR_NONE && this.getParent().isPresent()){
//			return this.getParent().get().getDefaultMaterial().getMaterialColor();
//
//		}
		return this.color;
	}

	public void setMaterialColor(int col){
		this.color = col;
	}
	@Override
	public Class getParentClass() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMaterial.class;
	}

	public ToolMaterial getToolMaterial() {

		if(this.toolMaterial.isPresent()){
			return this.toolMaterial.get();
		}else{
			if(this.getParent().isPresent()){
				return this.getParent().get().getDefaultMaterial().getToolMaterial();
			}
		}


		return this.TOOLMATERIAL_DEFAULT;
	}

	public float getBowModifier(){
		return this.getToolMaterial().getAttackDamage() - 1.0F;
	}
	public void setShieldModifier(int p){
		this.shieldPower = p;
	}


	/** 盾の能力値(バニラの33%+??)*/
	public int getShieldPower(){
		return this.shieldPower;
	}
	public void setArmorMaterial(ArmorMaterial armorMaterial) {
		this.armorMaterial = Optional.of(armorMaterial);
	}

	public void setToolMaterial(ToolMaterial toolMaterial) {
		this.toolMaterial = Optional.of(toolMaterial);
	}

	public ToolMaterial setToolMaterial(int harvestLevel,int maxUses,float efficiency,float damage,int enchantability){
		ToolMaterial tm = UtilUnsagaMaterial.addToolMaterial(this.getPropertyName(), harvestLevel, maxUses, efficiency, damage, enchantability);
		return tm;
	}

	public ArmorMaterial setArmorMaterial(int armor,int[] reduces,int enchant,int... toughIn){
		int toughness = toughIn.length>0 ? toughIn[0] : 0;
		ResourceLocation res = new ResourceLocation(name);
		ArmorMaterial am = EnumHelper.addArmorMaterial(this.getPropertyName(), "testarmortex",armor, reduces, enchant,SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,toughness);
		return am;

	}

	public Optional<String> getSubIconGetter(){
		return this.subIconGetter;
	}

	public void setSubIconGetter(String par1){
		this.subIconGetter = Optional.of(par1);
	}
	/**
	 * 親カテゴリーを設定している時に親が同じだとtrue.
	 * @param in
	 * @return
	 */
	public boolean isSameParent(UnsagaMaterial in){
		if(in.getParent().isPresent() && this.getParent().isPresent()){
			return in.getParent().get() == this.getParent().get();
		}
		return false;
	}
	public Optional<UnsagaMaterialRegistry.Category> getParent(){
		return this.category;
	}

	/**
	 * 親カテゴリーを設定する（アカシア・樫etc∈木
	 * @param c
	 */
	protected void setParent(UnsagaMaterialRegistry.Category c){
		this.category = Optional.of(c);
	}


//	public void addCategoryUseOriginalName(ToolCategory category){
//		this.useOriginalNameForcedList.add(category);
//	}

//	public boolean isUseOriginalName(ToolCategory cate){
//		return this.useOriginalNameForcedList.contains(cate);
//	}

	public void setAnotherName(ToolCategory cate,String name){
		this.useAnotherNameMap.put(cate, name);
	}

	public Optional<String> getAnotherName(ToolCategory cate){
		if(this.useAnotherNameMap.containsKey(cate)){
			return Optional.of(this.useAnotherNameMap.get(cate));
		}
		return Optional.empty();
	}
	/**
	 * アイテムアイコン用のプロパティゲッターを返す。
	 * @return
	 */
	public IItemPropertyGetter getPropertyGetter(){
		return new PropertyGetter(this);
	}


	public @Nullable ArmorTexture getSpecialArmorTexture(ToolCategory cate){
		return this.specialArmorTextureMap.get(cate);
	}

	public ArmorTexture addArmorTexture(ToolCategory cate,ArmorTexture textures){
		return this.specialArmorTextureMap.put(cate,textures);
	}


	public class PropertyGetter implements IItemPropertyGetter{

		UnsagaMaterial m;
		public PropertyGetter(UnsagaMaterial m){

			this.m = m;
		}
		@Override
		public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {

			if(UnsagaMaterialCapability.adapter.hasCapability(stack)){
				UnsagaMaterial mat = UnsagaMaterialCapability.adapter.getCapability(stack).getMaterial();

				if(mat==this.m){
					//					UnsagaMod.logger.trace(this.getClass().getName(),this.m);
					return 1.0F;
				}

//				if(mat.getSubIconGetter().isPresent() && this.m.getSubIconGetter().isPresent()){
//					if(mat.getSubIconGetter().get().equals(this.m.getSubIconGetter().get())){
//						return 1.0F;
//					}
//				}
			}
			return 0;
		}

	}

	public String getLocalized(){
		return HSLibs.translateKey("material."+this.getPropertyName());
	}
	@Override
	public int compareTo(UnsagaMaterial o) {
		return Integer.compare(this.rank, o.rank);
	}

	public void setPrice(int price){
		this.price = price;
	}
	@Override
	public void applyJson(UnsagaMaterialRegistry.JsonParserMaterial parser) {
		this.rank = parser.rank;
		this.weight = parser.weight;
		this.price = parser.price;
		UnsagaMod.logger.trace("parsing...", this.getPropertyName(),this.rank,this.weight,this.price);
		if(parser.harvestLevel>0){
			this.toolMaterial = Optional.of(EnumHelper.addToolMaterial("unsaga."+this.name, parser.harvestLevel, parser.maxUses, parser.efficiency, parser.attack, parser.enchantWeapon));
		}
		if(parser.damageFactor>0){
			this.armorMaterial =Optional.of(EnumHelper.addArmorMaterial("unsaga."+parser.name, "", parser.damageFactor, parser.reduction, parser.enchantArmor, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, parser.toughness));
		}
	}

}
