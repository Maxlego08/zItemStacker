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

		this.sender
				.sendMessage("�7(�bzItemStacker�7) �aVersion du plugin�7: �2" + plugin.getDescription().getVersion());
		this.sender.sendMessage("�7(�bzItemStacker�7) �aAuteur�7: �2Maxlego08");
		this.sender.sendMessage("�7(�bzItemStacker�7) �aDownload�7: �2https://groupez.dev/resources/zitemstacker.15");
		this.sender.sendMessage("�7(�bzItemStacker�7) �aDiscord�7: �2http://discord.groupez.dev/");
		this.sender.sendMessage(
				"�8(�fServeur Minecraft Vote�8) �aSponsor�7: �chttps://serveur-minecraft-vote.fr/?ref=345");

		return CommandType.SUCCESS;
	}

}
