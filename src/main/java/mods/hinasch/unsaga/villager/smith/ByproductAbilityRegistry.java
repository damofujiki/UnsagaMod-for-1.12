package mods.hinasch.unsaga.villager.smith;

import java.util.Map;

import com.google.common.collect.Maps;

import mods.hinasch.unsaga.ability.AbilityRegistry;
import mods.hinasch.unsaga.ability.IAbility;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterials;

public class ByproductAbilityRegistry {

	private Map<UnsagaMaterial,IAbility> map = Maps.newHashMap();

	private static ByproductAbilityRegistry INSTANCE;

	public static ByproductAbilityRegistry instance(){
		if(INSTANCE == null){
			INSTANCE = new ByproductAbilityRegistry();
		}
		return INSTANCE;
	}

	protected ByproductAbilityRegistry(){

	}

	public IAbility getByproductAbility(UnsagaMaterial m){
		return map.get(m);
	}
	public boolean hasByproductAbility(UnsagaMaterial m){
		return this.map.containsKey(m);
	}
	public void register(){
		this.map.put(UnsagaMaterials.CARNELIAN, AbilityRegistry.SPELL_FIRE);
		this.map.put(UnsagaMaterials.RAVENITE, AbilityRegistry.SPELL_WATER);
		this.map.put(UnsagaMaterials.OPAL, AbilityRegistry.SPELL_METAL);
		this.map.put(UnsagaMaterials.TOPAZ, AbilityRegistry.SPELL_EARTH);
		this.map.put(UnsagaMaterials.LAZULI, AbilityRegistry.SPELL_WOOD);
		this.map.put(UnsagaMaterials.DARK_STONE, AbilityRegistry.SPELL_FORBIDDEN);
	}
}
