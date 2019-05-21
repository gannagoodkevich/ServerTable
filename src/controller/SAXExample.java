package controller;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Department;
import model.Faculty;
import model.Lecturer;
import model.Uni;

public class SAXExample {
	public static boolean isFound;
	public Uni uni;

	public SAXExample(String FileName) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		System.out.println(FileName);
		uni = new Uni("New Uni");
		XMLHandler handler = new XMLHandler("faculties", uni);
		if(handler != null) {
			parser.parse(new File(FileName), handler);
		}
	}

	private static class XMLHandler extends DefaultHandler {

		private String element;
		boolean isEntered;

		Faculty currFaculty;
		Department currDepartment;
		String name;
		String surname;
		String secondName;
		String faculty;
		String department;
		String degree;
		String degreeName;
		String year;
		private Uni uni;

		
		
		public XMLHandler(String element, Uni uni) {
			this.element = element;
			this.uni = uni;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if (isEntered) {
				int length = attributes.getLength();
				for (int i = 0; i < length; i++) {
					if (qName.equals("faculty")) {
						faculty = attributes.getValue(i);
						currFaculty = new Faculty(faculty);
						uni.addFaculty(currFaculty);
					}
					if (qName.equals("department")) {
						department = attributes.getValue(i);
						currDepartment = new Department(department);
						currFaculty.addDepartment(currDepartment);
					} else if (qName.equalsIgnoreCase("employee")) {
						name = attributes.getValue("name");
						surname = attributes.getValue("surname");
						secondName = attributes.getValue("secondName");
						degreeName = attributes.getValue("degreeT");
						degree = attributes.getValue("degree");
						year = attributes.getValue("year");
					}
				}
			}

			if (qName.equals(element)) {
				isEntered = true;
				isFound = true;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals(element))
				isEntered = false;
			if (qName.equals("employee")) {
				Lecturer lecturer = new Lecturer(name, surname, secondName, degreeName, degree, year);
				currDepartment.addLecturer(lecturer);
			}
		}

	}
}