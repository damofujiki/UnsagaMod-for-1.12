package mods.hinasch.unsaga.core.item.wearable;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.iface.LivingHurtEventBase;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.AbilityAPI;
import mods.hinasch.unsaga.core.item.weapon.ItemShieldUnsaga;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.util.LivingHurtEventUnsagaBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@Deprecated
public class EventShield {

	/** バニラから*/
	public static boolean isBlockableVec(EntityLivingBase shielder,DamageSource damageSourceIn){
        Vec3d vec3d = damageSourceIn.getDamageLocation();

        if (vec3d != null)
        {
            Vec3d vec3d1 = shielder.getLook(1.0F);
            Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(shielder.posX, shielder.posY, shielder.posZ)).normalize();
            vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

            if (vec3d2.dotProduct(vec3d1) < 0.0D)
            {
                return true;
            }
        }
        return false;
	}

	public static LivingHurtEventBase shieldGuard = new LivingHurtEventUnsagaBase(){

		@Override
		public boolean apply(LivingHurtEvent e, DamageSourceUnsaga dsu) {
			// ブロックアクション中に発動（バニラ盾と同じ）
			return e.getEntityLiving().isActiveItemStackBlocking() && isBlockableVec(e.getEntityLiving(), dsu);
		}

		@Override
		public String getName() {
			// TODO 自動生成されたメソッド・スタブ
			return "Enemy's Melee Attack Guard Event";
		}

		/** 盾完全防御の確率*/
		private float getShieldProb(EntityPlayer ep,int shieldPower){
			float base = shieldPower * 0.01F;
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.SHIELD)){
				float multiply = 1.0F + (0.15F * (float)SkillPanelAPI.getHighestPanelLevel(ep, SkillPanels.SHIELD).getAsInt());
				base *= multiply;
			}
			return MathHelper.clamp(base, 0, 0.99F);
		}


		@Override
		public DamageSource process(LivingHurtEvent e, DamageSourceUnsaga dsu) {
			ItemStack shield = e.getEntityLiving().getActiveItemStack();
			if(UnsagaMaterialCapability.adapter.hasCapability(shield)){
				UnsagaMaterial m = UnsagaMaterialCapability.adapter.getCapability(shield).getMaterial();
				//ブロックできる攻撃に限る
				if(!dsu.isUnblockable()){

					//アイテムにかかわらずブロック中は33%のダメージ減が働くのでそこからさらにダメージを減らせばよい
					float reduce = e.getAmount() * ((float)m.getShieldPower() * 0.01F); //ダメージ - (33%+盾パワー%)
					float reduced = MathHelper.clamp(e.getAmount() - reduce, 0.01F, 65535F);
					e.setAmount(reduced);
					UnsagaMod.logger.trace("shield", e.getAmount(),reduce,reduced);
				}
				/** 盾スキルを持ってる事が条件*/
				if(e.getEntityLiving() instanceof EntityPlayer && SkillPanelAPI.hasPanel((EntityPlayer) e.getEntityLiving(),SkillPanels.SHIELD)){
					this.processAvoidDamage(e, shield,m.getShieldPower(), dsu);
				}


			}
			return dsu;
		}

		/** 完全回避の発動*/
		private void processAvoidDamage(LivingHurtEvent e, ItemStack shield,int shieldPower,DamageSourceUnsaga dsu){
			if(AbilityAPI.getAttachedPassiveAbilities(shield).stream().filter(in->in.getBlockableDamage()!=null)
					.anyMatch(in -> in.getBlockableDamage().test(dsu))){ //所持アビリティに攻撃が対応しているか

				EntityPlayer ep = (EntityPlayer) e.getEntityLiving();
				float prob = this.getShieldProb(ep, shieldPower);
				if(ep.getRNG().nextFloat()<prob){
					e.setAmount(0);
					if(WorldHelper.isServer(ep.getEntityWorld())){
						HSLib.getPacketDispatcher().sendTo(PacketSound.atEntity(SoundEvents.BLOCK_ANVIL_LAND, ep), (EntityPlayerMP) ep);
					}

				}

			}
		}};

		/** 盾へのダメージ*/
		public static LivingHurtEventBase shieldDamage = new LivingHurtEventBase(){

			@Override
			public boolean apply(LivingHurtEvent e, DamageSource dsu) {
				// TODO 自動生成されたメソッド・スタブ
				return e.getEntityLiving().isActiveItemStackBlocking() && EventShield.isBlockableVec(e.getEntityLiving(), dsu);
			}

			//バニラのままでは盾へのダメージが適用されないため
			protected void damageShield(float damage,EntityLivingBase living)
			{

				ItemStack shield = living.getActiveItemStack();
				UnsagaMod.logger.trace("shield",damage,shield);
				if (shield != null && shield.getItem() instanceof ItemShieldUnsaga)
				{
					int itemDamage = 0;
					if(damage>0.0F){
						itemDamage = 1 + (int)Math.floor(damage);
					}else{
						itemDamage = living.getEntityWorld().rand.nextInt(3)+1;
					}
					shield.damageItem(itemDamage,living);


					//バニラから盾の破壊プロセス
					if (ItemUtil.getStackSize(living.getActiveItemStack()) <= 0)
					{
						EnumHand enumhand = living.getActiveHand();
						if(living instanceof EntityPlayer){
							net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem((EntityPlayer) living, living.getActiveItemStack(), enumhand);
						}


						if (enumhand == EnumHand.MAIN_HAND)
						{
							living.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, (ItemStack)null);
						}
						else
						{
							living.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, (ItemStack)null);
						}

						living.resetActiveHand();
						living.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + living.getEntityWorld().rand.nextFloat() * 0.4F);
					}
				}
			}

			@Override
			public String getName() {
				// TODO 自動生成されたメソッド・スタブ
				return "Shield Events";
			}

			@Override
			public DamageSource process(LivingHurtEvent e, DamageSource dsu) {
				this.damageShield(e.getAmount(), e.getEntityLiving());
				return dsu;
			}
		};

		public static void registerEvents(){
			HSLib.events.livingHurt.getEventsMiddle().add(shieldDamage);
			HSLib.events.livingHurt.getEventsMiddle().add(shieldGuard);
		}
}
