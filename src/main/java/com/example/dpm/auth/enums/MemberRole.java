package com.example.dpm.auth.enums;

public enum MemberRole {
	 	USER("user"),
	    ADMIN("admin");

	    private final String role;

	    MemberRole(String role) {
	        this.role = role;
	    }

	    public String getRole() {
	        return role;
	    }
}
