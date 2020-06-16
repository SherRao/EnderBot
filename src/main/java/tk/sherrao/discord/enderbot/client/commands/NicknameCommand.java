package tk.sherrao.discord.enderbot.client.commands;

import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import tk.sherrao.discord.enderbot.client.Client;

public class NicknameCommand extends ChatCommandProcessor {

	public NicknameCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );

	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		JsonArray rankNames = client.getJsonConfig().get( "donator-ranks" ).getAsJsonArray();
		for( Iterator<JsonElement> it = rankNames.iterator(); it.hasNext(); ) {
			String rankName = it.next().getAsString();
			List<Role> list = server.getRolesByName( rankName, true );
			if( list == null || list.isEmpty() )
				server.getController().createRole().setName( rankName ).complete();

			Member member = server.getMember( sender );
			Role role = server.getRolesByName( rankName, true ).get(0);
			if( member.getRoles().contains( role ) && role.hasPermission( Permission.NICKNAME_CHANGE ) ) {
				if( args.length > 0 ) {
					try {
						String nick = args[0];
						for( int i = 1; i < args.length; i++ )
							nick += " " + args[i];
							
						server.getController().setNickname( member, nick ).complete();
						channel.sendMessage( ":white_check_mark: " + member.getAsMention() + ", you have successfully changed your nickname to '" + nick + "'!" ).complete();
						return;
							
					} catch( HierarchyException e ) { channel.sendMessage( ":negative_squared_cross_mark: " + sender.getAsMention() + ", the specified user has equal or higher privileges than the bot! Contact the Discord owner to fix this!" ).complete(); }
							
				} else
					channel.sendMessage( ":negative_squared_cross_mark: " + member.getAsMention() + ", you haven't specified a nickname!" ).complete();
						
			} else
				channel.sendMessage( ":negative_squared_cross_mark: " + member.getAsMention() + ", you do not have permission to change your nickname!" ).complete();
			
		}
	}
		
}