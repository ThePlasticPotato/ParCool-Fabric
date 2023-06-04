package com.alrex.parcool.server.command;

import java.util.Collections;
import java.util.function.Predicate;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.server.command.impl.ChangeIndividualLimitationCommand;
import com.alrex.parcool.server.command.impl.StaminaControlCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandRegistry {

	public static final Predicate<ServerCommandSource> SOURCE_IS_PLAYER = cs -> cs.getEntity() instanceof PlayerEntity;

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {

		LiteralCommandNode<ServerCommandSource> createRoot = dispatcher.register(CommandManager.literal(ParCool.MOD_ID)
						// general purpose
						.then(StaminaControlCommand.getBuilder())
						.then(ChangeIndividualLimitationCommand.getBuilder())

				// utility
		);

		CommandNode<ServerCommandSource> c = dispatcher.findNode(Collections.singleton("c"));
		if (c != null)
			return;

		dispatcher.getRoot()
				.addChild(buildRedirect("c", createRoot));

	}

	/**
	 * *****
	 * https://github.com/VelocityPowered/Velocity/blob/8abc9c80a69158ebae0121fda78b55c865c0abad/proxy/src/main/java/com/velocitypowered/proxy/util/BrigadierUtils.java#L38
	 * *****
	 * <p>
	 * Returns a literal node that redirects its execution to
	 * the given destination node.
	 *
	 * @param alias       the command alias
	 * @param destination the destination node
	 * @return the built node
	 */
	public static LiteralCommandNode<ServerCommandSource> buildRedirect(final String alias, final LiteralCommandNode<ServerCommandSource> destination) {
		// Redirects only work for nodes with children, but break the top argument-less command.
		// Manually adding the root command after setting the redirect doesn't fix it.
		// See https://github.com/Mojang/brigadier/issues/46). Manually clone the node instead.
		LiteralArgumentBuilder<ServerCommandSource> builder = LiteralArgumentBuilder
				.<ServerCommandSource>literal(alias)
				.requires(destination.getRequirement())
				.forward(destination.getRedirect(), destination.getRedirectModifier(), destination.isFork())
				.executes(destination.getCommand());
		for (CommandNode<ServerCommandSource> child : destination.getChildren()) {
			builder.then(child);
		}
		return builder.build();
	}
//	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//		dispatcher.register(
//				Commands.literal(ParCool.MOD_ID)
//						.then(StaminaControlCommand.getBuilder())
//						.then(ChangeIndividualLimitationCommand.getBuilder())
//		);
//	}
//
//	public static void registerArgumentTypes(FMLCommonSetupEvent event) {
//		ArgumentTypes.register("parcool:action_name", ActionArgumentType.class, new EmptyArgumentSerializer<>(ActionArgumentType::action));
//	}
}
