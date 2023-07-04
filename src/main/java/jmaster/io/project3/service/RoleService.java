package jmaster.io.project3.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jmaster.io.project3.dto.PageDTO;
import jmaster.io.project3.dto.RoleDTO;
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.entity.Role;
import jmaster.io.project3.repo.RoleRepo;

public interface RoleService {
	void create(RoleDTO roleDTO);

	void update(RoleDTO roleDTO);

	void delete(int id);
	
	List<RoleDTO> getAll();

	PageDTO<RoleDTO> search(SearchDTO searchDTO);



}

@Service
class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepo roleRepo;

	@Transactional
	public void create(RoleDTO roleDTO) {
		Role role = new ModelMapper().map(roleDTO, Role.class);
		roleRepo.save(role);
		roleDTO.setId(role.getId());
	}

	@Transactional
	public void update(RoleDTO roleDTO) {
		Role role = roleRepo.findById(roleDTO.getId()).orElseThrow(NoResultException::new);
		role.setName(roleDTO.getName());
		roleRepo.save(role);
	}

	@Transactional
	public void delete(int id) {
		roleRepo.deleteById(id);
	}

	public PageDTO<RoleDTO> search(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize());
		Page<Role> pageRS = roleRepo.searchByName(searchDTO.getKeyword(), pageable);

		return PageDTO.<RoleDTO>builder()
				.totalPages(pageRS.getTotalPages())
				.totalElements(pageRS.getTotalElements())
				.contents(pageRS.get()
						.map(r -> convert(r)).collect(Collectors.toList()))
				.build();
	}

	private RoleDTO convert(Role role) {
		return new ModelMapper().map(role, RoleDTO.class);
	}

	@Override
	public List<RoleDTO> getAll() {	
			List<Role> userList = roleRepo.findAll();
			return userList.stream().map(u -> convert(u))
					.collect(Collectors.toList());
		}

}
