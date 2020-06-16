package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;

public class MembersCommand extends ChatCommandProcessor {
	
	public MembersCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		channel.sendMessage( ":white_check_mark: " + "There are " + server.getMembers().size() + " members on this server!" ).complete();
		
	}
	
}