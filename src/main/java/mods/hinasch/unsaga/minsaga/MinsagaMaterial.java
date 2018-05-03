package mods.hinasch.unsaga.minsaga;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import mods.hinasch.lib.registry.PropertyElementBase;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability.IStatusModifier;
import mods.hinasch.unsaga.minsaga.MinsagaForging.Ability;
import mods.hinasch.unsaga.minsaga.MinsagaForging.ArmorModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MinsagaMaterial extends PropertyElementBase implements IStatusModifier{

	int repairCost =5;
	int repairDamage = 0;
	float attackModifier = 0;
	float efficiencyModifier = 0;
	int durabilityModifier = 0;
	List<Ability> abilities = Lists.newArrayList();
	public float getEfficiencyModifier() {
		return efficiencyModifier;
	}


	public void setEfficiencyModifier(float efficiencyModifier) {
		this.efficiencyModifier = efficiencyModifier;
	}
	ArmorModifier pairModifier = ArmorModifier.ZERO;


	int weight;


	Predicate<ItemStack> predicateItem;


	public MinsagaMaterial(String name) {
		super(new ResourceLocation(name), name);

	}


	public String getLocalized(){
		return HSLibs.translateKey(this.getUnlocalizedName());
	}
	public String getUnlocalizedName(){
		return "minsaga.material."+this.getPropertyName();
	}
	public ArmorModifier getArmorModifier() {
		return this.pairModifier;
	}

	public Predicate<ItemStack> checker(){
		return this.predicateItem;
	}

	public float getAttackModifier() {
		return attackModifier;
	}


	@Override
	public Class getParentClass() {
		// TODO 自動生成されたメソッド・スタブ
		return MinsagaMaterial.class;
	}


	public int getCostModifier() {
		return repairCost;
	}


	public int getRepairDamage() {
		return repairDamage;
	}


	public void setAbility(Ability... abilities){
		this.abilities = Lists.newArrayList(abilities);
	}
	public List<Ability> getAbilities(){
		return this.abilities;
	}
	public boolean hasAbilities(){
		return !this.abilities.isEmpty();
	}
	public boolean hasAbility(Ability ab){
		return this.abilities.contains(ab);
	}

	public boolean isMaterialItem(ItemStack stack){
		if(this.predicateItem==null){
			return false;
		}
		return this.predicateItem.test(stack);
	}
	public MinsagaMaterial setArmorModifier(float armor,float magic) {
		this.pairModifier = new ArmorModifier(armor,magic);
		return this;
	}
	public MinsagaMaterial setAttackModifier(float attackModifier) {
		this.attackModifier = attackModifier;
		return this;
	}
	public MinsagaMaterial setMaterialChecker(Predicate<ItemStack> predicate){
		this.predicateItem = predicate;
		return this;
	}


	public MinsagaMaterial setRepairCost(int repairCost) {
		this.repairCost = repairCost;
		return this;
	}

	public MinsagaMaterial setRepairDamage(int repairDamage) {
		this.repairDamage = repairDamage;
		return this;
	}
	public MinsagaMaterial setWeight(int weight) {
		this.weight = weight;
		return this;
	}



	public MinsagaMaterial setDurabilityModifier(int par1){
		this.durabilityModifier = par1;
		return this;
	}
	@Override
	public int getDurabilityModifier() {
		// TODO 自動生成されたメソッド・スタブ
		return this.durabilityModifier;
	}


	@Override
	public int getWeightModifier() {
		// TODO 自動生成されたメソッド・スタブ
		return this.weight;
	}

}