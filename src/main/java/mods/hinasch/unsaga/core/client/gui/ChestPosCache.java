package mods.hinasch.unsaga.core.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.chest.ChunkChestInfo;

/**
 *
 * マップ表示時のキャッシュ（クライアント）
 *
 */
public class ChestPosCache {

	private static List<ChunkChestInfo> chestPosCache = Lists.newArrayList();
	private static List<ChunkChestInfo> chestPosCache2 = Lists.newArrayList();
	public static List<ChunkChestInfo> getChestPosCache(int bank) {
		return bank ==0 ? chestPosCache : chestPosCache2;
	}

	public static void setChestPosCache(int bank,List<ChunkChestInfo> list) {
		if(bank==0){
			chestPosCache = list;
		}else{
			chestPosCache2 = list;
		}
	}


}
