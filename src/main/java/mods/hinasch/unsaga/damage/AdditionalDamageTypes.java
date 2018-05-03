package mods.hinasch.unsaga.damage;

import java.util.EnumSet;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;

public class AdditionalDamageTypes{
	private final EnumSet<DamageTypeUnsaga.General> general;
	protected EnumSet<DamageTypeUnsaga.Sub> subType = EnumSet.noneOf(Sub.class);

	public AdditionalDamageTypes(General... generals){
		this.general = EnumSet.copyOf(Lists.newArrayList(generals));
	}

	public EnumSet<General> getDamageTypes(){
		return this.general;
	}

	public AdditionalDamageTypes setSubTypes(Sub... subs){
		this.subType = EnumSet.copyOf(Sets.newHashSet(subs));
		return this;
	}

	public AdditionalDamageTypes setSubTypes(EnumSet<Sub> subs){
		this.subType = subs;
		return this;
	}

	public EnumSet<Sub> getSubDamageTypes(){
		return this.subType;
	}

	/**
	 * 属性に重なりがあるか
	 * @param other
	 * @return
	 */
	public boolean hasIntersection(AdditionalDamageTypes other){
		if(general.stream().anyMatch(in -> other.getDamageTypes().contains(in))){
			return true;
		}
		if(subType.stream().anyMatch(in -> other.getSubDamageTypes().contains(in))){
			return true;
		}
		return false;
	}
}