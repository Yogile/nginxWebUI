package com.cym.ext;

import com.cym.model.Department;
import com.cym.model.User;

public class UserExt {
	User user;
	
	Department department;
	
	

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
