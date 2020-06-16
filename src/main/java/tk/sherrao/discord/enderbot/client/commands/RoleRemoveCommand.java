package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import tk.sherrao.discord.enderbot.client.Client;

public class RoleRemoveCommand extends ChatCommandProcessor {

	public RoleRemoveCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
	}

	@Override
	public  void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		try {
			Role role = server.getRolesByName( args[0].replace( "_", " " ), true).get(0);
			Member selected = client.getClosestMember( server, args[1] );
			if( args.length == 1 || selected != null ) {
				if( selected.getRoles().contains( role ) ) {
					try {
						server.getController().removeRolesFromMember( selected, role ).complete();
						channel.sendMessage( ":white_check_mark: " + sender.getAsMention() + ", you have removed the role '" + role.getName() + "' from " + selected.getAsMention() ).complete();
				
					} catch( HierarchyException e ) { channel.sendMessage( ":negative_squared_cross_mark: " + "The specified user or role has equal or higher privileges than the bot!" ).complete(); }

				} else 
					channel.sendMessage( ":negative_squared_cross_mark: " + sender.getAsMention() + ", that user does not have the specified role!" ).complete();
				
			} else
				channel.sendMessage( ":negative_squared_cross_mark: " + sender.getAsMention() + ", that isn't a valid user!" ).complete();
			
		} catch( NullPointerException | IndexOutOfBoundsException e ) { channel.sendMessage( sender.getAsMention() + ", that isn't a valid role on the server!" ).complete(); }
			
	}
	
}