package me.fragment.fragbot.events;

import java.awt.Color;

import me.fragment.fragbot.FragBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

public class ServerEvents extends ListenerAdapter {

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		event.getGuild().getTextChannelsByName("welcome", false).forEach(textChannel -> {
			textChannel
					.sendMessage(new EmbedBuilder().setColor(new Color(47, 49, 54)).setTitle("Welcome to Fragment - Java Dev Station !")
							.setDescription("**👋 Welcome to " + event.getMember().getAsMention() + "**").setThumbnail(event.getUser().getAvatarUrl())
							.setFooter("Frag'Bot say welcome to " + event.getUser().getName(), FragBot.getJda().getSelfUser().getAvatarUrl()).build())
					.setActionRow(Button.of(ButtonStyle.PRIMARY, "welcome", "Say Welcome", Emoji.fromMarkdown("👋"))).queue();
		});
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		if (event.getButton().getId().equalsIgnoreCase("welcome") && event.getChannel().getName().equals("welcome")) {
			event.getMessage().editMessage(new EmbedBuilder(event.getMessage().getEmbeds().get(0)).setDescription("a").build()).queue();
			event.deferEdit().queue();
		}
	}

}
