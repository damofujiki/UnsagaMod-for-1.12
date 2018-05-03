package mods.hinasch.unsaga.damage;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import mods.hinasch.lib.core.HSLibEvents;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.util.LivingHurtEventUnsagaBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UnsagaEntityAttributes {


	public static final Map<DamageTypeUnsaga.General,IAttribute> GENERALS;
	public static final Map<DamageTypeUnsaga.Sub,IAttribute> SUBS;
	public static final Map<FiveElements.Type,IAttribute> ENTITY_ELEMENTS;

	public static final IAttribute EXPLODE_REDUCE = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".explode.defence", 1.0D, 0.0D, 2048.0D));
	public static final IAttribute PROJECTILE_REDUCE = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".projectile.defence", 1.0D, 0.0D, 2048.0D));
	public static final IAttribute NATURAL_HEAL_SPEED = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".healSpeed", 80.0D, 1.0D, 2048.0D));
	public static final IAttribute MENTAL = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".mental", 1.0D, -1.0D, 256.0D));
	public static final IAttribute INTELLIGENCE = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".intelligence", 1.0D, 0.001D, 2048.0D));
	public static final IAttribute STRENGTH_LP = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".lpstr", 1.0D, 0.001D, 2048.0D));
	public static final IAttribute REDUCE_LP = (new RangedAttribute((IAttribute)null, UnsagaMod.MODID+".lpdef", 1.0D, 0.001D, 2048.0D));
	static{


		GENERALS = new HashMap();
		for(DamageTypeUnsaga.General type:DamageTypeUnsaga.General.values()){

			GENERALS.put(type,new RangedAttribute(null,UnsagaMod.MODID+".armorValue."+type.getName(),0.0D,-255.0D,255.0D));
		}
		SUBS = new HashMap();
		for(DamageTypeUnsaga.Sub type:DamageTypeUnsaga.Sub.values()){

			if(type!=Sub.NONE){
				SUBS.put(type,new RangedAttribute(null,UnsagaMod.MODID+".armorValue."+type.getName(),0.0D,-255.0D,255.0D));


			}
		}
		ENTITY_ELEMENTS = Maps.newHashMap();
		for(FiveElements.Type type:FiveElements.Type.values()){
			ENTITY_ELEMENTS.put(type, new RangedAttribute(null,UnsagaMod.MODID+type.getUnlocalized(),0.0D,-255.0D,255.0D));
		}
	}

	@SubscribeEvent
	public void onEntityConstruct(EntityConstructing e){


//		UnsagaMod.logger.trace("trace");
		if(e.getEntity() instanceof EntityLivingBase){
			EntityLivingBase living = (EntityLivingBase) e.getEntity();
			//living.getAttributeMap().registerAttribute(generals.get(DamageTypeUnsaga.General.PUNCH));
			registerAndSetAttribute(living,STRENGTH_LP,1.0D);
			registerAndSetAttribute(living,INTELLIGENCE,1.0D);
			for(FiveElements.Type type:FiveElements.Type.values()){
				registerAndSetAttribute(living,ENTITY_ELEMENTS.get(type),0.0D);
			}
			if(living instanceof EntityPlayer){
				registerAndSetAttribute(living,NATURAL_HEAL_SPEED,NATURAL_HEAL_SPEED.getDefaultValue());
			}
			if(living instanceof EntitySkeleton){
//				UnsagaMod.logger.trace("register attribute to Skeleton");

				registerAndSetAttribute(living,GENERALS.get(General.PUNCH),+1.8D);
				registerAndSetAttribute(living,GENERALS.get(General.SPEAR),-1.9D);
				registerAndSetAttribute(living,GENERALS.get(General.SWORD),-0.3D);
			}
			if(living.getCreatureAttribute()==EnumCreatureAttribute.UNDEAD && !living.isImmuneToFire()){
				registerAndSetAttribute(living,SUBS.get(Sub.FIRE),+0.7D);
			}
			if(living instanceof EntityGuardian){
				registerAndSetAttribute(living,SUBS.get(Sub.ELECTRIC),+1.8D);
			}
			if(living instanceof EntityWaterMob){
				registerAndSetAttribute(living,SUBS.get(Sub.ELECTRIC),+1.8D);

			}
			if(living.getCreatureAttribute()==EnumCreatureAttribute.ARTHROPOD){
				registerAndSetAttribute(living,GENERALS.get(General.PUNCH),+0.5D);
				registerAndSetAttribute(living,GENERALS.get(General.SPEAR),-1.3D);
				registerAndSetAttribute(living,GENERALS.get(General.SWORD),-0.3D);
			}
			if(living instanceof EntitySlime){
				registerAndSetAttribute(living,GENERALS.get(General.PUNCH),-10.0D);
				registerAndSetAttribute(living,GENERALS.get(General.SWORD),+1.5D);
				registerAndSetAttribute(living,GENERALS.get(General.SPEAR),+1.5D);
				registerAndSetAttribute(living,GENERALS.get(General.MAGIC),+1.5D);
			}
			if(living.isImmuneToFire()){
				registerAndSetAttribute(living,SUBS.get(Sub.FIRE),-50.0D);
				registerAndSetAttribute(living,SUBS.get(Sub.FREEZE),+0.4D);
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

	public static void registerEvents(){

		HSLibEvents.livingHurt.getEventsMiddle().add(new LivingHurtEventUnsagaBase(){

			@Override
			public boolean apply(LivingHurtEvent e, DamageSourceUnsaga dsu) {
				// TODO 自動生成されたメソッド・スタブ
				return true;
			}

			@Override
			public DamageSource process(LivingHurtEvent e, DamageSourceUnsaga dsu) {
				EntityLivingBase victim = e.getEntityLiving();
				float reduceMultiply = 1.0F;
				if(victim.getEntityAttribute(EXPLODE_REDUCE)!=null && dsu.isExplosion()){
					reduceMultiply = (float) victim.getEntityAttribute(EXPLODE_REDUCE).getAttributeValue();
				}

				if(victim.getEntityAttribute(PROJECTILE_REDUCE)!=null && dsu.isProjectile()){
					reduceMultiply = (float)victim.getEntityAttribute(PROJECTILE_REDUCE).getAttributeValue();
				}
				if(victim.getEntityAttribute(MENTAL)!=null && !dsu.isUnblockable()){
					reduceMultiply = 1.0F - (float)victim.getEntityAttribute(MENTAL).getAttributeValue();
				}
				e.setAmount(e.getAmount() * reduceMultiply);
				dsu.setStrLPHurt(dsu.getStrLPHurt() * reduceMultiply);
				return dsu;
			}

			@Override
			public String getName() {
				// TODO 自動生成されたメソッド・スタブ
				return "additional.modifier";
			}}
		);
	}
}
