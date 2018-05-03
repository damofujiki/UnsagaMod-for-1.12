package mods.hinasch.unsaga.chest;

import io.netty.util.internal.StringUtil;
import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanels;

public enum FieldChestType implements IIntSerializable{

	FIELD(1),CAVE(2),NORMAL(3),BLOCK(4);

	private int meta;

	private FieldChestType(int meta){
		this.meta = meta;
	}
	@Override
	public int getMeta() {
		// TODO 自動生成されたメソッド・スタブ
		return meta;
	}

	public String getName(){
		switch(this){
		case BLOCK:
			return "Block";
		case CAVE:
			return "Cave";
		case FIELD:
			return "Field";
		case NORMAL:
			break;
		default:
			break;

		}
		return StringUtil.EMPTY_STRING;
	}
	public SkillPanel getSuitableSkill(){
		switch(this){
		case CAVE:
			return SkillPanels.GUIDE_CAVE;
		case FIELD:
			return SkillPanels.GUIDE_ROAD;
		default:
			break;

		}
		return null;
	}

	public static FieldChestType fromMeta(int meta){
		return HSLibs.fromMeta(FieldChestType.values(), meta);
	}
}
