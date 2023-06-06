package com.alrex.parcool;

import com.alrex.parcool.client.animation.AnimatorList;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.ActionList;
import com.alrex.parcool.common.action.ActionProcessor;
import com.alrex.parcool.common.capability.capabilities.Capabilities;
import com.alrex.parcool.common.item.ItemRegistry;
import com.alrex.parcool.common.potion.Effects;
import com.alrex.parcool.common.potion.Potions;
import com.alrex.parcool.proxy.ClientProxy;
import com.alrex.parcool.proxy.CommonProxy;
import com.alrex.parcool.proxy.ServerProxy;
import com.alrex.parcool.server.command.CommandRegistry;
//identifier

import com.alrex.parcool.utilities.FabricDistExecutor;
import com.alrex.parcool.utilities.ticker.*;
import dev.onyxstudios.cca.api.v3.entity.PlayerCopyCallback;
import io.github.fabricators_of_create.porting_lib.event.client.CameraSetupCallback;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTickStartCallback;
import io.github.fabricators_of_create.porting_lib.event.common.EntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.PlayerTickEvents;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParCool implements ModInitializer {

	public static float PARTIALTICK = 0;

	public static ActionProcessor ACTION_PROCESSOR = new ActionProcessor();
	public static final String MOD_ID = "parcool";
	private static final String PROTOCOL_VERSION =
			Integer.toHexString(ActionList.ACTION_REGISTRIES.size())
					+ "."
					+ Integer.toHexString(AnimatorList.ANIMATORS.size());


	//todo replace with new network system
	public static final SimpleChannel CHANNEL_INSTANCE = new SimpleChannel(new Identifier(ParCool.MOD_ID, "message"));

//	public static final ClientProxy CLIENT_PROXY = new ClientProxy();
//	public static final ServerProxy SERVER_PROXY = new ServerProxy();

//todo networking lol

	public static final CommonProxy PROXY = switch(FabricLoader.getInstance().getEnvironmentType()) {
		case CLIENT -> new ClientProxy();
		case SERVER -> new ServerProxy();
	};

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
		//todo finish capabilities :clueless:
//		Capabilities.register();
		Effects.registerAll();
		ItemRegistry.registerAll();

		PROXY.registerMessages(CHANNEL_INSTANCE);

		// nevermind configs work :3dsmile:
		ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.SERVER, ParCoolConfig.SERVER_SPEC);



		//todo client stuff :3dsmile:
		Potions.registerAll();

		registerEvents();
	}


	public void registerEvents() {
		RenderTickStartCallback.EVENT.register(new RenderTickHandler());
		CameraSetupCallback.EVENT.register(new CameraSetupHandler());
		PlayerTickEvents.END.register(new PlayerTickHandler());
		ServerTickEvents.END_SERVER_TICK.register(new ServerTickHandler());
		ClientTickEvents.END_CLIENT_TICK.register(new ClientTickHandler());
		ClientTickEvents.START_CLIENT_TICK.register(new ClientTickHandler());
		PlayerCopyCallback.EVENT.register(new PlayerCloneEventHandler());
		LivingEntityEvents.JUMP.register(new JumpEventHandler());
		LivingEntityEvents.FALL.register(new PlayerFallEventHandler());
		EntityEvents.ON_JOIN_WORLD.register(new PermissionSendEventHandler());
	}

	public ParCool() {

	}

	private void setup() {
		CommandRegistrationCallback.EVENT.register(CommandRegistry::register);
//		PROXY.registerMessages(CHANNEL_INSTANCE);
	}

	private void doClientStuff() {

	}

}
