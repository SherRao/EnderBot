package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;
import tk.sherrao.utils.strings.StringMultiJoiner;

public class KickCommand extends ChatCommandProcessor {

	public KickCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		Member member = server.getMember( sender );
		if( args.length > 0 ) {
			String name = args[0];
			String reason = "";
			if( args.length > 1 ) {
				StringMultiJoiner sj = new StringMultiJoiner( " " );
				for( int i = 1; i < args.length; i++ )
					sj.add( args[i] );
			
				reason = sj.toString();
				
			}
			
			Member selected = client.getClosestMember( server, name );
			if( selected != null ) {
				server.getController().kick( selected, reason ).complete();
				channel.sendMessage( ":skull_crossbones: " + member.getAsMention() + " has kicked " + selected.getAsMention() + "!" ).complete();
				
			} else 
				channel.sendMessage( ":negative_squared_cross_mark: " + "That isn't a valid user on the server" ).complete();
			
		} else 
			channel.sendMessage( ":negative_squared_cross_mark: " + "Wrong! Usage: " + super.getUsage() ).complete();
			
	}			
	
}