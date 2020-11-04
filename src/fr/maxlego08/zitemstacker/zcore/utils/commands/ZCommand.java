package fr.maxlego08.zitemstacker.zcore.utils.commands;

import java.util.function.BiConsumer;

import fr.maxlego08.zitemstacker.ZItemPlugin;
import fr.maxlego08.zitemstacker.command.VCommand;

public class ZCommand extends VCommand {

	private BiConsumer<VCommand, ZItemPlugin> command;

	@Override
	public CommandType perform(ZItemPlugin main) {
		
		if (command != null){
			command.accept(this, main);
		}

		return CommandType.SUCCESS;
	}

	public VCommand setCommand(BiConsumer<VCommand, ZItemPlugin> command) {
		this.command = command;
		return this;
	}

	public VCommand sendHelp(String command) {
		this.command = (cmd, main) -> main.getCommandManager().sendHelp(command, cmd.getSender());
		return this;
	}

}
