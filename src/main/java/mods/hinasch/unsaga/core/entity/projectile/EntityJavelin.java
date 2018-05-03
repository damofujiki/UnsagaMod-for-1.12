package mods.hinasch.unsaga.core.entity.projectile;

import java.util.EnumSet;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityJavelin extends EntityThrowableWeapon implements IEntityAdditionalSpawnData{

	public static enum MoveType implements IIntSerializable{
		JAVELIN(0),SWINGING(1);

		private int meta;
		private MoveType(int meta){
			this.meta = meta;
		}
		@Override
		public int getMeta() {
			// TODO 自動生成されたメソッド・スタブ
			return meta;
		}

		public boolean isRotationInRender(){
			return this==SWINGING ? true : false;
		}
		public boolean isEffectiveGravity(){
			return this==JAVELIN ? true : false;
		}
		public static MoveType fromMeta(int meta){
			return HSLibs.fromMeta(MoveType.values(), meta);
		}
	}

	MoveType moveType = MoveType.JAVELIN;
	int ticks = 0;
	public EntityJavelin(World worldIn) {
		super(worldIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public EntityJavelin(World worldIn,EntityLivingBase thrower) {
		super(worldIn,thrower);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public void setMoveType(MoveType type){
		this.moveType = type;
	}

	public MoveType getMoveType(){
		return this.moveType;
	}

	@Override
	public void preRenderFix(Entity entity){
		if(this.getMoveType()==MoveType.JAVELIN){
			/** 縦にする */
			GlStateManager.rotate(-90f, 0f, 1f, 0f);
			GlStateManager.rotate(-45f, 0f, 0f, 1f);

		}
		if(this.getMoveType()==MoveType.SWINGING){
			/** 縦にする */
//			GlStateManager.rotate(-90f, 0f, 1f, 0f);
//			BlockPos pos = UnsagaMod.proxy.getDebugPos(1);
//			GlStateManager.rotate(UnsagaMod.proxy.getDebugPos(0).getX(), pos.getX(), pos.getY(), pos.getZ());
		}
	}

	@Override
	public Axis getRotationAxis(){
		return this.getMoveType()==MoveType.JAVELIN ? Axis.X : Axis.Z;
	}
	@Override
	public void fixRenderPosition(){
		GlStateManager.translate(0,0F , 0);
	}

	@Override
	public boolean hasRotationInRender(){
		return this.getMoveType().isRotationInRender();
	}
	@Override
    public void onUpdate()
    {
    	super.onUpdate();
//    	this.tick ++;
//    	if(this.tick>10000){
//    		this.setDead();
//    	}
    	ticks ++;
    	if(this.getMoveType()==MoveType.SWINGING){
    		world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow(1.0D),in ->in!=this.getThrower() && in!=this)
    		.forEach(in -> this.getSwingConsumer(in));
    		for(int i=0;i<9;i++){
    			Vec3d vec = this.getLookVec().normalize().scale(1.0D);
    			vec = vec.rotateYaw(45F*i);
    			vec = vec.rotatePitch(90F);
        		world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow(1.0D).offset(vec),in ->in!=this.getThrower() && in!=this)
        		.forEach(in -> this.getSwingConsumer(in));
    		}
    		if(this.ticksExisted % 5 == 0){
    			this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);
    		}
        	if(ticks>100){
        		this.setDead();
        	}
    	}


//    	UnsagaMod.logger.trace("inground", this.inGround);
    }

	public void getSwingConsumer(Entity target){
		if(target instanceof EntityThrowable){
			target.setDead();
		}
		if(target instanceof EntityLivingBase){
			AdditionalDamageData data = new AdditionalDamageData(AdditionalDamageData.getSource(getThrower()),0.5F,1,General.SPEAR);
			target.attackEntityFrom(DamageHelper.create(data), 0.5F);
		}
	}
	public static EntityJavelin create(TechInvoker invoker,EntityLivingBase target){
		EntityJavelin javelin =  new EntityJavelin(invoker.getWorld(),invoker.getPerformer());
		javelin.setThrowStack(invoker.getPerformer().getHeldItemMainhand().copy());
		javelin.setAttackDamage(invoker.getStrengthHP());
		if(EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_ASPECT, invoker.getPerformer())>0){
			javelin.setFire(100);
		}
		javelin.damageToWeapon(invoker.getCost());
		javelin.setKnockbackStrength(0.1F);
		javelin.setLPAttack(invoker.getStrength().lp().amount(), invoker.getStrength().lp().chances());
		invoker.getPerformer().setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
		return javelin;
	}
	public static Entity javelin(TechInvoker invoker,EntityLivingBase target){
		EntityJavelin javelin =  create(invoker, target);
		javelin.setMoveType(MoveType.JAVELIN);
		javelin.shoot(invoker.getPerformer(), invoker.getPerformer().rotationPitch, invoker.getPerformer().rotationYaw, -10F, 1.0F,1.0F);
		return javelin;
	}
	public static Entity swinging(TechInvoker invoker,EntityLivingBase target){
		EntityJavelin javelin =  create(invoker, target);
		javelin.setMoveType(MoveType.SWINGING);
		javelin.shoot(invoker.getPerformer(), invoker.getPerformer().rotationPitch, invoker.getPerformer().rotationYaw, 0, 0.01F,0);
//		VecUtil.setEntityPositionTo(javelin, invoker.getPerformer().getPosition().add(0, 1, 0));
//		javelin.setRotation(invoker.getPerformer().rotationYaw, invoker.getPerformer().rotationPitch);
		return javelin;
	}
	@Override
	public EnumSet<General> getDamageTypes() {
		// TODO 自動生成されたメソッド・スタブ
		return EnumSet.of(General.SPEAR);
	}

	@Override
    protected float getGravityVelocity()
    {
        return this.getMoveType().isEffectiveGravity() ? 0.03F : 0;
    }
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		// TODO 自動生成されたメソッド・スタブ
		buffer.writeInt(this.getMoveType().getMeta());
	}
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		// TODO 自動生成されたメソッド・スタブ
		int meta = additionalData.readInt();
		this.setMoveType(MoveType.fromMeta(meta));
	}
}
