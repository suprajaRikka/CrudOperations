package com.ssmsconnect.demoSpringJpaCrud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.classic.Logger;

@RestController
public class Product1Controller {

    @Autowired
    Product1Service service;

    Logger logger = (Logger) LoggerFactory.getLogger(Product1Controller.class);

    
    @GetMapping("/products")
    
    public ResponseEntity<List<Product1>> getProducts() {
        List<Product1> products = service.getproducts();
        logger.info("INFO MESSAGE");
        logger.error("Error MESSAGE");
        logger.warn("Warn MESSAGE");
        logger.trace("Debug MESSAGE");
        
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product1> getProductById(@PathVariable int id) {
        Product1 product = service.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	/*
	 * @GetMapping("/data") public ResponseEntity<Product1> getAllData() { Product1
	 * product = (Product1) service.getproducts(); return
	 * ResponseEntity.ok(product); }
	 */


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a CSV file");
        }

        if (!file.getContentType().equals("text/csv")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only CSV files are allowed");
        }

        List<Product1> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                Product1 product = new Product1();
                product.setId(Integer.parseInt(data[0].trim()));
                product.setName(data[1].trim());
                product.setPrice(Integer.parseInt(data[2].trim()));
                products.add(product);
            }
            service.saveAll(products);
            return ResponseEntity.ok("File uploaded and data saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    @PostMapping("/products")
    public ResponseEntity<String> addProduct(@RequestBody Product1 product) {
        service.addProduct(product);
        return new ResponseEntity<>("Product added successfully!", HttpStatus.CREATED);
    }

    @PutMapping("/products")
    public ResponseEntity<String> updateProduct(@RequestBody Product1 product) {
        service.updateProduct(product);
        return new ResponseEntity<>("Product updated successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        if (service.getProductById(id) == null) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        service.deleteProduct(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }
}
