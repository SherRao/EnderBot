package tk.sherrao.discord.enderbot.client.commands;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tk.sherrao.discord.enderbot.client.Client;

@Deprecated
@SuppressWarnings( "all" )
public  class TemplateCommand extends ChatCommandProcessor {
	
	private MessageEmbed websiteMsg;
	
	public TemplateCommand( Client client, String command, Permission permission, String usage, String... aliases ) {
		super( client, command, permission, usage, aliases );
	
		this.websiteMsg = client.generateMessage( null, "Website", "The website is 'www.enderbyte.com'!", "EnderBot | Website" );
		
	}

	@Override
	public void execute( User sender, Guild server, TextChannel channel, String alias,  String[] args ) {
		// Create the EmbedBuilder instance
		EmbedBuilder eb = new EmbedBuilder();

		/*
		    Set the title:
		    1. Arg: title as string
		    2. Arg: URL as string or could also be null
		 */
		eb.setTitle("Title", null);

		/*
		    Set the color
		 */
		eb.setColor(Color.red);
		eb.setColor(new Color(0xF40C0C));
		eb.setColor(new Color(255, 0, 54));

		/*
		    Set the text of the Embed:
		    Arg: text as string
		 */
		eb.setDescription("Text");

		/*
		    Add fields to embed:
		    1. Arg: title as string
		    2. Arg: text as string
		    3. Arg: inline mode true / false
		 */
		eb.addField("Title of field 1", "test of field 1", true);
		eb.addField("Title of field 2", "test of field 2", true );
		eb.addField("Title of field 3", "test of field 3" , true );
		eb.addField("Title of field 4", "test of field 4", true );
		eb.addField("Title of field 5", "test of field 5", true );


		/*
		    Add spacer like field
		    Arg: inline mode true / false
		 */
		eb.addBlankField(false);

		/*
		    Add embed author:
		    1. Arg: name as string
		    2. Arg: url as string (can be null)
		    3. Arg: icon url as string (can be null)
		 */
		eb.setAuthor("name", null, null );

		/*
		    Set footer:
		    1. Arg: text as string
		    2. icon url as string (can be null)
		 */
		eb.setFooter("Text", null );

		/*
		    Set image:
		    Arg: image url as string
		 */
		//eb.setImage("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");

		/*
		    Set thumbnail image:
		    Arg: image url as string
		 */
		///eb.setThumbnail("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");

		channel.sendMessage( eb.build() ).complete();
		
		//	channel.sendMessage( websiteMsg ).complete();
		
		
	}
	
}