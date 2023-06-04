package com.alrex.parcool.server.command.impl;

import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.info.LimitationByServer;
import com.alrex.parcool.server.command.args.ActionArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;


import java.util.Collection;

public class ChangeIndividualLimitationCommand {
	private static final String ARGS_NAME_PLAYERS = "targets";
	private static final String ARGS_NAME_ACTION = "action";
	private static final String ARGS_NAME_MAX_STAMINA_VALUE = "max_stamina_value";
	private static final String ARGS_NAME_STAMINA_CONSUMPTION = "stamina_consumption";
	private static final String ARGS_NAME_POSSIBILITY = "possibility";

	public static ArgumentBuilder<ServerCommandSource, ?> getBuilder() {
		return CommandManager
				.literal("limitation")
				.requires(commandSource -> commandSource.hasPermissionLevel(2))
				.then(CommandManager
						.literal("enable")
						.then(CommandManager
								.argument(ARGS_NAME_PLAYERS, EntityArgumentType.players())
								.executes(ChangeIndividualLimitationCommand::enableLimitation))
				)
				.then(CommandManager
						.literal("disable")
						.then(CommandManager
								.argument(ARGS_NAME_PLAYERS, EntityArgumentType.players())
								.executes(ChangeIndividualLimitationCommand::disableLimitation))
				)
				.then(CommandManager
						.literal("set")
						.then(CommandManager
								.argument(ARGS_NAME_PLAYERS, EntityArgumentType.players())
								.then(CommandManager
										.literal("to_default")
										.executes(ChangeIndividualLimitationCommand::setLimitationDefault)
								)
								.then(CommandManager
										.literal("max_stamina")
										.then(CommandManager
												.argument(ARGS_NAME_MAX_STAMINA_VALUE, IntegerArgumentType.integer(0, Integer.MAX_VALUE))
												.executes(ChangeIndividualLimitationCommand::changeLimitationOfMaxStamina)
										)
								)
								.then(CommandManager
										.literal("possibility")
										.then(CommandManager
												.literal("infinite_stamina")
												.then(CommandManager
														.argument(ARGS_NAME_POSSIBILITY, BoolArgumentType.bool())
														.executes(ChangeIndividualLimitationCommand::changePossibilityOfInfiniteStamina)
												)
										)
										.then(CommandManager
												.argument(ARGS_NAME_ACTION, ActionArgumentType.action())
												.then(CommandManager
														.argument(ARGS_NAME_POSSIBILITY, BoolArgumentType.bool())
														.executes(ChangeIndividualLimitationCommand::changePossibilityOfAction)
												)
										)
								)
								.then(CommandManager
										.literal("least_stamina_consumption")
										.then(CommandManager
												.argument(ARGS_NAME_ACTION, ActionArgumentType.action())
												.then(CommandManager
														.argument(ARGS_NAME_STAMINA_CONSUMPTION, IntegerArgumentType.integer(0, Integer.MAX_VALUE))
														.executes(ChangeIndividualLimitationCommand::changeStaminaConsumption)
												)
										)
								)
						)
				);
	}

	private static int setLimitationDefault(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, ARGS_NAME_PLAYERS);
		int num = 0;
		for (ServerPlayerEntity player : targets) {
			new LimitationByServer.IndividualLimitationChanger(player)
					.setDefault()
					.sync();
			num++;
		}
		context.getSource().sendFeedback(new TranslatableText("parcool.command.message.success.setLimitationToDefault", num), true);
		return 0;
	}

	private static int enableLimitation(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, ARGS_NAME_PLAYERS);
		int num = 0;
		for (ServerPlayerEntity player : targets) {
			new LimitationByServer.IndividualLimitationChanger(player)
					.setEnforced(true)
					.sync();
			num++;
		}
		context.getSource().sendFeedback(new TranslatableText("parcool.command.message.success.enableLimitation", num), true);
		return 0;
	}

	private static int disableLimitation(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, ARGS_NAME_PLAYERS);
		int num = 0;
		for (ServerPlayerEntity player : targets) {
			new LimitationByServer.IndividualLimitationChanger(player)
					.setEnforced(false)
					.sync();
			num++;
		}
		context.getSource().sendFeedback(new TranslatableText("parcool.command.message.success.disableLimitation", num), true);
		return 0;
	}

	private static int changeLimitationOfMaxStamina(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, ARGS_NAME_PLAYERS);
		int newValue = IntegerArgumentType.getInteger(context, ARGS_NAME_MAX_STAMINA_VALUE);
		int num = 0;
		for (ServerPlayerEntity player : targets) {
			new LimitationByServer.IndividualLimitationChanger(player)
					.setMaxStaminaLimitation(newValue)
					.sync();
			num++;
		}
		context.getSource().sendFeedback(new TranslatableText("parcool.command.message.success.setMaxStamina", num, newValue), true);
		return 0;
	}

	private static int changeStaminaConsumption(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, ARGS_NAME_PLAYERS);
		Class<? extends Action> action = ActionArgumentType.getAction(context, ARGS_NAME_ACTION);
		int newValue = IntegerArgumentType.getInteger(context, ARGS_NAME_STAMINA_CONSUMPTION);
		int num = 0;
		for (ServerPlayerEntity player : targets) {
			new LimitationByServer.IndividualLimitationChanger(player)
					.setStaminaConsumptionOf(action, newValue)
					.sync();
			num++;
		}
		context.getSource().sendFeedback(new TranslatableText("parcool.command.message.success.setStaminaConsumption", num, action.getSimpleName(), newValue), true);
		return 0;
	}

	private static int changePossibilityOfInfiniteStamina(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, ARGS_NAME_PLAYERS);
		boolean newValue = BoolArgumentType.getBool(context, ARGS_NAME_POSSIBILITY);
		int num = 0;
		for (ServerPlayerEntity player : targets) {
			new LimitationByServer.IndividualLimitationChanger(player)
					.setInfiniteStaminaPermission(newValue)
					.sync();
			num++;
		}
		context.getSource().sendFeedback(new TranslatableText("parcool.command.message.success.setPermissionInfiniteStamina", num, newValue), true);
		return 0;
	}

	private static int changePossibilityOfAction(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, ARGS_NAME_PLAYERS);
		Class<? extends Action> action = ActionArgumentType.getAction(context, ARGS_NAME_ACTION);
		boolean newValue = BoolArgumentType.getBool(context, ARGS_NAME_POSSIBILITY);
		int num = 0;
		for (ServerPlayerEntity player : targets) {
			new LimitationByServer.IndividualLimitationChanger(player)
					.setPossibilityOf(action, newValue)
					.sync();
			num++;
		}
		context.getSource().sendFeedback(new TranslatableText("parcool.command.message.success.setPermissionOfAction", num, action.getSimpleName(), newValue), true);
		return 0;
	}
}
