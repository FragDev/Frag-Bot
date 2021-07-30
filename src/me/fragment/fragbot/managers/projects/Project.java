package me.fragment.fragbot.managers.projects;

import me.fragment.fragbot.managers.organizations.Organization;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class Project {

	private int id;
	private TextChannel channel;
	private Role role;
	private Organization organization;

	private String name;
	private String description;
	private Double budget;
	private Double hoursRate;
	private String wakatimeUrl;
	private ProjectStatus status = ProjectStatus.Quote;

	public Project(Role role, TextChannel channel, String name) {
		this.role = role;
		this.channel = channel;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TextChannel getChannel() {
		return channel;
	}

	public void setChannel(TextChannel channel) {
		this.channel = channel;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public Double getHoursRate() {
		return hoursRate;
	}

	public void setHoursRate(Double hoursRate) {
		this.hoursRate = hoursRate;
	}

	public String getWakatimeUrl() {
		return wakatimeUrl;
	}

	public void setWakatimeUrl(String wakatimeUrl) {
		this.wakatimeUrl = wakatimeUrl;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public static enum ProjectStatus {
		Quote, Accepted, InDevelopment, Fix, Done;
	}

}
