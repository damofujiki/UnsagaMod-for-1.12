package mods.hinasch.unsaga.ability.specialmove.action;

import java.util.EnumSet;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.particle.ParticleHelper.MovingType;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.lib.world.ScannerNew;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.ability.specialmove.action.AsyncTechEvents.GettingThrown;
import mods.hinasch.unsaga.common.specialaction.ActionAsyncEvent;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.common.specialaction.ActionCharged;
import mods.hinasch.unsaga.common.specialaction.ActionList;
import mods.hinasch.unsaga.common.specialaction.ActionMelee;
import mods.hinasch.unsaga.common.specialaction.ActionMultipleMelee;
import mods.hinasch.unsaga.common.specialaction.ActionProjectile;
import mods.hinasch.unsaga.common.specialaction.ActionRangedAttack;
import mods.hinasch.unsaga.common.specialaction.ActionRequireJump;
import mods.hinasch.unsaga.common.specialaction.ActionSelector;
import mods.hinasch.unsaga.common.specialaction.ActionTargettable;
import mods.hinasch.unsaga.common.specialaction.ActionWorld;
import mods.hinasch.unsaga.core.entity.passive.EntityBeam;
import mods.hinasch.unsaga.core.entity.passive.EntityEffectSpawner;
import mods.hinasch.unsaga.core.entity.projectile.EntityFlyingAxe;
import mods.hinasch.unsaga.core.entity.projectile.EntityJavelin;
import mods.hinasch.unsaga.core.entity.projectile.EntityThrowingKnife;
import mods.hinasch.unsaga.core.net.packet.PacketClientThunder;
import mods.hinasch.unsaga.core.potion.StateMovingTech;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TechActionFactory {

	public static final IAction<TechInvoker> SWINGSOUND_NOPARTICLE =  in ->{
		in.swingMainHand(true, false);
		return EnumActionResult.PASS;
	};
	public static final IAction<TechInvoker> SWINGSOUND_AND_PARTICLE= in ->{
		in.swingMainHand(true, true);
		return EnumActionResult.PASS;
	};

	public static final IAction<TechInvoker> STAFF_EFFECT = in ->{
		XYZPos pos = new XYZPos(in.getTargetCoordinate().get());
		in.playSound(pos, SoundEvents.ENTITY_GENERIC_EXPLODE, true);

		in.spawnParticle(MovingType.DIVERGE, pos, EnumParticleTypes.SMOKE_NORMAL, 20, 0.5D);
		return EnumActionResult.PASS;
	};
	public static final IAction<TechInvoker> WAVE_PARTICLE = in ->{
		in.playSound(new XYZPos(in.getTargetCoordinate().get()), SoundEvents.ENTITY_GENERIC_EXPLODE, true);
		BlockPos pos = in.getTargetCoordinate().get();
		IBlockState state = in.getWorld().getBlockState(pos);
		Block block = state.getBlock();
		if(block!=Blocks.AIR){
			in.spawnParticle(MovingType.WAVE, new XYZPos(pos.up()), EnumParticleTypes.BLOCK_DUST, 10, 1.2D,Block.getStateId(state));
		}
		return EnumActionResult.PASS;
	};

	/** 独妙点穴*/
	public static TechActionBase acupuncture(){
		ActionCharged<ActionMelee> chargedAc = ActionCharged.simpleChargedMelee(0,General.SPEAR,General.PUNCH,General.SWORD).setChargeThreshold(30);
		chargedAc.getAction().setAdditionalBehavior((context,target)->{
			context.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
			context.spawnParticle(MovingType.DIVERGE, target, EnumParticleTypes.PORTAL, 25,1.0D);
			target.addPotionEffect(new PotionEffect(UnsagaPotions.instance().DOWN_DEX,1,ItemUtil.getPotionTime(30)));
		});
		TechActionBase base = TechActionBase.create(InvokeType.CHARGE).addAction(chargedAc);
		return base;
	}
	/** 光の腕*/
	public static TechActionBase armOfLight(){
		TechActionBase base = TechActionBase.create(InvokeType.RIGHTCLICK);
		IAction<TechInvoker> action = t ->{
//			if(t.getTarget().isPresent()){
				EntityBeam beam = new EntityBeam(t.getWorld());
				XYZPos pos = XYZPos.createFrom(t.getPerformer());
				t.playSound(pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, false);
				beam.setPositionAndRotation(pos.dx, pos.dy, pos.dz,t.getPerformer().rotationYaw,t.getPerformer().rotationPitch);
				beam.setOwner(t.getPerformer());
				beam.setDamage(t.getActionStrength());
				//					beam.setTarget(t.getTarget().get());
				if(WorldHelper.isServer(t.getWorld())){
					WorldHelper.safeSpawn(t.getWorld(), beam);
				}
				return EnumActionResult.SUCCESS;
//			}
//			return EnumActionResult.FAIL;
		};
		base.addAction(SWINGSOUND_NOPARTICLE);
		base.addAction(action);
		return base;

	}

	/** 電光石火*/
	public static TechActionBase blitz(){
		TechActionBase blitz = new TechActionBase(InvokeType.RIGHTCLICK);
		blitz.addAction(new ActionAsyncTech(MovingStates.BLITZ, 4)
				.setStartSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F));
		return blitz;
	}
