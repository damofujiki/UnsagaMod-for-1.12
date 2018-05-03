package mods.hinasch.unsaga.core.item.misc.skillpanel;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.ICapabilityAdapterPlan;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelRegistry;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class SkillPanelCapability {

	@CapabilityInject(ISkillPanel.class)
	public static Capability<ISkillPanel> CAPA;

	public static CapabilityAdapterFrame<ISkillPanel> adapterBase =
			UnsagaMod.capabilityAdapterFactory.create(new ICapabilityAdapterPlan<ISkillPanel>(){

				@Override
				public Capability<ISkillPanel> getCapability() {
					// TODO 自動生成されたメソッド・スタブ
					return CAPA;
				}

				@Override
				public Class<ISkillPanel> getCapabilityClass() {
					// TODO 自動生成されたメソッド・スタブ
					return ISkillPanel.class;
				}

				@Override
				public Class<? extends ISkillPanel> getDefault() {
					// TODO 自動生成されたメソッド・スタブ
					return DefaultImpl.class;
				}

				@Override
				public IStorage<ISkillPanel> getStorage() {
					// TODO 自動生成されたメソッド・スタブ
					return new Storage();
				}}
		);


	public static class DefaultImpl implements ISkillPanel{

		boolean hasLocked = false;
		boolean hasJointed = false;
		SkillPanel panel = SkillPanels.DUMMY;
		int lv = 0;
		@Override
		public SkillPanel getPanel() {
			// TODO 自動生成されたメソッド・スタブ
			return panel;
		}

		@Override
		public int getLevel() {
			// TODO 自動生成されたメソッド・スタブ
			return lv;
		}

		@Override
		public void setPanel(SkillPanel panel) {
			this.panel = panel;

		}

		@Override
		public void setLevel(int level) {
			this.lv = level;

		}

		@Override
		public boolean hasJointed() {
			// TODO 自動生成されたメソッド・スタブ
			return this.hasJointed;
		}

		@Override
		public boolean hasLocked() {
			// TODO 自動生成されたメソッド・スタブ
			return this.hasLocked;
		}

		@Override
		public void setJointed(boolean par1) {
			this.hasJointed = par1;

		}

		@Override
		public void setLocked(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ

			this.hasLocked = par1;
		}

	}

	public static class Storage extends CapabilityStorage<ISkillPanel>{

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<ISkillPanel> capability, ISkillPanel instance,
				EnumFacing side) {
			comp.setString("id", instance.getPanel().getKey().getResourcePath());
			comp.setInteger("level", instance.getLevel());
			comp.setBoolean("lock", instance.hasLocked());
		}

		@Override
		public void readNBT(NBTTagCompound comp, Capability<ISkillPanel> capability, ISkillPanel instance,
				EnumFacing side) {
			if(comp.hasKey("id")){
				String id = comp.getString("id");
				SkillPanel panel = SkillPanelRegistry.instance().get(id);
				instance.setPanel(panel!=null?panel : SkillPanels.DUMMY);
			}
			if(comp.hasKey("level")){
				int lv = comp.getInteger("level");
				instance.setLevel(lv);
			}
			if(comp.hasKey("lock")){
				instance.setLocked(comp.getBoolean("lock"));
			}
		}

	}


	public static ComponentCapabilityAdapters.ItemStack<ISkillPanel> adapter = adapterBase.createChildItem("unsagaAbilityAttachable");

	static{
		adapter.setPredicate(ev -> HSLibs.cast(ev, in -> in.getItem() instanceof ItemSkillPanel));
		adapter.setRequireSerialize(true);
	}

	public static void registerEvents(){
		adapter.registerAttachEvent();
	}

	public static SkillPanel getPanel(ItemStack stack){
		return adapter.getCapability(stack).getPanel();
	}

	public static int getLevel(ItemStack is){
		return adapter.getCapability(is).getLevel();
	}
}
