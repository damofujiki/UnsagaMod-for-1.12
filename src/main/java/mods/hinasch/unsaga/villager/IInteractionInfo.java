package mods.hinasch.unsaga.villager;

import java.util.Optional;

import mods.hinasch.unsaga.core.entity.passive.EntityUnsagaChest;
import net.minecraft.entity.passive.EntityVillager;

public interface IInteractionInfo {

	public void setMerchant(Optional<EntityVillager> villager);
	public Optional<EntityVillager> getMerchant();
	public void setEntityChest(EntityUnsagaChest chest);
	public Optional<EntityUnsagaChest> getChest();
}
