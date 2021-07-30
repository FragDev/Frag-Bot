package me.fragment.fragbot.events;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.fragment.fragbot.FragBot;
import me.fragment.fragbot.managers.form.Form.Question.Type;
import me.fragment.fragbot.managers.form.FormManager.FormInstance;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

public class ServerEvents extends ListenerAdapter {

	private Map<String, List<String>> welcomeUsersMap = new HashMap<String, List<String>>();

	@Override
	@SuppressWarnings("deprecation")
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("855084292790812692")).queue();
		event.getGuild().getTextChannelsByName("welcome", false).forEach(textChannel -> {
			textChannel
					.sendMessage(new EmbedBuilder().setColor(new Color(47, 49, 54)).setAuthor(event.getGuild().getName(), null, event.getGuild().getIconUrl()).setDescription("\n"
							+ "**👋 Welcome to " + event.getMember().getAsMention() + "**" + "\n\n"
							+ "  <:dot:855016368633413632> Welcome to my Java development server. Here you will find everything you need!" + "\n"
							+ "  <:dot:855016368633413632> Go to the "
							+ event.getGuild().getChannels().stream().filter(channel -> channel.getType() == ChannelType.TEXT && channel.getName().equalsIgnoreCase("order-project"))
									.map(GuildChannel::getAsMention).findFirst().orElse(null)
							+ " channel to order a project").setThumbnail(event.getUser().getAvatarUrl())
							.setFooter("Frag'Bot say welcome to " + event.getUser().getName(), FragBot.getJda().getSelfUser().getAvatarUrl()).build())
					.setActionRow(Button.of(ButtonStyle.PRIMARY, "welcome", "Say Welcome", Emoji.fromMarkdown("👋"))).queue(message -> {
						this.welcomeUsersMap.put(message.getId(), new ArrayList<String>());
					});
		});
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onButtonClick(ButtonClickEvent event) {
		if (event.getButton().getId().equalsIgnoreCase("welcome") && event.getChannel().getName().equals("welcome")) {
			if (this.welcomeUsersMap.containsKey(event.getMessageId())) {
				if (!this.welcomeUsersMap.get(event.getMessageId()).contains(event.getUser().getId())) {
					MessageEmbed embed = event.getMessage().getEmbeds().get(0);

					if (!embed.getDescription().contains("<@" + event.getUser().getId() + ">")) {
						this.welcomeUsersMap.get(event.getMessageId()).add(event.getUser().getId());
						event.getMessage()
								.editMessage(new EmbedBuilder(embed).setFooter(
										embed.getFooter().getText().contains(" and ")
												? event.getUser().getName() + " and " + (Integer.valueOf(embed.getFooter().getText().split(" and ")[1].split(" other ")[0]) + 1)
														+ " other say welcome to " + embed.getFooter().getText().split(" say welcome to ")[1]
												: event.getUser().getName() + " and 1 other say welcome to " + embed.getFooter().getText().split(" say welcome to ")[1],
										event.getUser().getAvatarUrl()).build())
								.queue();
						event.deferEdit().queue();
					} else {
						event.reply("Welcome to you " + event.getUser().getAsMention() + "!").setEphemeral(true).queue();
					}
				} else {
					event.reply("You have already welcomed this member!").setEphemeral(true).queue();
				}
			} else {
				event.reply("No luck you arrived too late .. Maybe next time!").setEphemeral(true).queue(reply -> {
					event.editButton(null).queue();
				});
			}
		} else if (event.getButton().getId().equalsIgnoreCase("service-plugin") && event.getChannel().getName().equals("order-project")) {
			if (event.getTextChannel().getParent() != null) {
				event.getTextChannel().getParent().createTextChannel("new-project-plugin").queue(channel -> {
					FormInstance instance = new FormInstance(event.getUser(), channel, FragBot.getFormManager().getPluginForm());
					FragBot.getFormManager().addFormInstance(instance);
				});
			}

			event.deferEdit().queue();
		} else if (FragBot.getFormManager().hasFormInstance(event.getUser()) && FragBot.getFormManager().getUserFormInstance(event.getUser(), event.getTextChannel()) != null
				&& event.getButton().getId().equals("form-abort")) {
			FragBot.getFormManager().stopFormButton(event.getUser(), event.getMessage());
		} else if (FragBot.getFormManager().hasFormInstance(event.getUser()) && FragBot.getFormManager().getUserFormInstance(event.getUser(), event.getTextChannel()) != null
				&& FragBot.getFormManager().getUserFormInstance(event.getUser(), event.getTextChannel()).getActualQuestion().getType() == Type.BUTTONS) {
			FragBot.getFormManager().getUserFormInstance(event.getUser(), event.getTextChannel()).response(event.getButton().getId());
			event.deferEdit().queue();
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (FragBot.getFormManager().hasFormInstance(event.getAuthor()) && FragBot.getFormManager().getUserFormInstance(event.getAuthor(), event.getTextChannel()) != null
				&& FragBot.getFormManager().getUserFormInstance(event.getAuthor(), event.getTextChannel()).getActualQuestion().getType() == Type.INPUT) {
			FragBot.getFormManager().getUserFormInstance(event.getAuthor(), event.getTextChannel()).response(event.getMessage().getContentStripped());
		}
	}

}
