package me.fragment.fragbot;

import javax.security.auth.login.LoginException;

import me.fragment.fragbot.events.ServerEvents;
import me.fragment.fragbot.managers.form.FormManager;
import me.fragment.fragbot.managers.projects.ProjectsManager;
import me.fragment.fragbot.managers.services.ServicesManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import pro.husk.mysql.MySQL;

public class FragBot {

	private static JDA jda;
	private static MySQL database;

	private static ServicesManager servicesManager;
	private static FormManager formManager;
	private static ProjectsManager projectsManager;

	private static double defaultHoursRate = 7.00;

	public static void main(String[] args) throws LoginException, InterruptedException {
		jda = JDABuilder.createDefault(args[0]).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
		jda.addEventListener(new ServerEvents());
		jda.awaitReady();

		database = new MySQL(args[1], args[2], args[3]);

		servicesManager = new ServicesManager();
		formManager = new FormManager();
		projectsManager = new ProjectsManager();
	}

	public static JDA getJda() {
		return jda;
	}

	public static MySQL getDatabase() {
		return database;
	}

	public static ServicesManager getServicesManager() {
		return servicesManager;
	}

	public static FormManager getFormManager() {
		return formManager;
	}

	public static ProjectsManager getProjectsManager() {
		return projectsManager;
	}

	public static double getDefaultHoursRate() {
		return defaultHoursRate;
	}

}
