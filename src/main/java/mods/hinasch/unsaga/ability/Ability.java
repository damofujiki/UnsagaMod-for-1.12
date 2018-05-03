package mods.hinasch.unsaga.ability;

import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.Potion;
import net.minecraft.util.Tuple;

public class Ability extends AbilityBase{


	@Nullable Tuple<IAttribute,Double> modifiers = null;
	ImmutableSet<Potion> antiEffect = ImmutableSet.of();
	boolean isAntiAllDebuffs = false;
	@Nullable Predicate<DamageSourceUnsaga> blockableDamage;


	public Ability(String name) {
		super(name);
		this.exclusionNum = 0;
	}
//
//	public void addAssociation(ToolCategory category,UnsagaMaterial... materials){
//		for(UnsagaMaterial m:materials){
//			ResourceLocation key = AbilityAssociateRegistry.instance().buildKey(category, m);
//			AbilityAssociateRegistry.instance().addAssciation(category, m, this);
//		}
//	}
//
//	public void addAssociation(ToolCategory category,UnsagaMaterialRegistry.Category materials){
//		AbilityAssociateRegistry.instance().addAssociation(category, materials, this);
//	}
//
//	public void swapAssociation(ToolCategory category,UnsagaMaterial m,Ability old){
//		AbilityAssociateRegistry.instance().removeAssociation(category, m, old);
//		this.addAssociation(category, m);
//	}

	@Override
	public String getLocalized() {
		// TODO 自動生成されたメソッド・スタブ
		return HSLibs.translateKey("ability."+this.getPropertyName());
	}

	public Ability setAttributeModifier(IAttribute attribute,double amount){
		AttributeModifier md = AbilityRegistry.instance().watchableModifierMap.get(attribute);
		this.modifiers = new Tuple(attribute, amount);
		return this;
	}

	public @Nullable Tuple<IAttribute,Double> getAttributeModifier(){
		return this.modifiers;
	}


	public Set<Potion> getAntiEffects(){
		return this.antiEffect;
	}

	public boolean isAntiAllDebuffs(){
		return this.isAntiAllDebuffs;
	}

	public Predicate<DamageSourceUnsaga> getBlockableDamage(){
		return this.blockableDamage;
	}
	public Ability setBlockableDamage(Predicate<DamageSourceUnsaga> ds){
		this.blockableDamage = ds;
		return this;
	}
	public Ability setIsAntiAllDebuffs(boolean par1){
		this.isAntiAllDebuffs = par1;
		return this;
	}
	public Ability setAntiEffects(Potion... potions){
		this.antiEffect = ImmutableSet.copyOf(potions);
		return this;
	}

	@Override
	public int getExclusionNumber() {
		// TODO 自動生成されたメソッド・スタブ
		return this.exclusionNum;
	}
}
