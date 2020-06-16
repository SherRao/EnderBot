package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;
import tk.sherrao.discord.enderbot.client.FlickrImages;

public  class DogCommand extends ChatCommandProcessor {
	
    private FlickrImages flickr;
    
	public DogCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
		this.flickr = client.getFlickrImages();
		
	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
	    EmbedBuilder embed = new EmbedBuilder();
        embed.setImage( flickr.getImage( "dog" ) );
        embed.set
        channel.sendMessage( embed.build() ).complete();
        
	}

}