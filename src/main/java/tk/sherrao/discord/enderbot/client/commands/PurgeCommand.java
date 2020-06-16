package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;

public class PurgeCommand extends ChatCommandProcessor {

	public PurgeCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		channel.deleteMessages( channel.getIterableHistory().limit( 100 ).complete() ).complete();
		channel.sendMessage( ":white_check_mark: " + sender.getAsMention() + ", successfully purged the last 100 messages!" ).complete();
		
	}
	
}