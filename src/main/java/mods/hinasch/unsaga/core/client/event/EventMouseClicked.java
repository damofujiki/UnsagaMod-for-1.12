package mods.hinasch.unsaga.core.client.event;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.net.packet.PacketAddExhaution;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventMouseClicked {


	public static final float EXHAUTION_BASE = 0.8F;
//	SkillPanel swimming = SkillPanelRegistry.instance().swimming;

	@SubscribeEvent
	public void onRightClickedEmpty(RightClickEmpty ev){
		if(ev.getEntityPlayer().isInWater() && this.isHeldNoItem(ClientHelper.getPlayer()) && !ClientHelper.getPlayer().isSwingInProgress){
			this.swimmingEvent(ev);
		}
	}
//	@SubscribeEvent
//	public void onMouseClicked(MouseEvent ev){
//		if(ev.getButton()==1){
//
//			if(ClientHelper.getPlayer().isInWater() && this.isHeldNoItem(ClientHelper.getPlayer()) && !ClientHelper.getPlayer().isSwingInProgress){
//				this.swimmingEvent(ev);
//			}
//
//
//		}
//	}

	public void swimmingEvent(RightClickEmpty ev){
		if(SkillPanelAPI.hasPanel(ClientHelper.getPlayer(),SkillPanels.SWIMMING )){
//			ClientHelper.getPlayer().swingArm(EnumHand.MAIN_HAND);
//			ClientHelper.getPlayer().swingArm(EnumHand.OFF_HAND);
			ClientHelper.getPlayer().playSound(SoundEvents.ENTITY_PLAYER_SWIM, 0.5F, 1.0F);
			Vec3d vec = ClientHelper.getPlayer().getLookVec().normalize().scale(0.7D);
			int level = SkillPanelAPI.getHighestPanelLevel(ClientHelper.getPlayer(), SkillPanels.SWIMMING).getAsInt();
			float exhaution = EXHAUTION_BASE - (1.0F*(float)level);
//			double vecY = vec.yCoord >0.0F ? 0.0F : vec.yCoord;
			ClientHelper.getPlayer().setVelocity(vec.x,vec.y,vec.z);
			UnsagaMod.packetDispatcher.sendToServer(PacketAddExhaution.create(exhaution));
		}
	}
	public boolean isHeldNoItem(EntityPlayer ep){
		if(ep.getHeldItemMainhand()==null && ep.getHeldItemOffhand()==null){
			return true;
		}
		return false;
	}
}
