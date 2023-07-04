package jmaster.io.project3.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jmaster.io.project3.dto.PageDTO;
import jmaster.io.project3.dto.ResponseDTO;
import jmaster.io.project3.dto.RoleDTO;
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.service.RoleService;

@RestController
@RequestMapping("/admin/role")
public class RoleController {
	@Autowired 
	RoleService roleService;

	@PostMapping("/new")
	public ResponseDTO<Void> create(
			@RequestBody @Valid RoleDTO roleDTO) {
		roleService.create(roleDTO);
		return ResponseDTO.<Void>builder().status(200)
				.msg("ok").build();
	}
	
	@GetMapping("/list")
	public ResponseDTO<List<RoleDTO>> listRole() {
		List<RoleDTO> roleDTOs= roleService.getAll();
		return ResponseDTO.<List<RoleDTO>>builder().status(200)
				.msg("ok").data(roleDTOs).build();
	}
	
	
	@DeleteMapping("/delete/{id}") 
	public ResponseDTO<Void> delete(@PathVariable("id") int id) {
		roleService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("ok").build();
	}

	@PutMapping("/update")
	public ResponseDTO<Void> edit(@RequestBody @Valid RoleDTO roleDTO) {
		roleService.update(roleDTO);
		return ResponseDTO.<Void>builder().status(200)
				.msg("ok")
				.build();
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<RoleDTO>> search(
			@RequestBody @Valid SearchDTO searchDTO) {
		
		return ResponseDTO.<PageDTO<RoleDTO>>builder()
				.status(200)
				.data(roleService.search(searchDTO))
				.build();
	}
}
