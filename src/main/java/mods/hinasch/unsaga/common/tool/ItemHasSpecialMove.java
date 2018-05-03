package mods.hinasch.unsaga.common.tool;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.AbilityAPI;
import mods.hinasch.unsaga.ability.IAbilitySelector;
import mods.hinasch.unsaga.ability.specialmove.EventSprintTimer;
import mods.hinasch.unsaga.ability.specialmove.Tech;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.common.specialaction.IActionPerformer.TargetType;
import mods.hinasch.unsaga.core.net.packet.PacketSyncActionPerform;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHasSpecialMove extends ItemSword implements IAbilitySelector{
	public ItemHasSpecialMove(ToolMaterial material) {
		super(material);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	protected boolean hasSpecialMoveType(ItemStack is,InvokeType type){
		if(AbilityAPI.getLearnedSpecialMove(is).isPresent()){
			if(AbilityAPI.getLearnedSpecialMove(is).get().getAction().getInvokeTypes().contains(type)){
				return true;
			}
		}
		return false;
	}

	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if(hand==EnumHand.OFF_HAND && AbilityAPI.hasBlockingAbility(itemStackIn)){
			playerIn.setActiveHand(hand);
			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}
		if(this.hasSpecialMoveType(itemStackIn, InvokeType.CHARGE) && playerIn.isSneaking()){
			playerIn.setActiveHand(hand);
			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}
		ChatHandler.sendChatToPlayer(playerIn, String.valueOf(EventSprintTimer.getSprintTimer(playerIn)));
		if(this.hasSpecialMoveType(itemStackIn, InvokeType.SPRINT_RIGHTCLICK) && EventSprintTimer.getSprintTimer(playerIn)>10 && playerIn.isSneaking()){
			if(WorldHelper.isClient(worldIn))HSLib.getPacketDispatcher().sendToServer(PacketSyncCapability.create(LivingHelper.CAPA	, LivingHelper.ADAPTER.getCapability(playerIn)));
			EventSprintTimer.resetTimer(playerIn);
			Tech move = AbilityAPI.getLearnedSpecialMove(itemStackIn).get();
			TechInvoker invoker = new TechInvoker(worldIn, playerIn, move);
			invoker.setArtifact(itemStackIn);
			invoker.setInvokeType(InvokeType.SPRINT_RIGHTCLICK);
			invoker.invoke();
			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}
		if(this.hasSpecialMoveType(itemStackIn, InvokeType.RIGHTCLICK) && playerIn.isSneaking()){
			Tech move = AbilityAPI.getLearnedSpecialMove(itemStackIn).get();
			TechInvoker invoker = new TechInvoker(worldIn, playerIn, move);
			invoker.setArtifact(itemStackIn);
			invoker.setInvokeType(InvokeType.RIGHTCLICK);
			invoker.setTargetType(TargetType.TARGET);
			invoker.invoke();
			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}
		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack stack = player.getHeldItem(hand);
		if(this.hasSpecialMoveType(stack, InvokeType.USE) && player.isSneaking()){
			Tech move = AbilityAPI.getLearnedSpecialMove(stack).get();
			TechInvoker invoker = new TechInvoker(worldIn, player, move);
			invoker.setArtifact(stack);
			invoker.setTargetType(TargetType.POSITION);
			invoker.setInvokeType(InvokeType.USE);
			invoker.setTargetCoordinate(pos);
			invoker.invoke();
			return EnumActionResult.SUCCESS;

		}
		return EnumActionResult.PASS;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemStackIn, World worldIn, EntityLivingBase playerIn, int timeLeft)
	{
		if(WorldHelper.isClient(worldIn)){
			return;
		}

		this.invokeChargedAttack(itemStackIn, worldIn, playerIn, timeLeft);
		UnsagaMod.packetDispatcher.sendToAll(PacketSyncActionPerform.createSyncChargedAttackPacket(timeLeft));
	}

	public void invokeChargedAttack(ItemStack itemStackIn, World worldIn, EntityLivingBase playerIn, int timeLeft)
	{
//		ChatHandler.sendChatToPlayer((EntityPlayer) playerIn, WorldHelper.isServer(worldIn) ? "server" : "client");
		Tech move = AbilityAPI.getLearnedSpecialMove(itemStackIn).get();
		TechInvoker invoker = new TechInvoker(worldIn, playerIn, move);
		if(move.isRequireTarget()){
			invoker.setTargetType(TargetType.TARGET);
		}
		invoker.setArtifact(itemStackIn);
		invoker.setInvokeType(InvokeType.CHARGE);
		invoker.setChargedTime(this.getMaxItemUseDuration(itemStackIn)-timeLeft);
		invoker.invoke();


	}
	@Override
	public int getMaxAbilitySize() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
}
