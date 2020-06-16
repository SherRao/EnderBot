package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;

public  class WebsiteCommand extends ChatCommandProcessor {
	
	public WebsiteCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		channel.sendMessage( ":white_check_mark: " + sender.getAsMention() + ", the website for the server can be found at: " + client.getJsonConfig().get( "website" ).getAsString() ).complete();
		
	}

}