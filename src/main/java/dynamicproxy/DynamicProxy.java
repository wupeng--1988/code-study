package dynamicproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class DynamicProxy {

	public static void main(String[] args){
		Student wusp = new Student("wusp");

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Student.class);
		Class<?>[] infs = new Class<?>[1];
		infs[0] = Person.class;
		enhancer.setInterfaces(infs);
		enhancer.setCallback(new StudentMethodInterceptor(wusp));

		Class[] argumentTypes = new Class[1];
		argumentTypes[0] = String.class;
		Object[] arguments = new Object[1];
		arguments[0] = "wusp1";
		Student proxy = (Student)enhancer.create(argumentTypes, arguments);
		System.out.println(proxy.putMoney());
		proxy.readName();
	}


	static class StudentMethodInterceptor implements MethodInterceptor{

		Student student;

		public StudentMethodInterceptor(Student student) {
			this.student = student;
		}

		@Override
		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			System.out.println("proxy intercept method: " + method.getName());
			return method.invoke(student, objects);
		}
	}

}
