package controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import org.w3c.dom.Node;

import model.Uni;

public class DOMExample {
	public DOMExample(Uni uni, String fileName) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Node root = document.createElement("university");
		document.appendChild(root);
		Element nameUni = document.createElement("name");
		nameUni.setTextContent(uni.getTitle());
		root.appendChild(nameUni);
		Element faculties = document.createElement("faculties");
		root.appendChild(faculties);
		for (int indexOfCurrentFaculty = 0; indexOfCurrentFaculty < uni.getLenght(); indexOfCurrentFaculty++) {
			Element faculty = document.createElement("faculty");
			faculty.setAttribute("titleF", uni.getFaculty(indexOfCurrentFaculty).getTitle());
			faculties.appendChild(faculty);
			Element departments = document.createElement("departments");
			faculty.appendChild(departments);
			for (int indexOfCurrentDepartment = 0; indexOfCurrentDepartment < uni.getFaculty(indexOfCurrentFaculty).getLenght(); indexOfCurrentDepartment++) {
				Element department = document.createElement("department");
				department.setAttribute("titleK", uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getTitle());
				departments.appendChild(department);
				for (int indexOfCurrentLecturer = 0; indexOfCurrentLecturer < uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getLenght(); indexOfCurrentLecturer++) {
					Element lecture = document.createElement("employee");
					lecture.setAttribute("name", uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer).getName());
					lecture.setAttribute("surname", uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer).getSurname());
					lecture.setAttribute("secondName",
							uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer).getSecondName());
					lecture.setAttribute("degreeT", uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer).getDegreeName());
					lecture.setAttribute("degree", uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer).getDegree());
					lecture.setAttribute("year", uni.getFaculty(indexOfCurrentFaculty).getDepartment(indexOfCurrentDepartment).getlecturer(indexOfCurrentLecturer).getYear());
					department.appendChild(lecture);
				}
			}
		}

		TransformerFactory transform = TransformerFactory.newInstance();
		Transformer trans = transform.newTransformer();
		DOMSource domSource = new DOMSource(document);

		StreamResult res = new StreamResult(new File(fileName));

		trans.transform(domSource, res);
	}
	
	

	/*public static boolean findDepartment(Document document, String department, Node fin) {
		NodeList currentDepartment = document.getElementsByTagName("department");
		boolean foundDepartment = false;
		for (int i = 0; i < currentDepartment.getLength(); i++) {
			NamedNodeMap namedNodeMap = currentDepartment.item(i).getAttributes();
			Node nodeAttr = namedNodeMap.getNamedItem("titleK");
			if (department.equals(nodeAttr.getTextContent())) {
				foundDepartment = true;
				fin = nodeAttr;
				System.out.println(nodeAttr.getTextContent());
			}
		}
		return foundDepartment;
	}*/
}
