package mods.hinasch.unsaga.ability;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import mods.hinasch.lib.registry.PropertyRegistry;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.ability.specialmove.TechRegistry;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.status.UnsagaStatus;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class AbilityRegistry extends PropertyRegistry<IAbility>{

	protected static AbilityRegistry INSTANCE;

	public static final UUID HEAL_UUID = UUID.fromString("579cc71e-ab95-4c54-af23-300047895df8");

	//	public static final Ability DUMMY = new Ability("dummy");
	public static final Ability EMPTY = new Ability("empty");
	public static final Ability HEAL_DOWN1 = new AbilityNaturalHeal("heal_down1").setHealAmount(-5);
	public static final Ability HEAL_DOWN2 = new AbilityNaturalHeal("heal_down2").setHealAmount(-10);
	public static final Ability HEAL_DOWN3 = new AbilityNaturalHeal("heal_down3").setHealAmount(-15);
	public static final Ability HEAL_DOWN4 = new AbilityNaturalHeal("heal_down4").setHealAmount(-20);
	public static final Ability HEAL_DOWN5 = new AbilityNaturalHeal("heal_down5").setHealAmount(-25);
	public static final Ability HEAL_UP1 = new AbilityNaturalHeal("heal_up1").setHealAmount(5);
	public static final Ability HEAL_UP2 = new AbilityNaturalHeal("heal_up2").setHealAmount(10);
	public static final Ability SUPPORT_FIRE = new Ability("support_fire");
	public static final Ability SUPPORT_EARTH = new Ability("support_earth");
	public static final Ability SUPPORT_METAL = new Ability("support_metal");
	public static final Ability SUPPORT_WATER = new Ability("support_water");
	public static final Ability SUPPORT_WOOD = new Ability("support_wood");
	public static final Ability SUPPORT_FORBIDDEN = new Ability("support_forbidden");
	public static final Ability SPELL_FIRE = new Ability("spell_fire");
	public static final Ability SPELL_EARTH = new Ability("spell_earth");
	public static final Ability SPELL_METAL = new Ability("spell_metal");
	public static final Ability SPELL_WATER = new Ability("spell_water");
	public static final Ability SPELL_WOOD = new Ability("spell_wood");
	public static final Ability SPELL_FORBIDDEN = new AbilityBlocking("spell_forbidden");
	public static final Ability KNIFE_GUARD = new AbilityBlocking("knife_guard");
	public static final Ability STAFF_BLOCKING = new AbilityBlocking("blocking");
	public static final Ability DEFLECT = new Ability("deflect");
	public static final Ability BLOCK_FIRE = new AbilityShield("block_fire",1.5F,Sub.FIRE);
	public static final Ability BLOCK_MELEE = new AbilityShield("block_melee",General.PUNCH,General.SPEAR,General.SWORD);
	public static final Ability BLOCK_SLASH_BRUISE= new AbilityShield("block_slash_bruise",General.SWORD,General.PUNCH);
	public static final Ability BLOCK_SPEAR = new AbilityShield("block_spear",General.SPEAR);
	public static final Ability BLOCK_ELECTRIC = new AbilityShield("block_electric",1.5F,Sub.ELECTRIC);
	public static final Ability BLOCK_FIRE_FREEZE_ELECTRIC = new AbilityShield("block_fire_freeze_electric",1.0F,Sub.FIRE,Sub.ELECTRIC,Sub.FREEZE);
	public static final Ability ARMOR_BRUISE_EX = new Ability("armor_bruise_ex");
	public static final Ability ARMOR_FIRE_EX = new Ability("armor_fire_ex");
	public static final Ability ARMOR_COLD_EX = new Ability("armor_cold_ex");
	public static final Ability ARMOR_ELECTRIC_EX = new Ability("armor_electric_ex");
	public static final Ability ARMOR_SLASH = new Ability("armor_slash");
	public static final Ability ARMOR_BRUISE = new Ability("armor_bruise");
	public static final Ability ARMOR_PIERCE = new Ability("armor_pierce");
	public static final Ability ARMOR_FIRE = new Ability("armor_fire");
	public static final Ability ARMOR_COLD = new Ability("armor_cold");
	public static final Ability ARMOR_ELECTRIC = new Ability("armor_electric");
	public static final Ability ARMOR_LIGHT = new Ability("armor_light");
	public static final Ability ARMOR_DEBUFF = new Ability("armor_debuff");
	public static final Ability ARMOR_PIERCE_BAD = new Ability("armor_pierce_bad");
	public static final Ability ANTI_POISON = new Ability("anti_poison");
	public static final Ability ANTI_BLIND = new Ability("anti_blind");
	public static final Ability ANTI_FREEZE = new Ability("anti_freeze");
	public static final Ability ANTI_LEVITATION = new Ability("anti_levitation");
	public static final Ability ANTI_SLEEP = new Ability("anti_sleep");
	public static final Ability ANTI_WITHER = new Ability("anti_wither");
	public static final Ability ANTI_HUNGER = new Ability("anti_hunger");
	public static final Ability ANTI_FATIGUE = new Ability("anti_fatigue");
	public static final Ability PROTECTION_LIFE = new Ability("life_protection");
	public static final Ability PROTECTION_STR = new Ability("str_protection");
	public static final Ability PROTECTION_DEX = new Ability("dex_protection");
	public static final Ability PROTECTION_MIND = new Ability("mind_protection");
	public static final Ability PROTECTION_INT = new Ability("int_protection");
	public static final Ability PROTECTION_VIT = new Ability("vit_protection");
	public static final Ability SUPER_HEALING = new Ability("super_healing");
	public static final Ability GUARD_ALL_RANGE = new Ability("all_range_guard");


	public static final Ability CATCH_ARROW = new Ability("catch_arrow");
	/** アビリティの発動する間隔、全デバフ抑制とかは長めに*/
	public static final int SUPERHEALING_INTERVAL = 10;

	public static final int ANTI_DEBUFF_ALL_INTERVAL = 20;
	public static final int ANTI_DEBUFF_INTERVAL = 8;

	public static Ability empty(){
		return AbilityRegistry.instance().EMPTY;
	}


	public static AbilityRegistry instance(){
		if(INSTANCE ==null){
			INSTANCE = new AbilityRegistry();
		}
		return INSTANCE;
	}

	public static void onLivingUpdate(LivingUpdateEvent e){
		if(e.getEntityLiving() instanceof EntityPlayer){
			List<Ability> abilities = AbilityAPI.getEffectiveAllPassiveAbilities(e.getEntityLiving());
			if(e.getEntityLiving().ticksExisted % ANTI_DEBUFF_INTERVAL ==0){

				Set<Potion> antiAbilities = abilities.stream().filter(in -> !in.getAntiEffects().isEmpty())
						.flatMap(in -> in.getAntiEffects().stream()).collect(Collectors.toSet());
				antiAbilities.forEach(in ->{
					if(e.getEntityLiving().isPotionActive(in)){

						e.getEntityLiving().removeActivePotionEffect(in);
					}
				});



			}
			if(e.getEntityLiving().ticksExisted % ANTI_DEBUFF_ALL_INTERVAL ==0){
				if(abilities.contains(ARMOR_DEBUFF)){
					List<Potion> antis = e.getEntityLiving().getActivePotionEffects().stream().map(in -> in.getPotion()).filter(in -> in.isBadEffect()).collect(Collectors.toList());
					Potion randomPick = HSLibs.randomPick(e.getEntityLiving().getRNG(), antis);
					e.getEntityLiving().removeActivePotionEffect(randomPick);
//					antis.forEach(in ->{
//							e.getEntityLiving().removeActivePotionEffect(randomPick);
//					});
				}

			}
			if(e.getEntityLiving().ticksExisted % SUPERHEALING_INTERVAL ==0){
				if(AbilityAPI.getEffectiveAllAbilities(e.getEntityLiving()).contains(SUPER_HEALING)){
					e.getEntityLiving().heal(0.5F);
				}
			}
		}

	}

	Map<IAttribute,AttributeModifier> watchableModifierMap = Maps.newHashMap();
	//	Map<IAbility,Integer> healAmountMap;
	public TechRegistry specialArts;
	private AbilityAssociateRegistry association;
	public Map<IAttribute,AttributeModifier> getModifierBaseMaps(){
		return this.watchableModifierMap;
	}

	@Override
	public void init() {
		this.association.init();
		this.specialArts.init();

		this.registerModifiers();
		this.registerAntiEffects();
		this.registerShields();
//		this.healAmountMap = Maps.newHashMap();
//
//		this.healAmountMap.put(HEAL_DOWN1, -5);
//		this.healAmountMap.put(HEAL_DOWN2, -10);
//		this.healAmountMap.put(HEAL_DOWN3, -15);
//		this.healAmountMap.put(HEAL_DOWN4, -20);
//		this.healAmountMap.put(HEAL_DOWN5, -25);
//		this.healAmountMap.put(HEAL_UP1, 5);
//		this.healAmountMap.put(HEAL_UP2, 10);

		HSLibs.registerEvent(new EventRefleshAbilityModifier());

	}

	@Override
	public void preInit() {
		this.specialArts = TechRegistry.instance();
		this.specialArts.preInit();

		this.registerObjects();

		this.association = AbilityAssociateRegistry.instance();
		this.association.preinit();
	}
	private void registerAntiEffects(){
		ANTI_POISON.setAntiEffects(MobEffects.POISON);
		ANTI_LEVITATION.setAntiEffects(MobEffects.LEVITATION);
		ANTI_WITHER.setAntiEffects(MobEffects.WITHER);
		ANTI_BLIND.setAntiEffects(MobEffects.BLINDNESS);
		ARMOR_DEBUFF.setIsAntiAllDebuffs(true);
		ANTI_SLEEP.setAntiEffects(UnsagaPotions.instance().STUN);
		PROTECTION_STR.setAntiEffects(MobEffects.WEAKNESS);
		PROTECTION_DEX.setAntiEffects(UnsagaPotions.instance().DOWN_DEX);
		PROTECTION_MIND.setAntiEffects(UnsagaPotions.instance().DOWN_INT);
		PROTECTION_INT.setAntiEffects(UnsagaPotions.instance().DOWN_INT);
		PROTECTION_VIT.setAntiEffects(UnsagaPotions.instance().DOWN_VIT);

	}
	private void registerModifiers(){
		this.watchableModifierMap.put(UnsagaStatus.HEAL_SPEED, new AttributeModifier(HEAL_UUID,"ability.naturlaHeal",0,0));
		this.watchableModifierMap.put(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.FIRE), new AttributeModifier(UUID.fromString("d27c283b-65a7-4cde-ba95-6f1956f4566a"),"ability.supportFire",0,0));
		this.watchableModifierMap.put(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.WATER), new AttributeModifier(UUID.fromString("7bb8d1ee-3770-4439-a499-dca4194d7aee"),"ability.supportWater",0,0));
		this.watchableModifierMap.put(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.EARTH), new AttributeModifier(UUID.fromString("92f2d519-2fd3-4ec3-a79d-98ad983a6aad"),"ability.supportEarth",0,0));
		this.watchableModifierMap.put(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.METAL), new AttributeModifier(UUID.fromString("86818af7-6fe6-4743-930c-c31c8edfc7f0"),"ability.supportMetal",0,0));
		this.watchableModifierMap.put(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.WOOD), new AttributeModifier(UUID.fromString("a24275a1-031d-48dd-b97a-e7a2b8d75b72"),"ability.supportWood",0,0));
		this.watchableModifierMap.put(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.FORBIDDEN), new AttributeModifier(UUID.fromString("46dba524-b2a4-4763-a4a5-f52dce6575d3"),"ability.supportForbidden",0,0));
		this.watchableModifierMap.put(UnsagaStatus.GENERALS.get(General.SWORD), new AttributeModifier(UUID.fromString("e5d714b9-0b68-421c-9734-1dd58c31ba46"),"ability.armorSlash",0,0));
		this.watchableModifierMap.put(UnsagaStatus.GENERALS.get(General.PUNCH), new AttributeModifier(UUID.fromString("fe95abf1-1c68-4bd1-9ff7-7469ad9f4402"),"ability.armorPunch",0,0));
		this.watchableModifierMap.put(UnsagaStatus.GENERALS.get(General.SPEAR), new AttributeModifier(UUID.fromString("945ec805-fe23-4353-9882-0e7901bd0348"),"ability.armorSpear",0,0));
		this.watchableModifierMap.put(UnsagaStatus.GENERALS.get(General.MAGIC), new AttributeModifier(UUID.fromString("878cdefc-5a6b-4f58-bb3a-ae1069044675"),"ability.armorMagic",0,0));
		this.watchableModifierMap.put(UnsagaStatus.SUBS.get(Sub.FIRE), new AttributeModifier(UUID.fromString("6cad0b49-b6f0-4847-8c21-716a9d5c390f"),"ability.armorFire",0,0));
		this.watchableModifierMap.put(UnsagaStatus.SUBS.get(Sub.FREEZE), new AttributeModifier(UUID.fromString("28cdddf3-e062-48a8-9ba5-f30e2c90a86d"),"ability.armorFreeze",0,0));
		this.watchableModifierMap.put(UnsagaStatus.SUBS.get(Sub.ELECTRIC), new AttributeModifier(UUID.fromString("470b75f4-aded-4ee0-86bc-91f2feba5b7a"),"ability.armorElectric",0,0));
		this.watchableModifierMap.put(UnsagaStatus.SUBS.get(Sub.SHOCK), new AttributeModifier(UUID.fromString("2891b53b-7ed9-4424-a80d-0726c505ad74"),"ability.armorShock",0,0));
		this.watchableModifierMap.put(UnsagaStatus.RESISTANCE_LP_HURT, new AttributeModifier(UUID.fromString("95f178e8-97d3-48a5-9390-7187831ba7f1"),"ability.resitance.LP",0,0));

		HEAL_DOWN1.setAttributeModifier(UnsagaStatus.HEAL_SPEED, 5D);
		HEAL_DOWN2.setAttributeModifier(UnsagaStatus.HEAL_SPEED, 10D);
		HEAL_DOWN3.setAttributeModifier(UnsagaStatus.HEAL_SPEED, 15D);
		HEAL_DOWN4.setAttributeModifier(UnsagaStatus.HEAL_SPEED, 20D);
		HEAL_DOWN5.setAttributeModifier(UnsagaStatus.HEAL_SPEED, 25D);
		HEAL_UP1.setAttributeModifier(UnsagaStatus.HEAL_SPEED, -5D);
		HEAL_UP2.setAttributeModifier(UnsagaStatus.HEAL_SPEED, -10D);
		SUPPORT_FIRE.setAttributeModifier(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.FIRE), 5D);
		SUPPORT_EARTH.setAttributeModifier(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.EARTH), 5D);
		SUPPORT_METAL.setAttributeModifier(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.METAL), 5D);
		SUPPORT_WATER.setAttributeModifier(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.WATER), 5D);
		SUPPORT_WOOD.setAttributeModifier(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.WOOD), 5D);
		SUPPORT_FORBIDDEN.setAttributeModifier(UnsagaStatus.ENTITY_ELEMENTS.get(FiveElements.Type.FORBIDDEN), 5D);
		ARMOR_BRUISE_EX.setAttributeModifier(UnsagaStatus.GENERALS.get(General.PUNCH), 0.5D);
		ARMOR_FIRE_EX.setAttributeModifier(UnsagaStatus.SUBS.get(Sub.FIRE), 0.5D);
		ARMOR_COLD_EX.setAttributeModifier(UnsagaStatus.SUBS.get(Sub.FREEZE), 0.5D);
		ARMOR_ELECTRIC_EX.setAttributeModifier(UnsagaStatus.SUBS.get(Sub.ELECTRIC), 0.5D);
		ARMOR_SLASH.setAttributeModifier(UnsagaStatus.GENERALS.get(General.SWORD), 0.3D);
		ARMOR_BRUISE.setAttributeModifier(UnsagaStatus.GENERALS.get(General.PUNCH), 0.3D);
		ARMOR_PIERCE.setAttributeModifier(UnsagaStatus.GENERALS.get(General.SPEAR), 0.3D);
		ARMOR_FIRE.setAttributeModifier(UnsagaStatus.SUBS.get(Sub.FIRE), 0.25D);
		ARMOR_COLD.setAttributeModifier(UnsagaStatus.SUBS.get(Sub.FREEZE), 0.25D);
		ARMOR_ELECTRIC.setAttributeModifier(UnsagaStatus.SUBS.get(Sub.ELECTRIC), 0.25D);
		ARMOR_LIGHT.setAttributeModifier(UnsagaStatus.SUBS.get(Sub.SHOCK), 0.25D);
		ARMOR_PIERCE_BAD.setAttributeModifier(UnsagaStatus.GENERALS.get(General.SPEAR), -0.125D);
		PROTECTION_LIFE.setAttributeModifier(UnsagaStatus.RESISTANCE_LP_HURT, 0.1D);
	}
	@Override
	protected void registerObjects() {
		this.put(EMPTY);
		this.put(HEAL_DOWN1,HEAL_DOWN2,HEAL_DOWN3,HEAL_DOWN4,HEAL_DOWN5);
		this.put(HEAL_UP1,HEAL_UP2);
		this.put(SUPPORT_EARTH);
		this.put(SUPPORT_FIRE);
		this.put(SUPPORT_FORBIDDEN);
		this.put(SUPPORT_METAL);
		this.put(SUPPORT_WATER);
		this.put(SUPPORT_WOOD);
		this.put(SPELL_EARTH);
		this.put(SPELL_FIRE);
		this.put(SPELL_FORBIDDEN);
		this.put(SPELL_METAL);
		this.put(SPELL_WATER);
		this.put(SPELL_WOOD);
		this.put(KNIFE_GUARD);
		this.put(BLOCK_FIRE);
		this.put(BLOCK_MELEE);
		this.put(BLOCK_SLASH_BRUISE);
		this.put(BLOCK_SPEAR);
		this.put(BLOCK_ELECTRIC);
		this.put(BLOCK_FIRE_FREEZE_ELECTRIC);
		this.put(ARMOR_PIERCE);
		this.put(ARMOR_PIERCE_BAD);
		this.put(ARMOR_BRUISE);
		this.put(ARMOR_BRUISE_EX);
		this.put(ARMOR_COLD);
		this.put(ARMOR_COLD_EX);
		this.put(ARMOR_DEBUFF);
		this.put(ARMOR_ELECTRIC);
		this.put(ARMOR_ELECTRIC_EX);
		this.put(ARMOR_SLASH);
		this.put(ARMOR_LIGHT);
		this.put(ARMOR_FIRE);
		this.put(ARMOR_FIRE_EX);
		this.put(ANTI_POISON);
		this.put(ANTI_BLIND);
		this.put(ANTI_SLEEP);
		this.put(ANTI_FREEZE);
		this.put(ANTI_WITHER);
		this.put(ANTI_HUNGER);
		this.put(PROTECTION_LIFE);
		this.put(PROTECTION_STR);
		this.put(PROTECTION_DEX);
		this.put(PROTECTION_VIT);
		this.put(PROTECTION_MIND);
		this.put(PROTECTION_INT);
		this.put(SUPER_HEALING);
		this.put(GUARD_ALL_RANGE);
		this.put(CATCH_ARROW);

		this.specialArts.getProperties().forEach(obj -> this.put(obj));


	}

	private void registerShields(){
		BLOCK_FIRE_FREEZE_ELECTRIC.setBlockableDamage(in ->in.getParentDamageSource()==DamageSource.DRAGON_BREATH);
		BLOCK_MELEE.setBlockableDamage(in ->!in.isUnblockable() && !in.getDamageTypeUnsaga().contains(General.MAGIC));
		BLOCK_FIRE.setBlockableDamage(in ->in.isExplosion());
	}


}
