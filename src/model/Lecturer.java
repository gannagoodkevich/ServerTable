package model;

public class Lecturer {
	String name;
	String syrname;
	String secondName;
	String degree;
	String degreeName;
	String year;

	Lecturer(String name, String surname, String secondName) {
		this.setName(name, surname, secondName);
	}

	Lecturer(Lecturer lec) {
		this.setName(lec.getName(), lec.getSurname(), lec.getSecondName());
		this.setDegreeName(lec.getDegreeName());
		this.setDegree(lec.getDegree());
		this.setYear(lec.getYear());
	}

	Lecturer(String name, String surname, String secondName, String degree, String degreeName) {
		this.setName(name, surname, secondName);
		this.setDegree(degree);
		this.setDegreeName(degreeName);
	}

	public Lecturer(String name, String surname, String secondName, String degreeName, String degree, String year) {
		this.setName(name, surname, secondName);
		this.setDegreeName(degreeName);
		this.setDegree(degree);
		this.setYear(year);
	}
	
	public Lecturer(String name, String degreeName, String degree, String year) {
		 String[] fullName = name.split("\\s");
		 name = fullName[0];
		 String surname = fullName[1];
		 String secondName = fullName[2];
		this.setName(name, surname, secondName);
		this.setDegreeName(degreeName);
		this.setDegree(degree);
		this.setYear(year);
	}
	
	public void setName(String name, String surname, String secondName) {
		this.name = name;
		this.syrname = surname;
		this.secondName = secondName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getName() {
		return this.name;
	}

	public String getSurname() {
		return this.syrname;
	}

	public String getSecondName() {
		return this.secondName;
	}

	public String getDegreeName() {
		return this.degreeName;
	}

	public String getDegree() {
		return this.degree;
	}

	public String getYear() {
		return this.year;
	}
}
