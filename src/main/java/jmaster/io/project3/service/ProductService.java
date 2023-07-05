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
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.dto.UserDTO;
import jmaster.io.project3.entity.Category;
import jmaster.io.project3.entity.Product;
import jmaster.io.project3.entity.User;
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
		productDTO.setId(product.getId());
	}

	public void update(ProductDTO productDTO) {
		Product product = productRepo.findById(productDTO.getId()).orElseThrow(NoResultException::new);
		product.setName(product.getName());
		productRepo.save(product);
	}

	public void delete(int id) {
		productRepo.deleteById(id);
	}

	public PageDTO<ProductDTO> search(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize());

		Page<Product> pageRS = productRepo.searchByName(searchDTO.getKeyword(), pageable);

		return PageDTO.<ProductDTO>builder()
				.totalPages(pageRS.getTotalPages())
				.totalElements(pageRS.getTotalElements())
				.contents(pageRS.get()
						.map(r -> convert(r)).collect(Collectors.toList()))
				.build();
	}
	
	public List<ProductDTO> getAll() {
		List<Product> listCategory = productRepo.findAll();
		return listCategory.stream().map(u -> convert(u))
				.collect(Collectors.toList());
	}
	
	private ProductDTO convert(Product product) {
		return new ModelMapper().map(product, ProductDTO.class);
	}
	
	public ProductDTO getById(int id) {
		Product product = productRepo.findById(id).orElseThrow(NoResultException::new);
		return new ModelMapper().map(product, ProductDTO.class);
	}
	
	

}
