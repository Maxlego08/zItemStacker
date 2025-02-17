package fr.maxlego08.zitemstacker.command.commands;

import fr.maxlego08.zitemstacker.ItemStackerPlugin;
import fr.maxlego08.zitemstacker.command.VCommand;
import fr.maxlego08.zitemstacker.zcore.enums.Message;
import fr.maxlego08.zitemstacker.zcore.utils.commands.CommandType;

public class CommandItemVersion extends VCommand {

	public CommandItemVersion(ItemStackerPlugin plugin) {
		super(plugin);
		this.addSubCommand("version", "?", "ver", "v");
		this.setDescription(Message.DESCRIPTION_RELOAD);
	}

	@Override
	protected CommandType perform(ItemStackerPlugin plugin) {
		
		message(sender, "&aVersion&8: ");
		
		return CommandType.SUCCESS;
	}

}
