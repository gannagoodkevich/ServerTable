package model;

import java.util.ArrayList;
import java.util.List;

public class Faculty {
	private String title;
	private List<Department> departments = new ArrayList<Department>();
	
	public Faculty(String title){
		this.title = title;
	}
	
	Faculty(Faculty fac){
		this.title = fac.title;
		this.departments = fac.departments;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Department getDepartment(int index) {
		return departments.get(index);
	}
	
	public Department getDepartmentByName(String name) {
		Department dep = null;
		for (Department department : departments) {
			if (name.equals(department.getTitle())) {
				dep = new Department(department);
			}
		}
		return dep;
	}
	
	public void addDepartment(Department department) {
		departments.add(department);
	}

	public int getLenght() {
		return departments.size();
	}
}
