package mods.hinasch.unsaga.lp;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.google.common.collect.Maps;

import mods.hinasch.unsaga.core.item.weapon.ItemAxeUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemKnifeUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemSpearUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemStaffUnsaga;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class LPAssociation {

	protected static LPAssociation instance;
	Map<Predicate<Item>,LPAttribute> map = Maps.newLinkedHashMap();
	Map<Predicate<DamageSource>,LPAttribute> map2 = Maps.newLinkedHashMap();
	public static LPAssociation instance(){
		if(instance==null){
			instance = new LPAssociation();
		}
		return instance;
	}
	private LPAssociation(){
		map.put(item -> item instanceof ItemStaffUnsaga, new LPAttribute(0.4F,1));
		map.put(item -> item instanceof ItemSword, new LPAttribute(0.5F,1));
		map.put(item -> item instanceof ItemAxeUnsaga, new LPAttribute(0.6F,1));
		map.put(item -> item instanceof ItemAxe, new LPAttribute(0.6F,1));
		map.put(item -> item instanceof ItemKnifeUnsaga, new LPAttribute(0.8F,1));
		map.put(item -> item instanceof ItemSpearUnsaga, new LPAttribute(0.7F,1));
//		map.put(item -> item instanceof ItemBow, new LPAttribute(0.8F,1));
		map.put(item -> item instanceof ItemPickaxe, new LPAttribute(0.4F,1));

		map2.put(ds -> ds.getTrueSource()==null && ds==DamageSource.MAGIC, new LPAttribute(0.0f,0));
		map2.put(ds -> ds==DamageSource.STARVE, new LPAttribute(0.0f,0));
		map2.put(ds -> ds==DamageSource.FALL, new LPAttribute(2.0f,3));
		map2.put(ds -> ds==DamageSource.GENERIC, new LPAttribute(0.0f,0));
		map2.put(ds -> ds==DamageSource.HOT_FLOOR, new LPAttribute(0.1f,1));
		map2.put(ds -> ds==DamageSource.IN_FIRE, new LPAttribute(0.1f,1));
		map2.put(ds -> ds==DamageSource.ON_FIRE, new LPAttribute(0.1f,1));
		map2.put(ds -> ds==DamageSource.LAVA, new LPAttribute(0.1f,1));
		map2.put(ds -> ds==DamageSource.LIGHTNING_BOLT, new LPAttribute(2.0f,2));
		map2.put(ds -> ds==DamageSource.FALLING_BLOCK, new LPAttribute(0.1f,1));
		map2.put(ds -> ds==DamageSource.WITHER, new LPAttribute(0.1f,1));
		map2.put(ds -> ds.isExplosion() , new LPAttribute(1.5f,2));
		map2.put(ds -> (ds.getTrueSource() instanceof EntityPlayer) && !((EntityPlayer)ds.getTrueSource()).getHeldItemMainhand().isEmpty() , new LPAttribute(0.1F,0));
	}

	public Optional<LPAttribute> getData(ItemStack is){
		return map.entrySet().stream()
				.filter(entry -> entry.getKey().test(is.getItem())).map(entry -> entry.getValue()).findFirst();
	}

	public Optional<LPAttribute> getData(DamageSource ds){
		return map2.entrySet().stream()
				.filter(entry -> entry.getKey().test(ds)).map(entry -> entry.getValue()).findFirst();
	}
}
