package mods.hinasch.unsaga.core.potion;



import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.registry.PropertyRegistry;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.core.potion.old.ShieldProperty.ShieldEvent;
import mods.hinasch.unsaga.core.potion.old.ShieldPropertyRegistry;
import mods.hinasch.unsaga.damage.AdditionalDamageTypes;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.status.UnsagaStatus;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class UnsagaPotions extends PropertyRegistry<PotionUnsaga>{



	public static final Potion DARK_SEAL = new PotionRepel("darkSeal",4,2,in -> in.getCreatureAttribute()!=EnumCreatureAttribute.UNDEAD);
	public static final Potion HOLY_SEAL = new PotionRepel("holySeal",4,2,in -> in.getCreatureAttribute()==EnumCreatureAttribute.UNDEAD);
	@Deprecated
	public static final Potion SHADOW_SERVANT = new PotionBuff("shadowServant",5,1);
	public static final Potion GOLD_FINGER = new PotionBuff("goldFinger",1,2);
	public static final Potion SPELL_MAGNET = new PotionGravity("spellMagnet",false,3,2);
	/** レベル０で移動を封じるだけ、レベル１以上で行動も封じる*/
	public static final PotionUnsaga STUN = new PotionGravity("sleep",true,5,1);
	public static final Potion FEAR = new PotionDisturbAI("fear",0,0,c -> new EntityAIAvoidEntity(c,EntityPlayer.class, 10.0F, 1.0D, 1.2D));
	public static final Potion GRAVITY = new PotionGravity("gravity",true,8,0);
	public static final Potion DOWN_DEX = (PotionUnsaga) PotionUnsaga.badPotion("downDex",1,1,UnsagaStatus.DEXTALITY, "9eb8e2f7-9f67-4e7e-b55f-62cb060b8599", -0.10D);
	public static final Potion DOWN_VIT = (PotionUnsaga) PotionUnsaga.badPotion("downVit",2,1,SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "b1726066-1f04-45c9-8b01-8baa39bd9005", -1.0D);
	public static final Potion DOWN_INT = (PotionUnsaga) PotionUnsaga.badPotion("downInt",3,1,UnsagaStatus.INTELLIGENCE, "7ce9b1ce-5b19-427a-9281-4db7c90c041b", -0.10D);
	public static final Potion LOCK_SLIME = PotionUnsaga.badPotion("lockSlime",5,0);
	public static final Potion DETECTED = (PotionUnsaga) PotionUnsaga.badPotion("detected",8,1,SharedMonsterAttributes.ARMOR, "ec8a8e69-80a1-4c22-ba72-94c790e5c7d5", -0.1D);
	public static final Potion COOLDOWN = PotionUnsaga.badPotion("coolDown",0,1);
	public static final Potion WATER_SHIELD = new PotionMagicShield("waterShield",6,1,UnsagaStatus.getAttribute(Sub.FIRE), "16cc3856-1abc-4cf6-927d-adbb2aad4a23", 0.1D);
	public static final Potion SELF_BURNING = new PotionMagicShield("selfBurning",6,1,UnsagaStatus.getAttribute(Sub.FREEZE), "4219c6c6-21ed-4bbe-ab0f-ea8caf5b8701", 0.1D);
	public static final Potion MISSILE_GUARD = new PotionBlockableShield("missileGuard",6,1,new AdditionalDamageTypes(General.SPEAR),0.9F,false);
	public static final Potion LEAF_SHIELD = new PotionBlockableShield("leafShield",6,1,new AdditionalDamageTypes(General.SWORD,General.PUNCH,General.SPEAR),0.5F,false);
	public static final Potion AEGIES_SHIELD = new PotionBlockableShield("aegisShield",6,1,new AdditionalDamageTypes(General.SWORD,General.PUNCH,General.SPEAR,General.MAGIC),0.35F,true);
	public static final Potion DETECT_PLANT = new PotionBuff("detectPlant",8,1);
	public static final Potion DETECT_GOLD = new PotionOreDetector("detectGold",8,1);
	public static final Potion DETECT_TREASURE = new PotionOreDetector("detectTreasure",8,1);
	public static final Potion LIFE_BOOST = new PotionBuff("lifeBoost",7,2,UnsagaStatus.HEAL_SPEED, "7ce9b1ce-5b19-427a-9281-4db7c90c041b", -10D);
	public static final Potion INCREASE_VIT = new PotionBuff("upVit",2,0,SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "7e1e210e-fa95-4bb0-8649-962426819a4b", 1.0D);
	public static final Potion INCREASE_INT = new PotionBuff("upInt",3,0,UnsagaStatus.INTELLIGENCE, "7ce9b1ce-5b19-427a-9281-4db7c90c041e", 0.10D);
	public static final Potion INCREASE_MENTAL = new PotionBuff("upMental",3,0,UnsagaStatus.MENTAL, "7ce9b1ce-5b19-427a-9281-4fb7c90c041e", 0.10D);
	public static final Potion VEIL_FIRE = new PotionBuff("veilFire",6,0,UnsagaStatus.getAttribute(FiveElements.Type.FIRE), "1e3b0dc5-ddec-4a94-ab07-ed4d47b7b412", 5D);
	public static final Potion VEIL_WATER =new PotionBuff("veilWater",6,0,UnsagaStatus.getAttribute(FiveElements.Type.WATER), "6e6097e8-0cbe-402e-8fb8-5095be33ae4e", 5D);
	public static final Potion VEIL_EARTH = new PotionBuff("veilEarth",6,0,UnsagaStatus.getAttribute(FiveElements.Type.EARTH), "129b9b6a-addd-4578-a9dd-5ce8c50254af", 5D);
	public static final Potion VEIL_WOOD = new PotionBuff("veilWwood",6,0,UnsagaStatus.getAttribute(FiveElements.Type.WOOD), "f000b94d-f0fe-4ca8-9bfd-7171b367bd45", 5D);
	public static final Potion VEIL_METAL = new PotionBuff("veilMetal",6,0,UnsagaStatus.getAttribute(FiveElements.Type.METAL), "613cbb37-2c3f-4e57-be01-72c8bbac5ad1", 5D);
	public static final Potion SILENT_MOVE = (PotionUnsaga) PotionUnsaga.buff("silentMove",5,0);
	public static final Potion HURT = PotionUnsaga.badPotion("hurt",0,1).setHasStatusIcon(false);
	//状態
	public static final Potion ALLOW_MULTIPLE_ATTACK = PotionUnsaga.badPotion("allowMultipleAttack",5,0).setHasStatusIcon(false);
	public static final Potion DELAYED_EXPLODE = new StateDelayedExplode("delayedExplode");
	public static final Potion CANCEL_FALL = new LivingState("cancelFall");
	public static final Potion CANCEL_HURT = new LivingState("cancelHurt");
	public static final Potion MOVING_STATE = new StateMovingTech("movingState");
	public static final Potion ASYNC_SPELL = new StateAsyncSpell("asyncSpell");
	public static final Potion CASTING = new StateCasting("casting");
	public static final Potion SET_POS = new StateSetPos("set_pos");
	public static final Potion COMBO = new StateCombo("combo");
	public static final Potion PARTICLE = new LivingState("spawn_particle");
	public static final Potion ACTION_PROGRESS = new LivingState("action_progress");
//	public static final Potion ASYNC_BLOCK_BREAK;

	public static final ImmutableList<Potion> DEBUFFS_STATUS = ImmutableList.of(MobEffects.SLOWNESS,MobEffects.WEAKNESS,MobEffects.UNLUCK,MobEffects.MINING_FATIGUE,DOWN_DEX,DOWN_VIT,DOWN_INT);
	public static final ImmutableList<Potion> DEBUFFS_MENTAL = ImmutableList.of(MobEffects.BLINDNESS,MobEffects.WITHER,UnsagaPotions.FEAR);
	public static final ImmutableList<Potion> DEBUFFS_BODY= ImmutableList.of(MobEffects.POISON,MobEffects.BLINDNESS,MobEffects.NAUSEA);

	@Deprecated
	public ShieldPropertyRegistry shieldProperties;
	@Deprecated
	public List<ShieldEvent> shieldEvents = Lists.newArrayList();
	protected static UnsagaPotions INSTANCE;

	public static UnsagaPotions instance(){
		if(INSTANCE==null){
			INSTANCE = new UnsagaPotions();
		}
		return INSTANCE;
	}
	protected UnsagaPotions(){




	}

	@Override
	public void init() {


	}

	@Override
	public void preInit() {
		//		this.fear.setPotionType(new PotionType(fear.getName(),new PotionEffectFear[]{new PotionEffectFear(fear,ItemUtil.getPotionTime(30),0)}));
		this.registerObjects();
		this.shieldProperties = ShieldPropertyRegistry.instance();
	}


	@Override
	protected void registerObjects() {

		this.put(DARK_SEAL );
		this.put(HOLY_SEAL );
//		this.put( SHADOW_SERVANT );
		this.put(GOLD_FINGER );
		this.put(SPELL_MAGNET );
		this.put(STUN );
		this.put( FEAR );
		this.put( GRAVITY );
		this.put( DOWN_DEX );
		this.put( DOWN_VIT );
		this.put( DOWN_INT );
		this.put( LOCK_SLIME );
		this.put( DETECTED );
		this.put( COOLDOWN );
		this.put( WATER_SHIELD );
		this.put( SELF_BURNING );
		this.put( MISSILE_GUARD );
		this.put( LEAF_SHIELD );
		this.put( AEGIES_SHIELD );
		this.put( DETECT_PLANT );
		this.put( DETECT_GOLD );
		this.put( DETECT_TREASURE );
		this.put( LIFE_BOOST );
		this.put( INCREASE_VIT );
		this.put( INCREASE_INT );
		this.put( INCREASE_MENTAL );
		this.put( VEIL_FIRE );
		this.put( VEIL_WATER );
		this.put( VEIL_EARTH );
		this.put( VEIL_WOOD );
		this.put( VEIL_METAL );
		this.put( SILENT_MOVE );
		this.put( HURT );
		//状態
		this.put( ALLOW_MULTIPLE_ATTACK );
		this.put( DELAYED_EXPLODE );
		this.put( CANCEL_FALL );
		this.put( CANCEL_HURT );
		this.put( MOVING_STATE );
		this.put( CASTING );
		this.put( SET_POS );
		this.put( COMBO );
		this.put( PARTICLE );
		this.put( ACTION_PROGRESS );
		this.put( ASYNC_SPELL );

		for(PotionUnsaga potion:this.getProperties()){
			this.registerToGameData(potion);
		}
	}

	private void put(Potion p){
		this.put((PotionUnsaga)p);
	}
//	private put(PotionUnsaga p){
//		this.put(()p);
//	}
	public void registerToGameData(PotionUnsaga... potions){
		for(PotionUnsaga p:potions){
			ForgeRegistries.POTIONS.register(p);
			p.initPotionType();
//			if(HSLib.isDebug()){
//				GameRegistry.register(p.getPotionType(),new ResourceLocation(UnsagaMod.MODID,p.getPropertyName()));
//			}

		}
	}
	//	@Override
	//	public PotionUnsaga put(PotionUnsaga p){
	//		GameRegistry.register(p,p.getKey());
	//		p.initPotionType();
	//		UnsagaMod.logger.trace("potion", p.getPotionType());
	//		GameRegistry.register(p.getPotionType(),new ResourceLocation(UnsagaMod.MODID,p.getName()));
	//		return super.put(p);
	//	}

	public static void addRemoveQueue(EntityLivingBase living,Potion p){
		removePotionQueue.offer(new Tuple(living,p));
	}
	public static Queue<Tuple<EntityLivingBase,Potion>> removePotionQueue = new ArrayBlockingQueue(20);
	public static void onLivingUpdate(LivingUpdateEvent e){
		if(e.getEntityLiving() instanceof EntityLivingBase){

			EntityLivingBase living = (EntityLivingBase) e.getEntityLiving();
			World world = living.getEntityWorld();
			PotionDisturbAI.checkExpired(e);

			if(living.isPotionActive(STUN)){
				if(living.getActivePotionEffect(STUN).getAmplifier()>0){
					e.setCanceled(true);
				}
			}

			if(LivingHelper.isStateActive(living,UnsagaPotions.HURT)){
				living.hurtResistantTime --;
				if(living.hurtResistantTime<0){
					living.hurtResistantTime = 0;
				}
				living.hurtTime --;
				if(living.hurtTime<0){
					living.hurtTime = 0;
				}
			}

			if(LivingHelper.isStateActive(living,UnsagaPotions.PARTICLE)){
				if(WorldHelper.isClient(living.getEntityWorld())){
					ParticleHelper.MovingType.FLOATING.spawnParticle(world, XYZPos.createFrom(living)
							, EnumParticleTypes.CRIT, world.rand, 15, 0.1D);
				}
			}
//			if(e.getEntityLiving() instanceof EntityPlayer && e.getEntityLiving().ticksExisted % 4 ==0){
//				UnsagaMod.logger.trace("potions", WorldHelper.isServer(e.getEntityLiving().world),e.getEntityLiving().getActivePotionEffects());
//			}
			if(WorldHelper.isClient(e.getEntityLiving().getEntityWorld())){
				e.getEntityLiving().getActivePotionEffects().forEach(in ->{
					if(in.getDuration()<=0){
						addRemoveQueue(e.getEntityLiving(),in.getPotion());
					}

				});
			}
			Tuple<EntityLivingBase,Potion> tuple = removePotionQueue.poll();
			if(tuple!=null){
				if(tuple.getFirst()==e.getEntityLiving()){
					e.getEntityLiving().removeActivePotionEffect(tuple.getSecond());
				}else{
					removePotionQueue.offer(tuple);
				}

			}

		}
//		if(WorldHelper.isClient(e.getEntityLiving().getEntityWorld()) && !e.getEntityLiving().getActivePotionEffects().isEmpty()){
//			this.checkExpired(e.getEntityLiving());
//		}
	}

	private synchronized void checkExpired(EntityLivingBase el){
		List<PotionEffect> effects = el.getActivePotionEffects().stream().filter(in -> in.getDuration()<=0).collect(Collectors.toList());
		for(PotionEffect effect:effects){
			el.removePotionEffect(effect.getPotion());
		}
	}


	public static void onEntityHurt(LivingHurtEvent e){
		for(PotionEffect effect:e.getEntityLiving().getActivePotionEffects()){
			if(effect.getPotion() instanceof PotionUnsaga){
				PotionUnsaga pu = (PotionUnsaga) effect.getPotion();
				pu.affectOnHurt(e,effect.getAmplifier());
			}
		}
	}

	public static void setStunned(EntityLivingBase target,int time,int level){
		target.addPotionEffect(new PotionEffect(UnsagaPotions.STUN,time,level));
	}
	public static void registerEvent(){
//		for(ShieldProperty shield:instance().shieldProperties.shields){
//			instance().shieldEvents.add(new ShieldProperty.ShieldEvent(shield));
//		}
		HSLibs.registerEvent(new EventSilentMove());
//		HSLibs.registerEvent(UnsagaPotions.instance());
	}
}
