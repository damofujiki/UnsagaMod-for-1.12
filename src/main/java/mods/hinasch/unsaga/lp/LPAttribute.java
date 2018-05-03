package mods.hinasch.unsaga.lp;

import mods.hinasch.lib.iface.INBTWritable;
import mods.hinasch.lib.util.UtilNBT.RestoreFunc;
import net.minecraft.nbt.NBTTagCompound;

public class LPAttribute implements INBTWritable{




	public static final LPAttribute ZERO = new LPAttribute(0,0);

	public static final RestoreFunc<LPAttribute> RESTORE = in ->{
		if(in.hasKey("lpstr") && in.hasKey("chances")){
			float lpstr = in.getFloat("lpstr");
			int chances = in.getInteger("chances");
			return new LPAttribute(lpstr,chances);
		}
		return ZERO;
	};

	final float lpstr;
	public float amount() {
		return lpstr;
	}
	public int chances() {
		return numberOfAttack;
	}
	final int numberOfAttack;
	public LPAttribute(float lpstr, int numberOfAttack) {
		super();
		this.lpstr = lpstr;
		this.numberOfAttack = numberOfAttack;
	}
	@Override
	public void writeToNBT(NBTTagCompound stream) {
		// TODO 自動生成されたメソッド・スタブ
		stream.setFloat("lpstr", lpstr);
		stream.setInteger("chances", numberOfAttack);
	}
}