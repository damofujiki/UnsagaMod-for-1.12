package mods.hinasch.unsaga.material;

import mods.hinasch.lib.capability.ISyncCapability;
import mods.hinasch.lib.iface.IRequireInitializing;

public interface IUnsagaForgeableTool extends IRequireInitializing,ISyncCapability{

	public int getWeight();
	public void setWeight(int weight);
	public UnsagaMaterial getMaterial();
	public void setMaterial(UnsagaMaterial m);


}
