package mods.hinasch.unsaga.chest;

import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.core.client.render.projectile.IChestModel;
import net.minecraft.entity.player.EntityPlayer;

/** 宝箱の機能をもたせたいものに使う。
 * 自動的にキャパビリティ付加*/
public interface IChestBehavior<T> extends IChestModel{

	public XYZPos getChestPosition();
	public T getChestParent();
	public void sync(EntityPlayer ep);
	public IChestCapability getCapability();
}
