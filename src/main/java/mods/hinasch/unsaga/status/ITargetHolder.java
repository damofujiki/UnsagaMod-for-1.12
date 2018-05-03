package mods.hinasch.unsaga.status;

import java.util.Optional;

import mods.hinasch.lib.capability.ISyncCapability;
import net.minecraft.entity.EntityLivingBase;

public interface ITargetHolder extends ISyncCapability {

	public void updateTarget(EntityLivingBase target);
	public Optional<EntityLivingBase> getTarget();
	public double getTargetDistance(EntityLivingBase other);
}
