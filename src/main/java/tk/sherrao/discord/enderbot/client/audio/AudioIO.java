package tk.sherrao.discord.enderbot.client.audio;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.logging.impl.SimpleLog;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.managers.AudioManager;
import tk.sherrao.discord.enderbot.client.Client;
import tk.sherrao.discord.enderbot.client.MessageField;

public class AudioIO extends AudioEventAdapter {

	private final class Lock {}
	private final Object lock = new Lock();
	
	private final Client client;
	private final SimpleLog log;
	
	private DefaultAudioPlayerManager manager;
	private AudioPlayer player;
	
	private Stack< Entry<AudioTrack, VoiceChannel> > trackQueue;
	private TextChannel text;
	private AudioTrack currentTrack;
	private String imgURL;
	
	public AudioIO( Client client  ) {
		this.client = client;
		this.log = client.getAudioLogger();
		
		this.manager = new DefaultAudioPlayerManager();
		this.player = manager.createPlayer();
		player.setVolume( 50 );
		
		AudioSourceManagers.registerRemoteSources( manager );
		AudioSourceManagers.registerLocalSource( manager );

		player.addListener( this );
		
		this.trackQueue = new Stack<>();
		this.imgURL = "https://img.youtube.com/vi/<id>/hqdefault.jpg";
		
	}
	
	public void play( User user, String identifier, Guild server, VoiceChannel voice, TextChannel text ) {
		this.text = text;

		if( server.getAudioManager().getSendingHandler() == null )
			server.getAudioManager().setSendingHandler( new AudioPlayerSendHandler( player ) );

		if( !server.getAudioManager().isConnected() )
			client.getExecutor().execute( () -> {  
				try {
					text.getGuild().getAudioManager().openAudioConnection( voice ); 
				
				} catch( InsufficientPermissionException e ) { 
					text.sendMessage( user.getAsMention() + ", the bot does not have permission to join that channel!" ).complete(); 
					
				}
		} );
		
		manager.loadItemOrdered( server, identifier, new AudioLoadResultHandler() {

			@Override
			public void trackLoaded( AudioTrack track ) {
				if( player.startTrack( track, true ) ) {
					currentTrack = track;
					if( server.getAudioManager().isConnected() )
			            server.getAudioManager().closeAudioConnection();
					    
					client.getExecutor().execute( () -> {  
					    try {
					        text.getGuild().getAudioManager().openAudioConnection( voice ); 

					    } catch( InsufficientPermissionException e ) { 
					        text.sendMessage( user.getAsMention() + ", the bot does not have permission to join that channel!" ).complete(); 
			                    
					    }
			        } );
					
				} else {
					log.info( "Track Queued: " + track.getInfo().title + " (" + track.getIdentifier() + ")" );
					trackQueue.add( new SimpleEntry<>( track, voice ) );
					text.sendMessage( buildMessage( track, "queued" ) ).complete();

				}
			}

			@Override
			public void playlistLoaded( AudioPlaylist playlist ) {
				AudioTrack track = playlist.getTracks().get(0);
				if( player.startTrack( track, true ) ) {
					currentTrack = track;

				} else {
                    log.info( "Playlist Track Queued: " + track.getInfo().title + " (" + track.getIdentifier() + ")" );
                    trackQueue.add( new SimpleEntry<>( track, voice ) );
					text.sendMessage( buildMessage( track, "queued" ) ).complete();

				}
			}

			@Override
			public void noMatches() {
				text.sendMessage( user.getAsMention() + ", no video could be found on youtube with the search query '"
						+ identifier + "', try specifying a URL instead!" ).complete();
				
				if( trackQueue.isEmpty() )
					server.getAudioManager().closeAudioConnection();

			}

			@Override
			public void loadFailed( FriendlyException exception ) {
				text.sendMessage( user.getAsMention() + ", no video could be found on youtube with the search query '"
						+ identifier + "', try specifying a URL instead!" ).complete();
				
				if( trackQueue.isEmpty() )
					server.getAudioManager().closeAudioConnection();

			}

		} );
	}

	public void pause( User user, TextChannel text ) {
		synchronized( lock ) {
			if( currentTrack == null && trackQueue.isEmpty() )
				text.sendMessage( user.getAsMention() + ", the music player is currently inactive!" ).complete();

			else if( !player.isPaused() ) {
				player.setPaused( true );
				text.sendMessage( buildMessage( currentTrack, "paused" ) ).complete();
				
			} else 
				text.sendMessage( user.getAsMention() + ", the music player is already paused!" ).complete();
			
		}
	}
	
	public void resume( User user, TextChannel text ) {
		synchronized( lock ) {
			if( currentTrack == null && trackQueue.isEmpty() ) 
				text.sendMessage( user.getAsMention() + ", the music player is currently inactive!" ).complete();
				
			else if( player.isPaused() ) {
				player.setPaused( false );
				text.sendMessage( buildMessage( currentTrack, "resumed" ) ).complete();
				
			} else 
				text.sendMessage( user.getAsMention() + ", the music player is already playing!" ).complete();
			
		}
	}	
	
	public void skip( User user, Guild server, VoiceChannel voice, TextChannel text ) {
		synchronized( lock ) {
			if( currentTrack == null && trackQueue.isEmpty() )
				text.sendMessage( user.getAsMention() + ", the music player is currently inactive!" ).complete();
			
			else if( player.getPlayingTrack() != null ) 
				currentTrack.setPosition( currentTrack.getDuration() );
			
			else
				return;
				
		}
	}
	
