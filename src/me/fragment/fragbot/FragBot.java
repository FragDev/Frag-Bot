package me.fragment.fragbot;

import javax.security.auth.login.LoginException;

import me.fragment.fragbot.events.ServerEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class FragBot {

	private static JDA jda;

	public static void main(String[] args) throws LoginException {
		jda = JDABuilder.createDefault(System.getenv("bot-token")).enableIntents(GatewayIntent.GUILD_MEMBERS).setActivity(Activity.competing("Best Bot Cup ⚡")).build();
		jda.addEventListener(new ServerEvents());
	}

	public static JDA getJda() {
		return jda;
	}

}
