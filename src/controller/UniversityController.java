package controller;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Uni;

public class UniversityController {

	public List<String[]> getUniversity(Uni university) {
		List<String[]> rowList = new ArrayList<String[]>();

		for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < university
				.getLenght(); indexOfCurrentFaculty++) {
			for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < university
					.getFaculty(indexOfCurrentFaculty).getLenght(); indexOfCurrentDepartment++) {
				for (int indexOfCurrentLecturer = 0; indexOfCurrentLecturer < university
						.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
						.getLenght(); indexOfCurrentLecturer++) {
					rowList.add(new String[] { university.getFaculty(indexOfCurrentFaculty).getTitle(),
							university.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
									.getTitle(),
							university.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
									.getlecturer(indexOfCurrentLecturer).getName()
									+ " "
									+ university.getFaculty(indexOfCurrentFaculty)
											.getDepartment(indexOfCurrentDepartment)
											.getlecturer(indexOfCurrentLecturer).getSurname()
									+ " "
									+ university.getFaculty(indexOfCurrentFaculty)
											.getDepartment(indexOfCurrentDepartment)
											.getlecturer(indexOfCurrentLecturer).getSecondName(),
							university.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
									.getlecturer(indexOfCurrentLecturer).getDegreeName(),
							university.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
									.getlecturer(indexOfCurrentLecturer).getDegree(),
							university.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
									.getlecturer(indexOfCurrentLecturer).getYear() });
				}
			}
		}
		return rowList;
	}
	
	
	public List<String> getFaculties(Uni uni) {
		List<String> faculties = new ArrayList<String>();
		for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < uni.getLenght(); indexOfCurrentFaculty++) {
			faculties.add(uni.getFaculty(indexOfCurrentFaculty).getTitle());
		}
		return faculties;
	}
	
	public List<String> getDepartments(Uni uni) {
		List<String> departments = new ArrayList<String>();
		for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < uni.getLenght(); indexOfCurrentFaculty++) {
			for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < uni.getFaculty(indexOfCurrentFaculty)
					.getLenght(); indexOfCurrentDepartment++) {
				departments.add(uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
						.getTitle());
			}
		}
		return departments;
	}	
	
	public Set<String> getDegrees(Uni uni){
		int numberOfDepartment = 0;
		for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < uni.getLenght(); indexOfCurrentFaculty++) {
			numberOfDepartment += uni.getFaculty(indexOfCurrentFaculty).getLenght();
		}
		String[] departments = new String[numberOfDepartment];
		Set<String> degreeT = new HashSet<>();
		int numberOfCurrentDepartments = 0;
		for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < uni.getLenght(); indexOfCurrentFaculty++) {
			for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < uni.getFaculty(indexOfCurrentFaculty)
					.getLenght(); indexOfCurrentDepartment++) {
				departments[numberOfCurrentDepartments++] = uni.getFaculty(indexOfCurrentFaculty)
						.getDepartment(indexOfCurrentDepartment).getTitle();
				for (int indexOfCurrentLecturer = 0; indexOfCurrentLecturer < uni.getFaculty(indexOfCurrentFaculty)
						.getDepartment(indexOfCurrentDepartment).getLenght(); indexOfCurrentLecturer++) {
					degreeT.add(uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
							.getlecturer(indexOfCurrentLecturer).getDegreeName());
				}
			}
		}
		return degreeT;
	}
	
	public Set<String> getDegreesSc(Uni currentUniversity){
		Set<String> degreeScience = new HashSet<>();
		for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < currentUniversity
				.getLenght(); indexOfCurrentFaculty++) {
			for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < currentUniversity
					.getFaculty(indexOfCurrentFaculty).getLenght(); indexOfCurrentDepartment++) {
				for (int indexOfCurrentLecturer= 0; indexOfCurrentLecturer < currentUniversity.getFaculty(indexOfCurrentFaculty)
						.getDepartment(indexOfCurrentDepartment).getLenght(); indexOfCurrentLecturer++) {
					degreeScience.add(currentUniversity.getFaculty(indexOfCurrentFaculty)
							.getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer).getDegree());
				}
			}
		}
		return degreeScience;
	}
	
}
