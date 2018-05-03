package mods.hinasch.unsaga.common.specialaction.option;

import mods.hinasch.lib.util.HSLibs;

public class TargetOption implements IOption {

	final String name;
	public TargetOption(String name){
		this.name = name;
	}
	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return this.name;
	}
	@Override
	public String getLocalized() {
		// TODO 自動生成されたメソッド・スタブ
		return HSLibs.translateKey("unsaga.item.option."+this.getName());
	}


}
