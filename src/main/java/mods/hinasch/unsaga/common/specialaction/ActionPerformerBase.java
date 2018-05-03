package mods.hinasch.unsaga.common.specialaction;

import java.util.Optional;

import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.common.specialaction.option.IOption;
import mods.hinasch.unsaga.common.specialaction.option.TargetOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ActionPerformerBase<T, V> implements IActionPerformer<T,V>{

	protected final T property;
	final World world;
	final EntityLivingBase performer;
	protected IOption actionOption = TargetOptions.NONE;
	protected IActionPerformer.TargetType targetType;
	protected Optional<BlockPos> castPoint = Optional.empty();
	protected Optional<ItemStack> article = Optional.empty();
	protected Optional<EntityLivingBase> target = Optional.empty();
	public ActionPerformerBase(World world,EntityLivingBase performer,T property){
		this.world = world;
		this.performer = performer;
		this.property = property;
	}
	@Override
	public World getWorld() {
		// TODO 自動生成されたメソッド・スタブ
		return this.world;
	}


	public void setTargetCoordinate(BlockPos pos){
		this.castPoint = Optional.of(pos);
	}

	public IActionPerformer setTargetType(IActionPerformer.TargetType type){
//		UnsagaMod.logger.trace(this.getClass().getName(), type);
		this.targetType = type;
		return this;
	}
	@Override
	public EntityLivingBase getPerformer() {
		// TODO 自動生成されたメソッド・スタブ
		return this.performer;
	}
	@Override
	public T getActionProperty() {
		// TODO 自動生成されたメソッド・スタブ
		return this.property;
	}

	@Override
	public Optional<ItemStack> getArtifact() {
		// TODO 自動生成されたメソッド・スタブ
		return this.article;
	}

	public ActionPerformerBase setArtifact(ItemStack is){
		this.article = Optional.of(is);
		return this;
	}
	public IActionPerformer.TargetType getTargetType(){
		return this.targetType;
	}

	public Optional<BlockPos> getTargetCoordinate(){
		return this.castPoint;
	}

	public void spawnParticle(ParticleHelper.MovingType type,XYZPos pos,EnumParticleTypes par,int density,double speedscale,int... params){
		if(WorldHelper.isClient(getWorld())){
			type.spawnParticle(getWorld(), pos, par, this.getWorld().rand, density, speedscale,params);
		}

	}


	public void spawnParticle(ParticleHelper.MovingType type,Entity target,EnumParticleTypes par,int density,double speedscale){
		if(WorldHelper.isClient(getWorld())){
			XYZPos pos = new XYZPos(target.posX,target.posY+1.5F,target.posZ);
			type.spawnParticle(getWorld(), pos, par, this.getWorld().rand, density, speedscale);
		}

	}
	public void playSound(XYZPos pos,SoundEvent soundIn,boolean distanceDelay){
		this.playSound(pos, soundIn, distanceDelay, 1.0F);
	}

	public void playSound(XYZPos pos,SoundEvent soundIn,boolean distanceDelay,float pitch){
		if(WorldHelper.isClient(getWorld())){
			SoundAndSFX.playSound(this.getWorld(),pos.dx, pos.dy, pos.dz, soundIn, SoundCategory.PLAYERS, 1.0F, pitch, distanceDelay);
		}

	}
	public abstract int getCost();

	public IActionPerformer setTarget(EntityLivingBase liv){
		this.target = Optional.of(liv);
		return this;
	}
	public Optional<EntityLivingBase> getTarget(){
		return this.target;
	}
	@Override
	public IActionPerformer setOption(IOption option){
		this.actionOption = option;
		return this;
	}

	@Override
	public IOption getOption(){
		return this.actionOption;
	}

	public boolean checkItemCost(){
		if(this.getArtifact().isPresent()){
			int durability = this.getArtifact().get().getMaxDamage() - this.getArtifact().get().getItemDamage();
			return durability>=this.getCost();
		}
		return true;
	}
	public void broadCastMessage(String msg){
		if(WorldHelper.isClient(this.getWorld())){
			return;
		}
		if(this.getPerformer() instanceof EntityPlayer){
			ChatHandler.sendChatToPlayer((EntityPlayer) this.getPerformer(), msg);
		}else{
			this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, this.getPerformer().getEntityBoundingBox().grow(10.0D))
			.forEach(in ->{
				if(in instanceof EntityPlayer){
					ChatHandler.sendChatToPlayer((EntityPlayer)in, msg);
				}
			});
		}
	}
}
