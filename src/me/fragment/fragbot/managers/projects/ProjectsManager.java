package me.fragment.fragbot.managers.projects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import me.fragment.fragbot.FragBot;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

public class ProjectsManager {

	private Category projectCategory;

	public ProjectsManager() {
		this.projectCategory = FragBot.getJda().getCategoryById("854307931587084308");

		try {
			RestAction.allOf(
					this.projectCategory.getTextChannels().stream().filter(channel -> !channel.getName().equals("order-project")).map(TextChannel::delete).collect(Collectors.toList()))
					.queue();
		} catch (IllegalArgumentException e) {
		}
	}

	public CompletableFuture<Project> createProject(User user, TextChannel textChannel, String name, String description, Double budget) {
		CompletableFuture<Project> future = new CompletableFuture<Project>();

		textChannel.getGuild().createRole().setName(name).setColor(2552150).queue(role -> {
			Project project = new Project(role, textChannel, name);
			project.setDescription(description);
			project.setBudget(budget != 0 ? budget : null);

			textChannel.getManager().setName(name).queue();
			textChannel.getHistory().retrievePast(100).flatMap((messages) -> RestAction.allOf(messages.stream().map(Message::delete).collect(Collectors.toList()))).queue();

			saveProject(project).thenAccept(id -> {
				project.setId(id);
			});

			future.complete(project);
		});

		return future;
	}

	public CompletableFuture<Integer> saveProject(Project project) {
		CompletableFuture<Integer> future = new CompletableFuture<Integer>();

		new Thread(() -> {
			try {
				Statement statement = FragBot.getDatabase().getConnection().createStatement();
				statement.executeUpdate("INSERT INTO `projects` (`channelID`, `roleID`, `name`, `description`, `budget`, `hoursRate`, `wakatime`, `status`) VALUES ('"
						+ project.getChannel().getId() + "','" + project.getRole().getId() + "','" + project.getName() + "','" + project.getDescription() + "'," + project.getBudget()
						+ "," + project.getHoursRate() + ",'" + project.getWakatimeUrl() + "','" + project.getStatus() + "')", Statement.RETURN_GENERATED_KEYS);

				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					future.complete(resultSet.getInt(1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}).start();

		return future;
	}

	public Category getProjectCategory() {
		return projectCategory;
	}

}
