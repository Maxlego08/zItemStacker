package fr.maxlego08.zitemstacker.command.commands;

import fr.maxlego08.zitemstacker.ZItemPlugin;
import fr.maxlego08.zitemstacker.command.VCommand;
import fr.maxlego08.zitemstacker.zcore.enums.Message;
import fr.maxlego08.zitemstacker.zcore.utils.commands.CommandType;

public class CommandZItemVersion extends VCommand {

	public CommandZItemVersion() {
		super();
		this.setDescription(Message.DESCRIPTION_VERSION);
		this.addSubCommand("version");
		this.addSubCommand("v");
		this.addSubCommand("ver");
	}

	@Override
	protected CommandType perform(ZItemPlugin plugin) {

		sender.sendMessage("§7(§bzItemStacker§7) §aVersion du plugin§7: §2" + plugin.getDescription().getVersion());
		sender.sendMessage("§7(§bzItemStacker§7) §aAuteur§7: §2Maxlego08");
		sender.sendMessage("§7(§bzItemStacker§7) §aDiscord§7: §2http://discord.groupez.xyz/");

		return CommandType.SUCCESS;
	}

}
