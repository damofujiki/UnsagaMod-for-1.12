package mods.hinasch.unsaga.skillpanel;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.registry.PropertyElementBase;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.element.FiveElements;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class SkillPanel extends PropertyElementBase implements Comparable<SkillPanel>{

	public static enum IconType{
		KEY(0,"key"),MELEE(1,"melee"),ROLL(2,"roll"),COMMUNICATION(3,"communication"),NEGATIVE(4,"negative"),PROTECT(5,"protect"),FAMILIAR(6,"familiar");

		public static List<String> getJsonNames(){
			return Lists.newArrayList(IconType.values()).stream()
					.map(input -> input.getJsonName()).collect(Collectors.toList());
		}
		private int meta;
		private String jsonname;

		private IconType(int meta,String jsonname){
			this.jsonname = jsonname;
			this.meta = meta;
		}

		public String getJsonName(){
			return this.jsonname;
		}
		public int getMeta(){
			return this.meta;
		}
	}
	IconType iconType = IconType.KEY;
	int rarity = SkillPanelRegistry.RARITY_COMMON;
	Optional<FiveElements.Type> element = Optional.empty();
	@Nullable Pair<IAttribute,AttributeModifier> modifierPair;
	Set<DamageSource> damageAgainst = Sets.newHashSet();

	boolean isNegative = false;

	public SkillPanel(String name) {
		super(new ResourceLocation(name), name);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public int compareTo(SkillPanel o) {
		return Integer.compare(this.getPriority(), o.getPriority());
	}

	public Optional<FiveElements.Type> getElement(){
		return this.element;
	}

	public IconType getIconType(){
		return this.iconType;
	}

	public ItemStack getItemStack(int level){
		return SkillPanelRegistry.instance().getItemStack(this, level);
	}

	public String getLocalized(){
		return HSLibs.translateKey("skillPanel."+this.getPropertyName()+".name");
	}

	@Override
	public Class getParentClass() {
		// TODO 自動生成されたメソッド・スタブ
		return SkillPanel.class;
	}
	public int getPriority(){
		return this.iconType.getMeta();
	}

	public int getRarity() {
		return rarity;
	}

	public boolean isNegativeSkill() {
		return isNegative;
	}

	public SkillPanel setAttributeModifier(IAttribute attribute,AttributeModifier modifier){
		this.modifierPair = Pair.of(attribute, modifier);
		return this;
	}

	public @Nullable Pair<IAttribute,AttributeModifier> getModifierPair(){
		return this.modifierPair;
	}

	public Set<DamageSource> getDamageAgainst(){
		return this.damageAgainst;
	}
	public SkillPanel setDamageAgainst(DamageSource... damages){
		this.damageAgainst = Sets.newHashSet(damages);
		return this;
	}
	public SkillPanel setElement(FiveElements.Type type){
		this.element = Optional.of(type);
		return this;
	}

	public SkillPanel setIcon(IconType type){
		this.iconType = type;
		return this;

	}
	public SkillPanel setNegativeSkill(boolean isNegative) {
		this.isNegative = isNegative;
		return this;
	}
	public SkillPanel setRarity(int rare){
		this.rarity = rare;
		return this;
	}

}
