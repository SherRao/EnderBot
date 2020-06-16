package tk.sherrao.discord.enderbot.client.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {

	private final AudioPlayer player;
	private AudioFrame lastFrame;
	
	public AudioPlayerSendHandler( AudioPlayer player ) {
		this.player = player;
		
	}
	
	@Override
	public boolean canProvide() {
		return (lastFrame = player.provide()) != null;

	}

	@Override
	public byte[] provide20MsAudio() {
		return lastFrame.data;
		
	}
	
	@Override
	public boolean isOpus() {
	    return true;
	
	}

}
