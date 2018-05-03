package mods.hinasch.unsaga.core.entity.projectile;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.realmsclient.util.Pair;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.network.SoundPacket;
import mods.hinasch.lib.particle.PacketParticle;
import mods.hinasch.lib.particle.ParticleHelper.MovingType;
import mods.hinasch.lib.world.ScannerNew;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.common.specialaction.option.TargetOptions;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsagamagic.spell.action.SpellCaster;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityMagicBall extends EntityThrowableUnsaga implements IEntityAdditionalSpawnData{


	public static Set<Block> LAVA_BLOCKS = Sets.newHashSet(Blocks.LAVA,Blocks.FLOWING_LAVA);

	public static Entity blaster(SpellCaster caster,EntityLivingBase target){
		EntityMagicBall magicBall = create(caster, target);
		magicBall.setMagicBallType(MagicBallType.BLASTER);
		magicBall.shoot(caster.getPerformer(), caster.getPerformer().rotationPitch, caster.getPerformer().rotationYaw, 0, 1.5F,1.0F);
		return magicBall;
	}

	public static Entity boulder(SpellCaster caster,EntityLivingBase target){
		EntityMagicBall magicBall = staticBoulder(caster, target);
		magicBall.shoot(caster.getPerformer(), caster.getPerformer().rotationPitch, caster.getPerformer().rotationYaw, 0, 2.0F,1.0F);
		return magicBall;
	}
	public static Entity bubbleBlow(SpellCaster caster,EntityLivingBase target){
		EntityMagicBall magicBall = create(caster, target);
		magicBall.setMagicBallType(MagicBallType.BUBBLE);
		magicBall.setKnockbackStrength(1.5F);
		magicBall.shoot(caster.getPerformer(), caster.getPerformer().rotationPitch, caster.getPerformer().rotationYaw, 0, 2.0F,1.0F);
		return magicBall;
	}
	public static EntityMagicBall create(SpellCaster caster,EntityLivingBase target){
		EntityMagicBall magicBall = new EntityMagicBall(caster.getWorld(),caster.getPerformer());
		magicBall.setAttackDamage(caster.getStrength().hp());
		magicBall.setLPAttack(caster.getStrength().lp().amount(), caster.getStrength().lp().chances());
		return magicBall;
	}
	public static Entity fireBall(SpellCaster caster,EntityLivingBase target){
		EntityMagicBall magicBall = create(caster, target);
		if(caster.getOption()==TargetOptions.IGNITABLE){
			magicBall.setIgnitable(true);
		}
		magicBall.setMagicBallType(MagicBallType.FIREBALL);
		magicBall.shoot(caster.getPerformer(), caster.getPerformer().rotationPitch, caster.getPerformer().rotationYaw, 0, 2.0F,1.0F);
		return magicBall;
	}

	public static EntityMagicBall staticBoulder(SpellCaster caster,EntityLivingBase target){
		EntityMagicBall magicBall = create(caster, target);
		magicBall.setMagicBallType(MagicBallType.BOULDER);
		magicBall.setKnockbackStrength(1.5F);
		return magicBall;
	}

	public static Entity thunderCrap(SpellCaster caster,EntityLivingBase target){
		EntityMagicBall magicBall = create(caster, target);
		Random rand = magicBall.rand;
		magicBall.setMagicBallType(MagicBallType.THUNDER_CRAP);
		magicBall.shoot(caster.getPerformer(), caster.getPerformer().rotationPitch, caster.getPerformer().rotationYaw, -60.0F+rand.nextFloat()*10.0F,0.4F+rand.nextFloat(),2.0F);
		return magicBall;
	}

	public static Entity acid(EntityLivingBase thrower,EntityLivingBase target){
		EntityMagicBall magicBall = new EntityMagicBall(thrower.getEntityWorld(),thrower);
		magicBall.setMagicBallType(MagicBallType.ACID);
		magicBall.shoot(thrower, thrower.rotationPitch, thrower.rotationYaw, 0,1.0F,1.0F);
		return magicBall;
	}

	public MagicBallType magicType = MagicBallType.NONE;

	boolean canIgnite = false;
	public EntityMagicBall(World worldIn) {
		super(worldIn);

		// TODO 自動生成されたコンストラクター・スタブ
	}

	public EntityMagicBall(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}
    private List<BlockPos> checkBlockAround(IBlockState state){
    	List<BlockPos> list = Lists.newArrayList();
    	for(EnumFacing facing:EnumFacing.VALUES){
    		BlockPos pos = this.getPosition().add(facing.getDirectionVec());
    		if(world.getBlockState(pos)==state){
    			list.add(pos);
    		}
    	}
    	return list;
    }

    @Override
	public EnumSet<General> getDamageTypes() {
		// TODO 自動生成されたメソッド・スタブ
		return EnumSet.of(this.getMagicBallType().getDamageType());
	}
    public MagicBallType getMagicBallType(){
		return this.magicType;
	}

	@Override
	public EnumSet<Sub> getSubDamageTypes() {
		// TODO 自動生成されたメソッド・スタブ
		return this.getMagicBallType().getSubDamageTypes();
	}

	private IBlockState getTransformedBlock(IBlockState state){
    	if(state.getBlock()==Blocks.LAVA){
    		if(((int)state.getValue(BlockLiquid.LEVEL))==0){
    			return Blocks.OBSIDIAN.getDefaultState();
    		}else{
    			return Blocks.COBBLESTONE.getDefaultState();
    		}

    	}
    	if(state.getBlock()==Blocks.FLOWING_LAVA){
    		if(((int)state.getValue(BlockLiquid.LEVEL))==0){
    			return Blocks.OBSIDIAN.getDefaultState();
    		}else{
    			return Blocks.COBBLESTONE.getDefaultState();
    		}
    	}
    	return state;
    }

	public boolean isGlowingInRender(){
		return this.getMagicBallType()==MagicBallType.FIREBALL;
	}
	@Override
	protected void onEntityHit(RayTraceResult result){
		super.onEntityHit(result);
		if(this.getMagicBallType()==MagicBallType.FIREBALL){
			result.entityHit.setFire(5);
		}
		if(this.getMagicBallType()==MagicBallType.BUBBLE){
			result.entityHit.extinguish();
		}
		if(result.entityHit instanceof EntityLivingBase){
			EntityLivingBase target = (EntityLivingBase) result.entityHit;
			if(this.getMagicBallType()==MagicBallType.BLASTER){
				target.addPotionEffect(new PotionEffect(UnsagaPotions.STUN,ItemUtil.getPotionTime(15)));
			}
			if(this.getMagicBallType()==MagicBallType.THUNDER_CRAP){
				target.addPotionEffect(new PotionEffect(UnsagaPotions.STUN,ItemUtil.getPotionTime(5)));
			}
		}

	}



	@Override
	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);
