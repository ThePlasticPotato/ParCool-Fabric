package com.alrex.parcool;

import com.alrex.parcool.client.animation.AnimatorList;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.ActionList;
import com.alrex.parcool.common.capability.capabilities.Capabilities;
import com.alrex.parcool.common.item.ItemRegistry;
import com.alrex.parcool.common.potion.Effects;
import com.alrex.parcool.common.potion.PotionRecipeRegistry;
import com.alrex.parcool.common.potion.Potions;
import com.alrex.parcool.common.registries.EventBusForgeRegistry;
import com.alrex.parcool.common.registries.EventBusModRegistry;
import com.alrex.parcool.extern.feathers.FeathersManager;
import com.alrex.parcool.proxy.ClientProxy;
import com.alrex.parcool.proxy.CommonProxy;
import com.alrex.parcool.proxy.ServerProxy;
import com.alrex.parcool.server.command.CommandRegistry;
//identifier

import com.alrex.parcool.utilities.FabricDistExecutor;
import com.alrex.parcool.utilities.RenderTickHandler;
import com.mojang.brigadier.CommandDispatcher;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTickStartCallback;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParCool implements ModInitializer {

	public static float PARTIALTICK = 0;
	public static final String MOD_ID = "parcool";
	private static final String PROTOCOL_VERSION =
			Integer.toHexString(ActionList.ACTION_REGISTRIES.size())
					+ "."
					+ Integer.toHexString(AnimatorList.ANIMATORS.size());


	//todo replace with new network system
	public static final SimpleChannel CHANNEL_INSTANCE = new SimpleChannel(new Identifier(ParCool.MOD_ID, "message"));


	public static final CommonProxy PROXY = FabricDistExecutor.unsafeRunForDist(
			() -> ClientProxy::new,
			() -> ServerProxy::new
	);

	public static final Logger LOGGER = LogManager.getLogger();

	//only in Client
	public static boolean isActive() {
		return ParCoolConfig.CONFIG_CLIENT.parCoolActivation.get();
	}

	//only in Client
	public static void setActivation(boolean activation) {
		ParCoolConfig.CONFIG_CLIENT.parCoolActivation.set(activation);
	}

	@Override
	public void onInitialize() {
		setup();
		doClientStuff();
		//todo replace capabilities
		Capabilities.register();
		Effects.registerAll();
		ItemRegistry.registerAll();

		// todo : readd configs

		//todo client stuff :3dsmile:
		Potions.registerAll();

		registerEvents();
	}


	public void registerEvents() {
		RenderTickStartCallback.EVENT.register(new RenderTickHandler());

	}
	public ParCool() {

	}

	private void setup() {
		CommandRegistrationCallback.EVENT.register(CommandRegistry::register);
		PROXY.registerMessages(CHANNEL_INSTANCE);
	}

	private void doClientStuff() {

	}


	public class Client implements ClientModInitializer {

		@Override
		public void onInitializeClient() {
			KeyBindings.register();
		}
	}

}
