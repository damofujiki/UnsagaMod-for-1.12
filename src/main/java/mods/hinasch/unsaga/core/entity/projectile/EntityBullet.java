package mods.hinasch.unsaga.core.entity.projectile;


import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityBullet extends EntityThrowable implements IProjectile{


	private static final DataParameter<Boolean> CRIT = EntityDataManager.<Boolean>createKey(EntityBullet.class, DataSerializers.BOOLEAN);
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private Block inTile;
	private int inData = 0;
	private int rangeStoneShower = 10;


	private boolean isStoneShower = false;

	private boolean inGround = false;

	/** 1 if the player can pick up the arrow */
	public int canBePickedUp = 0;

	/** Seems to be some sort of timer for animating an arrow. */
	public int arrowShake = 0;

	/** The owner of this arrow. */
	public EntityLivingBase shootingEntity;
	private int ticksInGround;
	private int ticksInAir = 0;
	private double damage = 5.0D;

	/** The amount of knockback an arrow applies when it hits a mob. */
	private int knockbackStrength;
	public EntityBullet(World par1World) {
		super(par1World);
//		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
	}


	//	public EntityBarrett(World par1World, EntityLivingBase par2EntityLiving, float par4, float par5)
	//	{
	//		super(par1World);
	//		this.renderDistanceWeight = 10.0D;
	//		this.shootingEntity = par2EntityLiving;
	//
	//		if (par2EntityLiving instanceof EntityPlayer)
	//		{
	//			this.canBePickedUp = 0;
	//		}
	//
	//		this.posY = par2EntityLiving.posY + (double)par2EntityLiving.getEyeHeight() - 0.10000000149011612D;
	//		double var6 = par3EntityLiving.posX - par2EntityLiving.posX;
	//		double var8 = par3EntityLiving.posY + (double)par3EntityLiving.getEyeHeight() - 0.699999988079071D - this.posY;
	//		double var10 = par3EntityLiving.posZ - par2EntityLiving.posZ;
	//		double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var10 * var10);
	//
	//		if (var12 >= 1.0E-7D)
	//		{
	//			float var14 = (float)(Math.atan2(var10, var6) * 180.0D / Math.PI) - 90.0F;
	//			float var15 = (float)(-(Math.atan2(var8, var12) * 180.0D / Math.PI));
	//			double var16 = var6 / var12;
	//			double var18 = var10 / var12;
	//			this.setLocationAndAngles(par2EntityLiving.posX + var16, this.posY, par2EntityLiving.posZ + var18, var14, var15);
	//			this.yOffset = 0.0F;
	//			float var20 = (float)var12 * 0.2F;
	//			this.setThrowableHeading(var6, var8 + (double)var20, var10, par4, par5);
	//		}
	//	}
	public EntityBullet(World par1World, EntityLivingBase par2EntityLiving, float par3)
	{
		this(par1World,par2EntityLiving,par3,1.0F);
	}

	public EntityBullet(World par1World, EntityLivingBase par2EntityLiving, float par3,float speed)
	{
		super(par1World);
//		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = par2EntityLiving;
		//
		//        if (par2EntityLiving instanceof EntityPlayer)
		//        {
		//            this.canBePickedUp = 1;
		//        }

		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY + (double)par2EntityLiving.getEyeHeight(), par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);

		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		this.shoot(this.motionX, this.motionY, this.motionZ, par3*2.5F, 1.0F);
	}


	@Override
	public void onUpdate(){
		super.onUpdate();
		if (inGround) {
			if (this.shootingEntity == null) {
				setDead();
			}

			BlockPos bpos = new BlockPos(xTile,yTile,zTile);
			IBlockState state = world.getBlockState(bpos);
			Block i = state.getBlock();
			if(i != inTile) {
				setDead();
			} else {
				ticksInGround++;
				if(ticksInGround == 1200) {
					setDead();
				}

				if (isBurning() && (ticksInGround == 1)) {
					// 燃えてるときの光源判定
					//					mod_IFN_FN5728Guns.Debug("light");
					world.setLightFor(EnumSkyBlock.BLOCK,bpos, 0xff);
					world.checkLightFor(EnumSkyBlock.BLOCK, bpos.east());
					world.checkLightFor(EnumSkyBlock.BLOCK,bpos.west());
					world.checkLightFor(EnumSkyBlock.BLOCK,bpos.down());
					world.checkLightFor(EnumSkyBlock.BLOCK,bpos.up());
					world.checkLightFor(EnumSkyBlock.BLOCK,bpos.north());
					world.checkLightFor(EnumSkyBlock.BLOCK,bpos.south());
				}
			}
			return;
		} else {
			ticksInAir++;
		}
	}
	@Override
	protected void onImpact(RayTraceResult movingobjectposition) {

		if (movingobjectposition.entityHit != null) {
			// ダメージの距離減衰を付けた

			if(movingobjectposition.entityHit == this.shootingEntity){
				return;
			}
//			Unsaga.debug(movingobjectposition.entityHit);
			float lfd = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
			int ldam = (int)Math.ceil((double)lfd * damage * 1.0D );
			if (isBurning()) {
				movingobjectposition.entityHit.setFire(5);
			}

			if (this.shootingEntity instanceof EntityPlayer) {
				// RSHUD対策・当たり判定
				((EntityPlayer)this.shootingEntity).addStat(StatList.DAMAGE_DEALT, ldam);
			}
			//			mod_IFN_FN5728Guns.Debug(String.format("ss190 - %d : %s", ldam, worldObj.isRemote ? "CL" : "SV"));
			if (movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), ldam)) {
				// ダメージが通った
				if (movingobjectposition.entityHit instanceof EntityLivingBase) {
					EntityLivingBase lel = (EntityLivingBase)movingobjectposition.entityHit;
					// ノックバック
					if (knockbackStrength > 0) {
						if (lfd > 0.0F) {
							lel.addVelocity(
									(motionX * (double)knockbackStrength * 0.60000002384185791D) / (double)lfd,
									(motionY * (double)knockbackStrength * 0.60000002384185791D) / (double)lfd + 0.1D,
									(motionZ * (double)knockbackStrength * 0.60000002384185791D) / (double)lfd);
						}
					}
					// 反射
					if (this.shootingEntity != null) {
                        EnchantmentHelper.applyThornEnchantments(lel, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, lel);
					}


				}
			}
			movingobjectposition.entityHit.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, rand.nextFloat() * 0.2F + 0.9F);
			motionX = movingobjectposition.hitVec.x - posX;
			motionY = movingobjectposition.hitVec.y - posY;
			motionZ = movingobjectposition.hitVec.z - posZ;
			inGround = true;
			setDead();
		} else {
			// ブロックにあたった
			BlockPos hitpos = movingobjectposition.getBlockPos();
//			xTile = movingobjectposition.blockX;
//			yTile = movingobjectposition.blockY;
//			zTile = movingobjectposition.blockZ;
			IBlockState state = world.getBlockState(hitpos);
			inTile = state.getBlock();
			//            inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
			// 窓ガラス、鉢植の破壊
			if (inTile == Blocks.GLASS_PANE || inTile == Blocks.FLOWER_POT || inTile == Blocks.GLOWSTONE) {
				motionX *= 0.8;
				motionY *= 0.8;
				motionZ *= 0.8;

				onBlockDestroyed(hitpos);
			} else {
				if (inTile instanceof BlockTNT) {
					// TNTを起爆
					//					Block.blocksList[inTile].onBlockDestroyedByExplosion(worldObj, xTile, yTile, zTile, new Explosion(worldObj, thrower, xTile, yTile, zTile, 0.0F));
					((BlockTNT)inTile).explode(world, hitpos, state, this.shootingEntity);
					world.setBlockToAir(hitpos);
					setDead();
				} else {
				}
				motionX = movingobjectposition.hitVec.x - posX;
				motionY = movingobjectposition.hitVec.y - posY;
				motionZ = movingobjectposition.hitVec.z - posZ;
				inGround = true;


			}
			isAirBorne = true;
			velocityChanged = true;
		}
		if (inGround) {
			for (int i = 0; i < 8; i++) {
				world.spawnParticle(EnumParticleTypes.SNOWBALL,
						movingobjectposition.hitVec.x,
						movingobjectposition.hitVec.y,
						movingobjectposition.hitVec.z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public void onBlockDestroyed(BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block bid = state.getBlock();
		int bmd = bid.getMetaFromState(state);
		Block block = bid;
		if(block == Blocks.AIR) {
			return;
		}
		world.playEvent(2001, pos, Block.getIdFromBlock(bid) + (bmd  << 12));
		if(!world.isRemote){
			boolean flag = world.setBlockToAir(pos);
			if (block != null && flag) {
				block.onBlockDestroyedByPlayer(world, pos, state);

			}
		}
	}

	@Override
	public void setVelocity(double par1, double par3, double par5) {
		// 弾速が早過ぎるとパケットの方で速度制限がかかっているため弾道が安定しなくなる。
		// 基本的にVelocityのアップデートが無ければ問題ないが、燃えているとおかしくなる。
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float var7 = MathHelper.sqrt(par1 * par1 + par5 * par5);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)var7) * 180.0D / Math.PI);
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	protected void entityInit()
	{
		xTile = -1;
		yTile = -1;
		zTile = -1;
		inTile = null;
		ticksInAir = 0;
		ticksInGround = 0;

		damage = 2D;
		knockbackStrength = 0;
		// 弾道を安定させるために耐火属性を付与
		if (world != null) {
			isImmuneToFire = !world.isRemote;
		}
		this.getDataManager().register(CRIT, false);
	}

	public double getDamage()
	{
		return this.damage;
	}

	public void setDamage(double par1)
	{
		this.damage = par1;
	}

	public void setKnockbackStrength(int par1)
	{
		this.knockbackStrength = par1;
	}

	public void setIsCritical(boolean par1)
	{
		this.getDataManager().set(CRIT, par1);
		this.getDataManager().setDirty(CRIT);
	}
}
