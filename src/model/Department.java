package model;

import java.util.ArrayList;
import java.util.List;

public class Department {
	private String title;
	private List<Lecturer> lecturers = new ArrayList<Lecturer>();

	public Department(String title) {
		this.title = title;
	}

	Department(Department dep) {
		this.title = dep.title;
		this.lecturers = dep.lecturers;
	}

	public String getTitle() {
		return this.title;
	}

	public Lecturer getlecturer(int index) {
		return lecturers.get(index);
	}

	public Lecturer getLectureByInfo(String name, String surname, String secondName, String degree, String degreeT,
			String year) {
		Lecturer lecturerFind = null;
		for (Lecturer lecturer : lecturers) {
			if (name.equals(lecturer.getName()) && surname.equals(lecturer.getSurname())
					&& secondName.equals(lecturer.getSecondName()) && degree.equals(lecturer.getDegree())
					&& degreeT.equals(lecturer.getDegreeName()) && year.equals(lecturer.getYear())) {
				lecturerFind = new Lecturer(lecturer);
			}
		}
		return lecturerFind;
	}

	public List<Lecturer> getLectureByName(String name) {
		List<Lecturer> lecturerFind = new ArrayList<Lecturer>();
		for (Lecturer lecturer : lecturers) {
			if (name.equals(lecturer.getName())) {
				lecturerFind.add(lecturer);
			}
		}
		return lecturerFind;
	}

	public List<Lecturer> getLectureByYear(String year1, String year2) {
		List<Lecturer> lecturerFind = new ArrayList<Lecturer>();
		int year1Find = Integer.parseInt(year1);
		int year2Find = Integer.parseInt(year2);
		System.out.println(year1);
		System.out.println(year2);
		for (Lecturer lecturer : lecturers) {
			if (Integer.parseInt(lecturer.getYear()) >= year1Find && Integer.parseInt(lecturer.getYear()) <= year2Find) {
				lecturerFind.add(lecturer);
				
			}
		}
		return lecturerFind;
	}

	public List<Lecturer> getLectureByDegreeName(String name) {
		List<Lecturer> lecturerFind = new ArrayList<Lecturer>();
		for (Lecturer lecturer : lecturers) {
			if (name.equals(lecturer.getDegreeName())) {
				lecturerFind.add(lecturer);
			}
		}
		return lecturerFind;
	}

	public void addLecturer(Lecturer lecturer) {
		lecturers.add(lecturer);
	}

	public void deleteLecture(Lecturer lecturer) {
		lecturers.remove(lecturer);
	}
	
	public int getLenght() {
		return lecturers.size();
	}
}
