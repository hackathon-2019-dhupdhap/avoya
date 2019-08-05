package com.mlbd.avoya.Enum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum RoleType {

    ROLE_ADMIN("ROLE_POLICE"), 
    ROLE_LEAVE_USER("ROLE_CIVILIAN");

	private final String value;

	private static final Map<String, RoleType> lookup = new HashMap<String, RoleType>();

	static {
		for (RoleType roleType : EnumSet.allOf(RoleType.class))
			lookup.put(roleType.getValue(), roleType);
	}

	private RoleType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static RoleType findByValue(String value) {
		RoleType roleType = lookup.get(value);
		if (roleType == null) {
			throw new IllegalArgumentException("enums.invalid_role_type");
		}
		return roleType;
	}
}
