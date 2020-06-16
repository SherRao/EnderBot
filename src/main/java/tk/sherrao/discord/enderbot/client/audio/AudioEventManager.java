package tk.sherrao.discord.enderbot.client.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import tk.sherrao.discord.enderbot.client.Client;

public class AudioEventManager extends AudioEventAdapter {

	private final Client client;
	private final AudioPlayer player;
	
	public AudioEventManager( Client client, AudioPlayer player ) {
		this.client = client;
		this.player = player;
		
	}
	
	@Override
	public void onPlayerPause( AudioPlayer player ) {}

	@Override
	public void onPlayerResume( AudioPlayer player ) {}

	@Override
	public void onTrackStart( AudioPlayer player, AudioTrack track ) {}

	@Override
	public void onTrackEnd( AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason ) {}

	@Override
	public void onTrackException( AudioPlayer player, AudioTrack track, FriendlyException exception ) {}

	@Override
	public void onTrackStuck( AudioPlayer player, AudioTrack track, long thresholdMs ) {}

    public AudioPlayer getAudioPlayer() {
        return player;
    
    }
	
    public Client getClient() {
        return client;
    
    }
	
	
}
