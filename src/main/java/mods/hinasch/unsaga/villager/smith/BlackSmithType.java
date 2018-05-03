package mods.hinasch.unsaga.villager.smith;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;

public enum BlackSmithType implements IIntSerializable{

	NONE(0,"none"),DURABILITY(1,"durability"),ABILITY(2,"ability");

	final String name;
	final int meta;
	private  BlackSmithType(int meta,String name){
		this.meta = meta;
		this.name = name;
	}
	@Override
	public int getMeta() {
		// TODO 自動生成されたメソッド・スタブ
		return meta;
	}

	public String getName(){
		return this.name;
	}
	public String getLocalized(){
		return HSLibs.translateKey("gui.unsaga.smith.type."+this.getName());
	}
	public static BlackSmithType fromMeta(int meta){
		return HSLibs.fromMeta(BlackSmithType.values(), meta);
	}

}
