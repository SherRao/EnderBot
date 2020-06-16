package tk.sherrao.discord.enderbot.client.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;

public class CoinFlipCommand extends ChatCommandProcessor {

	public CoinFlipCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
		
	}
	
	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		double random = Math.random();
		if( random < .00016 ) 
			channel.sendMessage( "The coin landed on it's side!" ).complete();
		
		else
			channel.sendMessage( ":moneybag: " + sender.getAsMention() + ", the result is " + ( random < .5 ? "Heads" : "Tails" ) + "!" ).complete();
		
	}
	
}