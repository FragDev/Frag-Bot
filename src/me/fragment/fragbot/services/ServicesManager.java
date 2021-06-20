package me.fragment.fragbot.services;

import java.awt.Color;

import me.fragment.fragbot.FragBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

public class ServicesManager {

	private TextChannel textChannel;

	public ServicesManager() {
		textChannel = FragBot.getJda().getGuilds().get(0).getTextChannels().stream().filter(channel -> channel.getName().equalsIgnoreCase("order-project")).findFirst().get();

		this.textChannel.sendMessage(new EmbedBuilder().setColor(new Color(47, 49, 54)).setAuthor(this.textChannel.getGuild().getName(), null, this.textChannel.getGuild().getIconUrl())
				.setDescription("**Developement Services**" + "\n\n" + "🔌 **Quality custom plugins**" + "\n"
						+ "		*You want an additional server functionality but no plugin exists for that? Have yours developed!*" + "\n\n"
						+ "  <:dot:855016368633413632> **Quality** *Get a plugin that surpasses perfection*" + "\n"
						+ "  <:dot:855016368633413632> **Performances** *Combined power and lightweight, long live the 20 of tps!*" + "\n"
						+ "  <:dot:855016368633413632> **Bug-free warranty** *A bug ? Fast and free correction*" + "\n"
						+ "  <:dot:855016368633413632> **Flex Plugins** *Ability to add functionality several months after development*" + "\n"
						+ "  <:dot:855016368633413632> **Advanced Plugins** *Ability to integrate databases, API management, and more*" + "\n\n" + "🤖 **Quality discord bot**" + "\n"
						+ "		*Need a Discord bot to manage your server? Need to connect your minecraft server to Discord? Or something else? You are in the right place!*" + "\n\n"
						+ "  <:dot:855016368633413632> **Language** *Bot developed in Java with JDA*" + "\n" + "  <:dot:855016368633413632> **Quality** *Get a high-class Bot*" + "\n"
						+ "  <:dot:855016368633413632> **Performances** *Combined power and lightweight*" + "\n"
						+ "  <:dot:855016368633413632> **MC Bridge** *Link your Minecraft server to your Discord server*" + "\n"
						+ "  <:dot:855016368633413632> **Hosting** *Hosting possible directly on your Minecraft server*")
				.build())
				.setActionRow(Button.of(ButtonStyle.PRIMARY, "service-plugin", "Order Plugin", Emoji.fromMarkdown("🔌")),
						Button.of(ButtonStyle.PRIMARY, "service-discordbot", "Order Discord Bot", Emoji.fromMarkdown("🤖")))
				.queue();
	}

}