//		boolean deadFlag = true;
		if(result.typeOfHit==Type.BLOCK){

			BlockPos pos = result.getBlockPos();
			IBlockState block = world.getBlockState(pos);
//			UnsagaMod.logger.trace(block.toString(), block.getMaterial().getMobilityFlag());
			if(block.getMaterial().getMobilityFlag()!=EnumPushReaction.DESTROY){
				if(world.isAirBlock(pos.up()) && this.canIgnite){
					WorldHelper.setBlockSafe(world, pos.up(), Blocks.FIRE.getDefaultState());
				}
				if(this.getMagicBallType().isDestroyable(world, pos, block)){
					this.destroyBlock(result, block,this.getMagicBallType().isExtinguish(block));
				}
				if(this.getMagicBallType()==MagicBallType.BUBBLE){
					ScannerNew.create().base(this).range(1).ready().stream().forEach(in ->{
						if(this.getMagicBallType().isDestroyable(world, in,world.getBlockState(in))){
							this.destroyBlock(in, world.getBlockState(in),true);
						}
						if(LAVA_BLOCKS.contains(world.getBlockState(in).getBlock())){
							IBlockState state = this.getTransformedBlock(world.getBlockState(in));
	            			this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1.0F, 1.0F);
	            			WorldHelper.setBlockSafe(world, in, state);
						}
					});
				}

				if(this.getMagicBallType()==MagicBallType.THUNDER_CRAP && WorldHelper.isServer(world)){
					double range = 4.0D;
					if(this.isInWater() || world.isRaining()){
						range = 10.0D;
					}
					BiConsumer<RangedHelper<Object>,EntityLivingBase> consumer = (self,target)->{
						this.attackEntity(target);
					};
					if(this.getThrower() instanceof EntityPlayer){
						RangedHelper.create(world, this, range, range/2, range)
						.setSelector((parent,in)->IMob.MOB_SELECTOR.apply(in)).setConsumer(consumer).invoke();
					}else{
						RangedHelper.create(world, this, range, range/2, range)
						.setSelector((parent,in)->in!=this.thrower).setConsumer(consumer).invoke();
					}
				}
//				if(this.getMagicBallType()==MagicBallType.BOULDER){
//					if(block.getMaterial()==Material.GLASS){
//						if(block.getBlockHardness(world, pos)<=2.0F){
//							this.destroyBlock(result, block);
//						}
//					}
//				}
//				if(this.getMagicBallType()==MagicBallType.FIREBALL){
//					if(block.getMaterial()==Material.ICE){
//						if(block.getBlockHardness(world, pos)<=2.0F){
//							this.destroyBlock(result, block,true);
//						}
//					}
//
//				}
				this.setDead();

			}else{
				if(this.getMagicBallType().isDestroyable(world, pos, block)){
					this.destroyBlock(result, block,this.getMagicBallType().isExtinguish(block));
				}
//				if(this.getMagicBallType()==MagicBallType.FIREBALL){
//					if(block.getMaterial()==Material.VINE){
//						this.destroyBlock(result, block);
//					}
//					if(block.getMaterial()==Material.WEB){
//						this.destroyBlock(result, block);
//					}
//				}
//				if(this.getMagicBallType()==MagicBallType.BUBBLE){
//					if(block.getMaterial()==Material.FIRE){
//						this.destroyBlock(result, block,true);
//					}
//				}

			}


		}

	}

	public void onUpdate()
    {
    	super.onUpdate();
    	if(this.getMagicBallType().getParticle()!=null && this.getMagicBallType().getParticleSetting()!=null){
    		Pair<Integer,MovingType> setting = this.getMagicBallType().getParticleSetting();
    		setting.second().spawnParticle(world, XYZPos.createFrom(this)
    				, this.getMagicBallType().getParticle(), rand, setting.first(), 0.1D);
    	}
//    	if(!world.isAirBlock(this.getPosition())){
//    		this.throughBlock(world.getBlockState(getPosition()),this.getPosition());
//    	}

//    	if(this.getMagicBallType()==MagicBallType.BUBBLE){
//    		for(Block block:LAVA_BLOCKS){
//            	if(world.containsAnyLiquid(getEntityBoundingBox())){
//            		List<BlockPos> posList = this.checkBlockAround(block.getDefaultState());
//            		if(!posList.isEmpty()){
//            			IBlockState state = this.world.getBlockState(posList.get(0));
//            			this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1.0F, 1.0F);
//            			WorldHelper.setBlockSafe(world, posList.get(0), this.getTransformedBlock(state));
//            			this.setDead();
//            		}
//            	}
//    		}
//
//    	}

    	if(this.getMagicBallType()==MagicBallType.FIREBALL && this.isInWater()){
    		this.setDead();
    	}
    }
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		if(compound.hasKey("magicType")){
			int meta = compound.getInteger("magicType");
			this.magicType = MagicBallType.fromMeta(meta);
		}
		if(compound.hasKey("canIgnite")){
			this.canIgnite = compound.getBoolean("canIgnite");
		}
	}
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		// TODO 自動生成されたメソッド・スタブ
		int meta = additionalData.readInt();
		this.setMagicBallType(MagicBallType.fromMeta(meta));
	}
	@Override
	public void setDead()
	{
		if(this.getMagicBallType()==MagicBallType.THUNDER_CRAP){
			SoundPacket.sendToAllAround(PacketSound.atEntity(SoundEvents.ENTITY_FIREWORK_BLAST, this), PacketUtil.getTargetPointNear(this));
			HSLib.getPacketDispatcher().sendToAllAround(PacketParticle.toEntity(EnumParticleTypes.CRIT, this, 15), PacketUtil.getTargetPointNear(this));



		}else{
			if(this.getMagicBallType().getTextureItem()!=null){
				for (int i = 0; i < 8; ++i)
				{
					this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(this.getMagicBallType().getTextureItem()));
				}
			}
		}

		this.isDead = true;
	}
	public void setIgnitable(boolean par1){
		this.canIgnite = par1;
	}
	public void setMagicBallType(MagicBallType type){
		this.magicType = type;
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("magicType", this.magicType.getMeta());
		compound.setBoolean("canIgnite",this.canIgnite);
	}
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		// TODO 自動生成されたメソッド・スタブ
		buffer.writeInt(this.getMagicBallType().getMeta());
	}
}
