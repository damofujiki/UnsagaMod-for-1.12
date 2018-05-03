package mods.hinasch.unsaga.core.entity.projectile;

import java.util.EnumSet;
import java.util.OptionalDouble;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.status.TargetHolderCapability;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityFlyingAxe extends EntityThrowableWeapon implements IEntityAdditionalSpawnData {

	public static final ItemStack DEFAULT_RENDER_AXE = new ItemStack(Items.IRON_AXE);
	AxeMoveType type = AxeMoveType.NORMAL;
	int tickAxe = 0;
	public static enum AxeMoveType implements IIntSerializable{
		NORMAL,SKY_DRIVE;

		public OptionalDouble getGravity(){
			switch(this){
			case NORMAL:
				return OptionalDouble.empty();
			case SKY_DRIVE:
				return OptionalDouble.of(0);
			default:
				break;

			}
			return OptionalDouble.empty();
		}

		@Override
		public int getMeta() {
			switch(this){
			case NORMAL:
				return 0;
			case SKY_DRIVE:
				return 1;
			default:
				break;

			}
			return -1;
		}

		public static AxeMoveType fromMeta(int meta){
			return HSLibs.fromMeta(AxeMoveType.values(), meta);
		}
	}

	public EntityFlyingAxe(World worldIn) {
		super(worldIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}
    public EntityFlyingAxe(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }
	@Override
	public EnumSet<General> getDamageTypes() {
		// TODO 自動生成されたメソッド・スタブ
		return EnumSet.of(General.PUNCH,General.SWORD);
	}

//	@Override
//	public ItemStack getDefaultRenderStack(){
//		return this.DEFAULT_RENDER_AXE;
//	}
	public void setAxeMoveType(AxeMoveType type){
		this.type = type;
	}
	@Override
	public void onUpdate(){
		super.onUpdate();
		if(this.type==AxeMoveType.SKY_DRIVE){
			if(this.tickAxe>30){
				this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT,1.0F, 1.5F);
				if(WorldHelper.isClient(world)){
					return;
				}
				if(this.getThrower()==null){
					this.setDead();
				}
				if(this.getThrower()!=null && TargetHolderCapability.adapter.hasCapability(this.getThrower())){
					if(TargetHolderCapability.adapter.getCapability(this.getThrower()).getTarget().isPresent()){
						Entity target = TargetHolderCapability.adapter.getCapability(this.getThrower()).getTarget().get();
						if(target!=null){
					        Vec3d vec = VecUtil.getHeadingToEntityVec(this, target);
					        this.shoot(vec.x, vec.y, vec.z, 1.6F, 6);
						}else{
							this.setDead();
						}
					}
				}


			}
		}
		tickAxe ++;
	}

	@Override
	public void preRenderFix(Entity entity){
		/** 縦にする */
		GlStateManager.rotate(-90f, 0f, 1f, 0f);
	}

    @Override
    protected float getGravityVelocity()
    {
    	return (float) (this.type.getGravity().isPresent() ? this.type.getGravity().getAsDouble() : super.getGravityVelocity());
    }
	@Override
	public boolean hasRotationInRender(){
		return true;
	}

	@Override
	protected void onEntityHit(RayTraceResult result){
		super.onEntityHit(result);
		if(result.entityHit instanceof EntityPlayer){
			((EntityPlayer)result.entityHit).disableShield(true);
		}
	}
	@Override
	protected void onImpact(RayTraceResult result) {
		if(result.typeOfHit==Type.BLOCK){
			IBlockState state = world.getBlockState(result.getBlockPos());
			if(state.getMaterial()==Material.GLASS){
				if(state.getBlockHardness(world, result.getBlockPos())<=1.0F){
					this.destroyBlock(result, state);
				}
			}
		}
		super.onImpact(result);
	}
	public static EntityFlyingAxe create(TechInvoker invoker,EntityLivingBase target){
		EntityFlyingAxe axe =  new EntityFlyingAxe(invoker.getWorld(),invoker.getPerformer());
		axe.setThrowStack(invoker.getPerformer().getHeldItemMainhand().copy());
		axe.setAttackDamage(invoker.getStrengthHP());
		if(EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_ASPECT, invoker.getPerformer())>0){
			axe.setFire(100);
		}
		axe.damageToWeapon(invoker.getCost());
		axe.setKnockbackStrength(1.5F);
		axe.setLPAttack(invoker.getStrength().lp().amount(), invoker.getStrength().lp().chances());
		invoker.getPerformer().setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
		return axe;
	}
	public static Entity tomahawk(TechInvoker invoker,EntityLivingBase target){
		EntityFlyingAxe axe =  create(invoker, target);
		axe.shoot(invoker.getPerformer(), invoker.getPerformer().rotationPitch, invoker.getPerformer().rotationYaw, 0, 1.5F,1.0F);
		return axe;
	}
	public static Entity skyDrive(TechInvoker invoker,EntityLivingBase target){
		EntityFlyingAxe axe =  create(invoker, target);
		axe.shoot(invoker.getPerformer(), invoker.getPerformer().rotationPitch, invoker.getPerformer().rotationYaw, 0, 0,0);
		axe.setAxeMoveType(AxeMoveType.SKY_DRIVE);
		return axe;
	}
	@Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
    	super.readEntityFromNBT(compound);
    	if(compound.hasKey("type")){
    		this.type = AxeMoveType.fromMeta(compound.getInteger("type"));
    	}
    }

	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
    	super.writeEntityToNBT(compound);
    	compound.setInteger("type", this.type.getMeta());

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
		this.setAxeMoveType(AxeMoveType.fromMeta(meta));
	}
}
