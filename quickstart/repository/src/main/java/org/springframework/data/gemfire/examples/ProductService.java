package org.springframework.data.gemfire.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.examples.domain.Product;
import org.springframework.data.gemfire.examples.repository.ProductRepository;
import org.springframework.util.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author David Turanski
 */
public class ProductService {

	private static Log log = LogFactory.getLog(ProductService.class);
	@Autowired
	ProductRepository productRepository;

	/**
	 * Create a new product
	 */
	public Product createProduct(Product product) {
		Assert.notNull(product, "product cannot be null");
		Assert.notNull(product.getId(), "product ID cannot be null");
		Assert.hasText(product.getName(), "product name must contain text");

		log.debug("creating new product " + product.getId() + " " + product.getName());
		return productRepository.save(product);
	}

	/**
	 * Delete a product for a given id
	 */
	public boolean deleteProduct(long id) {

		Product product = productRepository.findById(id).orElse(null);

		if (product != null) {
			productRepository.delete(product);
			log.debug("deleted product " + product.getId() + ":" + product.getName());
			return true;
		}

		log.warn("product not found for id " + id);

		return false;
	}

	/**
	 * Delete a product
	 */
	public boolean deleteProduct(Product product) {
		Assert.notNull(product, "product cannot be null");
		Assert.notNull(product.getId(), "product ID cannot be null");
		return deleteProduct(product.getId());
	}

	/**
	 * return a list of products containing a description
	 */
	public List<Product> findProductsByDescription(String description) {
		return productRepository.findByDescriptionContaining(description);
	}

	/**
	 * return a list of products with the given name
	 */
	public List<Product> findProductsByName(String name) {
		return productRepository.findByName(name);
	}

	/**
	 * return a list of products with a given attribute value
	 *
	 * @param key - the attribute name
	 * @param value - the value
	 */
	public List<Product> findProductsByAttribute(String key, String value) {
		return productRepository.findByAttributes(key, value);
	}

	/**
	 * Return all products
	 */
	public List<Product> findAllProducts() {
		List<Product> products = new ArrayList<Product>();
		for (Product p : productRepository.findAll()) {
			products.add(p);
		}
		return Collections.unmodifiableList(products);
	}
}

