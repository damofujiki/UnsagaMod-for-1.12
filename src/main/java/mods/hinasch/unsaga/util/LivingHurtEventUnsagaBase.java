package mods.hinasch.unsaga.util;

import mods.hinasch.lib.iface.LivingHurtEventBase;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@Deprecated
public abstract class LivingHurtEventUnsagaBase extends LivingHurtEventBase{
	public boolean apply(LivingHurtEvent e,DamageSource dsu){
		return this.apply(e,(DamageSourceUnsaga) dsu);
	}
	public DamageSource process(LivingHurtEvent e,DamageSource dsu){
		return this.process(e, (DamageSourceUnsaga)dsu);
	}
	public abstract boolean apply(LivingHurtEvent e,DamageSourceUnsaga dsu);
	public abstract DamageSource process(LivingHurtEvent e,DamageSourceUnsaga dsu);
}
