package com.ssmsconnect.demoSpringJpaCrud;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
@Service
public class Product1Service {

//	List<Product1> prod = new ArrayList<>(Arrays.asList(
//			new Product1(1,"Lenovo",1000),
//			new Product1(2,"Dell",800),
//			new Product1(3,"HP",1200)));
	@Autowired
	private Repository repo;


	public List<Product1> getproducts(){
		return repo.findAll();
	}

	public Product1 getProductById(int id) {
		// TODO Auto-generated method stub
		return repo.findById(id).orElse(null);
	}
	
	public void addProduct(Product1 product) {
		repo.save(product);
	}

	public void updateProduct(Product1 product) {
		repo.save(product);
	}

	public void deleteProduct(int id) {
		repo.deleteById(id);
	}
	@Transactional
	public void saveAll(List<Product1> products) {
		// TODO Auto-generated method stub
		repo.saveAll(products);
	}
}
