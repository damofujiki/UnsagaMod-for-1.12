package mods.hinasch.unsaga.core.entity.projectile;

import java.util.List;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.world.XYZPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityDamageableItem extends EntityItem{

	public EntityLivingBase getShooter() {
		return shooter;
	}

	public EntityDamageableItem setShooter(EntityLivingBase shooter) {
		this.shooter = shooter;
		return this;
	}

	EntityLivingBase shooter;
	public EntityDamageableItem(World worldIn) {
		super(worldIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

    public EntityDamageableItem(World worldIn, double x, double y, double z)
    {
        super(worldIn,x,y,z);

    }
    public EntityDamageableItem(World worldIn, double x, double y, double z, ItemStack stack)
    {
        super(worldIn, x, y, z,stack);
    }

    @Override
    public void onUpdate()
    {

    	super.onUpdate();

    	if(this.world.rand.nextInt(5)==0){
    		ClientHelper.spawnParticle(world, EnumParticleTypes.VILLAGER_HAPPY, XYZPos.createFrom(this)	, new XYZPos(0,0.01D,0));
    	}
    	List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(),input -> input!=getShooter());

    	entities.remove(this);

    	entities.forEach(input -> input.attackEntityFrom(DamageSource.causeMobDamage(getShooter()), 1.0F));

    	if(this.onGround){
    		EntityItem entityItem = new EntityItem(this.world,posX,posY,posZ,this.getItem());
    		if(!this.world.isRemote){
    			this.world.spawnEntity(entityItem);
    		}
    		this.setDead();
    	}
    }
}
