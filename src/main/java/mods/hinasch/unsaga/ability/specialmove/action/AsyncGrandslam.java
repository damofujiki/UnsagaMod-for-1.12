package mods.hinasch.unsaga.ability.specialmove.action;

import java.util.List;

import com.google.common.collect.ImmutableList;

import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.sync.SafeUpdateEventByInterval;
import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AsyncGrandslam extends SafeUpdateEventByInterval{

	int index = 0;
	final int endLength;
	ImmutableList<BlockPos> explodePos;
	final World world;
	final double damage;
	final boolean canWorldDestroy;
	public AsyncGrandslam(World w,double damage,List<BlockPos> explodePos,boolean worldDestroy,EntityLivingBase sender) {
		super(sender, "grandslam");
		this.world = w;
		this.damage = damage;
		this.explodePos = ImmutableList.copyOf(explodePos);
		this.endLength = this.explodePos.size();
		this.canWorldDestroy = worldDestroy;
	}

	@Override
	public int getIntervalThresold() {
		// TODO 自動生成されたメソッド・スタブ
		return 3;
	}

	@Override
	public void loopByInterval() {
		if(this.index<this.explodePos.size()){
			BlockPos pos = this.explodePos.get(index);
			if(this.canWorldDestroy){
				if(WorldHelper.isServer(world)){
					world.createExplosion(sender, pos.getX(), pos.getY(), pos.getZ(), 3.0F, false);
				}

			}else{
				SoundAndSFX.playSound(world, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
				IBlockState state = world.getBlockState(pos.down());
				if(state.getBlock()!=Blocks.AIR){
					ParticleHelper.MovingType.FOUNTAIN.spawnParticle(world, new XYZPos(pos), EnumParticleTypes.BLOCK_DUST, world.rand, 20, 5.0F, Block.getStateId(state));
				}
//				ParticleHelper.MovingType.FOUNTAIN.spawnParticle(world, new XYZPos(pos), EnumParticleTypes.CLOUD, world.rand, 15, 3.0D);
			}
		}

		index ++;
	}

	@Override
	public boolean hasFinished() {
		// TODO 自動生成されたメソッド・スタブ
		return index>this.endLength;
	}

}
