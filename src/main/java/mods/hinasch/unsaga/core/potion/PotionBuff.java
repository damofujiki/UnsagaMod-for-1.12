package mods.hinasch.unsaga.core.potion;

import mods.hinasch.lib.util.Statics;
import net.minecraft.entity.ai.attributes.IAttribute;

public class PotionBuff extends PotionUnsaga{

	protected PotionBuff(String name, int u, int v) {
		super(name, false, 0x0000ff, u, v);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	protected PotionBuff(String name, int u,int v,IAttribute attribute,String uuid,double value) {
		super(name, false, 0x0000ff, u, v);
		this.registerPotionAttributeModifier(attribute, uuid, value, Statics.OPERATION_INCREMENT);
		// TODO 自動生成されたコンストラクター・スタブ
	}
}
