package jmaster.io.project3.service;

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
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.entity.Category;
import jmaster.io.project3.repo.CategoryRepo;

@Service
public class CategoryService {

	@Autowired
	CategoryRepo categoryRepo;

	@Transactional
	@CacheEvict(cacheNames = "category-search", allEntries = true)
	public void create(CategoryDTO categoryDTO) {
		Category category = new ModelMapper().map(categoryDTO, Category.class);
		categoryRepo.save(category);
		
		//tra ve idsau khi tao
		categoryDTO.setId(category.getId());
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
	public PageDTO<CategoryDTO> search(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize());

		Page<Category> pageRS = categoryRepo.searchByName(searchDTO.getKeyword(), pageable);

		return PageDTO.<CategoryDTO>builder()
				.totalPages(pageRS.getTotalPages())
				.totalElements(pageRS.getTotalElements())
				.contents(pageRS.get()
						.map(r -> convert(r)).collect(Collectors.toList()))
				.build();
	}

	@Cacheable(cacheNames = "categories", key = "#id", unless = "#result == null")
	public CategoryDTO getById(int id) { // java8, optinal
		Category category = categoryRepo.findById(id).orElseThrow(NoResultException::new);// java8 lambda
		return convert(category);
	}
	

	private CategoryDTO convert(Category category) {
		return new ModelMapper().map(category, CategoryDTO.class);
	}

}
