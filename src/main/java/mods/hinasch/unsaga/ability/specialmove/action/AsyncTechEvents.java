package mods.hinasch.unsaga.ability.specialmove.action;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.BiConsumer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.sync.AsyncUpdateEvent;
import mods.hinasch.lib.sync.SafeUpdateEventByInterval;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.lib.world.ScannerNew;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.ability.specialmove.TechRegistry;
import mods.hinasch.unsaga.common.specialaction.ActionAsyncEvent.AsyncEventFactory;
import mods.hinasch.unsaga.common.specialaction.ISimpleMelee;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.util.AsyncConnectedBlocksBreaker;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AsyncTechEvents {
	public static class CrashConnected implements AsyncEventFactory<TechInvoker>{

		Set<Block> blackList = Sets.newHashSet(Blocks.COBBLESTONE,Blocks.STONE,Blocks.SANDSTONE);
		String toolClass;
		public CrashConnected(String clazz){
			this.toolClass = clazz;
		}
		@Override
		public AsyncUpdateEvent apply(TechInvoker t) {
			if(WorldHelper.isClient(t.getWorld())){
				return null;
			}
			IBlockState state = t.getWorld().getBlockState(t.getTargetCoordinate().get());
			if(HSLibs.canBreakAndEffectiveBlock(t.getWorld(), t.getPerformer(), toolClass, t.getTargetCoordinate().get())){
				if(!blackList.contains(state.getBlock())){
					AsyncConnectedBlocksBreaker scannerBreak = new AsyncConnectedBlocksBreaker(t.getWorld(),5,Sets.newHashSet(state), t.getTargetCoordinate().get(), t.getPerformer());
					return scannerBreak;
				}

			}
			return null;
		}

	}


	public static class GrandSlam implements AsyncEventFactory<TechInvoker>{

		@Override
		public AsyncUpdateEvent apply(TechInvoker context) {
			boolean heavy = context.getArtifact().isPresent() && UnsagaMaterialCapability.adapter.hasCapability(context.getArtifact().get()) && UnsagaMaterialCapability.adapter.getCapability(context.getArtifact().get()).getWeight()>10;
			List<BlockPos> expos = Lists.newArrayList();
			AxisAlignedBB bb = context.getPerformer().getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D);
			RangedHelper<TechInvoker> helper = RangedHelper.create(context.getWorld(), context.getPerformer(),Lists.newArrayList(bb));
			helper.setSelector((self,target)->target.onGround).setConsumer((self,target)->{
				target.addVelocity(0, 1.0D, 0);
				expos.add(target.getPosition());
			}).invoke();
			if(!expos.isEmpty()){
				return new AsyncGrandslam(context.getWorld(), context.getStrengthHP(), expos, heavy, context.getPerformer());
			}
			return null;

		}

	}

	public static class Pulverizer implements AsyncEventFactory<TechInvoker>{

		@Override
		public AsyncUpdateEvent apply(TechInvoker t) {
			if(WorldHelper.isClient(t.getWorld())){
				return null;
			}
			return new ScannerPulverizer(t.getWorld(), t.getPerformer(),ScannerNew.create().base(t.getTargetCoordinate().get()).range(1).ready());
		}

	}

	public static class ArrowKnock extends SafeUpdateEventByInterval{


		Queue<EntityArrow> arrows;

		boolean first = false;

		public ArrowKnock(EntityLivingBase sender, List<EntityArrow> arrows) {
			super(sender, "arrowknock");
			this.forceFirstRun = true;
			this.arrows = new ArrayBlockingQueue(5);
			for(EntityArrow arrow:arrows){
				this.arrows.offer(arrow);
			}
		}

		@Override
		public int getIntervalThresold() {
			// TODO 自動生成されたメソッド・スタブ
			return 3000;
		}

		@Override
		public void loopByInterval() {
			UnsagaMod.logger.trace(this.getClass().getName(), "called");

			EntityArrow arrow = this.arrows.poll();
			if(arrow!=null){
				first = true;
				HSLib.getPacketDispatcher().sendTo(PacketSound.atEntity(SoundEvents.ENTITY_ARROW_SHOOT, sender), (EntityPlayerMP) sender);
				WorldHelper.safeSpawn(sender.getEntityWorld(), arrow);
			}

		}

		@Override
		public boolean hasFinished() {
			// TODO 自動生成されたメソッド・スタブ
			return arrows.isEmpty();
		}

	}
	public static class ScannerPulverizer extends SafeUpdateEventByInterval{

		static final String ID = "pulverizer";
		ScannerNew scanner;
		final World world;
		final EntityLivingBase owner;
		Iterator<BlockPos> iterator;

		public ScannerPulverizer(World world,EntityLivingBase owner,ScannerNew scanner) {
			super(owner,ID);
			this.world = world;
			this.scanner = scanner;
			this.owner = owner;
			this.iterator = this.scanner.getIterableInstance().iterator();
		}

		@Override
		public int getIntervalThresold() {
			// TODO 自動生成されたメソッド・スタブ
			return 0;
		}

		@Override
		public void loopByInterval() {

			BlockPos pos = this.iterator.next();
			UnsagaMod.logger.trace("loop", pos);
//			if(this.iterator.hasNext()){
				boolean flag = HSLibs.canBreakAndEffectiveBlock(world,owner,"pickaxe",pos);

				if(flag){
					IBlockState id = world.getBlockState(pos);
					id.getBlock().dropXpOnBlockBreak(world, pos, id.getBlock().getExpDrop(id, world, pos, 0));
					SoundAndSFX.playBlockBreakSFX(world, pos, id,true);

				}
//			}


		}

		@Override
		public boolean hasFinished() {
			// TODO 自動生成されたメソッド・スタブ
			return !this.iterator.hasNext();
		}

	}

	public static class GettingThrown extends SafeUpdateEventByInterval{

		TechInvoker invoker;
		int time = 0;
		double degree = 0;
		double speed = 0.01D;
		public GettingThrown(EntityLivingBase sender,TechInvoker invoker) {
			super(sender, "thrown");
			this.invoker = invoker;
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public int getIntervalThresold() {
			// TODO 自動生成されたメソッド・スタブ
			return 0;
		}

		@Override
		public void loopByInterval() {

			if(invoker.getTarget().isPresent()){
				if(invoker.getActionProperty()==TechRegistry.instance().CYCLONE){

					degree = MathHelper.wrapDegrees(degree);
					Vec3d vec = invoker.getTarget().get().getLookVec().scale(speed);
					vec = vec.rotateYaw((float) Math.toRadians(degree));
					invoker.getTarget().get().addVelocity(vec.x, 0, vec.z);
					speed += 0.01D;
					if(speed>3.0D){
						speed = 2.0D;
					}
					degree += 30.0D;
				}
				invoker.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, invoker.getTarget().get().getEntityBoundingBox(),in -> in!=sender && in!=invoker.getTarget().get())
				.forEach(in ->{
					in.attackEntityFrom(DamageSourceUnsaga.create(sender, 0.5F, General.PUNCH), 1.0F);
				});
			}
			time ++;
//			UnsagaMod.logger.trace("wait", time);
		}

		@Override
		public boolean hasFinished() {
			// TODO 自動生成されたメソッド・スタブ
			return invoker.getTarget().isPresent() ? this.time>5 && !invoker.getTarget().get().isPotionActive(MobEffects.LEVITATION) : true;
		}

	}
	public static class ContinuousAttack extends SafeUpdateEventByInterval implements ISimpleMelee<TechInvoker>{

		final int expire;
		int time = 0;
		World world;
		TechInvoker invoker;
		public ContinuousAttack(EntityLivingBase sender,int expire,TechInvoker invoker) {
			super(sender, "continuousAttack");
			this.expire = expire;
			this.world = sender.getEntityWorld();
			this.invoker = invoker;
		}

		@Override
		public int getIntervalThresold() {
			// TODO 自動生成されたメソッド・スタブ
			return 2;
		}

		@Override
		public void loopByInterval() {

			SoundAndSFX.playSound(world, XYZPos.createFrom(sender), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.0F, false);

			sender.swingArm(EnumHand.MAIN_HAND);
			this.performSimpleAttack(invoker);
			time ++;
		}

		@Override
		public boolean hasFinished() {
			// TODO 自動生成されたメソッド・スタブ
			return expire<time;
		}

		@Override
		public EnumSet<General> getAttributes() {
			// TODO 自動生成されたメソッド・スタブ
			return EnumSet.of(General.SPEAR);
		}

		@Override
		public EnumSet<Sub> getSubAttributes() {
			// TODO 自動生成されたメソッド・スタブ
			return EnumSet.noneOf(Sub.class);
		}

		@Override
		public BiConsumer<TechInvoker, EntityLivingBase> getAdditionalBehavior() {
			// TODO 自動生成されたメソッド・スタブ
			return (context,target)->{
				ParticleHelper.MovingType.FOUNTAIN.spawnParticle(world, new XYZPos(target.getPosition().up()), EnumParticleTypes.REDSTONE, world.rand, 10, 0.1D,new int[0]);
				target.hurtResistantTime = 0;
				target.hurtTime = 0;
			};
		}



		@Override
		public float getReach(TechInvoker context) {
			// TODO 自動生成されたメソッド・スタブ
			return context.getReach() ;
		}

		@Override
		public DamageComponent getDamage(TechInvoker context,EntityLivingBase target, DamageComponent base) {
			return invoker.getStrength();
		}

		@Override
		public float getKnockbackStrength() {
			// TODO 自動生成されたメソッド・スタブ
			return 0;
		}



	}
}
