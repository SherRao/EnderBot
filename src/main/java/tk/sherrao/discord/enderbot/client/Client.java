package tk.sherrao.discord.enderbot.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

import org.apache.commons.logging.impl.SimpleLog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import tk.sherrao.discord.enderbot.client.audio.AudioIO;
import tk.sherrao.discord.enderbot.client.commands.BanCommand;
import tk.sherrao.discord.enderbot.client.commands.CoinFlipCommand;
import tk.sherrao.discord.enderbot.client.commands.CreateCommand;
import tk.sherrao.discord.enderbot.client.commands.DogCommand;
import tk.sherrao.discord.enderbot.client.commands.GetInviteCommand;
import tk.sherrao.discord.enderbot.client.commands.HelpCommand;
import tk.sherrao.discord.enderbot.client.commands.IPCommand;
import tk.sherrao.discord.enderbot.client.commands.KickCommand;
import tk.sherrao.discord.enderbot.client.commands.MembersCommand;
import tk.sherrao.discord.enderbot.client.commands.MuteCommand;
import tk.sherrao.discord.enderbot.client.commands.MyInfoCommand;
import tk.sherrao.discord.enderbot.client.commands.NicknameCommand;
import tk.sherrao.discord.enderbot.client.commands.PlayCommand;
import tk.sherrao.discord.enderbot.client.commands.PurgeCommand;
import tk.sherrao.discord.enderbot.client.commands.ResumeCommand;
import tk.sherrao.discord.enderbot.client.commands.RoleAddCommand;
import tk.sherrao.discord.enderbot.client.commands.RoleRemoveCommand;
import tk.sherrao.discord.enderbot.client.commands.RolesCommand;
import tk.sherrao.discord.enderbot.client.commands.ShopCommand;
import tk.sherrao.discord.enderbot.client.commands.SkipCommand;
import tk.sherrao.discord.enderbot.client.commands.StopCommand;
import tk.sherrao.discord.enderbot.client.commands.UnmuteCommand;
import tk.sherrao.discord.enderbot.client.commands.WebsiteCommand;
import tk.sherrao.discord.enderbot.wrapper.ClientWrapper;

public class Client extends Thread implements Runnable {

	private final class Lock {}
	private final Object jsonLock = new Lock();
	
	private String[] vargs;
	private SimpleLog systemLog;
	private SimpleLog chatLog;
	private SimpleLog audioLog;
	
	private JDA jda;
	private FlickrImages flickr;
	private CommandManager commandMgr;
	private AudioIO audioIO;

	private ExecutorService executor;
	private Thread consoleThread;
	
	private File configFile;
	private Path filePath;
	private BufferedReader fileIn;
	private BufferedWriter fileOut;
	private Gson gson;
	private JsonParser jsonParser;
	private JsonObject jsonConfig;
	
	public Client( String[] vargs ) {
		this.vargs = vargs;
		this.systemLog = new SimpleLog( "EnderBot\\Client" );
		this.chatLog = new SimpleLog( "EnderBot\\Input Receiver" );
		this.audioLog = new SimpleLog( "EnderBot\\Audio IO" );
		
	}
	
