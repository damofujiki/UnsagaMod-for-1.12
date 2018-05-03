package mods.hinasch.unsaga.material;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.ICapabilityAdapterPlan;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnsagaMaterialCapability {


	@CapabilityInject(IUnsagaForgeableTool.class)
	public static Capability<IUnsagaForgeableTool> CAPA;
	public static final String SYNC_ID = "unsaga_forgeable_tool";

	public static ICapabilityAdapterPlan<IUnsagaForgeableTool> blueprint = new ICapabilityAdapterPlan(){

		@Override
		public Capability getCapability() {
			// TODO 自動生成されたメソッド・スタブ
			return CAPA;
		}

		@Override
		public Class getCapabilityClass() {
			// TODO 自動生成されたメソッド・スタブ
			return IUnsagaForgeableTool.class;
		}

		@Override
		public Class getDefault() {
			// TODO 自動生成されたメソッド・スタブ
			return DefaultImpl.class;
		}

		@Override
		public IStorage getStorage() {
			// TODO 自動生成されたメソッド・スタブ
			return new Storage();
		}

	};

	public static CapabilityAdapterFrame<IUnsagaForgeableTool> base = UnsagaMod.capabilityAdapterFactory.create(blueprint);
	public static ComponentCapabilityAdapters.ItemStack<IUnsagaForgeableTool> adapter = base.createChildItem(SYNC_ID);

	static{
		adapter.setPredicate(ev -> {
//			UnsagaMod.logger.trace("capa", ev.getItemStack().getItem());
			return HSLibs.cast(ev, in -> in.getItem() instanceof IUnsagaMaterialSelector);
		});
		adapter.setRequireSerialize(true);
	}
	public static class DefaultImpl implements IUnsagaForgeableTool{



		boolean hasInitialized = false;
		int weight = 0;
		UnsagaMaterial material = UnsagaMaterials.DUMMY;
		@Override
		public boolean hasInitialized() {
			// TODO 自動生成されたメソッド・スタブ
			return this.hasInitialized;
		}

		@Override
		public void setInitialized(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.hasInitialized = par1;
		}

		@Override
		public NBTTagCompound getSendingData() {
			// TODO 自動生成されたメソッド・スタブ
			return (NBTTagCompound) CAPA.writeNBT(this, null);
		}

		@Override
		public void catchSyncData(NBTTagCompound nbt) {
			CAPA.readNBT(this, null, nbt);

		}

		@Override
		public void onPacket(PacketSyncCapability message, MessageContext ctx) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public String getIdentifyName() {
			// TODO 自動生成されたメソッド・スタブ
			return SYNC_ID;
		}

		@Override
		public int getWeight() {

			return this.weight;
		}

		@Override
		public void setWeight(int weight) {
			// TODO 自動生成されたメソッド・スタブ
			this.weight = weight;
		}



		@Override
		public UnsagaMaterial getMaterial() {
			// TODO 自動生成されたメソッド・スタブ
			return this.material;
		}

		@Override
		public void setMaterial(UnsagaMaterial m) {
			// TODO 自動生成されたメソッド・スタブ
			this.material = m;
		}





	}

	public static class Storage extends CapabilityStorage<IUnsagaForgeableTool>{

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<IUnsagaForgeableTool> capability,
				IUnsagaForgeableTool instance, EnumFacing side) {
			comp.setString("material", instance.getMaterial().getKey().getResourcePath());
			comp.setInteger("weight", instance.getWeight());
			comp.setBoolean("initialized", instance.hasInitialized());

		}

		@Override
		public void readNBT(NBTTagCompound comp, Capability<IUnsagaForgeableTool> capability,
				IUnsagaForgeableTool instance, EnumFacing side) {
			// TODO 自動生成されたメソッド・スタブ
			if(comp.hasKey("material")){
				String key = comp.getString("material");
				UnsagaMaterial m = UnsagaMaterialRegistry.instance().getObject(new ResourceLocation(key));
				if(m!=null){
					instance.setMaterial(m);
				}else{
					instance.setMaterial(UnsagaMaterials.DUMMY);
				}

			}
			if(comp.hasKey("weight")){
				instance.setWeight(comp.getInteger("weight"));
			}

			if(comp.hasKey("initialized")){
				instance.setInitialized(comp.getBoolean("initialized"));
			}
		}

	}

	public static void register(){



		adapter.registerAttachEvent((inst,capa,facing,ev)->{
//			UnsagaMod.logger.trace("capa", "attach");
			if(ev.getObject() instanceof IUnsagaMaterialSelector){
				if(!inst.hasInitialized()){
					inst.setMaterial(UnsagaMaterials.DUMMY);
					inst.setWeight(1);
					inst.setInitialized(true);
				}

			}
		});
	}
}
