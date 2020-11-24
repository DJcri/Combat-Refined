package com.djcri.combatenhanced;

import java.util.List;
import java.util.function.Function;

import com.djcri.combatenhanced.util.Reference;
import com.djcri.combatenhanced.util.Reflector;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class Handler {
	public static final List<Function<EntityLivingBase, Boolean>> PREDICATES = Lists.newArrayList();

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void shieldFix(LivingAttackEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource damageSourceIn = event.getSource();
		if (CRConfig.shieldFix && !damageSourceIn.isUnblockable() && entity.isHandActive()
				&& !entity.getActiveItemStack().isEmpty()) {
			Item item = entity.getActiveItemStack().getItem();

			if (item.getItemUseAction(entity.getActiveItemStack()) == EnumAction.BLOCK) {
				if (item.getMaxItemUseDuration(entity.getActiveItemStack()) - entity.getItemInUseCount() < 5) {
					// entity.activeItemStackUseCount =
					// item.getMaxItemUseDuration(entity.getActiveItemStack()) - 5;
					new Reflector<>(entity, EntityLivingBase.class, "activeItemStackUseCount", "field_184628_bn",
							item.getMaxItemUseDuration(entity.getActiveItemStack()) - 5).setField();
					entity.hurtResistantTime = entity.maxHurtResistantTime / 2 + 1;
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void onLeftClick(MouseEvent event) {
		EntityPlayer plr = Minecraft.getMinecraft().player;
		if (CRConfig.waDisabled && !plr.getHeldItemMainhand().isEmpty() && event.getButton() == 0
				&& plr.getCooledAttackStrength(0.0F) < CRConfig.waPower) {
			event.setCanceled(true);
			Minecraft.getMinecraft().gameSettings.keyBindAttack
					.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
		}
	}

	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
		if (CRConfig.stgEnabled) {
			IBlockState state = event.getWorld().getBlockState(event.getPos()).getActualState(event.getWorld(),
					event.getPos());
			if (state.getCollisionBoundingBox(event.getWorld(), event.getPos()) != Block.NULL_AABB) {
				return;
			}

			EntityPlayer player = event.getEntityPlayer();
			if (player == null) {
				return;
			}

			float blockReachDistance = 4.5F;

			Vec3d from = new Vec3d(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
			Vec3d vec3d = player.getLook(1.0F);
			Vec3d to = from.addVector(vec3d.x * blockReachDistance, vec3d.y * blockReachDistance,
					vec3d.z * blockReachDistance);

			EntityLivingBase targetEntity = getEntityClosestToStartPos(player, event.getWorld(), from, to);

			if (targetEntity != null) {
				if (!event.getWorld().isRemote) {
					player.attackTargetEntityWithCurrentItem(targetEntity);
					player.resetCooldown();
				}
			}
		}
	}

	private static EntityLivingBase getEntityClosestToStartPos(EntityPlayer player, World world, Vec3d startPos,
			Vec3d endPos) {
		EntityLivingBase entityLiving = null;
		List<Entity> list = world.getEntitiesInAABBexcluding(player,
				new AxisAlignedBB(startPos.x, startPos.y, startPos.z, endPos.x, endPos.y, endPos.z),
				Predicates.and(EntitySelectors.CAN_AI_TARGET, e -> {
					boolean filter = e != null && e.canBeCollidedWith() && e instanceof EntityLivingBase
							&& !(e instanceof FakePlayer);

					if (filter) {
						for (Function<EntityLivingBase, Boolean> predicate : PREDICATES) {
							filter &= predicate.apply((EntityLivingBase) e);
						}
					}

					return filter;
				}));

		double d0 = 0.0D;
		AxisAlignedBB axisAlignedBB;

		for (Entity entity : list) {
			axisAlignedBB = entity.getEntityBoundingBox().expand(0.3D, 0.3D, 0.3D);
			RayTraceResult raytraceResult = axisAlignedBB.calculateIntercept(startPos, endPos);

			if (raytraceResult != null) {
				double d1 = startPos.squareDistanceTo(raytraceResult.hitVec);

				if (d1 < d0 || d0 == 0.0D) {
					entityLiving = (EntityLivingBase) entity;
					d0 = d1;
				}
			}
		}
		return entityLiving;
	}
}
