package controller;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import model.Department;
import model.Lecturer;
import model.Uni;

public class DeleteController {

	public int listenerSearchByFaculty(Uni uni, String faculty, String degreeeName) {
		List<String[]> rowList = new ArrayList<String[]>();
		int numberOfDelete = 0;
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
					uni.getFacultyByName(faculty).getDepartment(indexOfCurrentDepartment).deleteLecture(lecturer);
					numberOfDelete++;

				}
			}

		}
		return numberOfDelete;
	}

	public int listenerSearchByName(Uni uni, String department, String name) {
		int numberOfDelete = 0;
		String str = "";
		List<Lecturer> rowList = new ArrayList<Lecturer>();
		if (str.equals(name)) {
			for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < uni
					.getLenght(); indexOfCurrentFaculty++) {
				for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < uni
						.getFaculty(indexOfCurrentFaculty).getLenght(); indexOfCurrentDepartment++) {
					if (uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getTitle()
							.equals(department)) {
						System.out.println(uni
								.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
								.getLenght());
						for (int indexOfCurrentLecturer = 0; indexOfCurrentLecturer < uni
								.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
								.getLenght(); indexOfCurrentLecturer++) {
							rowList.add(uni
									.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer));
							System.out.println(uni
							.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer).getName());
							numberOfDelete++;
						}
						for(Lecturer lect : rowList) {
				System.out.println(lect.getName());
				uni
				.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).deleteLecture(lect);
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
							uni.getFaculty(indexOfCurrentFaculty).getDepartmentByName(department)
									.deleteLecture(lecturer);
							numberOfDelete++;
						}
						System.out.println("Find");
					}
				}else {
					for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < uni
							.getFaculty(indexOfCurrentFaculty).getLenght(); indexOfCurrentDepartment++) {

						System.out.println(uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
								.getLenght());
						for (int indexOfCurrentLecturer = 0; indexOfCurrentLecturer < uni
								.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
								.getLenght(); indexOfCurrentLecturer++) {
							if (uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
									.getlecturer(indexOfCurrentLecturer).getName().equals(name)) {
								rowList.add(uni
										.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer));
								System.out.println(uni
								.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer).getName());
								numberOfDelete++;
								System.out.println(
										uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
												.getlecturer(indexOfCurrentLecturer).getName());
							}
							for(Lecturer lect : rowList) {
								System.out.println(lect.getName());
								uni
								.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).deleteLecture(lect);
							}
						}
					}

				}
			}

		}
		return numberOfDelete;
	}

	public int listenerSearchByYear(Uni uni, String year1, String year2) {
		int numberOfDelete = 0;
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
						uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment)
								.deleteLecture(lecturer);
						numberOfDelete++;
					}
				}
			}
		}
		return numberOfDelete;
	}

}