	public void setVolume( int volume ) {
	    player.setVolume( volume );
	    
	}
	
	public void close() {
		closeConnections();
		player.destroy();
		manager.shutdown();
		
	}
	
	private void closeConnections() {
		for( Guild server : client.getJDA().getGuilds() )
			server.getAudioManager().closeAudioConnection();
		
		for( AudioManager manager : client.getJDA().getAudioManagers() ) 
			manager.closeAudioConnection();

	}
	
	private String getId( String link ) {
		if( link.startsWith( "https://www.youtube." ) || link.startsWith( "https://www.youtube." ) )
			return link.substring( link.lastIndexOf( "=" ) + 1, link.length() );
		
		else if( link.startsWith( "https://youtu.be/" ) || link.startsWith( "http://youtu.be/" ) ) 
			return link.substring( link.lastIndexOf( "/" ) + 1, link.length() );
		
		else	
			return null;
		
	}
	
	private MessageEmbed buildMessage( AudioTrack track, String action ) {
		switch( action ) {
			case "queued":
				return client.generateMessage( imgURL.replace( "<id>", getId( track.getInfo().uri )  ), 
						"Track Queued: " + track.getInfo().title, 
						null, 
						null, 
						new MessageField( "Video Link:", track.getInfo().uri ),
						new MessageField( "Duration: ", "" + (track.getInfo().length/1000) + " seconds" ) );

			case "started":
				return client.generateMessage( imgURL.replace( "<id>", getId( track.getInfo().uri )  ), 
						"Track Playing: " + track.getInfo().title, 
						null, 
						null, 
						new MessageField( "Video Link:", track.getInfo().uri ),
						new MessageField( "Duration: ", "" + (track.getInfo().length/1000) + " seconds" ) );

				
			case "paused":
				return client.generateMessage( imgURL.replace( "<id>", getId( track.getInfo().uri )  ), 
						"Track Paused: " + track.getInfo().title, 
						null, 
						null, 
						new MessageField( "Video Link:", track.getInfo().uri ),
						new MessageField( "Time: ", (track.getPosition()/1000) + " seconds / " + (track.getInfo().length/1000) + " seconds" ) );
				
			case "resumed":
				return client.generateMessage( imgURL.replace( "<id>", getId( track.getInfo().uri )  ), 
						"Track Resumed: " + track.getInfo().title, 
						null, 
						null, 
						new MessageField( "Video Link:", track.getInfo().uri ),
						new MessageField( "Time: ", (track.getPosition()/1000) + " seconds / " + (track.getInfo().length/1000) + " seconds" ) );
			
			default:
				throw new FriendlyException( "You done fucked somethin up", FriendlyException.Severity.FAULT, null );
				
		}
	}
	
	@Override
	public void onPlayerPause( AudioPlayer player ) {
		String title = currentTrack.getInfo().title;
		String id = currentTrack.getIdentifier();
		log.info( "Track Paused: " + title + " (" + id + ")" );

	}

	@Override
	public void onPlayerResume( AudioPlayer player ) { 
		String title = currentTrack.getInfo().title;
		String id = currentTrack.getIdentifier();
		log.info( "Track Resumed: " + title + " (" + id + ")" );

	}

	@Override
	public void onTrackStart( AudioPlayer player, AudioTrack track ) {
		log.info( "Track Starting: " + track.getInfo().title + " (" + track.getIdentifier() + ")" );
		text.sendMessage( buildMessage( track, "started" ) ).complete();	
		currentTrack = track;
		
	}
	
	@Override
	public void onTrackEnd( AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason ) {
		log.info( "Track Ending: " + track.getInfo().title + " (" + track.getIdentifier() + "), Reason: " + endReason );
 		if( endReason == AudioTrackEndReason.STOPPED )
 			return;
		
		else if( !trackQueue.isEmpty() && endReason == AudioTrackEndReason.FINISHED ) {
 			Entry<AudioTrack, VoiceChannel> entry = trackQueue.pop();
 			VoiceChannel voice = entry.getValue();
		    client.getExecutor().execute( () -> { 
                try {
                    if( voice.getGuild().getAudioManager().isConnected() )
                        voice.getGuild().getAudioManager().closeAudioConnection();
                    
                    voice.getGuild().getAudioManager().openAudioConnection( voice ); 
                    player.startTrack( currentTrack = entry.getKey(), true );
                    
                } catch( InsufficientPermissionException e ) { 
                    text.sendMessage( "The bot does not have permission to join the channel '" + voice.getName() + "' for the next track!" ).complete(); 
                    
                }
                
		    } );
		    
		} else if( trackQueue.isEmpty() ) {
 			currentTrack = null;
 			client.getExecutor().execute( () -> { text.getGuild().getAudioManager().closeAudioConnection(); }  );
 			text.sendMessage( "Queue cleared!" ).complete();
 		
 		}
	}

	@Override
	public void onTrackException( AudioPlayer pla7yer, AudioTrack track, FriendlyException exception ) {
		log.error( "Track Exception: " + track.getInfo().title + " (" + track.getIdentifier() + ")", exception );
		
	}

	@Override
	public void onTrackStuck( AudioPlayer player, AudioTrack track, long thresholdMs ) {
		log.error( "Track Stuck: " + track.getInfo().title + " (" + track.getIdentifier() + "), Wait Threshold (ms): " + thresholdMs );
		
	}
	
	public Client getClient() {
	    return client;
	    
	}
	
}