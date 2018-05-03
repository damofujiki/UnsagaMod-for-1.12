package mods.hinasch.unsaga.core.entity.projectile;

import java.util.EnumSet;

import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.lib.util.SoundAndSFX.SafeBlockHandler;
import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.lp.LPAttribute;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class EntityThrowableUnsaga extends EntityThrowable{

	float attackDamage = 0;
	float knockback = 0;
	LPAttribute  lpDamage = new LPAttribute(0,0);
	public EntityThrowableUnsaga(World worldIn) {
		super(worldIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}
    public EntityThrowableUnsaga(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }
	@Override
	protected void onImpact(RayTraceResult result) {
		// TODO 自動生成されたメソッド・スタブ

		if(result.entityHit instanceof EntityLivingBase && result.entityHit!=this.getThrower() && WorldHelper.isServer(getEntityWorld())){
			this.attackEntity(result.entityHit);
			this.onEntityHit(result);
		}
	}

	protected void attackEntity(Entity hitEntity){
		AdditionalDamageData data = new AdditionalDamageData(DamageSource.causeThrownDamage(thrower, this),this.getDamage().lp(),this.getDamageTypes());
		if(this.isBurning()){
			data.setSubTypes(Sub.FIRE);
			hitEntity.setFire(50);
		}
		if(!this.getSubDamageTypes().isEmpty()){
			data.setSubTypes(getSubDamageTypes());
		}
		hitEntity.attackEntityFrom(DamageHelper.create(data), this.getDamage().hp());

		VecUtil.knockback(this, (EntityLivingBase) hitEntity, this.getKnockbackStrength(), 0.1D);
	}
	protected void onEntityHit(RayTraceResult result){
		this.setDead();
	}

	public abstract EnumSet<General> getDamageTypes();

	public EnumSet<Sub> getSubDamageTypes(){
		return EnumSet.noneOf(Sub.class);
	}

	public DamageComponent getDamage(){
		return DamageComponent.of(this.getAttackDamage(), this.getLPAttack());
	}


	public float getKnockbackStrength(){
		return this.knockback;
	}

	public void setKnockbackStrength(float knock){
		this.knockback = knock;
	}

	public float getAttackDamage(){
		return this.attackDamage;
	}

	public void setAttackDamage(float damage){
		this.attackDamage = damage;
	}

	public LPAttribute getLPAttack(){
		return this.lpDamage;
	}

	public void setLPAttack(float prob,int chances){

		this.lpDamage = new LPAttribute(prob,chances);
	}

	@Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
    	super.readEntityFromNBT(compound);
    	if(compound.hasKey("damage")){
    		this.attackDamage = compound.getFloat("damage");
    	}
    	if(compound.hasKey("knockback")){
    		this.knockback = compound.getFloat("knockback");
    	}
    	this.lpDamage = LPAttribute.RESTORE.apply(compound);
    }


	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
    	super.writeEntityToNBT(compound);
    	compound.setFloat("damage", this.getAttackDamage());
    	compound.setFloat("knockback", this.getKnockbackStrength());
    	this.lpDamage.writeToNBT(compound);

    }
	public void destroyBlock(BlockPos pos,IBlockState state,boolean isExtinguish){
		if(isExtinguish){
			this.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 1.0F, 1.0F);
			World world = this.getEntityWorld();
			if(WorldHelper.isServer(world)){
				SafeBlockHandler runnable = new SafeBlockHandler(world, state,pos, false);
				WorldHelper.getWorldServer(world).addScheduledTask(runnable);
			}
		}else{
			SoundAndSFX.playBlockBreakSFX(world,pos,state);

		}
	}
	public void destroyBlock(RayTraceResult result,IBlockState state){
		this.destroyBlock(result.getBlockPos(), state,false);
	}
	public void destroyBlock(RayTraceResult result,IBlockState state,boolean isExtinguish){
		this.destroyBlock(result.getBlockPos(), state,isExtinguish);
	}
}
