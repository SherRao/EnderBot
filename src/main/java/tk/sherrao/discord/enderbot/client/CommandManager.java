package tk.sherrao.discord.enderbot.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringJoiner;

import org.apache.commons.logging.impl.SimpleLog;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import tk.sherrao.discord.enderbot.client.commands.ChatCommandProcessor;

public class CommandManager extends ListenerAdapter implements Runnable {
	
	private final Client client;
	private final SimpleLog log;
	
	private Scanner consoleIn;
	private String[] consoleInput;
	private Map<String, ChatCommandProcessor> commands;
	private String help;
	
	public CommandManager( Client client ) {
		this.client = client;
		this.log = client.getChatLogger();
		
		this.consoleIn = new Scanner( System.in );
		this.commands = new HashMap<>();
		this.help = new StringJoiner( "\n" )
				.add( "> Console Commands:" )
				.add( "  **Note: To change the \"donator rank\" and \"staff rank\" config properties, you will have to directly change" )
				.add( "  them from the configuration file**" )
				.add( "" )
				.add( "\t> exit, quit, stop, shutdown: " )
				.add( "\t  Shuts down and closes all neccessary threads and AuthIO" )
				.add( "" )
				.add( "\t> help: " )
				.add( "\t  Shows this menu" )
				.add( "" )
				.add( "\t> reload:" )
				.add( "\t  Reloads all configuration values from the disk" )
				.add( "" )
				.add( "\t> prefix <text> -> Sets the command prefix for chats" )
				.add( "\t  Currenty set to: '<prefix>" )
				.add( "" )
				.add( "\t> music <text> -> Sets the music channel" )
				.add( "\t  Currenty set to: '<music>" )
				.add( "" )
				.add( "\t> website <text> -> Sets the text for 'website'" )
				.add( "\t  Currenty set to: '<website>" )
				.add( "" )
				.add( "\t> shop <text> -> Sets the text for 'shop'" )
				.add( "\t  Currenty set to: '<shop>" )
				.add( "" )
				.add( "\t> ip <text> -> Sets the text for 'ip'" )
				.add( "\t  Currenty set to: '<ip>" )
				.toString();
		
	}
	
	public void registerCommand( ChatCommandProcessor command ) {
		commands.put( command.getCommand(), command );
		log.info( "Registered command '" + command.getCommand() + "'!" );
		
	}		
 	 	
	@Override
	public void onGuildMessageReceived( GuildMessageReceivedEvent event ) {
		Guild server = event.getGuild();
		User user = event.getAuthor();
		TextChannel channel = event.getChannel();
		String message = event.getMessage().getRawContent();
		String prefix = client.getJsonConfig().get( "chat-prefix" ).getAsString();
		if( message.startsWith( prefix ) ) {
			String[] split = message.replace( prefix, "" ).split( "\\s+" );
			String cmd = split[0];
			String[] args = new String[ split.length - 1 ];
			System.arraycopy( split, 1, args, 0, split.length - 1 );

			for( Entry<String, ChatCommandProcessor> command : commands.entrySet() ) {
				if( command.getValue().getCommand().equalsIgnoreCase( cmd ) ) {
					if( command.getValue().getPermission() == null || command.getValue().getRole() == null || command.getValue().canExecute( channel, user ) )
						command.getValue().execute( user, event.getGuild(), channel, cmd, args );

					else 
						channel.sendMessage( user.getAsMention() + ", you do not have permission to perform that command!" ).complete();

				} else {
					for( String alias : command.getValue().getAliases() )
						if( alias.equalsIgnoreCase( cmd ) ) {
							if( command.getValue().getPermission() == null || command.getValue().getRole() == null || command.getValue().canExecute( channel, user ) )
								command.getValue().execute( user, server, channel, cmd, args );
							
							else
								channel.sendMessage( user.getAsMention() + ", you do not have permission to perform that command!" ).complete();
							
					
						} else
							continue;
					
				}
			}
			
		} else
			return;
		
	}
	
	@Override
	public void run() {
		while( !client.getExecutor().isShutdown() ) {
			switch( (consoleInput = consoleIn.nextLine().split( "\\s+" ))[0].toLowerCase() ) {
				case "exit":
				case "quit":
				case "shutdown":
				case "stop":
					System.exit(0);
					break;
					
				case "reload":
					try {
						client.populateConfig();
						System.out.println( "> Reloaded configuration!" );

					} catch ( IOException e ) { log.error( "> Error while trying to load configuration!", e ); } 
					
					break;
					
				case "prefix":
					if( consoleInput.length > 0 ) {
						String pref = consoleInput[1];
						client.getJsonConfig().addProperty( "prefix", pref );
						System.out.println( "> Changed the chat prefix to '" + pref + "'!"  );
					
					} else 
						System.out.println( "> Wrong! Use 'help' for command usage!" );
					
					break;
					
				case "music":
					if( consoleInput.length > 0 ) {
						String channel = consoleInput[1];
						client.getJsonConfig().addProperty( "music-channel",channel );
						System.out.println( "> Set music channel to '" + channel + "'!"  );
					
					} else 
						System.out.println( "> Wrong! Use 'help' for command usage!" );
					
					break;

				case "website":
					if( consoleInput.length > 0 ) {
						client.getJsonConfig().addProperty( "website", consoleInput[1] );
						System.out.println( "> Set website to '" + consoleInput[1] + "'!"  );
					
					} else 
						System.out.println( "> Wrong! Use 'help' for command usage!" );
					
					break;
					
				case "shop":
					if( consoleInput.length > 0 ) {
						client.getJsonConfig().addProperty( "shop", consoleInput[1] );
						System.out.println( "> Set shop to '" + consoleInput[1] + "'!"  );
					
					} else 
						System.out.println( "> Wrong! Use 'help' for command usage!" );
					
					break;
					
				case "ip":
					if( consoleInput.length > 0 ) {
						client.getJsonConfig().addProperty( "ip", consoleInput[1] );
						System.out.println( "> Set the ip to '" + consoleInput[1] + "'!"  );
					
					} else 
						System.out.println( "> Wrong! Use 'help' for command usage!" );
					
					break;
					
				case "help":
				case "?":
					System.out.println( help.replace( "<prefix>", client.getJsonConfig().get( "chat-prefix" ).getAsString() )
							.replace( "<music>", client.getJsonConfig().get( "music-channel" ).getAsString() )
							.replace( "<donator>", client.getJsonConfig().get( "donator-ranks" ).getAsJsonArray().toString() )
							.replace( "<website>", client.getJsonConfig().get( "website" ).getAsString() )
							.replace( "<shop>", client.getJsonConfig().get( "shop" ).getAsString() )
							.replace( "<ip>", client.getJsonConfig().get( "ip" ).getAsString() ) );
					break;
					
				default:
					System.out.println( "> That isn't a valid console command! Use 'help' for commands!" );
					break;
					
			}
			
		}
	}
	
	public Map<String, ChatCommandProcessor> getCommands() {
	    return commands;
	    
	}
	
	public ChatCommandProcessor getCommand( String name ) {
		for( ChatCommandProcessor cmd : commands.values() ) {
			if( name.equalsIgnoreCase( cmd.getCommand() ) )
				return cmd;
			
			else {
				for( String alias : cmd.getAliases() ) {
					if( name.equalsIgnoreCase( alias ) )
						return cmd;
					
					else
						continue;
					
				}
			}
		}

		return null;
	}
	
    public Client getClient() {
        return client;
    
    }
	
}