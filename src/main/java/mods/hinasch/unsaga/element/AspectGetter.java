package mods.hinasch.unsaga.element;

import mods.hinasch.unsaga.core.world.chunk.UnsagaChunkCapability;
import mods.hinasch.unsaga.element.newele.ElementTable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class AspectGetter {

	/** 天候時のフィルターも加えた最終的な現在地の相を得る。*/
	public static ElementTable getCurrentAspect(World world,BlockPos pos){
		ElementTable biomeElements = BiomeElements.getBiomeElements(world.getBiome(pos));
		return biomeElements.add(getAspectOffsetFromChunk(world,pos).getCurrentOffset()).add(getAspectFilter(world,pos));
	}

	/** 五行相に加えるフィルター。今の所雨の時の水相のみ。*/
	private static ElementTable getAspectFilter(World world,BlockPos pos){

		if(world.isRaining()){
			return new ElementTable(-3,0,0,3,0,0);
		}
		return ElementTable.ZERO;
	}

	/** 五行相（変化）をチャンクから得る。*/
	public static AspectOffset getAspectOffsetFromChunk(World world,BlockPos pos){
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		if(UnsagaChunkCapability.ADAPTER.hasCapability(chunk)){
			return UnsagaChunkCapability.ADAPTER.getCapability(chunk).getAspectOffset();
		}
		return AspectOffset.ZERO;
	}
}
