package com.alrex.parcool.common.action;

import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTickStartCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.nio.ByteBuffer;
import java.util.List;

;import static net.fabricmc.api.EnvType.CLIENT;

public class ActionProcessor {
	private final ByteBuffer bufferOfPostState = ByteBuffer.allocate(128);
	private final ByteBuffer bufferOfPreState = ByteBuffer.allocate(128);
	private final ByteBuffer bufferOfStarting = ByteBuffer.allocate(128);
	private int staminaSyncCoolTimeTick = 0;

	//
	@Environment(CLIENT)
	public void onTickInClient() {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return;
		Animation animation = Animation.get(player);
		if (animation == null) return;
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		animation.tick(player, parkourability);
	}

	public void onTick(PlayerEntity player, boolean isClient) {
		Parkourability parkourability = Parkourability.get(player);
		IStamina stamina = IStamina.get(player);
		if (parkourability == null || stamina == null) return;
		List<Action> actions = parkourability.getList();
		boolean needSync = isClient && player.isMainPlayer();
		SyncActionStateMessage.Encoder builder = SyncActionStateMessage.Encoder.reset();

		if (needSync) {
			stamina.tick();
			staminaSyncCoolTimeTick++;
			if (staminaSyncCoolTimeTick > 5) {
				staminaSyncCoolTimeTick = 0;
				SyncStaminaMessage.sync(player);
			}
			if (stamina.isExhausted()) {
				player.setSprinting(false);
			}
		}
		parkourability.getAdditionalProperties().onTick(player, parkourability);
		for (Action action : actions) {
			StaminaConsumeTiming timing = action.getStaminaConsumeTiming();
			if (needSync) {
				bufferOfPreState.clear();
				action.saveSynchronizedState(bufferOfPreState);
				bufferOfPreState.flip();
			}
			if (action.isDoing()) {
				action.setDoingTick(action.getDoingTick() + 1);
				action.setNotDoingTick(0);
			} else {
				action.setDoingTick(0);
				action.setNotDoingTick(action.getNotDoingTick() + 1);
			}

			action.onTick(player, parkourability, stamina);
			if (isClient) {
				action.onClientTick(player, parkourability, stamina);
			} else {
				action.onServerTick(player, parkourability, stamina);
			}

			if (player.isMainPlayer()) {
				if (action.isDoing()) {
					boolean canContinue = action.canContinue(player, parkourability, stamina);
					if (!canContinue) {
						action.setDoing(false);
						action.onStopInLocalClient(player);
						action.onStop(player);
						builder.appendFinishMsg(parkourability, action);
					}
				} else {
					bufferOfStarting.clear();
					boolean start = action.canStart(player, parkourability, stamina, bufferOfStarting);
					bufferOfStarting.flip();
					if (start) {
						action.setDoing(true);
						action.onStartInLocalClient(player, parkourability, stamina, bufferOfStarting);
						bufferOfStarting.rewind();
						action.onStart(player, parkourability);
						builder.appendStartData(parkourability, action, bufferOfStarting);
						if (timing == StaminaConsumeTiming.OnStart)
							stamina.consume(parkourability.getActionInfo().getStaminaConsumptionOf(action.getClass()));
					}
				}
			}

			if (action.isDoing()) {
				action.onWorkingTick(player, parkourability, stamina);
				if (isClient) {
					action.onWorkingTickInClient(player, parkourability, stamina);
					if (player.isMainPlayer()) {
						action.onWorkingTickInLocalClient(player, parkourability, stamina);
						if (timing == StaminaConsumeTiming.OnWorking)
							stamina.consume(parkourability.getActionInfo().getStaminaConsumptionOf(action.getClass()));
					}
				} else {
					action.onWorkingTickInServer(player, parkourability, stamina);
				}
			}

			if (needSync) {
				bufferOfPostState.clear();
				action.saveSynchronizedState(bufferOfPostState);
				bufferOfPostState.flip();

				if (bufferOfPostState.limit() == bufferOfPreState.limit()) {
					while (bufferOfPreState.hasRemaining()) {
						if (bufferOfPostState.get() != bufferOfPreState.get()) {
							bufferOfPostState.rewind();
							builder.appendSyncData(parkourability, action, bufferOfPostState);
							break;
						}
					}
				} else {
					bufferOfPostState.rewind();
					builder.appendSyncData(parkourability, action, bufferOfPostState);
				}
			}
		}
		if (needSync) {
			SyncActionStateMessage.sync(player, builder);
		}
	}

	@Environment(CLIENT)
	public void onRenderTick() {
		PlayerEntity clientPlayer = MinecraftClient.getInstance().player;
		if (clientPlayer == null) return;
		for (PlayerEntity player : clientPlayer.world.getPlayers()) {
			Parkourability parkourability = Parkourability.get(player);
			if (parkourability == null) return;
			List<Action> actions = parkourability.getList();
			for (Action action : actions) {
				action.onRenderTick(player, parkourability);
			}
		}
	}
	//@SubscribeEvent
	@Environment(CLIENT)
	public void onViewRender() {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return;
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		Animation animation = Animation.get(player);
		if (animation == null) return;
		animation.cameraSetup(player, parkourability);
	}
}
