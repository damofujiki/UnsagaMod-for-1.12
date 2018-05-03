//package mods.hinasch.unsaga.ability.specialmove.action;
//
//import java.util.List;
//import java.util.Optional;
//
//import javax.annotation.Nullable;
//
//import com.google.common.collect.Lists;
//
//import mods.hinasch.lib.container.inventory.InventoryHandler;
//import mods.hinasch.lib.core.HSLib;
//import mods.hinasch.lib.item.ItemUtil;
//import mods.hinasch.lib.util.VecUtil;
//import mods.hinasch.unsaga.ability.specialmove.SpecialMoveInvoker;
//import mods.hinasch.unsaga.ability.specialmove.SpecialMoveInvoker.InvokeType;
//import mods.hinasch.unsaga.ability.specialmove.action.AsyncSpecialMoveEvents.ArrowKnock;
//import mods.hinasch.unsaga.common.tool.ItemBowUnsaga;
//import mods.hinasch.unsaga.core.entity.EntityStateCapability;
//import mods.hinasch.unsaga.core.entity.StatePropertyArrow.StateArrow;
//import mods.hinasch.unsaga.core.entity.StateRegistry;
//import net.minecraft.enchantment.EnchantmentHelper;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.projectile.EntityArrow;
//import net.minecraft.init.Enchantments;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemArrow;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumActionResult;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.math.Vec3d;
//
//public class SpecialMoveRapidArrow extends SpecialMoveBase{
//
//	final int additionalArrowNumber;
//
//	public SpecialMoveRapidArrow(int num) {
//		super(InvokeType.BOW);
//		this.additionalArrowNumber = num;
//		this.addAction(new IAction<SpecialMoveInvoker>(){
//
//			private ItemStack findAmmo(EntityLivingBase player)
//			{
//				if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
//				{
//					return player.getHeldItem(EnumHand.OFF_HAND);
//				}
//				else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
//				{
//					return player.getHeldItem(EnumHand.MAIN_HAND);
//				}
//				else
//				{
//					Optional<ItemStack> arrow = Optional.empty();
//					if(player instanceof EntityPlayer){
//						InventoryHandler inv = new InventoryHandler(((EntityPlayer)player).inventory);
//						arrow = inv.toStream(0,((EntityPlayer)player).inventory.getSizeInventory())
//								.map(in -> in.getStack()).filter(is -> ItemUtil.isItemStackPresent(is)&& this.isArrow(is)).findFirst();
//					}else{
//						arrow = Optional.of(new ItemStack(Items.ARROW));
//					}
//					return arrow.isPresent() ? arrow.get() : null;
//				}
//			}
//
//			protected boolean isArrow(@Nullable ItemStack stack)
//			{
//				return stack != null && stack.getItem() instanceof ItemArrow;
//			}
//
//			@Override
//			public EnumActionResult apply(SpecialMoveInvoker context) {
//				float f = context.getChargedTime() * 0.01F;
//				EntityLivingBase shooter = context.getPerformer();
//				ItemStack arrow = this.findAmmo(context.getPerformer());
//				boolean isCreative = context.getPerformer() instanceof EntityPlayer&& ((EntityPlayer)context.getPerformer()).isCreative();
//				boolean isInfinite = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, context.getArticle().get())>0;
//				if(context.getArticle().isPresent() && context.getArrowComponent().isPresent()){
//					List<EntityArrow> arrows = Lists.newArrayList();
//					arrows.add(context.getArrowComponent().get().arrowEntity);
//					for(int i=0;i<additionalArrowNumber;i++){
//
//						if(arrow!=null || isCreative || isInfinite){
//							if(arrow==null){
//								arrow = new ItemStack(Items.ARROW);
//							}
//							ItemArrow arrowItem = (ItemArrow)((arrow.getItem() instanceof ItemArrow) ? arrow.getItem() : Items.ARROW);
//							EntityArrow entityArrow = arrowItem.createArrow(context.getWorld(), arrow,shooter);
//							entityArrow.setAim(shooter, shooter.rotationPitch, shooter.rotationYaw, 0.0F, f * 3.0F, 1.0F);
//							ItemBowUnsaga.setArrowProperties(shooter, context.getArticle().get(), entityArrow, f, (isCreative || isInfinite));
//							Vec3d v = VecUtil.getShake(VecUtil.getVecFromEntityMotion(context.getWorld(), entityArrow), context.getWorld().rand, 2, 4, 2, 4);
//							entityArrow.setVelocity(v.xCoord, v.yCoord, v.zCoord);
//							arrows.add(entityArrow);
//
//							if(shooter instanceof EntityPlayer){
//			                    ItemBowUnsaga.processDecreaseArrow((EntityPlayer) shooter, arrow, isCreative || isInfinite);
//							}
//							if(EntityStateCapability.adapter.hasCapability(entityArrow)){
//								StateArrow state = (StateArrow) EntityStateCapability.adapter.getCapability(entityArrow).getState(StateRegistry.instance().stateArrow);
//								state.setCancelHurtRegistance(true);
//							}
//						}
//					}
//					ArrowKnock event = new ArrowKnock(shooter,arrows);
////					HSLib.core().events.scannerEventPool.addEvent(event);
//					HSLib.core().addAsyncEvent(shooter, event);
//					return EnumActionResult.SUCCESS;
//				}
//
//
//
//				return EnumActionResult.PASS;
//			}});
//	}
//
//}
