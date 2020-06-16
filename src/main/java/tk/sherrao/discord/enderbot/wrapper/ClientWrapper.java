package tk.sherrao.discord.enderbot.wrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.exceptions.RateLimitedException;
import tk.sherrao.discord.enderbot.client.Client;

public class ClientWrapper {

	public static final String BOT_VERSION = "1.1 RELEASE";
	public static String BOT_ID = "";
	public static String BOT_TOKEN = "";
	public static String BOT_SECRET = "";
	
	public static String FLICKR_API_KEY = "";
	public static String FLICKR_SHARED_SECRET = "";
	
    public static final String INVITE = "";
	
	private Client client;
	
	public static void main( String... vargs ) {
		ClientWrapper instance = new ClientWrapper();
		//instance.load();
		instance.start( vargs );	
	}
	
	public ClientWrapper( String... vargs ) {}

	public void load() {
		try {
			String path = URLDecoder.decode( new File( ClientWrapper.class.getProtectionDomain().getCodeSource().getLocation().getPath() ).getParent(), "UTF-8" );
			File file = new File( path, "account.dat" );
			if( !file.exists() ) {
				FileWriter out = new FileWriter( file );
				file.mkdirs();
				out.append( "id=null\n" );
				out.append( "token=null\n" );
				out.append( "secret=null" );
				System.out.println( "File config.dat does not exist! Creating file!" );
				out.close();
				System.exit(0);
				
			} else {
				Scanner sc = new Scanner( file );
				while( sc.hasNextLine() ) {
					String line = sc.nextLine();
					if( line.startsWith( "id=" ) ) {
						BOT_ID = line.replace( "id=", "" );
						
					} else if( line.startsWith( "token=" ) ) {
						BOT_TOKEN = line.replace( "token=", "" );
						
					} else if( line.startsWith( "secret=" ) ) {
						BOT_SECRET = line.replace( "secret=", "" );
						
					} else
						continue;
					
				}
				
				sc.close();

			}
			
		} catch( IOException e ) { e.printStackTrace(); } 
	}
	
	public void start( String... vargs ) {
		try {
			client = new Client( vargs );
			client.onEnable();
			client.start();
			Runtime.getRuntime().addShutdownHook( new Thread( () -> {
				try {
					client.onDisable();
					
				} catch ( InterruptedException | IOException e ) { e.printStackTrace(); }
					
			} ) );
			
		} catch ( LoginException | IllegalArgumentException | InterruptedException | RateLimitedException | IOException e ) { e.printStackTrace(); }
	}
	
	
}