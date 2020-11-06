package fr.maxlego08.zitemstacker.command.commands;

import fr.maxlego08.zitemstacker.ZItemPlugin;
import fr.maxlego08.zitemstacker.command.VCommand;
import fr.maxlego08.zitemstacker.save.Config;
import fr.maxlego08.zitemstacker.save.Lang;
import fr.maxlego08.zitemstacker.zcore.enums.Message;
import fr.maxlego08.zitemstacker.zcore.enums.Permission;
import fr.maxlego08.zitemstacker.zcore.utils.commands.CommandType;

public class CommandZItemReload extends VCommand {

	public CommandZItemReload() {
		super();
		this.setPermission(Permission.ZITEMSACKER_RELOAD);
		this.setDescription(Message.DESCRIPTION_RELOAD);
		this.addSubCommand("reload");
		this.addSubCommand("rl");
	}

	@Override
	protected CommandType perform(ZItemPlugin plugin) {

		Config.getInstance().load(plugin.getPersist());
		Lang.getInstance().load(plugin.getPersist());
		
		message(sender, Message.COMMAND_RELOAD);
		
		return CommandType.SUCCESS;
	}

}
