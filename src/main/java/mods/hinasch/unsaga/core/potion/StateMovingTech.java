package mods.hinasch.unsaga.core.potion;

import java.util.function.Consumer;

import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.ability.specialmove.action.IMovingStateAdapter;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class StateMovingTech extends LivingState{

	protected StateMovingTech(String name) {
		super(name);
	}

//	@Override
//	public IExtendedPotionData getExtendedPotionData() {
//		// TODO 自動生成されたメソッド・スタブ
//		return new Data();
//	}
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int meta)
	{
		super.performEffect(entityLivingBaseIn, meta);
		World world = entityLivingBaseIn.getEntityWorld();
		if(LivingHelper.getState(entityLivingBaseIn,this) instanceof Effect){
			Effect effect = (Effect) LivingHelper.getState(entityLivingBaseIn,this);
			int remaining = effect.getDuration();
			effect.getMovingConsumer().accept(new IMovingStateAdapter(){

				@Override
				public TechInvoker getInvoker() {
					// TODO 自動生成されたメソッド・スタブ
					return effect.getInvoker();
				}

				@Override
				public EntityLivingBase getOwner() {
					// TODO 自動生成されたメソッド・スタブ
					return entityLivingBaseIn;
				}

				@Override
				public World getWorld() {
					// TODO 自動生成されたメソッド・スタブ
					return entityLivingBaseIn.getEntityWorld();
				}}
			);
		}


//		Data data = (Data) ExtendedPotionCapability.adapter.getCapability(entityLivingBaseIn);


//		if(data.isCancelHurt && !entityLivingBaseIn.isPotionActive(UnsagaPotions.CANCEL_HURT)){
//			entityLivingBaseIn.addPotionEffect(new PotionEffect(UnsagaPotions.CANCEL_HURT,remaining));
//		}
//		if(data.isStopping){
//			entityLivingBaseIn.setVelocity(0, entityLivingBaseIn.motionY, 0);
//		}
//		if(data.moveX.isPresent()){
//			entityLivingBaseIn.addVelocity(data.moveX.getAsDouble(), 0, 0);
////			sender.motionX += x.getAsDouble();
//
//		}
//		if(data.moveY.isPresent()){
////			sender.motionY += y.getAsDouble();
//			entityLivingBaseIn.addVelocity(0,data.moveY.getAsDouble(), 0);
//		}
//		if(data.moveZ.isPresent()){
////			sender.motionZ += z.getAsDouble();
//			entityLivingBaseIn.addVelocity(0,0,data.moveZ.getAsDouble());
//		}
//
		if(entityLivingBaseIn.onGround){

			IBlockState state = world.getBlockState(entityLivingBaseIn.getPosition().down());
			if(state.getBlock()!=Blocks.AIR){
				ParticleHelper.MovingType.FOUNTAIN.spawnParticle(world, XYZPos.createFrom(entityLivingBaseIn), EnumParticleTypes.BLOCK_DUST, entityLivingBaseIn.getRNG(), 10, 0.1D, Block.getIdFromBlock(state.getBlock()));
			}

//			if(this.isStopOnLanded){
//				this.time = 65535;
//			}
		}
//		if(range>0 && (!data.isAttacked && data.isSingleAttack)){
//
//
//			RangedHelper.<Data>create(world,entityLivingBaseIn, range).setConsumer((self,target)->{
//				EntityLivingBase attacker = (EntityLivingBase) self.getOrigin();
//
//				if(data.additional!=null){
//					DamageSource source = DamageHelper.create(data.additional);
//					target.attackEntityFrom(source, (float) data.damage);
//				}
//
//				if(data.isStopOnHit){
//					attacker.removePotionEffect(UnsagaPotions.MOVING_STATE);
//				}
//				if(data.damageConsumer!=null){
//					data.damageConsumer.accept(self, target);
//				}
//				if(!data.potions.isEmpty()){
//					data.potions.forEach(in ->{
//						target.addPotionEffect(new PotionEffect(in,ItemUtil.getPotionTime(20),1));
//					});
//				}
////				SoundAndSFX.playSound(world, XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
//			}).setParent(data).invoke();
////			attacked = true;
//		}
//
//		if(data.isStopOnLanded && entityLivingBaseIn.onGround){
//			entityLivingBaseIn.removePotionEffect(UnsagaPotions.MOVING_STATE);
//		}
	}

