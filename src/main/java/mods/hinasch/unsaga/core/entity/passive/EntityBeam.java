package mods.hinasch.unsaga.core.entity.passive;

import javax.annotation.Nullable;

import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBeam extends Entity{

//	private static final DataParameter<Integer> TARGET_ID = EntityDataManager.<Integer>createKey(EntityBeam.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> OWNER_ID = EntityDataManager.<Integer>createKey(EntityBeam.class, DataSerializers.VARINT);
//	final TechInvoker invoker;
	public int tick;
	public static final double ATTACK_DISTANCE = 25.0D;
	public DamageComponent damage = DamageComponent.ZERO;
	public int decay = 0;

//	public int innerRotation;
//	public int moveCount = 0;
	double rotateVelocity = 0;
	public EntityBeam(World w){
		super(w);
//		this.invoker = null;
	}

	@Deprecated
	public EntityBeam(World worldIn,TechInvoker invoker) {
		super(worldIn);
//		this.invoker = invoker;
		this.tick = 0;
//		this.innerRotation = rand.nextInt(10000);
	}

	public @Nullable EntityLivingBase getOwner(){
		int id = this.getDataManager().get(OWNER_ID);

		return (EntityLivingBase) this.world.getEntityByID(id);
	}

	public void setOwner(EntityLivingBase par1){

		if(par1!=null){
			this.dataManager.set(OWNER_ID, par1.getEntityId());
		}

	}
//	public @Nullable EntityLivingBase getTarget(){
//		int id = this.getDataManager().get(TARGET_ID);
//
//		return (EntityLivingBase) this.world.getEntityByID(id);
//	}

//    public void setPosition(double x, double y, double z)
//    {
//        this.posX = x;
//        this.posY = y;
//        this.posZ = z;
//        float f = this.width / 2.0F;
//        float f1 = this.height;
//
//       	Vec3 vec3 = (new Vec3(this.posX-1,this.posY-1,this.posZ+5)).rotateYaw(this.rotationYaw);;
//    	Vec3 vec32 =( new Vec3(this.posX+1,this.posY+1,this.posZ)).rotateYaw(this.rotationYaw);
//        this.setEntityBoundingBox(new AxisAlignedBB(vec3.xCoord,vec3.yCoord,vec3.zCoord,vec32.xCoord,vec32.yCoord,vec32.zCoord));
//    }
//    public AxisAlignedBB getEntityBoundingBox()
//    {
//        return this.boundingBox;
//    }
//	public void setTarget(EntityLivingBase par1){
//		if(par1!=null){
//			this.dataManager.set(TARGET_ID, par1.getEntityId());
//		}
//
//	}

	public void setDamage(DamageComponent dm){
		this.damage = dm;
	}
	public DamageComponent getDamage(){
		return this.damage;
	}
	@Override
	protected void entityInit() {


//		this.getDataManager().register(TARGET_ID, -1);
		this.getDataManager().register(OWNER_ID, -1);
	}

	private double getVelocity(double ownerPos,double pos,double scale){
		return Double.compare(ownerPos, pos) * scale;
	}

	@Override
    public void onUpdate()
    {
        super.onUpdate();
        if(this.ticksExisted % 4 ==0){
        	this.tick ++;
        }
//        ++this.innerRotation;
        if(this.getOwner()==null){
        	this.setDead();
        }



//        int count = 0;
//        if(this.getOwner()!=null &&this.getTarget()!=null){
//        	BlockPos pos = this.getTarget().getPosition();
//        	if(count<5){
//        		Vec3d vec =  this.getTarget().getPositionVector().subtract(getPositionVector()).normalize().scale(1.0D);
//        		this.setVelocity(vec.x, 0, vec.z);
//        		this.posX += this.motionX;
//        		this.posZ += this.motionZ;
//        		count ++;
//        		UnsagaMod.logger.trace("vec", vec);
//        	}else{
//        		count = 0;
//        		this.setPosition(this.getOwner().posX, this.getOwner().posY, this.getOwner().posZ);
//        	}
//
//        }
//        if(this.getOwner()!=null){
////        	Vec3d vec = this.getOwner().getLookVec();

//
////        	if(this.getOwner().posX>this.posX){
////        		this.addVelocity(, y, z);
////        	}
////        	Float.compare(this.getOwner().rotationYaw, this.rotationYaw);
////        	this.setVelocity(this.getVelocity(this.getOwner().posX, this.posX, 1),this.getVelocity(this.getOwner().posY, this.posY, 1)
////        			,this.getVelocity(this.getOwner().posZ, this.posZ, 1));
//        	rotateVelocity += this.getVelocity(this.getOwner().rotationYaw, this.rotationYaw, 0.5D);
//        	this.rotationYaw += rotateVelocity;
        	if(this.getOwner()!=null){
            	WorldHelper.setEntityPosition(this, XYZPos.createFrom(getOwner()));
            	double distance = ATTACK_DISTANCE;
            	int resolution = 8;
            	double step = distance / resolution;
            	Vec3d vec = this.getLookVec().scale(step);
            	UnsagaMod.logger.trace("vec", vec.x);
            	for(int i=0;i<5;i++){
            		AxisAlignedBB offsetBB = this.getEntityBoundingBox().offset(vec.x*(i+1), 0, vec.z*(i+1));
            		this.world.getEntitiesWithinAABB(EntityLivingBase.class, offsetBB,in -> in!=this.getOwner())
            		.forEach(in ->{
            			in.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(AdditionalDamageData.getSource(getOwner()),this.getDamage().lp(),General.SPEAR))
            					, MathHelper.clamp(this.getDamage().hp()-decay, 1, 65535F));
            			decay += 1;
            		});
            	}
        	}

