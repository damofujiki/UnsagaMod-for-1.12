package mods.hinasch.unsaga.ability.specialmove.action;

import java.util.OptionalDouble;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.sync.SafeUpdateEventByInterval;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class MovingAttack extends SafeUpdateEventByInterval{

		final int expire;
		int time =0;
		OptionalDouble x;
		OptionalDouble y;
		OptionalDouble z;
		final double range;
		final World world;
		final DamageSourceUnsaga dsu;
		final double damage;
		boolean attacked = false;
		boolean isStopOnHit = false;
		boolean isStopOnLanded = false;
		ImmutableSet<Potion> potions = ImmutableSet.of();
		boolean isCancelFall = false;
		@Nullable BiConsumer<RangedHelper<MovingAttack>,EntityLivingBase> damageConsumer;
		@Nullable Consumer<MovingAttack> consumer;

		public MovingAttack(EntityLivingBase sender,OptionalDouble x,OptionalDouble y,OptionalDouble z,int expire,double range,DamageSourceUnsaga dsu,double damage) {
			super(sender, "movingAttack");
//			if(EntityStateCapability.adapter.hasCapability(sender)){
//				StateSpecialMove state = (StateSpecialMove) EntityStateCapability.adapter.getCapability(sender).getState(StateRegistry.instance().stateSpecialMove);
//				sender.addPotionEffect(new PotionEffect(UnsagaPotions.instance().CANCEL_HURT,ItemUtil.getPotionTime(60)));
//
////				state.setCancelHurt(65535);
//				if(isCancelFall){
//					sender.addPotionEffect(new PotionEffect(UnsagaPotions.instance().CANCEL_FALL,ItemUtil.getPotionTime(60)));
////					state.setCancelFall(true);
//				}
//			}
			this.world = sender.getEntityWorld();
			this.x = x;
			this.y = y;
			this.z = z;
			this.expire = expire;
			this.range = range;
			this.dsu = dsu;
			this.damage = damage;

			// TODO 自動生成されたコンストラクター・スタブ
		}

		public MovingAttack setConsumer(Consumer<MovingAttack> c){
			this.consumer = c;
			return this;
		}
		@Override
		public int getIntervalThresold() {
			// TODO 自動生成されたメソッド・スタブ
			return 1;
		}

		public MovingAttack setCancelFall(boolean par1){
			this.isCancelFall = par1;
			return this;
		}

		public MovingAttack setStopOnLanded(boolean par1){
			this.isStopOnLanded = par1;
			return this;
		}
		public MovingAttack setStopOnHit(boolean par1){
			this.isStopOnHit = par1;
			return this;
		}
		public MovingAttack setPotions(Potion... potions){
			this.potions = ImmutableSet.copyOf(potions);
			return this;
		}
		@Override
		public void loopByInterval() {


			if(this.isCancelFall){
				this.sender.fallDistance = 0;
			}
			if(x.isPresent()){
				if(x.getAsDouble()==0 && z.getAsDouble()==0){
					sender.setVelocity(0, sender.motionY, 0);
				}else{
					sender.addVelocity(x.getAsDouble(), 0, 0);
				}
//				sender.motionX += x.getAsDouble();

			}
			if(y.isPresent()){
//				sender.motionY += y.getAsDouble();
				sender.addVelocity(0,y.getAsDouble(), 0);
			}
			if(z.isPresent()){
//				sender.motionZ += z.getAsDouble();
				sender.addVelocity(0,0,z.getAsDouble());
			}

			if(this.consumer!=null){
				this.consumer.accept(this);
			}

			if(this.sender.onGround){
				IBlockState state = world.getBlockState(sender.getPosition().down());
				if(state.getBlock()!=Blocks.AIR){
					ParticleHelper.MovingType.FOUNTAIN.spawnParticle(world, XYZPos.createFrom(sender), EnumParticleTypes.BLOCK_DUST, sender.getRNG(), 10, 0.1D, Block.getIdFromBlock(state.getBlock()));
				}

				if(this.isStopOnLanded){
					this.time = 65535;
				}
			}
			if(range>0 && !this.attacked){


				RangedHelper.<MovingAttack>create(world,sender, range).setConsumer((self,target)->{
					target.attackEntityFrom(dsu, (float) damage);
					if(this.isStopOnHit){
						this.time = 65535;
					}
					if(this.damageConsumer!=null){
						this.damageConsumer.accept(self, target);
					}
					if(!this.potions.isEmpty()){
						this.potions.forEach(in ->{
							target.addPotionEffect(new PotionEffect(in,ItemUtil.getPotionTime(20),1));
						});
					}
//					SoundAndSFX.playSound(world, XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
				}).setParent(this).invoke();
//				attacked = true;
			}

			time ++;
			UnsagaMod.logger.trace("time", time);
		}

		@Override
		public boolean hasFinished() {
			// TODO 自動生成されたメソッド・スタブ
			return time>expire;
		}

		@Override
		public void onExpire(){
//			sender.removeActivePotionEffect(UnsagaPotions.instance().CANCEL_HURT);
//			sender.removeActivePotionEffect(UnsagaPotions.instance().CANCEL_FALL);
//			if(EntityStateCapability.adapter.hasCapability(sender)){
//				StateSpecialMove state = (StateSpecialMove) EntityStateCapability.adapter.getCapability(sender).getState(StateRegistry.instance().stateSpecialMove);
//				state.setCancelHurt(0);
//
////				state.setCancelFall(false);
//			}
		}

		public MovingAttack setDamageConsumer(BiConsumer<RangedHelper<MovingAttack>,EntityLivingBase> damageConsumer){
			this.damageConsumer = damageConsumer;
			return this;
		}

		/**
		 * ビルダーを呼び出す。expireとダメージ、ダメージソース,rangeは必ず設定のこと
		 * @return
		 */
		public static Builder builder(){
			return new Builder();
		}

		public static class Builder{

			int expire;
			int time =0;
			OptionalDouble x = OptionalDouble.empty();
			OptionalDouble y = OptionalDouble.empty();
			OptionalDouble z = OptionalDouble.empty();
			OptionalDouble range;
			World world;
			DamageSourceUnsaga dsu;
			double damage;
			boolean attacked = false;
			boolean isStopOnHit = false;
			ImmutableSet<Potion> potions = ImmutableSet.of();
			boolean isCancelFall = false;
			@Nullable Consumer<MovingAttack> consumer;
			@Nullable BiConsumer<RangedHelper<MovingAttack>,EntityLivingBase> damageConsumer;

			/**
			 * 落下ダメージをキャンセルするかどうか
			 * @return
			 */
			public Builder setCancelFall(){
				this.isCancelFall = true;;
				return this;
			}

			public MovingAttack build(EntityLivingBase sender){
				MovingAttack m = new MovingAttack(sender, x, y, z, expire, range.isPresent() ? range.getAsDouble() : -1, dsu, damage);
				m.setStopOnHit(isStopOnHit);
				if(this.consumer!=null){
					m.setConsumer(consumer);
				}
				if(this.damageConsumer!=null){
					m.setDamageConsumer(damageConsumer);
				}
				return m;
			}
			public Builder setVelX(double d){
				this.x = OptionalDouble.of(d);
				return this;
			}

			public Builder setVelY(double d){
				this.y = OptionalDouble.of(d);
				return this;
			}

			public Builder setVelZ(double d){
				this.z = OptionalDouble.of(d);
				return this;
			}

			public Builder setExpire(int expire){
				this.expire = expire;
				return this;
			}

			public Builder setRange(double range){
				this.range = OptionalDouble.of(range);
				return this;

			}


			public Builder setDamage(DamageSourceUnsaga dsu,double damage){
				this.dsu = dsu;
				this.damage = damage;
				return this;
			}
			public Builder setPotions(Potion... potions){
				this.potions = ImmutableSet.copyOf(Sets.newHashSet(potions));
				return this;
			}

			public Builder setComsumer(Consumer<MovingAttack> c){
				this.consumer = c;
				return this;
			}
			public Builder setDamageComsumer(BiConsumer<RangedHelper<MovingAttack>,EntityLivingBase> c){
				this.damageConsumer = c;
				return this;
			}
			public Builder setStopOnHit(boolean par1){
				this.isStopOnHit = par1;
				return this;
			}
		}
	}