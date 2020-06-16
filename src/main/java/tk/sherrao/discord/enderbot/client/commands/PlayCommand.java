package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;
import tk.sherrao.discord.enderbot.client.audio.AudioIO;

public class PlayCommand extends ChatCommandProcessor {
	
	protected AudioIO player;
	
	public PlayCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );

		this.player = client.getAudioIO();
		
	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		String musicChannel = client.getJsonConfig().get( "music-channel" ).getAsString();
		if( channel.getName().equals( musicChannel ) ) {
			if( args.length > 0 ) {
				Member member = server.getMember( sender );
				String request = args[0];
				if( !request.startsWith( "http" ) ) {
					request = "ytsearch:" + request;
					for( int i = 1; i < args.length; i++ ) 
						request += " " + args[i];	
			
				} 
		
				if( member.getVoiceState().inVoiceChannel() )
					player.play( sender, request, server, member.getVoiceState().getChannel(), channel );
			
				else 
					channel.sendMessage( ":negative_squared_cross_mark: " + sender.getAsMention() + ", you are not in a voice channel!" ).complete();
			
			} else
				channel.sendMessage( ":negative_squared_cross_mark: " + sender.getAsMention() + ", you have to specify a Youtube URL or search query!" ).complete();
			
		} else 
			channel.sendMessage( ":negative_squared_cross_mark: " + sender.getAsMention() + ", wrong channel! Use the #" + musicChannel + " channel!" ).complete();
	
	}
	
}