	public void onEnable() 
			throws LoginException, IllegalArgumentException, InterruptedException, RateLimitedException, IOException {
		systemLog.info( "Logging onto Discord Auth servers with TokenId=" + ClientWrapper.BOT_TOKEN + "..." );
		jda = new JDABuilder( AccountType.BOT ).setToken( ClientWrapper.BOT_TOKEN ).buildBlocking();
		flickr = new FlickrImages( this );
		commandMgr = new CommandManager( this );
		audioIO = new AudioIO( this );

		executor = Executors.newFixedThreadPool(2);
		consoleThread = new Thread( commandMgr, "EnderBot/Console" );
		executor.execute( commandMgr );
		
		configFile = new File( URLDecoder.decode( new File( ClientWrapper.class.getProtectionDomain().getCodeSource().getLocation().getPath() ).getParent(), "UTF-8" ), "config.json" );
		filePath = Paths.get( configFile.getPath() );
		gson = new GsonBuilder()
				.setPrettyPrinting()
				.setLenient()
				.create();
		
		jsonParser = new JsonParser();
		populateConfig();
		
		jda.addEventListener( commandMgr );
		jda.getPresence().setGame( Game.of( "Welcome! Use '" +  jsonConfig.get( "chat-prefix" ).getAsString() + "help'" ) );
		systemLog.info( "Changed current game!" );

		commandMgr.registerCommand( new DogCommand( this, "dog", Permission.MESSAGE_WRITE, "dog", "woof", "doggo", "pupper" ) );
		commandMgr.registerCommand( new BanCommand( this, "ban", Permission.BAN_MEMBERS, "ban <user>", "hammer" ) );
		commandMgr.registerCommand( new CoinFlipCommand( this, "coinflip", Permission.MESSAGE_WRITE, null, "coin", "flip", "cf" ) );
		commandMgr.registerCommand( new CreateCommand( this, "create", null, null ) );
		commandMgr.registerCommand( new GetInviteCommand( this, "getinvite", Permission.CREATE_INSTANT_INVITE, null, "invite", "inv" ) );
		commandMgr.registerCommand( new IPCommand( this, "ip", Permission.MESSAGE_WRITE, null ) );
		commandMgr.registerCommand( new KickCommand( this, "kick", Permission.KICK_MEMBERS, "kick <user>" ) );
		commandMgr.registerCommand( new MembersCommand( this, "members", Permission.MESSAGE_WRITE, null ) );
		commandMgr.registerCommand( new MuteCommand( this, "mute", Permission.VOICE_MUTE_OTHERS, "mute <user>" ) );
		commandMgr.registerCommand( new MyInfoCommand( this, "myinfo", Permission.MESSAGE_WRITE, null ) );
		commandMgr.registerCommand( new NicknameCommand( this, "nickname", Permission.NICKNAME_CHANGE, "nickname <text>", "nick", "name" ) );
		commandMgr.registerCommand( new PlayCommand( this, "play", Permission.MESSAGE_WRITE, "play <search query|youtube link>", "queue" ) );
		commandMgr.registerCommand( new PurgeCommand( this, "purge", Permission.MESSAGE_MANAGE, null ) );
		commandMgr.registerCommand( new ResumeCommand( this, "resume", Permission.MESSAGE_WRITE, null, "start" ) );
		commandMgr.registerCommand( new RoleAddCommand( this, "roleadd", Permission.MANAGE_ROLES, "roleadd <role name", "ra", "rolea" ) );
		commandMgr.registerCommand( new RoleRemoveCommand( this, "roleremove", Permission.MANAGE_ROLES, "roleremove <role>", "rr", "roler" ) );
		commandMgr.registerCommand( new RolesCommand( this, "roles", Permission.MANAGE_ROLES, null, "ranks" ) );
		commandMgr.registerCommand( new ShopCommand( this, "shop", Permission.MESSAGE_WRITE, null, "donate", "store" ) );
		commandMgr.registerCommand( new SkipCommand( this, "skip", Permission.MESSAGE_WRITE, null ) );
		commandMgr.registerCommand( new StopCommand( this, "stop", Permission.MESSAGE_WRITE, null, "pause" ) );
		commandMgr.registerCommand( new UnmuteCommand( this, "unmute", Permission.VOICE_MUTE_OTHERS, null ) );
		commandMgr.registerCommand( new WebsiteCommand( this, "website", Permission.MESSAGE_WRITE, null ) );
		commandMgr.registerCommand( new HelpCommand( this, "help", Permission.MESSAGE_WRITE, null, "?" ) );

		systemLog.info( "Registered " + commandMgr.getCommands().size() + " commands with the ChatManager!" );
		
	}
	
	public void populateConfig() 
			throws IOException {
		synchronized( jsonLock ) {
			boolean firstTime;
			if( !configFile.exists() || Files.size( filePath ) == 0 ) {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				firstTime = true;

			} else
				firstTime = false;

			fileIn = new BufferedReader( new FileReader( configFile ) );
			fileOut = new BufferedWriter( new FileWriter( configFile, true ) );
			if( !firstTime ) {
				String str = "";
				for( Iterator<String> it = fileIn.lines().iterator(); it.hasNext(); )
					str += it.next();

				jsonConfig = jsonParser.parse( str ).getAsJsonObject();

			} else {
				JsonArray donatorRoles = new JsonArray();
				donatorRoles.add( "Donator" );
				JsonArray staffRoles = new JsonArray();
				staffRoles.add( "Administrator" );
				
				jsonConfig = new JsonObject();
				jsonConfig.addProperty( "chat-prefix", "!" );
				jsonConfig.addProperty( "music-channel", "music" );
				jsonConfig.add( "donator-ranks", donatorRoles );
				jsonConfig.add( "staff-roles", staffRoles );
				jsonConfig.addProperty( "website", "website" );
				jsonConfig.addProperty( "shop", "shop" );
				jsonConfig.addProperty( "ip", "ip" );
				jsonConfig.addProperty( "donator-channel-category", "donator-channels" );

			}

			systemLog.info( "New configuration detected! " );
			systemLog.info( gson.toJson( jsonConfig ) );
			
		}
	}
	
