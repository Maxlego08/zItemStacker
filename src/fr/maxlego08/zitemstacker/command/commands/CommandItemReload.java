package fr.maxlego08.zitemstacker.command.commands;

import fr.maxlego08.zitemstacker.ItemStackerPlugin;
import fr.maxlego08.zitemstacker.command.VCommand;
import fr.maxlego08.zitemstacker.zcore.enums.Message;
import fr.maxlego08.zitemstacker.zcore.enums.Permission;
import fr.maxlego08.zitemstacker.zcore.utils.commands.CommandType;

public class CommandItemReload extends VCommand {

	public CommandItemReload(ItemStackerPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.ZITEMSTACKER_RELOAD);
		this.addSubCommand("reload", "rl");
		this.setDescription(Message.DESCRIPTION_RELOAD);
	}

	@Override
	protected CommandType perform(ItemStackerPlugin plugin) {
		
		plugin.reloadFiles();
		message(sender, Message.RELOAD);
		
		return CommandType.SUCCESS;
	}

}
