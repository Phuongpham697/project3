package jmaster.io.project3.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jmaster.io.project3.dto.ProductDTO;
import jmaster.io.project3.dto.ResponseDTO;
import jmaster.io.project3.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService productService;

	// jackson - ve doc them chuan GRPC, GRAPHQL
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseDTO<ProductDTO> add(
			@ModelAttribute @Valid ProductDTO productDTO)
			throws IllegalStateException, IOException {
		if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
			final String UPLOAD_FOLDER = "D:/file/product/";

			if (!(new File(UPLOAD_FOLDER).exists())) {
				new File(UPLOAD_FOLDER).mkdirs();
			}

			String filename = productDTO.getFile().getOriginalFilename();
			// lay dinh dang file
			String extension = filename.substring(filename.lastIndexOf("."));
			// tao ten moi
			String newFilename = UUID.randomUUID().toString() + extension;

			File newFile = new File(UPLOAD_FOLDER + newFilename);

			productDTO.getFile().transferTo(newFile);

			productDTO.setImage(newFilename);// save to db
		}

		productService.create(productDTO);
		return ResponseDTO.<ProductDTO>builder().status(200).data(productDTO).build();
	}
//
//	@GetMapping("/search")
//	public ResponseDTO<PageDTO<CategoryDTO>> search(
//			@RequestParam(name = "name", required = false) String name,
//			@RequestParam(name = "size", required = false) Integer size,
//			@RequestParam(name = "page", required = false) Integer page
//			) {
//
//		size = size == null ? 10 : size;
//		page = page == null ? 0 : page;
//		name = name == null ? "" : name;
//
//		PageDTO<CategoryDTO> pageRS = 
//				categoryService.searchByName("%" + name + "%", page, size);
//
//		return ResponseDTO.<PageDTO<CategoryDTO>>builder()
//				.status(200)
//				.data(pageRS).build();
//	}
//
//	@GetMapping("/{id}") // 10
//	public ResponseDTO<CategoryDTO> get(@PathVariable("id") int id) {
//		CategoryDTO categoryDTO = categoryService.getById(id);
//		return ResponseDTO.<CategoryDTO>builder().status(200).data(categoryDTO).build();
//	}
//
//	@DeleteMapping("/{id}") // /1
//	public ResponseDTO<Void> delete(@PathVariable("id") int id) {
//		categoryService.delete(id);
//		return ResponseDTO.<Void>builder().status(200).build();
//	}
//
//	@PutMapping("/")
//	public ResponseDTO<Void> update(@RequestBody @Valid CategoryDTO category) {
//		categoryService.update(category);
//		return ResponseDTO.<Void>builder().status(200).build();
//	}
}
