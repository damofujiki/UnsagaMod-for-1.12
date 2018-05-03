package mods.hinasch.unsaga.util;

import java.util.List;
import java.util.Set;

import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.lib.world.AbstractAsyncConnectScanner;
import mods.hinasch.lib.world.WorldHelper;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AsyncConnectedBlocksBreaker extends AbstractAsyncConnectScanner{

	protected int breaked;

	public AsyncConnectedBlocksBreaker(World world,int length,Set<IBlockState> compareBlock, BlockPos startpoint,EntityLivingBase sender) {
		super(world, compareBlock, startpoint, length, sender);
		this.breaked = 0;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCheckScheduledPos(World world, IBlockState currentBlock, BlockPos currentPos) {

		net.minecraft.block.Block block = currentBlock.getBlock();
		if(WorldHelper.isServer(world)){
			block.dropXpOnBlockBreak(world, startPoint, block.getExpDrop(currentBlock,world, startPoint, 0));
			SoundAndSFX.playBlockBreakSFX(world, currentPos, currentBlock,true);
		}


		breaked += 1;


	}

	@Override
	public void addToList(List<IBlockState> blockToCompare,IBlockState checkBlock,BlockPos rotatedPos,List<BlockPos> listToAdd){

		super.addToList(blockToCompare, checkBlock, rotatedPos, listToAdd);
		for(IBlockState aState:blockToCompare){
			if((aState.getBlock() instanceof BlockRedstoneOre) && (checkBlock.getBlock() instanceof BlockRedstoneOre)){
				listToAdd.add(rotatedPos);
			}
		}


	}

	public int getBreakedBlock(){
		return this.breaked;
	}

	@Override
	public int getIntervalThresold() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}



}
