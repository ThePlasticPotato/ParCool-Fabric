package com.alrex.parcool.server.command;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.server.command.args.ActionArgumentType;
import com.alrex.parcool.server.command.impl.ChangeIndividualLimitationCommand;
import com.alrex.parcool.server.command.impl.StaminaControlCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;

public class CommandRegistry {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean b) {
		dispatcher.register(
				Commands.literal(ParCool.MOD_ID)
						.then(StaminaControlCommand.getBuilder())
						.then(ChangeIndividualLimitationCommand.getBuilder())
		);
	}

	public static void registerArgumentTypes() {
		ArgumentTypes.register("parcool:action_name", ActionArgumentType.class, new EmptyArgumentSerializer<>(ActionArgumentType::action));
	}
}
