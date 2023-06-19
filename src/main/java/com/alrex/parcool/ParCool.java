package com.alrex.parcool;

import com.alrex.parcool.client.animation.AnimatorList;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.ActionList;
import com.alrex.parcool.common.action.ActionProcessor;
import com.alrex.parcool.common.capability.capabilities.Capabilities;
import com.alrex.parcool.common.item.ItemRegistry;
import com.alrex.parcool.common.potion.Effects;
import com.alrex.parcool.common.potion.Potions;
<<<<<<< Updated upstream
import com.alrex.parcool.common.registries.EventBusForgeRegistry;
import com.alrex.parcool.common.registries.EventBusModRegistry;
import com.alrex.parcool.extern.feathers.FeathersManager;
=======
import com.alrex.parcool.proxy.AllProxy;
>>>>>>> Stashed changes
import com.alrex.parcool.proxy.ClientProxy;
import com.alrex.parcool.proxy.CommonProxy;
import com.alrex.parcool.proxy.ServerProxy;
import com.alrex.parcool.server.command.CommandRegistry;
<<<<<<< Updated upstream
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
=======
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
>>>>>>> Stashed changes
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

// todo: temp
//	public static final CommonProxy PROXY = switch(FabricLoader.getInstance().getEnvironmentType()) {
//		case CLIENT -> new ClientProxy();
//		case SERVER -> new ServerProxy();
//	};

	public static final AllProxy PROXY = new AllProxy();

	public static final Logger LOGGER = LogManager.getLogger();



	//only in Client
	public static boolean isActive() {
		return ParCoolConfig.CONFIG_CLIENT.parCoolActivation.get();
	}

	//only in Client
	public static void setActivation(boolean activation) {
		ParCoolConfig.CONFIG_CLIENT.parCoolActivation.set(activation);
	}

<<<<<<< Updated upstream
	public ParCool() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::setup);
		eventBus.addListener(this::processIMC);
		eventBus.addListener(this::doClientStuff);
		eventBus.addListener(this::doServerStuff);
		eventBus.register(Capabilities.class);
		Effects.registerAll(eventBus);
		Potions.registerAll(eventBus);
		MinecraftForge.EVENT_BUS.addListener(this::registerCommand);
		MinecraftForge.EVENT_BUS.register(this);
		ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		FeathersManager.init();

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ParCoolConfig.SERVER_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ParCoolConfig.CLIENT_SPEC);
		DistExecutor.unsafeRunWhenOn(
				Dist.CLIENT,
				() -> () -> {
					EventBusForgeRegistry.registerClient(MinecraftForge.EVENT_BUS);
					EventBusModRegistry.registerClient(FMLJavaModLoadingContext.get().getModEventBus());
				}
		);
	}

	private void setup(final FMLCommonSetupEvent event) {
		CommandRegistry.registerArgumentTypes(event);
		EventBusForgeRegistry.register(MinecraftForge.EVENT_BUS);
		EventBusModRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
		PotionRecipeRegistry.register(event);
=======
	@Override
	public void onInitialize() {
		setup();
		doClientStuff();
		//todo finish capabilities :clueless:
//		Capabilities.register();
		Effects.registerAll();
		ItemRegistry.registerAll();
		CHANNEL_INSTANCE.initServerListener();
		if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
			CHANNEL_INSTANCE.initClientListener();
		}
>>>>>>> Stashed changes
		PROXY.registerMessages(CHANNEL_INSTANCE);


		// nevermind configs work :3dsmile:
		ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.SERVER, ParCoolConfig.SERVER_SPEC);



		//todo client stuff :3dsmile:
		Potions.registerAll();

		registerEvents();
	}

<<<<<<< Updated upstream
	private void doClientStuff(final FMLClientSetupEvent event) {
		KeyBindings.register(event);
	}

	private void doServerStuff(final FMLDedicatedServerSetupEvent event) {
	}

	private void processIMC(final InterModProcessEvent event) {
=======

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
>>>>>>> Stashed changes
	}

	private void doClientStuff() {

	}

}
