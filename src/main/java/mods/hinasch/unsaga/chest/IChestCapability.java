package mods.hinasch.unsaga.chest;

import java.util.List;
import java.util.Optional;

import mods.hinasch.lib.capability.ISyncCapability;
import mods.hinasch.lib.iface.IRequireInitializing;
import mods.hinasch.unsaga.chest.ChestCapability.ChestTreasureType;
import net.minecraft.entity.player.EntityPlayer;

/**
 * 宝箱のキャパビリティの中身
 */
public interface IChestCapability extends ISyncCapability,IRequireInitializing{

	public int getLevel();
	public void setLevel(int par1);
	public List<ChestTrap> getTraps();
	public void setTraps(List<ChestTrap> traps);
	public boolean hasLocked();
	public void setLocked(boolean par1);
	public boolean hasDefused();
	public void setDefused();
	public boolean hasAnalyzed();
	public void setAnalyzed(boolean par1);
	public Class getChestType();
	public void setChestType(Class clazz);
	public void setOpeningPlayer(EntityPlayer ep);
	public Optional<EntityPlayer> getOpeningPlayer();
	public boolean hasOpened();
	public void setOpened(boolean par1);
	public boolean hasMagicLocked();
	public void setMagicLocked(boolean par1);
	public ChestTreasureType getTreasureType();
	public void setTreasureType(ChestTreasureType par1);
}