//	public static TechActionBase bloodyMary(){
//		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK);
//
//		IAction<TechInvoker> action = new ActionMelee(General.SPEAR)
//				.setAdditionalBehavior((context,target)->{
//					context.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, false,0.5F);
//					context.spawnParticle(MovingType.FOUNTAIN, new XYZPos(target.getPosition().up()), EnumParticleTypes.REDSTONE, 15, 0.1D);
//					LivingHelper.getCapability(context.getPerformer()).addState(new StateCombo.Effect(0,4,context.getStrength(),EnumParticleTypes.REDSTONE,context.getArtifact().isPresent() ? context.getArtifact().get() : null));
//					LivingHelper.getCapability(target).addState(new PotionEffect(UnsagaPotions.HURT,ItemUtil.getPotionTime(2),0));
//				});
//		base.addAction(action);
//		return base;
//	}


	/** 追突剣*/
	public static TechActionBase chargeBlade(){
		TechActionBase chargeBladeAction = new TechActionBase(InvokeType.SPRINT_RIGHTCLICK);
		chargeBladeAction.addAction(t ->{
			t.swingMainHand(true, true);
			boolean flag = RangedHelper.<TechInvoker>create(t.getWorld(), t.getPerformer(), 4.0D).setConsumer((self,target)->{
				//				UnsagaMod.logger.trace("damage", target);
				target.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(t,General.PUNCH,General.SWORD)), t.getStrengthHP());
				VecUtil.knockback(t.getPerformer(), target, 1.0D, 0.5D);
				UnsagaPotions.setStunned(target,ItemUtil.getPotionTime(3),0);
				SoundAndSFX.playSound(t.getWorld(), XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
			}).invoke();
			PotionEffect state = StateMovingTech.create(15, t,MovingStates.VELOCITY_ZERO);
			LivingHelper.getCapability(t.getPerformer()).addState(state);
			return flag ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		});
		return chargeBladeAction;
	}

	/** 薪割りダイナマイト。ジャンプ必須。
	 * 得られる枝は一個多い。*/
	public static TechActionBase firewoodChopper(){
		ActionMelee melee = new ActionMelee(General.PUNCH,General.SWORD)
				.setDamageDelegate(TechActionFactoryHelper.getWoodPlantSlayer());

		ActionWorld<TechInvoker> actionWorld = new ActionWorld<TechInvoker>(1, 1)
				.setWorldConsumer((self,pos)->{
					IBlockState state = self.getWorld().getBlockState(pos);
					if(state.getBlock().isWood(self.getWorld(), pos) ){
						SoundAndSFX.playBlockBreakSFX(self.getWorld(), pos, state,false);
						int drop = 9;
						if(self.getArtifact().isPresent()){
							int fortune = EnchantmentHelper.getEnchantments(self.getArtifact().get()).get(Enchantments.FORTUNE);
							drop += fortune;
						}

						Stream.generate(()->new ItemStack(Items.STICK,1)).limit(drop).forEach(in ->ItemUtil.dropAndFlyItem(self.getWorld(), in, new XYZPos(pos)));
						return EnumActionResult.SUCCESS;
					}
					return EnumActionResult.PASS;
				}
						);
		ActionSelector selector = new ActionSelector();
		selector.addAction(InvokeType.RIGHTCLICK, melee);
		selector.addAction(InvokeType.USE, actionWorld);
		ActionList actionList = new ActionList();
		actionList.addAction(SWINGSOUND_NOPARTICLE);
		actionList.addAction(selector);
		ActionRequireJump jump = new ActionRequireJump(actionList);
		TechActionBase base = TechActionBase.create(InvokeType.RIGHTCLICK,InvokeType.USE).addAction(jump);
		return base;
	}

	/** 土竜撃*/
	public static TechActionBase earthDragon(){
		ActionRangedAttack<TechInvoker> rangedAttackAction = new ActionRangedAttack<TechInvoker>(General.PUNCH)
				.setBoundingBoxFactory(in ->Lists.newArrayList(HSLibs.getBounding( in.getTargetCoordinate().get(), 4.0D, 3.0D)));
		rangedAttackAction.setSubBehavior((self,target)->{
			VecUtil.knockback(self.getParent().getPerformer(), target, 1.0D, 0.2D);
			UnsagaPotions.setStunned(target, ItemUtil.getPotionTime(3), 0);
		});
		rangedAttackAction.setEntitySelector((self,target)->target.onGround);
		TechActionBase base = new TechActionBase(InvokeType.USE)
				.addAction(SWINGSOUND_NOPARTICLE)
				.addAction(WAVE_PARTICLE)
				.addAction(rangedAttackAction);
		return base;
	}

	/** 富嶽八景*/
	public static TechActionBase fujiView(){
		TechActionBase base = new TechActionBase(InvokeType.CHARGE);
		ActionMelee melee = new ActionMelee(General.PUNCH,General.SWORD)
				.setAdditionalBehavior((self,target)->{
					Vec3d lookVec = self.getPerformer().getLookVec().normalize().scale(0.6D);
					lookVec = lookVec.rotateYaw((float) Math.toRadians(180));
					self.getPerformer().addVelocity(lookVec.x,0.3D, lookVec.z); //反動
					Random rand = self.getWorld().rand;
					XYZPos targetPos = XYZPos.createFrom(target);
					self.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_GENERIC_EXPLODE, false);


					//打ち上げる
					target.addVelocity(0, self.getPerformer().onGround ? 4.0D : 1.5D, 0);
					LivingHelper.getCapability(target).addState(new PotionEffect(UnsagaPotions.PARTICLE,ItemUtil.getPotionTime(3),0));

					IBlockState down = self.getWorld().getBlockState(targetPos.down());
					if(down.getBlock()!=Blocks.AIR){
						int id = Block.getIdFromBlock(down.getBlock());
						self.spawnParticle(MovingType.FOUNTAIN,XYZPos.createFrom(target), EnumParticleTypes.BLOCK_DUST, 20, 0.1D,id);
					}
					//			self.spawnParticle(MovingType.FLOATING,target, EnumParticleTypes.VILLAGER_HAPPY, 20, 0.1D);

					if(WorldHelper.isClient(self.getWorld())){
						self.getWorld().spawnEntity(EntityEffectSpawner.create(target, 30, 3.0D));
					}
					self.spawnParticle(MovingType.FOUNTAIN,target, EnumParticleTypes.SMOKE_LARGE, 25, 0.05D);

					//ブロック破壊
					if(!self.getPerformer().onGround){
						ScannerNew.create().base(target).range(1).ready().stream().forEach(pos ->{
							NonNullList<ItemStack> list = NonNullList.create();
							if(!self.getWorld().isAirBlock(pos)){
								IBlockState state = self.getWorld().getBlockState(pos);
								if(HSLibs.canBreakAndEffectiveBlock(self.getWorld(), null,"pickaxe", pos,new ItemStack(Items.IRON_PICKAXE))){

									state.getBlock().getDrops(list,self.getWorld(), pos, state, 0);
									WorldHelper.setAir(self.getWorld(), pos);
								}else{
									if(HSLibs.canBreakAndEffectiveBlock(self.getWorld(), null, "shovel", pos,new ItemStack(Items.IRON_SHOVEL))){

										state.getBlock().getDrops(list,self.getWorld(), pos, state, 0);
										WorldHelper.setAir(self.getWorld(), pos);
									}
								}

							}
							list.forEach(in ->ItemUtil.dropAndFlyItem(self.getWorld(), in,new XYZPos(pos)));
						});
					}
				});
		base.addAction(SWINGSOUND_NOPARTICLE);
		base.addAction(new ActionCharged(melee));
		return base;
	}

	/** ドラ鳴らし*/
	public static TechActionBase gonger(){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK,InvokeType.USE);
		ActionMelee melee = new ActionMelee(General.PUNCH);
		melee.setAdditionalBehavior((context,target)->{
			context.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
		});
		ActionList listAction = new ActionList();
		listAction.addAction(TechActionFactory.WAVE_PARTICLE);
		listAction.addAction(new ActionWorld.AirRoomDetector());
		base.addAction(new ActionSelector()
				.addAction(InvokeType.RIGHTCLICK, melee)
				.addAction(InvokeType.USE, listAction));
		return base;
	}
	public static TechActionBase grandslam(){
		ActionRangedAttack<TechInvoker> rangedAction = new ActionRangedAttack<TechInvoker>(General.PUNCH)
				.setBoundingBoxFactory(in ->Lists.newArrayList(HSLibs.getBounding(in.getTargetCoordinate().get(), 9.0D, 5.0D)));
		rangedAction.setAttackFlag(false);
		rangedAction.setSubBehavior((self,target)->{
			LivingHelper.getCapability(target).addState(new PotionEffect(UnsagaPotions.instance().DELAYED_EXPLODE,ItemUtil.getPotionTime(6)));
			self.getParent().spawnParticle(MovingType.FLOATING, target, EnumParticleTypes.EXPLOSION_NORMAL, 3, 0.2D);
			VecUtil.knockback(self.getParent().getPerformer(), target, 5.0D, 1.0D);
		});
		rangedAction.setEntitySelector((self,target)->target.onGround);
		TechActionBase grandSlamBase = new TechActionBase(InvokeType.USE)
				.addAction(SWINGSOUND_NOPARTICLE)
				.addAction(WAVE_PARTICLE)
				.addAction(rangedAction);
		return grandSlamBase;
	}

	/** 草伏せ*/
	public static TechActionBase grassHopper(){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK,InvokeType.USE);
		ActionRangedAttack<TechInvoker> rangedAction = new ActionRangedAttack(General.SWORD,General.SPEAR);
		rangedAction.setBoundingBoxFactory(new RangedBoundingBoxFactory.RangeSwing(4));
		rangedAction.setEntitySelector((self,target)->target.onGround);
		rangedAction.setSubBehavior((self,target)->UnsagaPotions.setStunned(target, ItemUtil.getPotionTime(3), 0));
		ActionWorld worldAction = new ActionWorld(2,1);
		worldAction.setWorldConsumer(new ActionWorld.WeedCutter());
		base.addAction(SWINGSOUND_AND_PARTICLE);
		base.addAction(new ActionSelector()
				.addAction(InvokeType.RIGHTCLICK, rangedAction) //空中なら足払い
				.addAction(InvokeType.USE, worldAction)); //ブロックに対してなら草刈り
		return base;
	}

	/** 逆風の太刀*/
	public static TechActionBase gust(){
		TechActionBase gustBase = new TechActionBase(InvokeType.CHARGE);
		gustBase.addAction(new ActionAsyncTech(MovingStates.GUST,10).setStartSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.5F));
		return gustBase;
	}

	/** ホークブレード*/
	public static TechActionBase hawkBlade(){
		TechActionBase hawkBlade = new TechActionBase(InvokeType.RIGHTCLICK);
		hawkBlade.addAction(in -> !in.getPerformer().onGround ? EnumActionResult.SUCCESS : EnumActionResult.PASS);
		hawkBlade.addAction(new ActionAsyncTech(MovingStates.HAWK_BLADE,ItemUtil.getPotionTime(3)).setStartSound(SoundEvents.ENTITY_WITHER_SHOOT, 0.5F));
		return hawkBlade;
	}
	public static TechActionBase knifeThrow(){
		return TechActionFactoryHelper.createProjectile(EntityThrowingKnife::knifeThrow, 10);
	}

	public static TechActionBase javelin(){
		return TechActionFactoryHelper.createProjectile(EntityJavelin::javelin, 20);
	}

	public static TechActionBase lightningThrust(){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK);
		ActionMelee meleeLightningThrust = new ActionMelee(General.SPEAR)
				.setAdditionalBehavior((context,target)->{
					UnsagaPotions.setStunned(target, ItemUtil.getPotionTime(20), 0); //長めのスタン
					UnsagaMod.packetDispatcher.sendToAllAround(PacketClientThunder.create(XYZPos.createFrom(target)), PacketUtil.getTargetPointNear(target));
					context.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_LIGHTNING_THUNDER, false);
				}).setSubAttributes(EnumSet.of(Sub.ELECTRIC));
		base.addAction(meleeLightningThrust);
		return base;
	}
	public static TechActionBase connectedBlocksCrasher(){
		TechActionBase pulverizerBase = new TechActionBase(InvokeType.RIGHTCLICK,InvokeType.USE);
		ActionMelee meleePulverizer = new ActionMelee(General.PUNCH);
		meleePulverizer.setAdditionalBehavior((context,target)->{
			context.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
			target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,ItemUtil.getPotionTime(30),0));
		});
		ActionList mainPulverizer = new ActionList();
		mainPulverizer.addAction(STAFF_EFFECT);
		mainPulverizer.addAction(new ActionAsyncEvent().setEventFactory(new AsyncTechEvents.CrashConnected("pickaxe")));
		pulverizerBase.addAction(new ActionSelector()
				.addAction(InvokeType.RIGHTCLICK, meleePulverizer)
				.addAction(InvokeType.USE, mainPulverizer));
		return pulverizerBase;
	}

	/** クイックチェッカー、溜めいらずの速射*/
	public static TechActionBase quickChecker(){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK);
		base.addAction(t ->{
			if(t.getArtifact().isPresent() && t.getArtifact().get().getItem() instanceof ItemBow){
				ItemBow bow = (ItemBow) t.getArtifact().get().getItem();
				bow.onPlayerStoppedUsing(t.getArtifact().get(), t.getWorld(), t.getPerformer(), 71400);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.PASS;
		});
		return base;
	}
	public static TechActionBase rockCrasher(){
		TechActionBase rockCrasherBase = new TechActionBase(InvokeType.RIGHTCLICK,InvokeType.USE);
		ActionMelee meleeRockCrasher = new ActionMelee(General.PUNCH);
		meleeRockCrasher.setAdditionalBehavior((context,target)->{
			context.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
			target.addPotionEffect(new PotionEffect(UnsagaPotions.instance().DOWN_VIT,ItemUtil.getPotionTime(30),0));
		});
		ActionList mainRockCrasher = new ActionList();
		mainRockCrasher.addAction(STAFF_EFFECT);
		mainRockCrasher.addAction(new ActionAsyncEvent().setEventFactory(new AsyncTechEvents.Pulverizer()));
		rockCrasherBase.addAction(new ActionSelector()
				.addAction(InvokeType.RIGHTCLICK, meleeRockCrasher)
				.addAction(InvokeType.USE, mainRockCrasher));
		return rockCrasherBase;
	}
	public static TechActionBase skullCrasher(){
		TechActionBase skullCrashBase = new TechActionBase(InvokeType.RIGHTCLICK);
		ActionMelee melee = new ActionMelee(General.PUNCH)
				.setDamageDelegate(in -> (in.getMiddle() instanceof EntityLivingBase) ? DamageComponent.of(in.getRight().hp() * 1.5F, in.getRight().lp()) : in.getRight())
				.setAdditionalBehavior((context,target)->{
					context.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
					target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,ItemUtil.getPotionTime(30),0));
				});
		skullCrashBase.addAction(melee);
		return skullCrashBase;
	}
	public static TechActionBase skyDrive(){
		TechActionBase skyDriveBase = TechActionBase.create(InvokeType.CHARGE);
		ActionProjectile<TechInvoker> projectileSkyDrive = new ActionProjectile<TechInvoker>()
				.setProjectileFunction(EntityFlyingAxe::skyDrive)
				.setShootSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP);
		skyDriveBase.addAction(new ActionCharged(new ActionTargettable(projectileSkyDrive)).setChargeThreshold(20));
		return skyDriveBase;
	}
	/** しびれ突き*/
	public static TechActionBase stunner(){
		TechActionBase stunnerBase = new TechActionBase(InvokeType.RIGHTCLICK)
				.addAction(new ActionMelee(General.SPEAR)
						.setAdditionalBehavior((context,target)->{
							context.spawnParticle(MovingType.DIVERGE, target, EnumParticleTypes.CRIT_MAGIC, 10, 1D);
							target.addPotionEffect(new PotionEffect(UnsagaPotions.instance().STUN,ItemUtil.getPotionTime(10),0));
						})
						);
		return stunnerBase;
	}
	public static TechActionBase swing(){
		TechActionBase swingBase = new TechActionBase(InvokeType.RIGHTCLICK)
				.addAction(SWINGSOUND_AND_PARTICLE)
				.addAction(new ActionRangedAttack<TechInvoker>(General.SWORD,General.SPEAR)
						.setBoundingBoxFactory(new RangedBoundingBoxFactory.RangeSwing(4)));
		return swingBase;
	}
	//	public static TechActionBase tomahawk(){
	//		TechActionBase tomahawkBase = TechActionBase.create(InvokeType.CHARGE);
	//		ActionProjectile<TechInvoker> projectileTomahawk = new ActionProjectile<TechInvoker>().setProjectileFunction((context,target)->{
	//			EntityFlyingAxe axe = new EntityFlyingAxe(context.getWorld(),context.getPerformer(),context.getArticle().get().copy());
	//			axe.setDamage(context.getStrengthHP());
	//			context.getPerformer().setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
	//			XYZPos pos = XYZPos.createFrom(context.getPerformer());
	//			axe.shoot(context.getPerformer(), context.getPerformer().rotationPitch, context.getPerformer().rotationYaw, 0, 2.0F, 1.0F);
	//
	//			return axe;
	//		}).setShootSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP);
	//		tomahawkBase.addAction(new ActionCharged(projectileTomahawk).setChargeThreshold(20));
	//		return tomahawkBase;
	//	}
	public static TechActionBase tomahawk(){
		return TechActionFactoryHelper.createProjectile(EntityFlyingAxe::tomahawk, 20);
	}
	public static TechActionBase vandalize(){
		TechActionBase base = TechActionBase.create(InvokeType.CHARGE);
		IAction<TechInvoker> explode = context ->{
			BlockPos pos = context.getTargetCoordinate().get();
			context.getWorld().createExplosion(context.getPerformer(), pos.getX(), pos.getY(), pos.getZ(), 3, false);
			return EnumActionResult.SUCCESS;
		};
		ActionMelee meleeVandalize = new ActionMelee(General.SWORD).setAdditionalBehavior((context,target)->{
			XYZPos pos = XYZPos.createFrom(target);
			LivingHelper.getCapability(target).addState(new PotionEffect(UnsagaPotions.PARTICLE,ItemUtil.getPotionTime(3)));
			explode.apply(context);
			if(WorldHelper.isClient(context.getWorld())){
				context.getWorld().spawnEntity(EntityEffectSpawner.create(target, 30, 3.0D));
			}
		});
		ActionSelector selector = new ActionSelector();
		selector.addAction(InvokeType.CHARGE, new ActionCharged(meleeVandalize).setChargeThreshold(25));
		selector.addAction(InvokeType.USE, explode);
		base.addAction(selector);
		return base;
	}


	/** 大木断*/
	public static TechActionBase woodChopper(){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK,InvokeType.USE);
		ActionMelee melee = new ActionMelee(General.PUNCH,General.SWORD)
				.setDamageDelegate(TechActionFactoryHelper.getWoodPlantSlayer());
		melee.setAdditionalBehavior((context,target)->	context.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, false));
		ActionAsyncEvent chopperAction = new ActionAsyncEvent();
		chopperAction.setEventFactory(new AsyncTechEvents.CrashConnected("axe"));
		base.addAction(SWINGSOUND_NOPARTICLE);
		base.addAction(new ActionSelector().addAction(InvokeType.RIGHTCLICK, melee)
				.addAction(InvokeType.USE, chopperAction));
		return base;
	}

	public static TechActionBase airThrow(){
		return throwAction(invoker -> {
			Vec3d vec = invoker.getPerformer().getLookVec();
			invoker.getTarget().get().addVelocity(vec.x, 0.7D, vec.z);
		});
	}

	public static TechActionBase callBack(){
		return throwAction(invoker -> {
			Vec3d vec = invoker.getPerformer().getLookVec();
			invoker.getTarget().get().addVelocity(-vec.x, 0.7D, -vec.z);
		});
	}

	public static TechActionBase cyclone(){
		return throwAction(invoker -> {
			Vec3d vec = invoker.getPerformer().getLookVec();
			invoker.getTarget().get().addVelocity(0, 0.7D, 0);
			invoker.getTarget().get().addPotionEffect(new PotionEffect(MobEffects.LEVITATION,ItemUtil.getPotionTime(10)));
		});
	}

	public static TechActionBase raksha(){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK);
		ActionMelee action = new ActionMelee(General.PUNCH);
		action.setAdditionalBehavior((in,target)->{
			XYZPos p = XYZPos.createFrom(target);
			WorldHelper.createExplosionSafe(in.getWorld(), in.getPerformer(), p, 1.0F, true);
		});

		base.addAction(action);
		return base;
	}

	public static TechActionBase triangleKick(){
		TechActionBase base = new TechActionBase(InvokeType.USE);
		IAction<TechInvoker> action = new IAction<TechInvoker>(){

			@Override
			public EnumActionResult apply(TechInvoker t) {

				if(t.getTargetCoordinate().isPresent()){
					BlockPos pos = t.getTargetCoordinate().get();
					BlockPos epos = t.getPerformer().getPosition();
					double dis = pos.getDistance(epos.getX(),epos.getY(),epos.getZ());
					if(dis<=3.0D){
						MovingAttack m = MovingAttack.builder().setCancelFall().setExpire(15).setRange(0.5D).setDamage(DamageSourceUnsaga.create(t.getPerformer(), t.getStrength().lp().amount(), General.PUNCH), t.getStrength().hp())
								.build(t.getPerformer());
						HSLib.addAsyncEvent(t.getPerformer(), m);
						t.playSound(XYZPos.createFrom(t.getPerformer()), SoundEvents.ENTITY_SHEEP_STEP, false);

						if(t.getWorld().isBlockFullCube(pos)){
							Vec3d vec = t.getPerformer().getLookVec().scale(0.5F);
							t.getPerformer().addVelocity(-vec.x, 0.65D, -vec.z);
							return EnumActionResult.SUCCESS;
						}
					}

				}
				return EnumActionResult.PASS;
				//				Vec3d vecRight = t.getPerformer().getLookVec().rotateYaw((float) Math.toRadians(90.0D)).scale(1.2D);
				//				Vec3d vecLeft = vecRight.rotateYaw((float) Math.toRadians(180.0D));
				//				BlockPos right = t.getPerformer().getPosition().add(vecRight.x>=1.0D ? 1 : 0, 0,vecRight.z>=1.0D ? 1 : 0);
				//				vecRight = vecRight.rotateYaw((float) Math.toRadians(180.0D));
				//				BlockPos left = t.getPerformer().getPosition().add(vecRight.x>=1.0D ? 1 : 0, 0,vecRight.z>=1.0D ? 1 : 0);
				//				List<Vec3d> posses = Lists.newArrayList(vecRight,vecLeft);
				//				UnsagaMod.logger.trace("trian", posses,vecRight);
				//				for(Vec3d vec:posses){
				//					if(t.getWorld().isBlockFullCube(t.getPerformer().getPosition().add(vec.x>=1.0D ? 1 : 0, 0, vec.z>=1.0D ? 1: 0))){
				//						t.playSound(XYZPos.createFrom(t.getPerformer()), SoundEvents.BLOCK_STONE_STEP, false);
				//						t.getPerformer().addVelocity(-vec.x, 1.0D, -vec.z);
				//						return EnumActionResult.SUCCESS;
				//					}
				//				}
				//				return EnumActionResult.PASS;
			}

		};
		base.addAction(action);
		return base;
	}
	public static TechActionBase flyingKnee(){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK);
		IAction<TechInvoker> action = new IAction<TechInvoker>(){

			@Override
			public EnumActionResult apply(TechInvoker t) {
				t.playSound(XYZPos.createFrom(t.getPerformer()), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, false);
				Vec3d vec = t.getPerformer().getLookVec();
				t.getPerformer().addVelocity(vec.x, 1.0D, vec.z);
				//				MovingAttack m = MovingAttack.builder()
				//						.setStopOnHit(true).setDamageComsumer((self,target) ->{
				//							t.playSound(XYZPos.createFrom(t.getPerformer()), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
				//							VecUtil.knockback(t.getPerformer(), target, 1.0F, 0.2D);
				//
				//						})
				//						.setCancelFall()
				//						.setExpire(15).setRange(1.0D)
				//						.setDamage(DamageSourceUnsaga.create(t.getPerformer(), t.getModifiedStrength().lp(),General.PUNCH), t.getModifiedStrength().hp())
				//						.build(t.getPerformer());
				PotionEffect state = StateMovingTech.create(15, t,MovingStates.FLYING_KNEE);
				//				PotionMovingState.Data data = PotionMovingState.getData(t.getPerformer());
				//				data.setAdditional(new AdditionalDamageData(t,General.PUNCH));
				//				data.setDamage(t.getStrength().hp());
				//				data.setStopOnHit(true);
				//				data.setVelocity(vec.x, 1.0D, vec.z);
				//				data.setDamageConsumer((self,target) ->{
				//							t.playSound(XYZPos.createFrom(t.getPerformer()), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
				//							VecUtil.knockback(t.getPerformer(), target, 1.0F, 0.2D);
				//						});
				//				data.setStopOnLanded(true);
				t.getPerformer().addPotionEffect(state);
				//				t.getPerformer().addPotionEffect(new PotionEffect(UnsagaPotions.CANCEL_FALL,15));
				//				HSLib.addAsyncEvent(t.getPerformer(), m);
				return EnumActionResult.SUCCESS;
			}

		};
		base.addAction(action);
		return base;
	}
	public static TechActionBase kick(){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK);
		IAction<TechInvoker> action = new IAction<TechInvoker>(){

			@Override
			public EnumActionResult apply(TechInvoker t) {
				t.playSound(XYZPos.createFrom(t.getPerformer()), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, false);
				Vec3d vec = t.getPerformer().getLookVec();
				PotionEffect state = StateMovingTech.create(100, t,MovingStates.SINKER);
				//				PotionMovingState.Data data = PotionMovingState.getData(t.getPerformer());
				//				data.setStopOnLanded(true);
				//				data.setStopOnHit(true);
				//				data.setAdditional(new AdditionalDamageData(t,General.PUNCH));
				//				data.setDamage(t.getStrength().hp());
				//				MovingAttack m = MovingAttack.builder().setVelX(vec.x).setVelZ(vec.z).setVelY(-1.0D)
				//						.setStopOnHit(true).setDamageComsumer((self,target) ->{
				//							if(t.getActionProperty()==TechRegistry.instance().THUNDER_KICK){
				//								t.playSound(XYZPos.createFrom(t.getPerformer()), SoundEvents.ENTITY_LIGHTNING_IMPACT, false);
				//								VecUtil.knockback(t.getPerformer(), target, 1.0F, 0.2D);
				//							}else{
				//								t.playSound(XYZPos.createFrom(t.getPerformer()), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
				//								target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,ItemUtil.getPotionTime(12)));
				//							}
				//
				//							self.getParent().getSender().addVelocity(-vec.x, 0.5D, -vec.z);
				//						})
				//						.setExpire(100).setRange(1.0D)
				//						.setDamage(DamageSourceUnsaga.create(t.getPerformer(), t.getStrength().lp().strength(),General.PUNCH), t.getStrength().hp())
				//						.build(t.getPerformer());
				//				m.setStopOnLanded(true);
				//				HSLib.addAsyncEvent(t.getPerformer(), m);
				t.getPerformer().addPotionEffect(state);
				return EnumActionResult.SUCCESS;
			}

		};
		base.addAction(action);
		return base;
	}

	public static TechActionBase waterfall(){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK,InvokeType.USE);
		ActionMultipleMelee action = new ActionMultipleMelee(General.PUNCH){

			@Override
			public int getMultipleMeleeNumber() {
				// TODO 自動生成されたメソッド・スタブ
				return 5;
			}

			@Override
			public void attack(EntityLivingBase ev,TechInvoker invoker) {
				invoker.playSound(XYZPos.createFrom(ev), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
				ev.attackEntityFrom(DamageSourceUnsaga.create(invoker.getPerformer(), invoker.getStrength().lp().amount(), General.PUNCH).setModified(true), invoker.getStrengthHP());
				ev.setVelocity(0, 0, 0);
				invoker.getPerformer().addVelocity(0, 1, 0);
				ev.addVelocity(0, 1, 0);
			}

			@Override
			public int getMultipleAttackAllowingTime() {
				// TODO 自動生成されたメソッド・スタブ
				return 100;
			}


		};
		base.addAction(action);
		return base;
	}
	public static TechActionBase throwAction(Consumer<TechInvoker> consumer){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK);
		IAction<TechInvoker> action = new IAction<TechInvoker>(){

			@Override
			public EnumActionResult apply(TechInvoker t) {
				//				UnsagaMod.logger.trace("swing", "called");
				t.swingMainHand(true, false);
				if(t.getTarget().isPresent()){
					consumer.accept(t);
					HSLib.addAsyncEvent(t.getTarget().get(), new GettingThrown(t.getPerformer(),t));
					//					EntityLivingBase target = t.getTarget().get();
					//					Vec3d vec = t.getPerformer().getLookVec();
					//					target.addVelocity(vec.x, 1.0D, vec.z);

					return EnumActionResult.SUCCESS;
				}
				return EnumActionResult.PASS;
			}
		};
		base.addAction(action);
		return base;
	}

	//	public static interface ProjectileFunctionTechAction extends ProjectileFunction<TechInvoker>{
	//
	//	}
}