////        	XYZPos pos = XYZPos.createFrom(this.getOwner());
////        	this.setPosition(pos.dx+vec.x, pos.dy, pos.dz+vec.z);
////        	this.setRotation(this.getOwner().rotationYaw, 0);
////        	UnsagaMod.logger.trace("yaw",WorldHelper.isServer(getEntityWorld()), this.rotationYaw);
//        }


        if(this.tick>20){
        	this.setDead();
        }
//
//        if(this.getOwner()!=null && this.getTarget()!=null){
//            WorldHelper.setEntityPosition(this, XYZPos.createFrom(this.getOwner()));
//
//            final EntityLivingBase p = this.getOwner();
//            final EntityLivingBase t = this.getTarget();
//
//        	List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10.0D, 10.0D, 10.0D), input ->{
//				if(input!=getOwner()){
//					EntityLivingBase el = (EntityLivingBase) input;
//
////					Vec3d added = new Vec3d(p.posX,p.posY+0.5D,p.posZ).addVector(lookvec.xCoord, 0, lookvec.zCoord);
//					for(int i=0;i<5;i++){
//						double d1 = (t.posX-p.posX)/5*(i+1);
//						double d2 = (t.posZ-p.posZ)/5*(i+1);
//						Vec3d disvec = new Vec3d(d1,0,d2);
//						Vec3d added = p.getPositionVector().add(disvec);
////						Unsaga.debug(added);
//    					if(el.getEntityBoundingBox().grow(0.25D, 0.25D, 0.25D).contains(added)){
//    						return true;
//    					}
//					}
//
//
//				}
//				return false;
//        	});
//
//
//        	if(WorldHelper.isServer(world)){
//            	list.forEach(input ->{
////            		LPAttribute lpd = invoker.getModifiedStrength().lp();
//    				DamageSource ds = DamageHelper.create(new AdditionalDamageData(invoker,General.SPEAR));
////    				DamageSourceUnsaga uds = DamageSourceUnsaga.fromVanilla(ds);
////    				uds.setDamageTypeUnsaga(General.SPEAR);
////    				uds.setStrLPHurt(invoker.getModifiedStrength().lp());
//
//    				input.attackEntityFrom(ds,invoker.getStrength().hp());
//            	});
//        	}
//
//
//
////            if(this.rotationYaw<this.getOwner().rotationYaw){
////            	this.rotationYaw +=10;
////            }else{
////            	this.rotationYaw -=10;
////            }
//        }
//
//
//
//    	//this.setEntityBoundingBox(new AxisAlignedBB(vec3.xCoord,vec3.yCoord,vec3.zCoord,vec32.xCoord,vec32.yCoord,vec32.zCoord));
//        if(this.tick>100){
////        	Unsaga.debug(this.getClass(),"ビーム消します");
//        	this.setDead();
//        }
    }

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {


	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		// TODO 自動生成されたメソッド・スタブ


	}

}
