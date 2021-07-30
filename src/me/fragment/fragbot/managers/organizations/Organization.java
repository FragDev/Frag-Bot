package me.fragment.fragbot.managers.organizations;

import net.dv8tion.jda.api.entities.Role;

public class Organization {

	private int id;
	private Role role;
	private String name;

	public Organization(Role role, String name) {
		this.role = role;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
