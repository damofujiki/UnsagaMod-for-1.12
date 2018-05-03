//package mods.hinasch.unsaga.damage;
//
//import javax.annotation.Nullable;
//
//import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
//import mods.hinasch.lib.capability.CapabilityAdapterFactory.ICapabilityAdapterPlan;
//import mods.hinasch.lib.capability.CapabilityAdapterFrame;
//import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
//import mods.hinasch.lib.capability.StorageDummy;
//import mods.hinasch.unsaga.UnsagaMod;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.CapabilityInject;
//
//public class AdditionalDamageAttributeCapability {
//
//	@CapabilityInject(IAdditionalDamageAttribute.class)
//	public static Capability<IAdditionalDamageAttribute> CAPA;
//
//	public static final String SYNC_ID = "unsaga.additional_damage_data";
//
//	public static interface IAdditionalDamageAttribute{
//
//		public AdditionalDamageData getData();
//		public void setData(AdditionalDamageData data);
//	}
//
//	public static final ICapabilityAdapterPlan<IAdditionalDamageAttribute> _INTERFACE =
//			new CapabilityAdapterPlanImpl(()->CAPA,()->IAdditionalDamageAttribute.class,()->DefaultImpl.class,()->new StorageDummy());
//
//	public static class DefaultImpl implements IAdditionalDamageAttribute{
//
//		AdditionalDamageData data;
//
//		@Override
//		public @Nullable AdditionalDamageData getData() {
//			// TODO 自動生成されたメソッド・スタブ
//			return data;
//		}
//
//		@Override
//		public void setData(AdditionalDamageData data) {
//			// TODO 自動生成されたメソッド・スタブ
//			this.data = data;
//		}
//
//	}
//
//	public static CapabilityAdapterFrame<IAdditionalDamageAttribute> builder = UnsagaMod.capabilityAdapterFactory.create(_INTERFACE);
//	public static ComponentCapabilityAdapters.Entity<IAdditionalDamageAttribute> adapter = builder.createChildEntity(SYNC_ID);
//
//	static{
//		adapter.setPredicate(ev -> ev.getObject() instanceof EntityLivingBase);
//		adapter.setRequireSerialize(false);
//	}
//}
