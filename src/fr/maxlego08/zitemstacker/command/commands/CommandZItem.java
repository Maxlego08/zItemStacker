package fr.maxlego08.zitemstacker.command.commands;

import fr.maxlego08.zitemstacker.ZItemPlugin;
import fr.maxlego08.zitemstacker.command.VCommand;
import fr.maxlego08.zitemstacker.zcore.enums.Message;
import fr.maxlego08.zitemstacker.zcore.enums.Permission;
import fr.maxlego08.zitemstacker.zcore.utils.commands.CommandType;

public class CommandZItem extends VCommand {

	public CommandZItem() {
		this.setPermission(Permission.ZITEMSACKER_USE);
		this.addSubCommand(new CommandZItemVersion());
		this.addSubCommand(new CommandZItemReload());
	}

	@Override
	protected CommandType perform(ZItemPlugin main) {

		this.subVCommands.forEach(command -> {
			message(sender, Message.COMMAND_SYNTAXE_HELP, command.getSyntaxe(), command.getDescription());
		});

		return CommandType.SUCCESS;
	}

}
