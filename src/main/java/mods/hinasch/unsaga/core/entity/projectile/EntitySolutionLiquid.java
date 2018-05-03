package mods.hinasch.unsaga.core.entity.projectile;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.entity.EntityThrowableBase;
import mods.hinasch.lib.entity.EntityThrowableItem;
import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.network.SoundPacket;
import mods.hinasch.lib.particle.PacketParticle;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.ScannerNew;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsagamagic.spell.SpellRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySolutionLiquid extends EntityThrowableBase{

	private static final DataParameter<Byte> TYPE  = EntityDataManager.<Byte>createKey(EntityThrowableItem.class, DataSerializers.BYTE);
	protected float damageHP;
	protected float damageLP;

	public EntitySolutionLiquid(World par1World) {
		super(par1World);
		this.damageHP = 1.0F;
		this.damageLP = 0.1F;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	protected void entityInit()
    {
		super.entityInit();
		this.getDataManager().register(TYPE, (byte)0);
    }
    public EntitySolutionLiquid(World par1World, EntityLivingBase par2EntityLivingBase)
    {
    	super(par1World,par2EntityLivingBase);
		this.damageHP = 1.0F;
		this.damageLP = 0.1F;
    }
	@Override
	protected void onImpact(RayTraceResult mop) {
		if(this.isThunderCrap()){
			this.doThunderCrap(mop);
		}else{
			super.onImpact(mop);
		}



	}

	//サンダークラップ部分（サンダークラップと共用なので）
	private void doThunderCrap(RayTraceResult mop) {
		SpellRegistry spells = SpellRegistry.instance();
		if(mop.typeOfHit==RayTraceResult.Type.BLOCK || mop.typeOfHit==RayTraceResult.Type.ENTITY){
			DamageSourceUnsaga ds = DamageSourceUnsaga.createProjectile(this.getThrower(),this,this.damageLP,General.MAGIC).setSubTypes(Sub.ELECTRIC);
			AxisAlignedBB bb = HSLibs.getBounding(XYZPos.createFrom(this), 2, 1);


//			RangeDamage.create(worldObj).causeDamage(ds, bb, spells.thunderCrap.getEffectStrength().hp());


			if(WorldHelper.isServer(world)){


	            SoundPacket.sendToAllAround(PacketSound.atEntity(SoundEvents.ENTITY_FIREWORK_BLAST, this), PacketUtil.getTargetPointNear(this));
	            HSLib.getPacketDispatcher().sendToAllAround(PacketParticle.toEntity(EnumParticleTypes.CRIT, this, 15), PacketUtil.getTargetPointNear(this));


	            if(ScannerNew.create().base(this).range(1).ready().stream()
	            .anyMatch(in -> this.world.getBlockState(in).getBlock()==Blocks.WATER || this.world.getBlockState(in).getBlock()==Blocks.FLOWING_WATER || this.world.isRaining())){
	    			RangedHelper.create(world, this.getThrower(), HSLibs.getBounding(XYZPos.createFrom(this), 4, 4))
	    			.setConsumer((self,target)->{
	    				target.attackEntityFrom(ds, damageHP);
	    			}).invoke();
	            }else{
	    			RangedHelper.create(world, this.getThrower(), HSLibs.getBounding(XYZPos.createFrom(this), 2, 2))
	    			.setConsumer((self,target)->{
	    				target.attackEntityFrom(ds, damageHP);
	    			}).invoke();
	            }
//				UnsagaMod.packetDispatcher.sendToAllAround(PacketClientThunder.create(XYZPos.createFrom(this)), PacketUtil.getTargetPointNear(this));
				//液体のキャッチが不安定なのでもうちょっと範囲広げる
//				if(WorldHelper.findNearMaterial(this.worldObj,Material.WATER, XYZPos.createFrom(this), 15)!=null){
//
//					XYZPos pos = WorldHelper.findNearMaterial(worldObj,Material.WATER, XYZPos.createFrom(this), 15);
//					RangedHelper.create(worldObj, this.getThrower(), HSLibs.getBounding(pos, 2, 1))
//					.setConsumer((self,target)->{
//						target.attackEntityFrom(ds, damageHP);
//					}).invoke();
////					RangeDamage.create(worldObj).causeDamage(ds, HSLibs.getBounding(pos, 2, 1), spells.thunderCrap.getEffectStrength().hp());
////					RangeDamageHelper.causeDamage(worldObj, null, bb, ds, spells.thunderCrap.getStrHurtHP());
////					Unsaga.debug("液体発見");
//					Set<IBlockState> compare = Sets.newHashSet();//new PairID(Blocks.water,0).setCheckMetadata(false);
//					compare.add(Blocks.WATER.getDefaultState());
//					compare.add(Blocks.FLOWING_WATER.getDefaultState());//.sameBlocks.add(new PairID(Blocks.flowing_water,0).setCheckMetadata(false));
//
//					HSLibEvents.scannerEventPool.addEvent(new ScannerElectricShock(worldObj,10,compare,pos,this.getThrower()));
//				}


			}
				this.setDead();
		}

	}



	public void setDamage(float hp,float lp){
		this.damageHP = hp;
		this.damageLP = lp;
	}

	public boolean isThunderCrap(){
		return this.getDataManager().get(TYPE) == 1;
	}
	public void setThunderCrap(){
		this.getDataManager().set(TYPE, (byte)1);
		this.getDataManager().setDirty(TYPE);
	}
	public boolean isPoison(){
		return this.getDataManager().get(TYPE) == 2;
	}
	public void setPoison(){
		this.getDataManager().set(TYPE, (byte)2);
		this.getDataManager().setDirty(TYPE);
	}
	@Override
	public DamageSource getDamageSource(RayTraceResult result) {
		// TODO 自動生成されたメソッド・スタブ
		return DamageSourceUnsaga.createProjectile(this.getThrower(),this,damageLP,General.MAGIC);
	}
	public void onImpactBlock(RayTraceResult result){
		super.onImpactBlock(result);
		this.setDead();
	}
	public void onImpactEntity(RayTraceResult result){
		Entity hitEntity = result.entityHit;

		if(hitEntity!=this.getThrower()){
			if(hitEntity instanceof EntityLivingBase){
				EntityLivingBase living = (EntityLivingBase)hitEntity;
				living.attackEntityFrom(DamageSourceUnsaga.createProjectile(this.getThrower(),this,damageLP,General.PUNCH), damageHP);
				if(this.isPoison()){
					living.addPotionEffect(new PotionEffect(MobEffects.POISON,ItemUtil.getPotionTime(6),0));
				}
			}
		}

		super.onImpactEntity(result);
	}
}
