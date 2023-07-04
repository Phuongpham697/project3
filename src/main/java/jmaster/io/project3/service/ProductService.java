package jmaster.io.project3.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jmaster.io.project3.dto.CategoryDTO;
import jmaster.io.project3.dto.PageDTO;
import jmaster.io.project3.dto.ProductDTO;
import jmaster.io.project3.entity.Category;
import jmaster.io.project3.entity.Product;
import jmaster.io.project3.repo.CategoryRepo;
import jmaster.io.project3.repo.ProductRepo;

@Service
public class ProductService {

	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	CategoryRepo categoryRepo;


	@Transactional
	public void create(ProductDTO productDTO) {
		Category category = 
				categoryRepo.findById(productDTO.getCategory().getId()).orElseThrow(NoResultException::new);// java8 lambda
		
		Product product = new ModelMapper().map(productDTO, Product.class);
		product.setCategory(category);
		
		productRepo.save(product);
		
		//tra ve id sau khi tao
		productDTO.setId(product.getId());
	}

	@Transactional
	@Caching(evict = {
			@CacheEvict(cacheNames = "categories", key = "#categoryDTO.id"),
			@CacheEvict(cacheNames = "category-search", allEntries = true)
	})
	public void update(CategoryDTO categoryDTO) {
		Category category = categoryRepo.findById(categoryDTO.getId()).orElseThrow(NoResultException::new);
		category.setName(categoryDTO.getName());
		categoryRepo.save(category);
	}

	@Transactional
	@Caching(evict = {
			@CacheEvict(cacheNames = "categories", key = "#id"),
			@CacheEvict(cacheNames = "category-search", allEntries = true)
	})
	public void delete(int id) {
		categoryRepo.deleteById(id);
	}

	@Cacheable(cacheNames = "category-search")
	public PageDTO<CategoryDTO> searchByName(String name, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<Category> pageRS = categoryRepo.searchByName("%" + name + "%", pageable);

		PageDTO<CategoryDTO> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(pageRS.getTotalPages());
		pageDTO.setTotalElements(pageRS.getTotalElements());

		// java 8 : lambda, stream
		List<CategoryDTO> categoryDTOs = pageRS.get()
				.map(category -> new ModelMapper().map(category, CategoryDTO.class)).collect(Collectors.toList());

		pageDTO.setContents(categoryDTOs);// set vao pagedto
		return pageDTO;
	}

	@Cacheable(cacheNames = "categories", key = "#id", unless = "#result == null")
	public CategoryDTO getById(int id) { // java8, optinal
		Category category = categoryRepo.findById(id).orElseThrow(NoResultException::new);// java8 lambda
		return new ModelMapper().map(category, CategoryDTO.class);
	}

}
