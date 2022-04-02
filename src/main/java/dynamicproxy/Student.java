package dynamicproxy;

public class Student implements Person{

	String name;

	public Student(String name) {
		this.name = name;
	}

	@Override
	public int putMoney(){
		System.out.println("student putMoney 50 yuan");
		return 50;
	}

	public void readName(){
		System.out.println("student name is " + name);
	}

}
