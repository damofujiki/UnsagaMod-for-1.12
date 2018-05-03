package mods.hinasch.unsaga.core.entity.projectile.custom_arrow;

import mods.hinasch.lib.capability.ISyncCapability;
import mods.hinasch.unsaga.lp.LPAttribute;

public interface ICustomArrow extends ISyncCapability{


	public SpecialArrowType getArrowType();
	public void setArrowType(SpecialArrowType state);
	public LPAttribute getArrowLPStrength();
	public void setArrowLPStrength(LPAttribute f);
}
