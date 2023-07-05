package jmaster.io.project3.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jmaster.io.project3.dto.PageDTO;
import jmaster.io.project3.dto.ProductDTO;
import jmaster.io.project3.dto.ResponseDTO;
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.dto.UserDTO;
import jmaster.io.project3.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService productService;

	@PostMapping("/new")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseDTO<ProductDTO> add(
			@ModelAttribute @Valid ProductDTO productDTO)
			throws IllegalStateException, IOException {
		if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
			final String UPLOAD_FOLDER = "D:/product/";

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
	
	@PostMapping("/update")
	public ResponseDTO<ProductDTO> update(
			@ModelAttribute @Valid ProductDTO productDTO)
			throws IllegalStateException, IOException {
		    if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
			final String UPLOAD_FOLDER = "D:/product/";

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

			productDTO.setImage(newFilename);
		}

		productService.update(productDTO);
		return ResponseDTO.<ProductDTO>builder().status(200).data(productDTO).build();
	}
	
	@GetMapping("/list")
	public ResponseDTO<List<ProductDTO>> lisUsers(){
		List<ProductDTO> listpProductDTOs = productService.getAll();
		return ResponseDTO.<List<ProductDTO>>builder().status(200).data(listpProductDTOs).build();
		
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<ProductDTO>> search(
			@RequestBody @Valid SearchDTO searchDTO) {
		return ResponseDTO.<PageDTO<ProductDTO>>builder()
				.status(200)
				.data(productService.search(searchDTO))
				.build();
	}

	@GetMapping("/{id}")
	public ResponseDTO<ProductDTO> get(@PathVariable("id") int id) {
		ProductDTO productDTO = productService.getById(id);
		return ResponseDTO.<ProductDTO>builder().status(200).data(productDTO).build();
	}

	@DeleteMapping("/{id}") 
	public ResponseDTO<Void> delete(@PathVariable("id") int id) {
		productService.delete(id);
		return ResponseDTO.<Void>builder().status(200).build();
	}

}
