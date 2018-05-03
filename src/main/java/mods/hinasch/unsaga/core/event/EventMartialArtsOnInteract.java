package mods.hinasch.unsaga.core.event;

import java.util.Optional;
import java.util.Set;

import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.lib.util.VecUtil.EnumHorizontalDirection;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.specialmove.MartialArtsSetting;
import mods.hinasch.unsaga.ability.specialmove.Tech;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.common.specialaction.IActionPerformer.TargetType;
import mods.hinasch.unsaga.material.UnsagaWeightType;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class EventMartialArtsOnInteract{

	@SubscribeEvent
	public void rightClickBlock(RightClickBlock e){
		if(!e.getEntityPlayer().getEntityWorld().isRemote){
			return;
		}
		this.performMartialArts(e.getEntityPlayer(), Optional.empty(),e);
	}
	@SubscribeEvent
	public void rightClick(RightClickEmpty e){
		this.performMartialArts(e.getEntityPlayer(), Optional.empty());
	}
	@SubscribeEvent
	public void interact(EntityInteract e){
		if(!e.getEntityPlayer().getEntityWorld().isRemote){
			return;
		}
		this.performMartialArts(e.getEntityPlayer(), (e.getTarget() instanceof EntityLivingBase)?Optional.of((EntityLivingBase)e.getTarget()) : Optional.empty());
	}

	public void performMartialArts(EntityPlayer ep,Optional<EntityLivingBase> target,Object... args){
		if(this.checkMartialArtsPrecondition(ep)){
			Set<EnumHorizontalDirection> playerDirections = VecUtil.getHorizontalDirection(ep);
			UnsagaMod.logger.trace("matrialArts", playerDirections);
			UnsagaWeightType weightType = UnsagaWeightType.calcArmorWeightType(ep);
//			Set<EnumHorizontalDirection> directionsKeySet =
			Optional<Tech> wazaEffect = MartialArtsSetting.getSpecialMove(weightType, playerDirections, !ep.onGround, ep.isSneaking(),ep.isSprinting());
			UnsagaMod.logger.trace("matrialArts", wazaEffect);

			RightClickBlock ev = null;
			if(args!=null && args.length>0 && args[0]!=null){
				ev = (RightClickBlock) args[0];
			}
//			StateSpecialMove prop = (StateSpecialMove) EntityStateCapability.adapter.getCapability(ep).getState(StateRegistry.instance().stateSpecialMove);

			if(wazaEffect.isPresent()  ){
				if(wazaEffect.get().getAction()==null){
					return;
				}
//				UnsagaMod.packetDispatcher.sendToServer(new PacketSyncActionPerform(target.isPresent() ? target.get() : null,false,wazaEffect.get().getKey().getResourcePath()));
				TechInvoker invoker = new TechInvoker(ep.getEntityWorld(),ep,wazaEffect.get());
				if(target.isPresent()){
					invoker.setTarget(target.get());
					invoker.setTargetType(TargetType.TARGET);
				}

				if(ev!=null){
					invoker.setInvokeType(InvokeType.USE);
					invoker.setTargetCoordinate(ev.getPos());
				}else{
					invoker.setInvokeType(InvokeType.RIGHTCLICK);
				}

				invoker.invoke();
			}
		}

	}

	public boolean checkMartialArtsPrecondition(EntityPlayer ep) {
		if(ep.getHeldItemMainhand().isEmpty()){

			if(SkillPanelAPI.hasPanel(ep, SkillPanels.PUNCH)){
				return true;
			}
		}
		return false;
	}


}
