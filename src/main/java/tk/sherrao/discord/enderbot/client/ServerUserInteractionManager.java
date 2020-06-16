package tk.sherrao.discord.enderbot.client;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ServerUserInteractionManager extends ListenerAdapter {
	
    private final Client client;
    
	private String joinMsg;
	private String leaveMsg;
	private String banMsg;
	
	public ServerUserInteractionManager( Client client ) {
	    this.client = client;
		
		this.joinMsg = "[user] has joined the server!";
		this.leaveMsg = "[user] has left the server!";
		this.banMsg = "[user] has been barred from the server!";
		
	}
	
	@Override
	public void onGuildMemberJoin( GuildMemberJoinEvent event ) {
		User user = event.getUser();
		event.getGuild().getDefaultChannel().sendMessage( joinMsg.replace( "[user]", user.getAsMention() ) ).complete();
		
	}
	
	@Override
	public void onGuildMemberLeave( GuildMemberLeaveEvent event ) {
		User user = event.getUser();
		event.getGuild().getDefaultChannel().sendMessage( leaveMsg.replace( "[user]", user.getAsMention() ) ).complete();	
		
	}

	
	@Override
	public void onGuildBan( GuildBanEvent event ) {
		User user = event.getUser();
		event.getGuild().getDefaultChannel().sendMessage( banMsg.replace( "[user]", user.getAsMention() ) ).complete();
		
	}

	public void setJoinMessage( String msg ) {
		this.joinMsg = msg;
		
	}
	
	public void setLeaveMessage( String msg ) {
		this.leaveMsg = msg;
		
	}
	
	public void setBanMessage( String msg ) {
		this.banMsg = msg;
	
	}
	
    public Client getClient() {
        return client;
    
    }
		
}