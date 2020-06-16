package tk.sherrao.discord.enderbot.client.commands;

import org.apache.commons.logging.impl.SimpleLog;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;
import tk.sherrao.discord.enderbot.client.CommandManager;
import tk.sherrao.utils.strings.StringMultiJoiner;

public abstract class ChatCommandProcessor {

	protected final Client client;
	protected final SimpleLog log;
	protected final CommandManager commands;
	
	protected final String command;	
	protected Permission permission;
	protected Role role;
	protected final String usage;
	protected final String[] aliases;
	protected final String aliasList;
	
	public ChatCommandProcessor( Client client, String command, Permission permission, String usage, String... aliases ) {
		this.client = client;  
		this.log = new SimpleLog( "EnderBot/ChatCommandProcessor" );
		this.commands = client.getCommandManager();
		
		this.command = command;
		this.permission = permission;
		this.usage = "'" + client.getJsonConfig().get( "chat-prefix" ).getAsString() + ((usage == null || usage.isEmpty()) ? command + "'" :  " " + usage + "'" );
		this.aliases = aliases;
		this.aliasList = new StringMultiJoiner( ", ", "[", "]" ).setEmptyValue( "N/A" ).add( aliases ).toString();
		
	}
	
	public ChatCommandProcessor( Client client, String command, Role role, String usage, String... aliases ) {
		this.client = client;  
		this.log = new SimpleLog( "EnderBot/ChatCommandProcessor" );
		this.commands = client.getCommandManager();

		this.command = command;
		this.role = role;
		this.usage = "'" + client.getJsonConfig().get( "chat-prefix" ).getAsString() + ((usage == null || usage.isEmpty()) ? command + "'" :  " " + usage + "'" );
		this.aliases = aliases;		
		this.aliasList = new StringMultiJoiner( ", ", "[", "]" ).setEmptyValue( "N/A" ).add( aliases ).toString();

	}

	public abstract void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args );
	
	public final String getCommand() {
		return command;
		
	}
	
	public final boolean canExecute( Guild server, User user ) {
		if( permission != null )
			return server.getMember( user ).hasPermission( permission );
		
		else
			return server.getMember( user ).getRoles().contains( role );
		
	}
	
	public final boolean canExecute( Member member ) {
		if( permission != null )
			return member.hasPermission( permission );
		
		else
			return member.getRoles().contains( role );
		
	}
	
	public final boolean canExecute( TextChannel channel, User user ) {
		if( permission != null )
			return channel.getGuild().getMember( user ).hasPermission( channel, permission );
			
		else
			return channel.getGuild().getMember( user ).getRoles().contains( role );
		
	}
	
	public final boolean canExecute( TextChannel channel, Member member ) {
		if( permission != null )
			return member.hasPermission( channel, permission );
		
		else
			return member.getRoles().contains( role );
		
	}
	
	public final String getUsage() {
		return usage;
		
	}
	
	public final Permission getPermission() {
		return permission;
		
	}
	
	public final Role getRole() {
		return role;
		
	}
	
	public final String[] getAliases() {
		return aliases;
		
	}
	
	public final String getAliasList() {
		return aliasList;
		
	}
	
	@Override
	public final String toString() {
		return command;
		
	}
	
}