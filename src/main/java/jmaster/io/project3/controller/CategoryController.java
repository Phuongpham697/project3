package jmaster.io.project3.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jmaster.io.project3.dto.CategoryDTO;
import jmaster.io.project3.dto.PageDTO;
import jmaster.io.project3.dto.ResponseDTO;
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.service.CategoryService;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;


	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseDTO<CategoryDTO> add(@RequestBody @Valid CategoryDTO category) {
		categoryService.create(category);
		return ResponseDTO.<CategoryDTO>builder().status(200).data(category).build();
	}
	
	@GetMapping("/list")
	public ResponseDTO<List<CategoryDTO>> listCategory() {
			List<CategoryDTO> listCategoryDTOs=categoryService.getAll();
			return ResponseDTO.<List<CategoryDTO>>builder().status(200).data(listCategoryDTOs).build();
	}
	
	@GetMapping("/search")
	public ResponseDTO<PageDTO<CategoryDTO>> search(
@RequestBody @Valid SearchDTO searchDTO) {
		
		return ResponseDTO.<PageDTO<CategoryDTO>>builder()
				.status(200)
				.data(categoryService.search(searchDTO))
				.build();
	}

	@GetMapping("/{id}") 
	public ResponseDTO<CategoryDTO> get(@PathVariable("id") int id) {
		CategoryDTO categoryDTO = categoryService.getById(id);
		return ResponseDTO.<CategoryDTO>builder().status(200).data(categoryDTO).build();
	}

	@DeleteMapping("/delete/{id}")
	public ResponseDTO<Void> delete(@PathVariable("id") int id) {
		categoryService.delete(id);
		return ResponseDTO.<Void>builder().status(200).build();
	}

	@PutMapping("/update")
	public ResponseDTO<Void> update(@RequestBody @Valid CategoryDTO category) {
		categoryService.update(category);
		return ResponseDTO.<Void>builder().status(200).build();
	}
}
