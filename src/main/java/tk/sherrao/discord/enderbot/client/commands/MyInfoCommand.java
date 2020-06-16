package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;
import tk.sherrao.discord.enderbot.client.MessageField;

public class MyInfoCommand extends ChatCommandProcessor {

	public MyInfoCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );	
		
	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		channel.sendMessage( client.generateMessage( sender.getEffectiveAvatarUrl(), "User Information about " + sender.getName(), null, null, 
				new MessageField( "Name", sender.getName() ), 
				new MessageField( "Tag", sender.getAsMention() ),
				new MessageField( "Discriminator", sender.getDiscriminator() ),
				new MessageField( "ID", sender.getId() ),
				new MessageField( "Avatar", sender.getEffectiveAvatarUrl() ),
				new MessageField( "Account Creation Time", sender.getCreationTime().toString() ),
				new MessageField( "Presence", sender.getJDA().getPresence().getStatus().toString() ) ) ).complete();
				
	}
	
}