package tk.sherrao.discord.enderbot.client.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;
import tk.sherrao.discord.enderbot.client.MessageField;

public class HelpCommand extends ChatCommandProcessor {

	private MessageEmbed defaultOutput, staffOutput1, staffOutput2;
	
	public HelpCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
		this.defaultOutput = client.generateMessage( "https://d30y9cdsu7xlg0.cloudfront.net/png/45447-200.png", client.getJsonConfig().get( "chat-prefix" ).getAsString() + "Help", "_A list of all bot commands:_", null, false,
				
				//general
				new MessageField( "", "" ),
				new MessageField( "_General Chat Commands:_", "" ),				
				
				new MessageField( "GetInvite - Chat Command", new StringBuilder()
						.append( "Used to generate an invitation link\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "getinvite" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "getinvite" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Help - Chat Command", new StringBuilder()
						.append( "Display this menu\n" )
						.append( "Usage: " )
						.append( super.getUsage() )
						.append( "\nAliases: " )
						.append( super.getAliasList() )
						.toString() ),
				
				new MessageField( "CoinFlip - Chat Command", new StringBuilder()
						.append( "Used to settle an argument or a bet\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "coinflip" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "coinflip" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Website - Chat Command", new StringBuilder()
						.append( "Print the server website\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "website" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "website" ).getAliasList() )
						.toString() ),
				
				new MessageField( "IP - Chat Command", new StringBuilder()
						.append( "Print the IP of the server\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "ip" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "ip" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Shop - Chat Command", new StringBuilder()
						.append( "Print the server shop\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Shop" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Shop" ).getAliasList() )
						.toString() ),
			
				new MessageField( "MyInfo - Chat Command", new StringBuilder()
						.append( "Used to change your Discord user information\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "myinfo" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "myinfo" ).getAliasList() )
						.toString() ),

				new MessageField( "Ranks - Chat Command", new StringBuilder()
						.append( "Print the server roles\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Roles" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Roles" ).getAliasList() )
						.toString() ),

				new MessageField( "Members - ChatCommand", new StringBuilder()
						.append( "Print the server member count\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "members" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "members" ).getAliasList() )
						.toString() ),
				
				
				
				//music
				new MessageField( "", "" ),
				new MessageField( "_Music Channel Commands:_", "" ),
				
				new MessageField( "Play - Music Command", new StringBuilder()
						.append( "Used to queue a song to the music player\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Play" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Play" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Resume - Music Command", new StringBuilder()
						.append( "Used to resume the music player\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Resume" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Resume" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Pause - Music Command", new StringBuilder()
						.append( "Used to pause the music player\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Stop" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Stop" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Skip - Music Command", new StringBuilder()
						.append( "Used to skip the current track on the music player\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Skip" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Skip" ).getAliasList() )
						.toString() ),
				
				
				
				//donator
				new MessageField( "", "" ),
				new MessageField( "_Donator Only Commands:_", "" ),
				
				new MessageField( "Create - Donator Command", new StringBuilder()
						.append( "Used to create a custom donator channel\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "create" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "create" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Nickname - Chat Command", new StringBuilder()
						.append( "Used to change your nickname\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "nickname" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "nickname" ).getAliasList() )
						.toString() ) );
		
		
		
		this.staffOutput1 = client.generateMessage( "https://d30y9cdsu7xlg0.cloudfront.net/png/45447-200.png", client.getJsonConfig().get( "chat-prefix" ).getAsString() + "Help", "_A list of all bot commands:_", null, false,
				
				//general
				new MessageField( "", "" ),
				new MessageField( "_General Chat Commands:_", "" ),				
				
				new MessageField( "GetInvite - Chat Command", new StringBuilder()
						.append( "Used to generate an invitation link\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "getinvite" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "getinvite" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Help - Chat Command", new StringBuilder()
						.append( "Display this menu\n" )
						.append( "Usage: " )
						.append( super.getUsage() )
						.append( "\nAliases: " )
						.append( super.getAliasList() )
						.toString() ),
				
				new MessageField( "CoinFlip - Chat Command", new StringBuilder()
						.append( "Used to settle an argument or a bet\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "coinflip" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "coinflip" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Website - Chat Command", new StringBuilder()
						.append( "Print the server website\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Website" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Website" ).getAliasList() )
						.toString() ),
				
				new MessageField( "IP - Chat Command", new StringBuilder()
						.append( "Print the IP of the server\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "ip" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "ip" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Shop - Chat Command", new StringBuilder()
						.append( "Print the server shop\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Shop" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Shop" ).getAliasList() )
						.toString() ),
			
				new MessageField( "MyInfo - Chat Command", new StringBuilder()
						.append( "Used to change your Discord user information\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "myinfo" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "myinfo" ).getAliasList() )
						.toString() ),

				new MessageField( "Ranks - Chat Command", new StringBuilder()
						.append( "Print the server roles\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Roles" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Roles" ).getAliasList() )
						.toString() ),

				new MessageField( "Members - ChatCommand", new StringBuilder()
						.append( "Print the server member count\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "members" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "members" ).getAliasList() )
						.toString() ),
				
				
				
				//music
				new MessageField( "", "" ),
				new MessageField( "_Music Channel Commands:_", "" ),
				
				new MessageField( "Play - Music Command", new StringBuilder()
						.append( "Used to queue a song to the music player\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Play" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Play" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Resume - Music Command", new StringBuilder()
						.append( "Used to resume the music player\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Resume" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Resume" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Pause - Music Command", new StringBuilder()
						.append( "Used to pause the music player\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Stop" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Stop" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Skip - Music Command", new StringBuilder()
						.append( "Used to skip the current track on the music player\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Skip" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Skip" ).getAliasList() )
						.toString() ),
				
				
				
				//donator
				new MessageField( "", "" ),
				new MessageField( "_Donator Only Commands:_", "" ),
				
				new MessageField( "Create - Donator Command", new StringBuilder()
						.append( "Used to create a custom donator channel\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "create" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "create" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Nickname - Chat Command", new StringBuilder()
						.append( "Used to change your nickname\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "nickname" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "nickname" ).getAliasList() )
						.toString() ),
				
				
				
				//staff
				new MessageField( "", "" ),
				new MessageField( "_Staff Commands:_", "" ),
				
				new MessageField( "Mute - Server Command", new StringBuilder()
						.append( "Used to mute a user\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "mute" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "mute" ).getAliasList() )
						.toString() ),
				
				new MessageField( "Unmute - Server Command", new StringBuilder()
						.append( "Used to unmute a user\n" )
						.append( "Usage: " )
						.append( commands.getCommand( "Unmute" ).getUsage() )
						.append( "\nAliases: " )
						.append( commands.getCommand( "Unmute" ).getAliasList() )
						.toString() ) );
	
		this.staffOutput2 = client.generateMessage( null, null, null, null, false,
		new MessageField( "Kick - Server Command", new StringBuilder()
				.append( "Used to kick a user\n" )
				.append( "Usage: " )
				.append( commands.getCommand( "kick" ).getUsage() )
				.append( "\nAliases: " )
				.append( commands.getCommand( "kick" ).getAliasList() )
				.append( "\n" )
				.toString() ),
		
		new MessageField( "Ban - Server Command", new StringBuilder()
				.append( "Used to ban a user\n" )
				.append( "Usage: " )
				.append( commands.getCommand( "ban" ).getUsage() )
				.append( "\nAliases: " )
				.append( commands.getCommand( "ban" ).getAliasList() )
				.toString() ),
		
		new MessageField( "Purge - Chat Command", new StringBuilder()
				.append( "Used to purge the last 100 messages from a channel\n" )
				.append( "Usage: " )
				.append( commands.getCommand( "Purge" ).getUsage() )
				.append( "\nAliases: " )
				.append( commands.getCommand( "Purge" ).getAliasList() )
				.toString() ),
		
		new MessageField( "RoleAdd - Server Command", new StringBuilder()
				.append( "Used to add a role to the server\n" )
				.append( "Usage: " )
				.append( commands.getCommand( "RoleAdd" ).getUsage() )
				.append( "\nAliases: " )
				.append( commands.getCommand( "RoleAdd" ).getAliasList() )
				.toString() ),
		
		new MessageField( "RoleRemove - Server Command", new StringBuilder()
				.append( "Used to remove a role from the server\n" )
				.append( "Usage: " )
				.append( commands.getCommand( "RoleRemove" ).getUsage() )
				.append( "\nAliases: " )
				.append( commands.getCommand( "RoleRemove" ).getAliasList() )
				.toString() ) );
		
	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		Member member = server.getMember( sender );
		List<Role> staff = new ArrayList<>();
		for( JsonElement element : client.getJsonConfig().get( "staff-roles" ).getAsJsonArray() ) {
			String rolename = element.getAsString();
			List<Role> list = server.getRolesByName( rolename, true );
			if( list == null || list.isEmpty() )
				server.getController().createRole().setName( rolename ).complete();
				
			staff.add( server.getRolesByName( rolename, true ).get(0) );
			
		}

		channel.sendMessage( ":white_check_mark: " + sender.getAsMention() + ", you have been sent a list of all commnds in PM!" ).complete();
		for( Role r : staff ) {
			if( member.getRoles().contains( r ) ) {
				sender.openPrivateChannel().complete().sendMessage( staffOutput1 ).complete();
				sender.openPrivateChannel().complete().sendMessage( staffOutput2 ).complete();
				return;
				
			} else
				continue;
			
		}

		sender.openPrivateChannel().complete().sendMessage( defaultOutput ).complete();
		
	}
	
}