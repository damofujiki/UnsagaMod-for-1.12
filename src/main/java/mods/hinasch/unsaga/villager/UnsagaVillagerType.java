package mods.hinasch.unsaga.villager;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;

public enum UnsagaVillagerType implements IIntSerializable{

	CARRIER(0,"carrier"),BARTERING(1,"bartering"),SMITH(2,"smith"),UNKNOWN(10,"unknown");

	final int meta;
	final String name;

	private UnsagaVillagerType(int meta,String name){
		this.meta = meta;
		this.name = name;
	}
	@Override
	public int getMeta() {
		// TODO 自動生成されたメソッド・スタブ
		return meta;
	}

	public static UnsagaVillagerType fromMeta(int meta){
		return HSLibs.fromMeta(UnsagaVillagerType.values(),meta);
	}
}
