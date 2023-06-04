package com.alrex.parcool.server.command.impl;

import com.alrex.parcool.common.network.StaminaControlMessage;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class StaminaControlCommand {
	private static final String ARGS_NAME_VALUE = "value";
	private static final String ARGS_NAME_PLAYER = "target";

	public static ArgumentBuilder<ServerCommandSource, ?> getBuilder() {
		return (CommandManager
				.literal("stamina")
				.requires(commandSource -> commandSource.hasPermissionLevel(2))
				.then(CommandManager.literal("set")
						.then(CommandManager.argument(ARGS_NAME_PLAYER, EntityArgumentType.player()).then(CommandManager.argument(ARGS_NAME_VALUE, IntegerArgumentType.integer(1, 99999)).executes(context -> {
							ServerPlayerEntity player = EntityArgumentType.getPlayer(context, ARGS_NAME_PLAYER);
							StaminaControlMessage.sync(
									player,
									IntegerArgumentType.getInteger(context, ARGS_NAME_VALUE),
									false
							);
							context.getSource().sendFeedback(new LiteralText("Set-Stamina operation is requested to " + player.getDisplayName().getString()), false);
							return 0;
						})))
				)
				.then(CommandManager.literal("add")
						.then(CommandManager.argument(ARGS_NAME_PLAYER, EntityArgumentType.player()).then(CommandManager.argument(ARGS_NAME_VALUE, IntegerArgumentType.integer(1, 99999)).executes(context -> {
							ServerPlayerEntity player = EntityArgumentType.getPlayer(context, ARGS_NAME_PLAYER);
							StaminaControlMessage.sync(
									player,
									IntegerArgumentType.getInteger(context, ARGS_NAME_VALUE),
									true
							);
							context.getSource().sendFeedback(new LiteralText("Add-Stamina operation is requested to " + player.getDisplayName().getString()), false);
							return 0;
						})))
				)
		);
	}

}
