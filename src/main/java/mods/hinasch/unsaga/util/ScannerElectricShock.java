package mods.hinasch.unsaga.util;

import java.util.List;
import java.util.Set;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.AbstractAsyncConnectScanner;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsagamagic.spell.SpellRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ScannerElectricShock extends AbstractAsyncConnectScanner{

	protected EntityLivingBase owner;

	public ScannerElectricShock(World world,int length,Set<IBlockState> compareBlock, XYZPos startpoint,EntityLivingBase owner) {
		super(world, compareBlock, startpoint, length, owner);
		this.owner = owner;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCheckScheduledPos(World world, IBlockState currentBlock, BlockPos currentPos) {

		SpellRegistry spells = mods.hinasch.unsagamagic.spell.SpellRegistry.instance();
		AxisAlignedBB bb = HSLibs.getBounding(currentPos, 2.0D, 2.0D);
		if(!world.getEntitiesWithinAABB(EntityLivingBase.class, bb).isEmpty()){
//			Unsaga.debug("範囲内に発見");
			List<EntityLivingBase> livings = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			for(EntityLivingBase living:livings){
				DamageSourceUnsaga ds = DamageSourceUnsaga.create(this.owner,spells.THUNDER_CRAP.getStrength().lp().amount(),General.MAGIC);
				ds.setSubTypes(Sub.ELECTRIC);
//				ds.setElement(FiveElements.Type.WOOD);
				living.attackEntityFrom(ds, spells.THUNDER_CRAP.getStrength().hp());
			}


		}
	}


	@Override
	public void addToList(List<IBlockState> blockToCompare,IBlockState checkBlock,BlockPos rotatedPos,List<BlockPos> listToAdd){

		super.addToList(blockToCompare, checkBlock, rotatedPos, listToAdd);
//		if((compareBlock.getBlock() instanceof BlockRedstoneOre) && (checkBlock.getBlock() instanceof BlockRedstoneOre)){
//			listToAdd.add(rotatedPos);
//		}

	}

	@Override
	public int getIntervalThresold() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}


}
