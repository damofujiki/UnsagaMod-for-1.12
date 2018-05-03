package mods.hinasch.unsaga.ability;

import java.util.List;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.AbilityAPI.EquipmentSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AbilityCapability {


	@CapabilityInject(IAbilityAttachable.class)
	public static Capability<IAbilityAttachable> CAPA;
	public static final String SYNC_ID = "unsaga.ability_attachable";

	public static CapabilityAdapterFrame<IAbilityAttachable> adapterBase = UnsagaMod.capabilityAdapterFactory.create(
			new CapabilityAdapterPlanImpl(()->CAPA,()->IAbilityAttachable.class,()->DefaultImpl.class,Storage::new));
	public static ComponentCapabilityAdapters.ItemStack<IAbilityAttachable> adapter = adapterBase.createChildItem(SYNC_ID);

	static{

		adapter.setPredicate(ev -> HSLibs.cast(ev, in -> in.getItem() instanceof IAbilitySelector));
		adapter.setRequireSerialize(true);
	}

	public static class DefaultImpl implements IAbilityAttachable{

		int maxAbilitySize = 1;
		NonNullList<IAbility> list = NonNullList.create();
		NonNullList<IAbility> egoAbilityList = NonNullList.create();
		boolean hasInitialized = false;

		@Override
		public int getMaxAbilitySize() {
			// TODO 自動生成されたメソッド・スタブ
			return maxAbilitySize;
		}

		@Override
		public void setMaxAbilitySize(int size) {
			this.maxAbilitySize = size;
		}

		@Override
		public NonNullList<IAbility> getLearnedAbilities() {
			return list;
		}

		@Override
		public void setAbilities(NonNullList<IAbility> list) {
			this.list = list;

		}

		@Override
		public void removeAbility(IAbility ab) {
			if(this.list.indexOf(ab)>=0){
				this.list.set(this.list.indexOf(ab),AbilityRegistry.instance().EMPTY);
			}

		}

		@Override
		public boolean hasAbility(IAbility ab) {
			return this.list.contains(ab);
		}

		@Override
		public void addAbility(IAbility ab) {
			if(this.list.indexOf(AbilityRegistry.instance().EMPTY)>=0){
				this.list.set(this.list.indexOf(AbilityRegistry.instance().EMPTY),ab);
			}



		}

		@Override
		public boolean hasInitialized() {
			// TODO 自動生成されたメソッド・スタブ
			return hasInitialized;
		}

		@Override
		public void setInitialized(boolean par1) {
			this.hasInitialized = par1;
		}

		@Override
		public NBTTagCompound getSendingData() {
			return (NBTTagCompound) CAPA.writeNBT(this, null);
		}

		@Override
		public void catchSyncData(NBTTagCompound nbt) {
			CAPA.readNBT(this, null, nbt);

		}

		@Override
		public void onPacket(PacketSyncCapability message, MessageContext ctx) {
			// TODO 自動生成されたメソッド・スタブ
			if(ctx.side.isClient()){
				EntityPlayer ep = ClientHelper.getPlayer();
				if(message.getArgs()!=null){
					String id = message.getArgs().getString("slot");
					AbilityAPI.EquipmentSlot slot = AbilityAPI.EquipmentSlot.fromName(id);
					ItemStack stack = slot.getStackFrom(ep);
					if(adapter.hasCapability(stack)){
						adapter.getCapability(stack).catchSyncData(message.getNbt());
					}
				}else{
					ItemStack held = ep.getHeldItemMainhand();
					if(!held.isEmpty() && adapter.hasCapability(held)){
						adapter.getCapability(held).catchSyncData(message.getNbt());
					}
				}


			}
		}

		@Override
		public String getIdentifyName() {
			// TODO 自動生成されたメソッド・スタブ
			return SYNC_ID;
		}

		@Override
		public boolean isUniqueItem() {
			// TODO 自動生成されたメソッド・スタブ
			return !this.egoAbilityList.isEmpty();
		}


		@Override
		public void setLearnableUniqueAbilities(NonNullList<IAbility> in) {
			// TODO 自動生成されたメソッド・スタブ
			this.egoAbilityList =  in;
		}

		@Override
		public NonNullList<IAbility> getLearnableUniqueAbilities() {
			// TODO 自動生成されたメソッド・スタブ
			return this.egoAbilityList;
		}

		@Override
		public boolean isAbilityFull() {
			// TODO 自動生成されたメソッド・スタブ
			return !this.list.contains(AbilityRegistry.empty());
		}

		@Override
		public boolean isAbilityEmpty() {
			// TODO 自動生成されたメソッド・スタブ
			return this.list.stream().allMatch(in -> in==AbilityRegistry.empty());
		}

		@Override
		public void setAbility(int index, IAbility ab) {

			this.list.set(index, ab);

			UnsagaMod.logger.trace("ability", list);
		}

		@Override
		public void clearAbility(int size) {
			this.list = NonNullList.withSize(size, AbilityRegistry.instance().EMPTY);
		}

	}

	public static class Storage extends CapabilityStorage<IAbilityAttachable>{

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<IAbilityAttachable> capability,
				IAbilityAttachable instance, EnumFacing side) {
			comp.setBoolean("initialized", instance.hasInitialized());
			comp.setByte("size", (byte)instance.getMaxAbilitySize());
			UtilNBT.writeListToNBT(instance.getLearnedAbilities(), comp, "abilities");
			if(instance.isUniqueItem()){
				UtilNBT.writeListToNBT(instance.getLearnableUniqueAbilities(), comp, "egoAbilities");
			}
		}

		@Override
		public void readNBT(NBTTagCompound comp, Capability<IAbilityAttachable> capability, IAbilityAttachable instance,
				EnumFacing side) {
			if(comp.hasKey("initialized")){
				instance.setInitialized(comp.getBoolean("initialized"));
			}
			if(comp.hasKey("size")){
				instance.setMaxAbilitySize((int)comp.getByte("size"));
			}
			if(comp.hasKey("abilities")){
				List<IAbility> list = UtilNBT.readListFromNBT(comp, "abilities", Ability.FUNC_RESTORE);
				instance.setAbilities(ItemUtil.toNonNull(list,AbilityRegistry.EMPTY));
			}
			if(comp.hasKey("egoAbilities")){
				List<IAbility> list = UtilNBT.readListFromNBT(comp, "egoAbilities", Ability.FUNC_RESTORE);
				instance.setLearnableUniqueAbilities(ItemUtil.toNonNull(list, AbilityRegistry.EMPTY));
			}

		}

	}

	public static void registerEvents(){
		PacketSyncCapability.registerSyncCapability(AbilityCapability.SYNC_ID, AbilityCapability.CAPA);
		adapter.registerAttachEvent((inst,capa,face,ev)->{
			ItemStack obj = (ItemStack) ev.getObject();
			if(obj.getItem() instanceof IAbilitySelector && !inst.hasInitialized()){
				IAbilitySelector intf = (IAbilitySelector) obj.getItem();
				inst.setMaxAbilitySize(intf.getMaxAbilitySize());
				inst.clearAbility(intf.getMaxAbilitySize());
				inst.setInitialized(true);
			}
		});
	}
}
