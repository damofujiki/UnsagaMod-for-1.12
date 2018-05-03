package mods.hinasch.unsaga.core.entity.projectile.custom_arrow;

import javax.annotation.Nullable;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.particle.PacketParticle;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.specialmove.TechRegistry;
import mods.hinasch.unsaga.core.net.packet.PacketClientThunder;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.lp.LPAttribute;
import mods.hinasch.unsagamagic.spell.SpellRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public enum SpecialArrowType implements IIntSerializable{
			NONE(0),MAGIC_ARROW(1),EXORCIST(2),SHADOW_STITCH(3),ZAPPER(4)
			,PHOENIX(5);

			final int meta;
			private SpecialArrowType(int meta){
				this.meta = meta;
			}
			@Override
			public int getMeta() {
				// TODO 自動生成されたメソッド・スタブ
				return this.meta;
			}

			public void onImpact(ProjectileImpactEvent.Arrow e){
				if(this==NONE){
					return;
				}
				Entity shooter = e.getArrow().shootingEntity;
		    	if(this==SpecialArrowType.ZAPPER){
//		    		UnsagaMod.logger.trace(getName(), "called");
		    		HSLib.getPacketDispatcher().sendToAllAround(PacketSound.atEntity(SoundEvents.ENTITY_LIGHTNING_THUNDER, e.getArrow()), PacketUtil.getTargetPointNear(e.getArrow()));
		    		UnsagaMod.packetDispatcher.sendToAllAround(PacketClientThunder.create(XYZPos.createFrom(e.getArrow())), PacketUtil.getTargetPointNear(e.getArrow()));
		    	}
				if(e.getRayTraceResult().typeOfHit==RayTraceResult.Type.BLOCK){
					if(this==SpecialArrowType.SHADOW_STITCH){
						World world = e.getArrow().getEntityWorld();
						world.getEntitiesWithinAABB(EntityLivingBase.class,e.getArrow().getEntityBoundingBox().grow(3D, 2D, 3D),in ->in != e.getArrow().shootingEntity)
						.forEach(in ->{
							in.addPotionEffect(new PotionEffect(UnsagaPotions.STUN,ItemUtil.getPotionTime(8),0));
						});
					}

				}
				if(e.getRayTraceResult().typeOfHit==RayTraceResult.Type.ENTITY){

					Entity hitEntity = e.getRayTraceResult().entityHit;
					//フェニックスアローの自己回復
					if(this==PHOENIX && shooter instanceof EntityLivingBase){
		    			HSLib.getPacketDispatcher().sendToAllAround(PacketSound.atEntity(SoundEvents.ENTITY_PLAYER_LEVELUP, shooter), PacketUtil.getTargetPointNear(shooter));
		    			HSLib.getPacketDispatcher().sendToAllAround(PacketParticle.create(XYZPos.createFrom(shooter), EnumParticleTypes.VILLAGER_HAPPY	, 10), PacketUtil.getTargetPointNear(shooter));

		        		((EntityLivingBase) shooter).heal(1.0F);
					}
			    	if(this==EXORCIST && hitEntity instanceof EntityLivingBase){
			    		if(((EntityLivingBase) hitEntity).getCreatureAttribute()==EnumCreatureAttribute.UNDEAD){
			    			hitEntity.setFire(4);
			        		HSLib.getPacketDispatcher().sendToAllAround(PacketSound.atEntity(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, hitEntity), PacketUtil.getTargetPointNear(hitEntity));
			        		HSLib.getPacketDispatcher().sendToAllAround(PacketParticle.create(XYZPos.createFrom(hitEntity), EnumParticleTypes.VILLAGER_HAPPY	, 10), PacketUtil.getTargetPointNear(hitEntity));
			    		}

			    	}
				}


			}
			public void onLivingHurt(LivingHurtEvent e){
				if(this==EXORCIST && e.getEntityLiving().getCreatureAttribute()==EnumCreatureAttribute.UNDEAD){
					e.setAmount(e.getAmount() * 1.5F);
				}
				return;
			}
			public float getDamage(EntityLivingBase victim,float amount){
				if(this==EXORCIST && victim.getCreatureAttribute()==EnumCreatureAttribute.UNDEAD){
					return amount * 2.0F;
				}
				return amount;
			}
			public AdditionalDamageData getDamageSource(EntityLivingBase attacker,Entity arrow){
				AdditionalDamageData data = new AdditionalDamageData(DamageSource.causeArrowDamage((EntityArrow) arrow, attacker),this.getLPStrength(),General.SPEAR);
				if(this==EXORCIST){
					data.setSubTypes(Sub.SHOCK);
				}
//				if(this==MAGIC_ARROW){
//					data.setDamageTypeUnsaga(General.MAGIC);
//					data.setMagicDamage();
//				}
				return data;
			}

			public LPAttribute getLPStrength(){
				TechRegistry reg = TechRegistry.instance();
				switch(this){
				case EXORCIST:
					return reg.EXORCIST.getStrength().lp();
				case MAGIC_ARROW:
					return SpellRegistry.instance().FIRE_ARROW.getStrength().lp();
				case NONE:
					break;
				case PHOENIX:

					return reg.PHOENIX.getStrength().lp();
				case SHADOW_STITCH:
					return LPAttribute.ZERO;
				case ZAPPER:
					return reg.ZAPPER.getStrength().lp();
				default:
					break;

				}
				return new LPAttribute(0.3F,1);
			}
			public @Nullable EnumParticleTypes getParticle(){
				switch(this){
				case PHOENIX:
					return EnumParticleTypes.FLAME;
				case EXORCIST:
					return EnumParticleTypes.CRIT;
				case MAGIC_ARROW:
					return EnumParticleTypes.FLAME;
				case NONE:
					break;
				case SHADOW_STITCH:
					return EnumParticleTypes.PORTAL;
				case ZAPPER:
					return EnumParticleTypes.CRIT;
				default:
					break;

				}
				return null;
			}
			public static SpecialArrowType fromMeta(int meta){
				return HSLibs.fromMeta(SpecialArrowType.values(), meta);
			}
		}