package mods.hinasch.unsaga.status;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.entity.mob.EntityRuffleTree;
import mods.hinasch.unsaga.core.entity.mob.EntityStormEater;
import mods.hinasch.unsaga.core.entity.passive.EntityUnsagaChest;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.IUnsagaDamageType;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.element.FiveElements;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UnsagaStatus {

	protected static Map<Predicate<EntityLivingBase>,Consumer<EntityLivingBase>> entityStatusApplies = Maps.newHashMap();
	/** LP攻撃力に関わる*/
	public static final IAttribute DEXTALITY = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".dextality", 1.0D, 0.01D, 2048.0D));
	/** ステータス異常のダメージに弱くなる*/
	public static final IAttribute MENTAL = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".mental", 1.0D, 0.01D, 256.0D));
	/** 魔法攻撃に関わる*/
	public static final IAttribute INTELLIGENCE = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".intelligence", 1.0D, 0.01D, 2048.0D));
	/** LP防御力*/
	public static final IAttribute RESISTANCE_LP_HURT =(new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".resistance.lphurt", 1.0D, 0.01D, 2048.0D));

	/** 攻撃属性（斬、殴、突、魔法）*/
	public static final ImmutableMap<DamageTypeUnsaga.General,IAttribute> GENERALS;
	/** 攻撃属性（火など）*/
	public static final ImmutableMap<DamageTypeUnsaga.Sub,IAttribute> SUBS;
	/** 五行値*/
	public static final ImmutableMap<FiveElements.Type,IAttribute> ENTITY_ELEMENTS;

	/** 自然回復値*/
	public static final IAttribute HEAL_SPEED = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".healSpeed", 80.0D, 1.0D, 2048.0D));
	static{



		Builder<DamageTypeUnsaga.General,IAttribute> builder = ImmutableMap.builder();
		for(DamageTypeUnsaga.General type:DamageTypeUnsaga.General.values()){


				builder.put(type,new RangedAttribute(null,UnsagaMod.MODID+".armorValue."+type.getName(),0.0D,-255.0D,255.0D));


		}
		GENERALS = builder.build();



		Builder<DamageTypeUnsaga.Sub,IAttribute> builder2  = ImmutableMap.builder();
		for(DamageTypeUnsaga.Sub type:DamageTypeUnsaga.Sub.values()){

			if(type!=Sub.NONE){
				builder2.put(type,new RangedAttribute(null,UnsagaMod.MODID+".armorValue."+type.getName(),0.0D,-255.0D,255.0D));


			}
		}
		SUBS = builder2.build();


		Builder<FiveElements.Type,IAttribute> builder3 = ImmutableMap.builder();
		for(FiveElements.Type type:FiveElements.Type.values()){
			builder3.put(type, new RangedAttribute(null,UnsagaMod.MODID+type.getUnlocalized(),0.0D,-255.0D,255.0D));
		}
		ENTITY_ELEMENTS = builder3.build();
	}

	public static IAttribute getAttribute(General gen){
		return GENERALS.get(gen);
	}
	public static IAttribute getAttribute(Sub gen){
		return SUBS.get(gen);
	}

	public static IAttribute getAttribute(FiveElements.Type gen){
		return ENTITY_ELEMENTS.get(gen);
	}
	public static String getTypeString(IAttribute at){
		for(DamageTypeUnsaga.General type:GENERALS.keySet()){
			if(GENERALS.get(type)==at){
				return type.getName();
			}
		}
		for(DamageTypeUnsaga.Sub type:SUBS.keySet()){
			if(SUBS.get(type)==at){
				return type.getName();
			}
		}
		return "?";
	}
	@SubscribeEvent
	public void onEntityConstruct(EntityConstructing e){
		if(e.getEntity() instanceof EntityLivingBase){
			EntityLivingBase living = (EntityLivingBase) e.getEntity();
			registerAndSetAttribute(living,DEXTALITY,1.0D);

			if(e.getEntity() instanceof EntityPlayer){
				registerAndSetAttribute(living,INTELLIGENCE,1.0D);
			}else{
				registerAndSetAttribute(living,INTELLIGENCE,0.5D);
			}
			registerAndSetAttribute(living,MENTAL,1.0D);
			if(UnsagaMod.configs.getDifficulty().isLPDamageEasy()){
				registerAndSetAttribute(living,RESISTANCE_LP_HURT,0.65D);
			}else{
				registerAndSetAttribute(living,RESISTANCE_LP_HURT,1.0D);
			}


			if(living instanceof EntityPlayer){
				registerAndSetAttribute(living,HEAL_SPEED,HEAL_SPEED.getDefaultValue());
			}else{
				registerAndSetAttribute(living,HEAL_SPEED,HEAL_SPEED.getDefaultValue()*2);
			}

			for(IAttribute at:GENERALS.values()){
				registerAndSetAttribute(living,at,1.0D);
			}
			for(IAttribute at:SUBS.values()){
				registerAndSetAttribute(living,at,1.0D);
			}
			for(IAttribute at:ENTITY_ELEMENTS.values()){
				registerAndSetAttribute(living,at,0.0D);
			}

			for(Predicate<EntityLivingBase> pre:entityStatusApplies.keySet()){
				if(pre.test(living)){
					entityStatusApplies.get(pre).accept(living);
				}
			}
		}
	}

	public static void registerAndSetAttribute(EntityLivingBase living,IAttribute attribute,double value){

		if(living.getEntityAttribute(attribute)==null){
			living.getAttributeMap().registerAttribute(attribute).setBaseValue(value);;
		}else{
			living.getEntityAttribute(attribute).setBaseValue(value);
		}


	}

	public static final double DECR_RATIO = 10.0D*-0.1D; //0.1あたりの影響（x%*-0.1D）
	public static final double DECR_MAX = 1.0D; //0.0あたりの値（減少最大値%）
	public static double getAppliedDamage(Set<IUnsagaDamageType> types,EntityLivingBase victim,float amount, int op){
		float base = op==Statics.OPERATION_INC_MULTIPLED ? amount : 5.0F;
//		float modifier = 0.0F;


		OptionalDouble opd = types.stream().mapToDouble(in -> victim.getEntityAttribute(in.getAttribute()).getAttributeValue())
		.map(in ->MathHelper.clamp(in, 0, 5.0D)).average();
		UnsagaMod.logger.trace(ID, opd.getAsDouble());
		double df = opd.isPresent() ? opd.getAsDouble() : 1.0D;
		double modifier = base*(-df  + 1.0D);
//		for(IUnsagaDamageType type:types){
//			double value = victim.getEntityAttribute(type.getAttribute()).getBaseValue();
//			UnsagaMod.logger.trace(ID, value);
//			double decrRatio = (DECR_RATIO * value) + DECR_MAX;
//			modifier = (float) (base * decrRatio);
//
//		}

		double damage = amount + modifier;
		UnsagaMod.logger.trace(ID, "加算後の攻撃力:"+base,"加算された値:"+modifier);
		damage = MathHelper.clamp((float) damage, 0.0F, 65536.0F);
		return damage;
	}

	public static void registerEntityStatus(Predicate<EntityLivingBase> pre,Consumer<EntityLivingBase> con){
		entityStatusApplies.put(pre, con);

	}
	public static void register(){
		HSLibs.registerEvent(new UnsagaStatus());


		//プレイヤーは素手のLP攻撃力は弱めに
		registerEntityStatus(liv -> liv instanceof EntityPlayer,liv ->{
			registerAndSetAttribute(liv,DEXTALITY,0.1D);

		});
		registerEntityStatus(liv -> liv instanceof EntitySkeleton,liv ->{
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.PUNCH),0.5D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.SPEAR),1.5D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.SWORD),1.3D);
		});
		registerEntityStatus(liv -> liv instanceof EntityStormEater,liv ->{
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.PUNCH),1.5D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.SPEAR),1.5D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.SWORD),1.5D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.MAGIC),0.5D);
		});
		registerEntityStatus(liv -> liv.getCreatureAttribute()==EnumCreatureAttribute.UNDEAD,liv ->{
			registerAndSetAttribute(liv,SUBS.get(DamageTypeUnsaga.Sub.FIRE),0.5D);
		});
		registerEntityStatus(liv -> liv.getCreatureAttribute()==EntityRuffleTree.PLANT,liv ->{
			registerAndSetAttribute(liv,SUBS.get(DamageTypeUnsaga.Sub.FIRE),0.5D);
		});
		registerEntityStatus(liv -> liv.getCreatureAttribute()==EnumCreatureAttribute.ARTHROPOD
				|| liv instanceof EntityShulker || liv instanceof EntityGolem,liv ->{
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.PUNCH),0.7D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.SPEAR),1.2D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.SWORD),1.2D);
		});
		registerEntityStatus(liv -> liv instanceof EntityWitch || liv instanceof EntityEnderman,liv ->{
			registerAndSetAttribute(liv,MENTAL,2.0D);
			registerAndSetAttribute(liv,INTELLIGENCE,2.0D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.MAGIC),0.5D);
		});
		registerEntityStatus(liv -> liv instanceof EntitySlime,liv ->{
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.PUNCH),2.0D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.SPEAR),0.5D);
			registerAndSetAttribute(liv,GENERALS.get(DamageTypeUnsaga.General.MAGIC),0.5D);
		});
		registerEntityStatus(liv -> liv instanceof EntityUnsagaChest,liv ->{
			registerAndSetAttribute(liv,SUBS.get(DamageTypeUnsaga.Sub.FIRE),100.0D);
			registerAndSetAttribute(liv,SUBS.get(DamageTypeUnsaga.Sub.FREEZE),100.0D);

		});
		registerEntityStatus(liv -> liv.isImmuneToFire() || liv instanceof EntityMagmaCube || liv instanceof EntityBlaze,liv ->{
			registerAndSetAttribute(liv,SUBS.get(DamageTypeUnsaga.Sub.FIRE),100.0D);
			registerAndSetAttribute(liv,SUBS.get(DamageTypeUnsaga.Sub.FREEZE),0.5D);
		});
		registerEntityStatus(liv -> liv instanceof EntityGuardian,liv ->{
			registerAndSetAttribute(liv,SUBS.get(DamageTypeUnsaga.Sub.ELECTRIC),0.5D);
		});
		registerEntityStatus(liv -> liv instanceof EntitySnowman,liv ->{
			registerAndSetAttribute(liv,SUBS.get(DamageTypeUnsaga.Sub.FIRE),0.5D);
			registerAndSetAttribute(liv,SUBS.get(DamageTypeUnsaga.Sub.FREEZE),2.0D);
		});
	}


	public static final String ID = "ADDITIONAL_TYPE";
}
