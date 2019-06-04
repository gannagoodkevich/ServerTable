package controller;

import java.util.ArrayList;
import java.util.List;

import model.Lecturer;
import model.Uni;

public class SearchController {

	public List<String[]> listenerSearchByFaculty(Uni uni, String faculty, String degreeeName) {
		List<String[]> rowList = new ArrayList<String[]>();
		System.out.println("Uni is workng on controller" + uni.getFaculty(0).getTitle());
		for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < uni.getFacultyByName(faculty)
				.getLenght(); indexOfCurrentDepartment++) {
			if (uni.getFacultyByName(faculty).getDepartment(indexOfCurrentDepartment)
					.getLectureByDegreeName(degreeeName) != null) {
				for (Lecturer lecturer : uni.getFacultyByName(faculty).getDepartment(indexOfCurrentDepartment)
						.getLectureByDegreeName(degreeeName)) {
					rowList.add(new String[] { uni.getFacultyByName(faculty).getTitle(),
							uni.getFacultyByName(faculty).getDepartment(indexOfCurrentDepartment).getTitle(),
							lecturer.getName() + " " + lecturer.getSurname() + " " + lecturer.getSecondName(),
							lecturer.getDegreeName(), lecturer.getDegree(), lecturer.getYear() });
				}
			}

		}
		return rowList;
	}

	public List<String[]> listenerSearchByName(Uni uni, String department, String name) {
		List<String[]> rowList = new ArrayList<String[]>();
		String str = "";
		if (str.equals(name)) {
			for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < uni.getLenght(); indexOfCurrentFaculty++) {
				for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < uni.getFaculty(indexOfCurrentFaculty)
						.getLenght(); indexOfCurrentDepartment++) {
					if (uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getTitle()
							.equals(department)) {
						System.out.println(uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
								.getLenght());
						for (int indexOfCurrentLecturer = 0; indexOfCurrentLecturer < uni
								.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
								.getLenght(); indexOfCurrentLecturer++) {
							rowList.add(new String[] { uni.getFaculty(indexOfCurrentFaculty).getTitle(),
									uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department).getTitle(),
									uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department)
											.getlecturer(indexOfCurrentLecturer).getName()
											+ " "
											+ uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department)
													.getlecturer(indexOfCurrentLecturer).getSurname()
											+ " "
											+ uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department)
													.getlecturer(indexOfCurrentLecturer).getSecondName(),
									uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department)
											.getlecturer(indexOfCurrentLecturer).getDegreeName(),
									uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department)
											.getlecturer(indexOfCurrentLecturer).getDegree(),
									uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department)
											.getlecturer(indexOfCurrentLecturer).getYear() });
							System.out.println(
									uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
											.getlecturer(indexOfCurrentLecturer).getName());
						}
					}
				}
			}

		} else {
			for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < uni.getLenght(); indexOfCurrentFaculty++) {
				if (uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department) != null) {
					if (uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department)
							.getLectureByName(name) != null) {
						for (Lecturer lecturer : uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department)
								.getLectureByName(name)) {
							rowList.add(new String[] { uni.getFaculty(indexOfCurrentFaculty).getTitle(),
									uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department).getTitle(),
									lecturer.getName() + " " + lecturer.getSurname() + " " + lecturer.getSecondName(),
									lecturer.getDegreeName(), lecturer.getDegree(), lecturer.getYear() });
						}
					}
				} else {
					for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < uni
							.getFaculty(indexOfCurrentFaculty).getLenght(); indexOfCurrentDepartment++) {

						System.out.println(uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
								.getLenght());
						for (int indexOfCurrentLecturer = 0; indexOfCurrentLecturer < uni
								.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
								.getLenght(); indexOfCurrentLecturer++) {
							if (uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
									.getlecturer(indexOfCurrentLecturer).getName().equals(name)) {
								rowList.add(new String[] { uni.getFaculty(indexOfCurrentFaculty).getTitle(),
										uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
												.getTitle(),
										uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
												.getlecturer(indexOfCurrentLecturer).getName()
												+ " "
												+ uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
														.getlecturer(indexOfCurrentLecturer).getSurname()
												+ " "
												+ uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
														.getlecturer(indexOfCurrentLecturer).getSecondName(),
										uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
												.getlecturer(indexOfCurrentLecturer).getDegreeName(),
										uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
												.getlecturer(indexOfCurrentLecturer).getDegree(),
										uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
												.getlecturer(indexOfCurrentLecturer).getYear() });
								System.out.println(
										uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
												.getlecturer(indexOfCurrentLecturer).getName());
							}
						}
					}

				}
			}

		}
		return rowList;

	}

	public List<String[]> listenerSearchByYear(Uni uni, String year1, String year2) {
		List<String[]> rowList = new ArrayList<String[]>();
		for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < uni.getLenght(); indexOfCurrentFaculty++) {
			for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < uni.getFaculty(indexOfCurrentFaculty)
					.getLenght(); indexOfCurrentDepartment++) {
				if (uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
						.getLectureByYear(year1, year2) != null) {
					for (Lecturer lecturer : uni.getFaculty(indexOfCurrentFaculty)
							.getDepartment(indexOfCurrentDepartment).getLectureByYear(year1, year2)) {
						rowList.add(new String[] { uni.getFaculty(indexOfCurrentFaculty).getTitle(),
								uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
										.getTitle(),
								lecturer.getName() + " " + lecturer.getSurname() + " " + lecturer.getSecondName(),
								lecturer.getDegreeName(), lecturer.getDegree(), lecturer.getYear() });
					}
				}
			}
		}
		return rowList;
	}

}
