package mods.hinasch.unsaga.core.entity.projectile;

import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.world.WorldHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityThrowableWeapon extends EntityThrowableUnsaga{


    private static final DataParameter<ItemStack> ITEM_STACK = EntityDataManager.<ItemStack>createKey(EntityThrowableWeapon.class, DataSerializers.ITEM_STACK);
    int tick = 0;
    boolean isRenderRotate = false;


	public EntityThrowableWeapon(World worldIn) {
		super(worldIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);
		if(result.typeOfHit==Type.BLOCK){
            this.setDead();
		}
	}
    public EntityThrowableWeapon(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }
	@Override
    protected void entityInit(){
		super.entityInit();
		this.getDataManager().register(ITEM_STACK, ItemStack.EMPTY);


	}

	/**
	 * 自分の向きは適用されているのでここでは横や縦に倒したりする
	 * @param entity
	 */
	@SideOnly(Side.CLIENT)
	public void preRenderFix(Entity entity){

	}

	@SideOnly(Side.CLIENT)
	public boolean hasRotationInRender(){
		return this.isRenderRotate;
	}

	@SideOnly(Side.CLIENT)
	public Axis getRotationAxis(){
		return Axis.X;
	}

//	public ItemStack getDefaultRenderStack(){
//		return null;
//	}

	@SideOnly(Side.CLIENT)
	public void fixRenderPosition(){
		GlStateManager.translate(0,1.5F , 0);
	}

	@SideOnly(Side.CLIENT)
	public void setRotationInRender(boolean par1){
		this.isRenderRotate = par1;
	}
	@Override
    public void onUpdate()
    {
    	super.onUpdate();
//    	this.tick ++;
//    	if(this.tick>10000){
//    		this.setDead();
//    	}


//    	UnsagaMod.logger.trace("inground", this.inGround);
    }

	public void damageToWeapon(int damage){
		if(!this.getThrowStack().isEmpty()){
			this.getThrowStack().damageItem(damage, getThrower());
		}
	}
	@Override
    public void setDead()
    {

		if(!this.getThrowStack().isEmpty() && !this.isDead && WorldHelper.isServer(world)){
			EntityItem entityItem = new EntityItem(world, this.posX, this.posY, this.posZ, this.getThrowStack());
			entityItem.setPickupDelay(10);
			entityItem.setAlwaysRenderNameTag(true);
			entityItem.setCustomNameTag("HERE");
			entityItem.setGlowing(true);
			WorldHelper.safeSpawn(world, entityItem);
		}
        this.isDead = true;
    }

	@Override
    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy)
    {
		super.shoot(entityThrower, rotationPitchIn, rotationYawIn, pitchOffset, velocity, inaccuracy);
    }
	@Override
	protected void onEntityHit(RayTraceResult result){
		this.setDead();
	}
	public void setThrowStack(ItemStack is){
		this.getDataManager().set(ITEM_STACK, is);
	}

	public ItemStack getThrowStack(){
		return this.getDataManager().get(ITEM_STACK);
	}

	@Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
    	super.readEntityFromNBT(compound);
    	if(compound.hasKey("item")){
    		NBTTagCompound child = (NBTTagCompound) compound.getTag("item");
    		this.setThrowStack(new ItemStack(child));
    	}
    }

	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
    	super.writeEntityToNBT(compound);
    	if(!this.getThrowStack().isEmpty()){
    		NBTTagCompound child = UtilNBT.compound();
    		this.getThrowStack().writeToNBT(child);
    		compound.setTag("item", child);
    	}

    }
}
