package me.fragment.fragbot;

import javax.security.auth.login.LoginException;

import me.fragment.fragbot.events.ServerEvents;
import me.fragment.fragbot.services.ServicesManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class FragBot {

	private static JDA jda;

	private static ServicesManager servicesManager;

	public static void main(String[] args) throws LoginException, InterruptedException {
		jda = JDABuilder.createDefault(System.getenv("bot-token")).enableIntents(GatewayIntent.GUILD_MEMBERS).setActivity(Activity.competing("Best Bot Cup ⚡")).build();
		jda.addEventListener(new ServerEvents());
		jda.awaitReady();

		servicesManager = new ServicesManager();
	}

	public static JDA getJda() {
		return jda;
	}

	public static ServicesManager getServicesManager() {
		return servicesManager;
	}

}