	public void onDisable() 
			throws InterruptedException, IOException {
		systemLog.info( "onDisable() called!" );
		systemLog.info( "Saving memory configuration to disk (config.dat)...");
		
		fileIn.close();
		fileOut.close();
		configFile.delete();
		configFile.createNewFile();
		
		fileOut = new BufferedWriter( new FileWriter( configFile ) );
		gson.toJson( jsonConfig, fileOut );
		fileOut.close();
		
		systemLog.info( "Terminating console thread..." );
		executor.shutdownNow();

		systemLog.info( "Closing Audio connections..." );
		audioIO.close();
		
		systemLog.info( "Shutting down the Java Discord API (JDA) " );
		jda.shutdownNow();
		
		systemLog.info( "Disabled EnderBot - " + ClientWrapper.BOT_VERSION );
	
	}
	
	public Member getClosestMember( Guild server, String name ) {
		List<Member> first = server.getMembersByEffectiveName( name, true );
		if( first.isEmpty() ) {
			List<Member> second = server.getMembersByName( name, true );
			if( second.isEmpty() ) {
				List<Member> third = server.getMembersByNickname( name, true );
				if( third.isEmpty() ) {
					return null;
					
				} else
					return third.get(0);
				
			} else
				return second.get(0);
			
		} else
			return first.get(0);
		
	}
	
	/**
	 * @param icon The Icon for the message
	 * @param title The title of the message (256 characters)
	 * @param description The description can hold 2048 characters
	 * @param footer The footer can hold 2048 characters
	 * @param fields The fields that should be added to the MessageEmbed
	 * 
	 */
	public MessageEmbed generateMessage( String icon, String title, String description, String footer, MessageField... fields ) {
		return generateMessage( icon, title, description, footer, true, fields );
		
	}
	
	/**
	 * @param icon The Icon for the message
	 * @param title The title of the message (256 characters)
	 * @param description The description can hold 2048 characters
	 * @param footer The footer can hold 2048 characters
	 * @param indentFields Whether or not to indent the MessageFields
	 * @param fields The fields that should be added to the MessageEmbed
	 * 
	 */
	public MessageEmbed generateMessage( String icon, String title, String description, String footer,  boolean indentFields, MessageField... fields ) {
		EmbedBuilder msg = new EmbedBuilder();
		if( icon != null )
			msg.setThumbnail( icon );

		if( title != null && icon != null )
			msg.setAuthor( title, null, icon );
			
		else if( title != null )
			msg.setAuthor( title );
			
		if( description != null )
			msg.setDescription( description );
			
		if( footer != null )
			msg.setFooter( footer, null );

		if( fields.length != 0 )
			for( MessageField field : fields )
				msg.addField( field.title, field.description, indentFields );
			
		return msg.build();
		
	}
	
	public SimpleLog getSystemLogger() {
		return systemLog;
		
	}
	
	public SimpleLog getChatLogger() {
		return chatLog;
		
	}
	
	public SimpleLog getAudioLogger() {
		return audioLog;
		
	}
	
	public JDA getJDA() {
		return jda;
		
	}
	
	public FlickrImages getFlickrImages() {
	    return flickr;
	    
	}
	
	public CommandManager getCommandManager() {
		return commandMgr;
		
	}
	
	public AudioIO getAudioIO() {
		return audioIO;
		
	}	
	
	public ExecutorService getExecutor() {
		return executor;
		
	}
	
	public JsonObject getJsonConfig() {
		synchronized( jsonLock ) {
			return jsonConfig;
		
		}
	}
	
}