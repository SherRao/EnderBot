package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;

public class MuteCommand extends ChatCommandProcessor {

	public MuteCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
	
	
	}

	@Override
	public void execute( User user, Guild server, TextChannel channel, String alias,  String[] args ) {
		Member sender = server.getMember( user );	
		if( sender.hasPermission( channel, Permission.VOICE_MUTE_OTHERS ) ) {
			if( args.length > 0 ) {
				Member member = client.getClosestMember( server, args[0] );
				if( !member.getVoiceState().isMuted() ) {
					server.getController().setMute( member, true ).complete();
					channel.sendMessage( ":skull_crossbones: " + sender.getAsMention() + " has muted " + member.getAsMention() ).complete();
					
				} else
					channel.sendMessage( ":negative_squared_cross_mark: " + "That user is already muted!" ).complete();
			
			} else
				channel.sendMessage( ":negative_squared_cross_mark: " + "You have not specified a user to mute!" ).complete();		
		
		} else 
			channel.sendMessage( ":negative_squared_cross_mark: " + "You do not have permission to execute that command!" ).complete();
		
	}
	
}