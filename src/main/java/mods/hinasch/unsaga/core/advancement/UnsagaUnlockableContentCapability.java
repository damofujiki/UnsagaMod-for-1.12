package mods.hinasch.unsaga.core.advancement;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.ICapabilityAdapterPlan;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.IAbilityAttachable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class UnsagaUnlockableContentCapability {

	@CapabilityInject(IUnsagaUnlockableFeature.class)
	public static Capability<IUnsagaUnlockableFeature> CAPA;
	public static final String SYNC_ID = "unsaga.unlockable_features";

	public static ICapabilityAdapterPlan<IAbilityAttachable> blueprint = new ICapabilityAdapterPlan(){

		@Override
		public Capability getCapability() {
			// TODO 自動生成されたメソッド・スタブ
			return CAPA;
		}

		@Override
		public Class getCapabilityClass() {
			// TODO 自動生成されたメソッド・スタブ
			return IUnsagaUnlockableFeature.class;
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

	public static class Storage extends CapabilityStorage<IUnsagaUnlockableFeature>{

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<IUnsagaUnlockableFeature> capability,
				IUnsagaUnlockableFeature instance, EnumFacing side) {
			// TODO 自動生成されたメソッド・スタブ
			comp.setBoolean("unlock.deciphering", instance.hasUnlockedDeciphering());
		}

		@Override
		public void readNBT(NBTTagCompound comp, Capability<IUnsagaUnlockableFeature> capability,
				IUnsagaUnlockableFeature instance, EnumFacing side) {
			// TODO 自動生成されたメソッド・スタブ
			if(comp.hasKey("unlock.deciphering")){
				instance.unlockDeciphering(comp.getBoolean("unlock.deciphering"));
			}
		}

	}
	public static class DefaultImpl implements IUnsagaUnlockableFeature{

		boolean hasUnlockedDeciphering = false;
		@Override
		public boolean hasUnlockedDeciphering() {
			// TODO 自動生成されたメソッド・スタブ
			return hasUnlockedDeciphering;
		}

		@Override
		public void unlockDeciphering(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.hasUnlockedDeciphering = par1;
		}

	}


	public static CapabilityAdapterFrame<IUnsagaUnlockableFeature> builder = UnsagaMod.capabilityAdapterFactory.create(blueprint);
	public static ComponentCapabilityAdapters.Entity<IUnsagaUnlockableFeature> adapter = builder.createChildEntity(SYNC_ID);

	static{

		adapter.setPredicate(ev -> ev.getObject() instanceof EntityPlayer);
		adapter.setRequireSerialize(true);
	}
}
