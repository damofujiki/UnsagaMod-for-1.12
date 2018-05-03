package mods.hinasch.unsaga.damage;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import mods.hinasch.unsaga.core.item.weapon.ItemAxeUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemKnifeUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemSpearUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemStaffUnsaga;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class DamageTypeAssociation {

	private static DamageTypeAssociation instance;
	private Map<Predicate<Item>,Attribute> map = Maps.newHashMap();
	private Map<Predicate<Entity>,Attribute> map2 = Maps.newHashMap();
	private Map<Predicate<DamageSource>,Attribute> map3 = Maps.newHashMap();

	public static DamageTypeAssociation instance(){
		if(instance == null){
			instance = new DamageTypeAssociation();
		}
		return instance;
	}

	private DamageTypeAssociation(){
		map.put(item -> item instanceof ItemAxe, new Attribute(2,General.PUNCH,General.SWORD));
		map.put(item -> item instanceof ItemSword, new Attribute(0,General.SWORD));
		map.put(item -> item instanceof ItemKnifeUnsaga, new Attribute(4,General.SWORD,General.SPEAR));
		map.put(item -> item instanceof ItemStaffUnsaga, new Attribute(4,General.PUNCH));
		map.put(item -> item instanceof ItemSpearUnsaga, new Attribute(4,General.SPEAR));
		map.put(item -> item instanceof ItemBow, new Attribute(0,General.SPEAR));
		map.put(item -> item instanceof ItemAxeUnsaga, new Attribute(4,General.PUNCH,General.SWORD));
		map.put(item -> item instanceof ItemPickaxe, new Attribute(0,General.PUNCH,General.SPEAR));

		map2.put(e -> e instanceof EntitySpider, new Attribute(0,General.SPEAR));
		map2.put(e -> e instanceof EntityGuardian, new Attribute(0,General.MAGIC,Sub.SHOCK));
		map2.put(e -> e instanceof EntitySlime, new Attribute(0,General.PUNCH));
		map2.put(e -> e instanceof EntityMagmaCube, new Attribute(2,General.PUNCH,Sub.FIRE));
		map2.put(e -> e instanceof EntityBlaze, new Attribute(0,General.MAGIC,Sub.FIRE));

		map3.put(d -> d == DamageSource.CACTUS, new Attribute(0,General.SPEAR));
		map3.put(d -> d == DamageSource.ON_FIRE, new Attribute(0,General.MAGIC,Sub.FIRE));
		map3.put(d -> d == DamageSource.IN_FIRE, new Attribute(0,General.MAGIC,Sub.FIRE));
		map3.put(d -> d == DamageSource.LAVA, new Attribute(0,General.MAGIC,Sub.FIRE));
		map3.put(d -> d == DamageSource.DRAGON_BREATH, new Attribute(0,General.MAGIC,Sub.FIRE));
		map3.put(d -> d == DamageSource.LIGHTNING_BOLT, new Attribute(0,General.MAGIC,Sub.ELECTRIC));
		map3.put(d -> d == DamageSource.WITHER, new Attribute(0,General.MAGIC));
		map3.put(d -> d == DamageSource.STARVE, new Attribute(0,General.MAGIC));
		map3.put(d -> d.getImmediateSource() instanceof EntityArrow, new Attribute(3,General.SPEAR));
		map3.put(d -> d.isExplosion(), new Attribute(0,General.MAGIC,Sub.FIRE));
		map3.put(d -> d.isFireDamage(), new Attribute(0,Sub.FIRE));
		map3.put(d -> {
//			if(d.getImmediateSource() instanceof EntityArrow && EntityStateCapability.adapter.hasCapability(d.getImmediateSource())){
//				StateArrow state = (StateArrow) EntityStateCapability.adapter.getCapability(d.getImmediateSource()).getState(StateRegistry.instance().stateArrow);
//				return state.getType()==StateArrow.Type.MAGIC_ARROW;
//			}
			return false;
		},new Attribute(3,General.MAGIC,Sub.FIRE));

//		if(UnsagaMod.plugin.isLoadedHAC()){
//			UnsagaMod.plugin.hac.registerDamageSources(map3);
//		}
	}

	public Optional<List<Attribute>> getData(ItemStack is){
		List<Attribute> list = map.entrySet().stream().filter(entry -> entry.getKey().test(is.getItem())).map(entry -> entry.getValue()).collect(Collectors.toList());
		return Optional.of(list);
	}

	public Optional<List<Attribute>> getData(Entity en){
		List<Attribute> list = map2.entrySet().stream().filter(entry -> entry.getKey().test(en)).map(entry -> entry.getValue()).collect(Collectors.toList());
		return Optional.of(list);
	}

	public Optional<List<Attribute>> getData(DamageSource ds){
		List<Attribute> list = map3.entrySet().stream().filter(entry -> entry.getKey().test(ds)).map(entry -> entry.getValue()).collect(Collectors.toList());
		return Optional.of(list);
	}

	public static Optional<EnumSet<General>> getGeneralByPriority(List<Attribute> list){
		return  list.stream().sorted().filter(in -> !in.generals.isEmpty()).map(in -> in.generals).findFirst();
	}

	public static Optional<EnumSet<Sub>> getSubs(List<Attribute> list){
		Set<Sub> set =  list.stream().filter(in -> !in.subs.isEmpty()).flatMap(in -> in.subs.stream()).collect(Collectors.toSet());
		return set.isEmpty() ? Optional.empty() :Optional.of(EnumSet.copyOf(set));
	}
	public static class Attribute implements Comparable<Attribute>{

		final int priority;
		EnumSet<General> generals = EnumSet.noneOf(General.class);
		EnumSet<Sub> subs = EnumSet.noneOf(Sub.class);

		public Attribute(int priority,General... in){
			generals = EnumSet.copyOf(Lists.newArrayList(in));
			this.priority = priority;
		}
		public Attribute(int priority,General g,Sub s){
			generals = EnumSet.of(g);
			subs = EnumSet.of(s);
			this.priority = priority;
		}

		public Attribute(int priority,Sub... in){
			subs = EnumSet.copyOf(Lists.newArrayList(in));
			this.priority = priority;
		}
		@Override
		public int compareTo(Attribute o) {

			return this.priority > o.priority ? -1 : this.priority == o.priority ? 0 : 1;
		}
	}
}
