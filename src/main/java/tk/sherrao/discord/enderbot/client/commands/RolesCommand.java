package tk.sherrao.discord.enderbot.client.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;
import tk.sherrao.utils.strings.StringMultiJoiner;

public class RolesCommand extends ChatCommandProcessor {

	public RolesCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
	}

	@Override
	public  void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		List<Role> roles = new ArrayList<>( server.getRoles() );
		roles.remove( server.getPublicRole() );
		
		StringJoiner sj = new StringJoiner( "\n" );
		for( Role role : roles )
			sj.add( "\t" + role.getName() + ": " + server.getMembersWithRoles( role ).size() + " members" );
		
		channel.sendMessage( new StringMultiJoiner( "\n" )
				.add( ":white_check_mark: " + sender.getAsMention() + ", there are currently " + roles.size() + " roles in the discord:" )
				.add( sj.toString() )
				.toString() ).complete();
		
	}
	
}