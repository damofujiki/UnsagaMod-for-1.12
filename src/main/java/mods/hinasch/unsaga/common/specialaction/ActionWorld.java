package mods.hinasch.unsaga.common.specialaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.lib.world.ScannerNew;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.common.specialaction.IActionPerformer.TargetType;
import mods.hinasch.unsaga.core.world.chunk.UnsagaChunkCapability;
import mods.hinasch.unsaga.element.AspectOffset;
import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.element.newele.ElementTable;
import mods.hinasch.unsagamagic.spell.action.SpellCaster;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ActionWorld<T extends IActionPerformer> implements IAction<T>{

	final int horizontal;
	final int vertical;
	BiFunction<T,BlockPos,EnumActionResult> worldConsumer = (context,pos) ->{
		return EnumActionResult.PASS;
	};
	public ActionWorld(int horizontal,int vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	@Override
	public EnumActionResult apply(T context) {

		if(context.getTargetType()==TargetType.OWNER){
			context.setTargetCoordinate(context.getPerformer().getPosition());
		}
		if(context.getTargetCoordinate().isPresent()){

			List<EnumActionResult> list = Lists.newArrayList();
			/** 終端が設定されたない場合*/
			if(!this.getEndPos(context).isPresent()){
				//範囲設定からスキャナーを作る
				list = ScannerNew.create().base(this.getTargetCoordinate(context).get())
				.range(horizontal, vertical, horizontal).ready().stream()
				.map(in ->this.worldConsumer.apply(context, in)).collect(Collectors.toList());
			}else{
				//始点終点からスキャナーを作る
				list = ScannerNew.create().base(this.getTargetCoordinate(context).get())
				.to(this.getEndPos(context).get()).ready().stream()
				.map(in ->this.worldConsumer.apply(context, in)).collect(Collectors.toList());
			}
			UnsagaMod.logger.trace("world", list);
			return list.contains(EnumActionResult.SUCCESS) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		}

		return EnumActionResult.PASS;
	}

	protected Optional<BlockPos> getEndPos(T context){
		return Optional.empty();
	}

	protected Optional<BlockPos> getTargetCoordinate(T context){
		return context.getTargetCoordinate();
	}

	public ActionWorld setWorldConsumer(BiFunction<T,BlockPos,EnumActionResult> worldConsumer){
		this.worldConsumer = worldConsumer;
		return this;
	}

	public static class AirRoomDetector extends ActionWorld<TechInvoker>{

		public AirRoomDetector() {
			super(0, 0);
			/** 空洞のカウントが７以上でサクセス*/
			this.setWorldConsumer((context,pos)->{
				if(ScannerNew.create().base(pos).range(1).ready().stream().map(in -> context.getWorld().isAirBlock(in)).filter(in -> in).count()>=7){
					return EnumActionResult.SUCCESS;
				}
				return EnumActionResult.FAIL;
			});
		}
		@Override
		public EnumActionResult apply(TechInvoker context) {
			BlockPos pos = context.getTargetCoordinate().get();
			context.setTargetCoordinate(pos.down());
			EnumActionResult result = super.apply(context);
			if(result==EnumActionResult.SUCCESS){
				context.broadCastMessage("Detected Air Room!");
				context.playSound(new XYZPos(context.getTargetCoordinate().get()), SoundEvents.AMBIENT_CAVE, false);
			}
			return EnumActionResult.SUCCESS;
		}

		@Override
		protected Optional<BlockPos> getEndPos(TechInvoker context){

			return Optional.of(context.getTargetCoordinate().get().down(30));
		}
	}

	public static class WeedCutter implements BiFunction<TechInvoker,BlockPos,EnumActionResult>{

		List<Predicate<Block>> allowedBlocks = Lists.newArrayList();
		boolean sweep = false;

		public WeedCutter(){
			allowedBlocks.add(in -> in instanceof BlockCrops);
			allowedBlocks.add(in -> in instanceof BlockTallGrass);
			allowedBlocks.add(in -> in instanceof BlockDoublePlant);
			allowedBlocks.add(in -> in instanceof BlockDeadBush);
			allowedBlocks.add(in -> in instanceof BlockWeb);
			allowedBlocks.add(in -> in instanceof BlockFire);
			allowedBlocks.add(in -> in instanceof BlockCrops);
		}


		@Override
		public EnumActionResult apply(TechInvoker context, BlockPos pos) {
			IBlockState block = context.getWorld().getBlockState(pos);
			if(allowedBlocks.stream().anyMatch(in ->in.test(block.getBlock()))){
				SoundAndSFX.playBlockBreakSFX(context.getWorld(), pos, block);
			}
			return EnumActionResult.PASS;
		}

	}

	public static class Freezer implements BiFunction<SpellCaster,BlockPos,EnumActionResult>{

		Map<Block,Block> changeMap = Maps.newHashMap();

		public Freezer() {
			changeMap.put(Blocks.WATER, Blocks.ICE);
			changeMap.put(Blocks.FLOWING_WATER, Blocks.FROSTED_ICE);
			changeMap.put(Blocks.LAVA, Blocks.OBSIDIAN);
			changeMap.put(Blocks.FLOWING_LAVA, Blocks.STONE);
			// TODO 自動生成されたコンストラクター・スタブ
		}



		@Override
		public EnumActionResult apply(SpellCaster context, BlockPos pos) {
			World world = context.getWorld();
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock()!=Blocks.AIR){
				//置き換え
				if(this.changeMap.containsKey(state.getBlock())){
					Block changed = this.changeMap.get(state.getBlock());
					world.setBlockState(pos, changed.getDefaultState());
				}
				//雪（レイヤー）の場合一層増やす
				if(state.getBlock()==Blocks.SNOW_LAYER){
					int layer = (Integer)state.getValue(BlockSnow.LAYERS);
					layer ++;
					layer = MathHelper.clamp(layer, 1, 8);
					if(layer>=8){
						world.setBlockState(pos, Blocks.SNOW.getDefaultState());
					}else{
						world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, layer));
					}

				}

			}
			IBlockState downState = world.getBlockState(pos.down());
			//雪が配置できる場合雪配置
			if(downState.getBlock()!=Blocks.AIR && state.getBlock()==Blocks.AIR){
				if(Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos)){
					world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, 1));
				}
			}

			Chunk chunk = context.getWorld().getChunkFromBlockCoords(context.getPerformer().getPosition());
			if(UnsagaChunkCapability.ADAPTER.hasCapability(chunk)){
				UnsagaChunkCapability.ADAPTER.getCapability(chunk).getAspectOffset().addOffset(FiveElements.Type.WATER,32 ,context.getWorld().getTotalWorldTime());
			}
			AspectOffset.setOffsetTo(context.getWorld(), context.getPerformer().getPosition(), new ElementTable(-8,-8,-8,16,-8,16));
			return EnumActionResult.SUCCESS;
		}
	}
}
