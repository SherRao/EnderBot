package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;
import tk.sherrao.discord.enderbot.client.audio.AudioIO;

public class StopCommand extends ChatCommandProcessor {
	
	protected AudioIO player;

	public StopCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
		this.player = client.getAudioIO();

	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		String musicChannel = client.getJsonConfig().get( "music-channel" ).getAsString();
		if( channel.getName().equals( musicChannel ) ) {	
			Member member = server.getMember( sender );
			if( member.getVoiceState().inVoiceChannel() )
				if( member.getVoiceState().inVoiceChannel() )
					player.pause( sender, channel );
			
				else 
					channel.sendMessage( ":negative_squared_cross_mark: " + sender.getAsMention() + ", you are not in a voice channel!" ).complete();
			
			else 
				channel.sendMessage( ":negative_squared_cross_mark: " + sender.getAsMention() + ", you are not in a voice channel!" ).complete();		
		
		} else 
			channel.sendMessage( ":negative_squared_cross_mark: " +sender.getAsMention() + ", wrong channel! Use the #" + musicChannel + " channel!" ).complete();
		
	}
	
}