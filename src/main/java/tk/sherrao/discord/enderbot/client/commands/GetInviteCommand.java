package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;

public class GetInviteCommand extends ChatCommandProcessor {

	public GetInviteCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );

	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		Invite invite = server.getDefaultChannel().createInvite().complete();
		channel.sendMessage( ":white_check_mark: " + sender.getAsMention() + ", here's your new invite!: " + invite.getURL() ).complete();

	}
	
}