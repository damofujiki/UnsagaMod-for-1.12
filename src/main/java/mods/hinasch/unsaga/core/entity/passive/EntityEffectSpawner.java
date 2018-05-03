package mods.hinasch.unsaga.core.entity.passive;

import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityEffectSpawner extends Entity{

	int age = 0;
	int maxAge = 60;
	int dens = 10;
	double speed = 1.0D;
	public EntityEffectSpawner(World worldIn) {
		super(worldIn);
		// TODO 自動生成されたコンストラクター・スタブ

	}

	public void setMaxAge(int age){
		this.maxAge = age;
	}
	@Override
	protected void entityInit() {
		// TODO 自動生成されたメソッド・スタブ

	}

	public static Entity create(Entity origin,int dens,double speed){
		EntityEffectSpawner en = new EntityEffectSpawner(origin.getEntityWorld());
		VecUtil.setEntityPositionTo(en, origin);
		en.setFountainStatus(dens, speed);
		return en;
	}
	public void setFountainStatus(int dens,double speed){
		this.dens = dens;
		this.speed = speed;
	}
	@Override
    public void onUpdate()
    {
    	super.onUpdate();
    	if(WorldHelper.isClient(world)){
    		ParticleHelper.MovingType.FOUNTAIN.spawnParticle(world, XYZPos.createFrom(this), EnumParticleTypes.CRIT, rand, dens, speed);
    	}
		UnsagaMod.logger.trace("age", age);
    	age ++;
    	if(age>this.maxAge){

    		this.setDead();
    	}

    }
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
