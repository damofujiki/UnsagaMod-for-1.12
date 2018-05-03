package mods.hinasch.unsaga.core.entity.projectile;

import java.util.EnumSet;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.status.TargetHolderCapability;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityThrowingKnife extends EntityThrowableWeapon implements IEntityAdditionalSpawnData{

	MovingType type = MovingType.STRAIGHT;
	int tickKnife = 0;

	public static enum MovingType implements IIntSerializable{
		STRAIGHT,TRICK;

		@Override
		public int getMeta() {
			switch(this){
			case STRAIGHT:
				return 0;
			case TRICK:
				return 1;
			default:
				break;

			}
			return -1;
		}

		public float getGravity(float def){
			switch(this){
			case STRAIGHT:
				return def;
			case TRICK:
				return 0;
			default:
				break;

			}
			return def;
		}


		public static MovingType fromMeta(int meta){
			return HSLibs.fromMeta(MovingType.values(), meta);
		}
	}
	public EntityThrowingKnife(World worldIn) {
		super(worldIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}
    public EntityThrowingKnife(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }
	@Override
	public boolean hasRotationInRender(){
		return this.type == MovingType.STRAIGHT ? false : true;
	}
	@Override
	public EnumSet<General> getDamageTypes() {
		// TODO 自動生成されたメソッド・スタブ
		return EnumSet.of(General.SPEAR);
	}


	@Override
    public void onUpdate()
    {
    	super.onUpdate();
    	if(this.type==MovingType.TRICK){
    		if(WorldHelper.isClient(world)){
    			return;
    		}
    		Entity target = null;
    		if(this.tickKnife<100){
        		if(TargetHolderCapability.adapter.hasCapability(thrower) && TargetHolderCapability.adapter.getCapability(thrower).getTarget().isPresent()){
        			target = TargetHolderCapability.adapter.getCapability(thrower).getTarget().get();


        		}
    		}else{
    			target = this.getThrower();
    		}
    		if(target!=null){
    			Vec3d vec = target.getPositionVector().addVector(0, 0.5D, 0).subtract(this.getPositionVector()).normalize().scale(0.2D);
    			this.setVelocity(vec.x, vec.y, vec.z);
    		}

    		if(tickKnife>100){
    			if(this.getThrower().getEntityBoundingBox().contains(this.getPositionVector())){
    				this.setDead();
    			}
    		}

    	}
    	this.tickKnife ++;
    	if(this.tickKnife>200){
    		this.setDead();
    	}
    }
	@Override
	public void preRenderFix(Entity entity){

		if(this.type==MovingType.STRAIGHT){
			GlStateManager.rotate(45F, 1f, 0f, 0f);
			GlStateManager.rotate(90, 0, 1f, 0f);
		}
		if(this.type==MovingType.TRICK){
			GlStateManager.rotate(-90, 0f, 0f, 1f);
		}
	}
    @Override
    protected float getGravityVelocity()
    {
    	return type.getGravity(super.getGravityVelocity());
    }
	@Override
	protected void onEntityHit(RayTraceResult result){
		if(this.type==MovingType.STRAIGHT){

			this.setDead();
		}

	}
	@Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
    	super.readEntityFromNBT(compound);
    	if(compound.hasKey("type")){
    		this.type = MovingType.fromMeta(compound.getInteger("type"));
    	}
    }

	public void setMovingType(MovingType type){
		this.type = type;
	}
	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
    	super.writeEntityToNBT(compound);
    	compound.setInteger("type", this.type.getMeta());

    }
	public static EntityThrowingKnife create(TechInvoker invoker,EntityLivingBase target){
		EntityThrowingKnife knife =  new EntityThrowingKnife(invoker.getWorld(),invoker.getPerformer());
		knife.setThrowStack(invoker.getPerformer().getHeldItemMainhand().copy());
		knife.setAttackDamage(invoker.getStrengthHP());
		if(EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_ASPECT, invoker.getPerformer())>0){
			knife.setFire(100);
		}
		knife.damageToWeapon(invoker.getCost());
		knife.setKnockbackStrength(0.1F);
		knife.setLPAttack(invoker.getStrength().lp().amount(), invoker.getStrength().lp().chances());
		invoker.getPerformer().setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
		return knife;
	}

	public static Entity knifeThrow(TechInvoker invoker,EntityLivingBase target){
		EntityThrowingKnife throwingKnife =  create(invoker, target);
		throwingKnife.shoot(invoker.getPerformer(), invoker.getPerformer().rotationPitch, invoker.getPerformer().rotationYaw, 0, 2.0F,1.0F);
		return throwingKnife;
	}

	public static Entity trickThrow(TechInvoker invoker,EntityLivingBase target){
		EntityThrowingKnife throwingKnife =  create(invoker, target);
		throwingKnife.setMovingType(MovingType.TRICK);
		return throwingKnife;
	}
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		// TODO 自動生成されたメソッド・スタブ
		buffer.writeInt(this.type.getMeta());
	}
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		// TODO 自動生成されたメソッド・スタブ
		int meta = additionalData.readInt();
		this.type = MovingType.fromMeta(meta);
	}
}
