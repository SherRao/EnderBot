package tk.sherrao.discord.enderbot.client.commands;

import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import tk.sherrao.discord.enderbot.client.Client;

public class CreateCommand extends ChatCommandProcessor {

	public CreateCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
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
			if( member.getRoles().contains( role ) ) {
				String categoryName = client.getJsonConfig().get( "donator-channel-category" ).getAsString();		
				List<Category> list1 = server.getCategoriesByName( categoryName, true );
				if( list1 == null || list1.isEmpty() )
					server.getController().createCategory( categoryName ).complete();
				
				VoiceChannel createdChannel = (VoiceChannel) server.getCategoriesByName( categoryName, true ).get(0).createVoiceChannel( args[0] ).complete();
				PermissionOverride adminPerms = createdChannel.createPermissionOverride( member ).complete();
				PermissionOverride publicPerms = createdChannel.createPermissionOverride( server.getPublicRole() ).complete();
				adminPerms.getManager().grant( Permission.ALL_CHANNEL_PERMISSIONS ).complete();
				publicPerms.getManager().deny( Permission.values() ).complete();
				channel.sendMessage( ":white_check_mark: " + sender.getAsMention() + ", you have created a donator channel called '" + args[0] + "'!" ).complete();
				return;
				
			} else
				continue;
		}
		
		channel.sendMessage( ":negative_squared_cross_mark: " + sender.getAsMention() + ", you don't have the required rank for that command!" ).complete();
		
	}
	
}