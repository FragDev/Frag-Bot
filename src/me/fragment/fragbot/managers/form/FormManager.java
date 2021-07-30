package me.fragment.fragbot.managers.form;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.fragment.fragbot.FragBot;
import me.fragment.fragbot.managers.form.Form.Question;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

public class FormManager {

	private List<FormInstance> formInstances = new ArrayList<>();
	private Form pluginForm;
	private Form botForm;

	public FormManager() {
		this.pluginForm = new Form("Here is a dedicated text channel where you can discuss your project with a developer!" + "\n" + "But first, please answer a few questions.");
		this.pluginForm.addQuestions(new Question(Question.Type.INPUT, "***" + "What is the name of your plugin?" + "***").setInputPredicate(new Predicate<String>() {

			@Override
			public boolean test(String input) {
				return input.length() > 0;
			}
		}));
		this.pluginForm.addQuestions(new Question(Question.Type.INPUT, "***" + "Can you briefly describe your plugin?" + "***").setInputPredicate(new Predicate<String>() {

			@Override
			public boolean test(String input) {
				return input.length() > 0;
			}
		}));
		this.pluginForm.addQuestions(new Question(Question.Type.BUTTONS, "***" + "Do you have a defined budget for this plugin?" + "***")
				.addButton(Button.of(ButtonStyle.PRIMARY, "form-plugin-budget-yes", "Yes", Emoji.fromMarkdown("✅")))
				.addButton(Button.of(ButtonStyle.DANGER, "form-plugin-budget-no", "No", Emoji.fromMarkdown("❌")))
				.addButton(Button.of(ButtonStyle.SECONDARY, "form-plugin-budget-unlimited", "Unlimited", Emoji.fromMarkdown("♾️")))
				.setConsumer(new Consumer<FormManager.FormInstance>() {

					@Override
					public void accept(FormInstance instance) {
						if (instance.getResponses().get(pluginForm.getQuestions().get(2)).equals("form-plugin-budget-no")) {
							instance.getResponses().put(pluginForm.getQuestions().get(3), "0");
						} else if (instance.getResponses().get(pluginForm.getQuestions().get(2)).equals("form-plugin-budget-unlimited")) {
							instance.getResponses().put(pluginForm.getQuestions().get(3), "-1.0");
						}
					}
				}));
		this.pluginForm.addQuestions(new Question(Question.Type.INPUT, "***" + "Please enter your budget" + "***").setInputPredicate(new Predicate<String>() {

			@Override
			public boolean test(String input) {
				return Double.parseDouble(input.replaceAll("[^\\d.]", "")) > 0;
			}
		}));
		this.pluginForm.setCancelConsumer(new Consumer<FormInstance>() {

			@Override
			public void accept(FormInstance instance) {
				instance.getTextChannel().delete().queue();
			}
		});
		this.pluginForm.setConsumer(new Consumer<FormManager.FormInstance>() {

			@Override
			public void accept(FormInstance instance) {
				FragBot.getProjectsManager().createProject(instance.getUser(), instance.getTextChannel(), instance.getResponses().get(instance.getForm().getQuestions().get(0)),
						instance.getResponses().get(instance.getForm().getQuestions().get(1)),
						Double.parseDouble(instance.getResponses().get(instance.getForm().getQuestions().get(3))));
			}
		});
	}

	public boolean hasFormInstance(User user) {
		return this.formInstances.stream().anyMatch(instance -> instance.getUser().getId().equals(user.getId()));
	}

	public List<FormInstance> getUserFormInstances(User user) {
		return this.formInstances.stream().filter(instance -> instance.getUser().getId().equals(user.getId())).collect(Collectors.toList());
	}

	public FormInstance getUserFormInstance(User user, TextChannel textChannel) {
		return this.formInstances.stream().filter(instance -> instance.getUser().getId().equals(user.getId()) && instance.getTextChannel().getId().equals(textChannel.getId()))
				.findFirst().orElse(null);
	}

	public void addFormInstance(FormInstance formInstance) {
		this.formInstances.add(formInstance);
	}

	public void removeFormInstance(FormInstance formInstance) {
		this.formInstances.remove(formInstance);
	}

	public List<FormInstance> getFormInstances() {
		return formInstances;
	}

	public void stopFormButton(User user, Message message) {
		if (hasFormInstance(user) && getUserFormInstance(user, message.getTextChannel()) != null) {
			getUserFormInstance(user, message.getTextChannel()).stop();
			removeFormInstance(getUserFormInstance(user, message.getTextChannel()));
		}
	}

	public Form getPluginForm() {
		return pluginForm;
	}

	public Form getBotForm() {
		return botForm;
	}

	@SuppressWarnings("deprecation")
	public static class FormInstance {

		private User user;
		private TextChannel textChannel;
		private Form form;
		private List<Message> cacheMessages = new ArrayList<>();
		private Map<Question, String> responses = new HashMap<>();

		public FormInstance(User user, TextChannel textChannel, Form form) {
			this.user = user;
			this.textChannel = textChannel;
			this.form = form;

			this.textChannel
					.sendMessage(new EmbedBuilder().setColor(new Color(47, 49, 54)).setAuthor(this.textChannel.getGuild().getName(), null, this.textChannel.getGuild().getIconUrl())
							.setDescription(this.form.getMessage()).build())
					.setActionRow(Button.of(ButtonStyle.DANGER, "form-abort", "Stop Form", Emoji.fromMarkdown("❌"))).queue(message -> {
						getCacheMessages().add(message);
					});
			next();
		}

		public void next() {
			Question question = getActualQuestion();

			if (question == null) {
				this.form.getConsumer().accept(this);
			} else if (question.getButtons().isEmpty()) {
				this.textChannel.sendMessage(new EmbedBuilder().setColor(new Color(47, 49, 54)).setDescription(question.getMessage()).setFooter("Entry type question").build())
						.queue(message -> {
							getCacheMessages().add(message);
						});
			} else if (!question.getButtons().isEmpty()) {
				this.textChannel.sendMessage(new EmbedBuilder().setColor(new Color(47, 49, 54)).setDescription(question.getMessage()).setFooter("Select type question").build())
						.setActionRow(question.getButtons()).queue(message -> {
							getCacheMessages().add(message);
						});
			}
		}

		public void response(String input) {
			Question question = getActualQuestion();

			if (question.getButtons().isEmpty()) {
				if (question.getInputPredicate().test(input)) {
					getResponses().put(question, input);
				} else {
					this.textChannel.sendMessage(
							new EmbedBuilder().setColor(new Color(47, 49, 54)).setDescription("❌ **Invalid response, please try again**").setFooter("Entry type question").build())
							.queue(message -> {
								getCacheMessages().add(message);
							});
				}
			} else {
				getResponses().put(question, input);
			}

			if (question.getConsumer() != null) {
				question.getConsumer().accept(this);
			}
			next();
		}

		public void stop() {
			this.form.getCancelConsumer().accept(this);
		}

		public Question getActualQuestion() {
			return this.form.getQuestions().stream().filter(q -> !this.responses.keySet().contains(q)).findFirst().orElse(null);
		}

		public User getUser() {
			return user;
		}

		public TextChannel getTextChannel() {
			return textChannel;
		}

		public Form getForm() {
			return form;
		}

		public List<Message> getCacheMessages() {
			return cacheMessages;
		}

		public Map<Question, String> getResponses() {
			return responses;
		}

	}

}
