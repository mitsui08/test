package cn.itcast.mydao;



public class SimpleJpaRep<T> implements JpaRep<T>{

	public void save(T t) {
		System.out.println("save");
	}
	
	public void findBy(T t) {
		System.out.println("ssss");
	}
}