//	public static Data getData(EntityLivingBase living){
//		return (Data) ExtendedPotionCapability.adapter.getCapability(living);
//	}
	public static PotionEffect create(int time,TechInvoker invoker,Consumer<IMovingStateAdapter> move){
		return new Effect(time,invoker,move);
	}

//	public static void setState(EntityLivingBase living,int time,int range,double x,double y,double z,AdditionalDamageData additional){
//		PotionEffect state = create(time,range);
//		living.addPotionEffect(state);
//		Data data = (Data) ExtendedPotionCapability.adapter.getCapability(living).getData(state.getPotion());
//
//		if(x>0){
//			data.moveX = OptionalDouble.of(x);
//		}
//		if(y>0){
//			data.moveY = OptionalDouble.of(y);
//		}
//		if(z>0){
//			data.moveZ = OptionalDouble.of(z);
//		}
//		data.additional = additional;
//	}

//
//	public static class Data implements IExtendedPotionData{
//
//
//		public void setDamage(float damage) {
//			this.damage = damage;
//		}
//
//		public void setAdditional(AdditionalDamageData additional) {
//			this.additional = additional;
//		}
//
//		public void setVelocity(double x,double y,double z) {
//			if(x>0){
//				this.moveX = OptionalDouble.of(x);
//			}
//			if(y>0){
//				this.moveY = OptionalDouble.of(y);
//			}
//			if(z>0){
//				this.moveZ = OptionalDouble.of(z);
//			}
//
//		}
//
//		public void setSingleAttack(boolean isSingleAttack) {
//			this.isSingleAttack = isSingleAttack;
//		}
//
//		public void setAttacked(boolean isAttacked) {
//			this.isAttacked = isAttacked;
//		}
//
//		public void setStopOnHit(boolean isStopOnHit) {
//			this.isStopOnHit = isStopOnHit;
//		}
//
//		public void setPotions(Set<Potion> potions) {
//			this.potions = potions;
//		}
//
//		public void setVelocityStopping(boolean par1){
//			this.isStopping = par1;
//		}
//		public void setDamageConsumer(BiConsumer<RangedHelper<Data>, EntityLivingBase> damageConsumer) {
//			this.damageConsumer = damageConsumer;
//		}
//
//		public void setConsumer(Consumer<Data> consumer) {
//			this.consumer = consumer;
//		}
//
//		public void setStopOnLanded(boolean par1){
//			this.isStopOnLanded = par1;
//		}
//		float damage = 0;
//		AdditionalDamageData additional;
//		OptionalDouble moveX = OptionalDouble.empty();
//		OptionalDouble moveY = OptionalDouble.empty();
//		OptionalDouble moveZ = OptionalDouble.empty();
//		boolean isSingleAttack = false;
//		boolean isAttacked = false;
//		boolean isStopOnHit = false;
//		boolean isStopOnLanded = false;
//		boolean isCancelHurt = true;
//		boolean isStopping =  false;
//		Set<Potion> potions = Sets.newHashSet();
//		@Nullable BiConsumer<RangedHelper<Data>,EntityLivingBase> damageConsumer;
//		@Nullable Consumer<Data> consumer;
//		@Override
//		public void writeToNBT(NBTTagCompound nbt) {
//			// TODO 自動生成されたメソッド・スタブ
//
//		}
//
//		@Override
//		public void readFromNBT(NBTTagCompound nbt) {
//			// TODO 自動生成されたメソッド・スタブ
//
//		}
//
//	}

	public static class Effect extends PotionEffect{

		TechInvoker invoker;
		Consumer<IMovingStateAdapter> behavior;
		boolean hasInitialized = false;
		public Effect(int durationIn, TechInvoker invoker,Consumer<IMovingStateAdapter> behavior) {
			super(UnsagaPotions.MOVING_STATE, durationIn, 0,false,false);
			this.invoker = invoker;
			this.behavior = behavior;
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public TechInvoker getInvoker(){
			return this.invoker;
		}

		/** 紐付けられた動作consumerを得る。*/
		public Consumer<IMovingStateAdapter> getMovingConsumer(){
			return this.behavior;
		}

		public boolean hasInitialized(){
			return this.hasInitialized;
		}
	}

}
