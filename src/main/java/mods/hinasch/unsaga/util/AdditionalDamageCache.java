package mods.hinasch.unsaga.util;

import java.util.Map;

import com.google.common.collect.Maps;

import mods.hinasch.unsaga.damage.AdditionalDamageData;
import net.minecraft.util.DamageSource;

public class AdditionalDamageCache {


	private static Map<DamageSource,AdditionalDamageData> map = Maps.newHashMap();


	public static void addCache(DamageSource anker,AdditionalDamageData ds){

		if(map.size()>50){
			map.clear();
		}
		map.put(anker, ds);
	}

	public static void removeCache(DamageSource anker){
		map.remove(anker);
	}

	public static AdditionalDamageData  getData(DamageSource source){
		return map.get(source);
	}
}
