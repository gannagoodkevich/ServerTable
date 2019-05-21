package model;

import java.util.ArrayList;
import java.util.List;

public class Uni {
	private String title;
	private List<Faculty> faculties = new ArrayList<Faculty>();

	public Uni(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public Faculty getFaculty(int index) {
		return faculties.get(index);
	}

	public Faculty getFacultyByName(String name) {
		Faculty facultyFound = null;
		for (Faculty faculty : faculties) {
			if (name.equals(faculty.getTitle())) {
				facultyFound = new Faculty(faculty);
			}
		}
		return facultyFound;
	}

	public void addFaculty(Faculty faculty) {
		faculties.add(faculty);
	}

	public int getLenght() {
		return faculties.size();
	}

}
