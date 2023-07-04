package jmaster.io.project3.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jmaster.io.project3.dto.BillDTO;
import jmaster.io.project3.dto.BillStatisticDTO;
import jmaster.io.project3.dto.PageDTO;
import jmaster.io.project3.dto.ResponseDTO;
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.dto.UserDTO;
import jmaster.io.project3.service.BillService;
import jmaster.io.project3.service.UserService;

@RestController
public class BillController {

	@Autowired
	BillService billService;
	
	@Autowired
	UserService userService;

	// admin tao don cho khach hang
	/*
	 * {
		    "user": {
		        "id": 6
		    },
		    "billItems": [
		        {
		            "quantity":10,
		            "price": 5000,
		            "product": {
		                "id": 2
		            }
		        },
		        {
		            "quantity":5,
		            "price": 1000,
		            "product": {
		                "id": 3
		            }
		        }
		    ]
		}
	 */
	@PostMapping("/admin/bill")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseDTO<BillDTO> add(@RequestBody @Valid BillDTO billDTO) {
		billService.create(billDTO);
		return ResponseDTO.<BillDTO>builder().status(200).data(billDTO).build();
	}
	
	//tu tay khach hang tao don
	/*
	 * {
		    "billItems": [
		        {
		            "quantity":10,
		            "price": 5000,
		            "product": {
		                "id": 2
		            }
		        },
		        {
		            "quantity":5,
		            "price": 1000,
		            "product": {
		                "id": 3
		            }
		        }
		    ]
		}
	 */
	@PostMapping("/customer/bill")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseDTO<BillDTO> add(@RequestBody @Valid BillDTO billDTO,
			Principal p) {
//		SecurityContextHolder.getContext().getAuthentication().getPrincipal()
//		String username = p.getName();
//		UserDTO user = userService.findByUsername(username);
//		billDTO.setUser(user);
		
		billService.create(billDTO);
		return ResponseDTO.<BillDTO>builder().status(200).data(billDTO).build();
	}

	@GetMapping("/search")
	public ResponseDTO<PageDTO<BillDTO>> search(
			@RequestBody @Valid SearchDTO searchDTO) {
		return ResponseDTO.<PageDTO<BillDTO>>builder()
				.status(200)
				.data(billService.search(searchDTO))
				.build();
	}

	@GetMapping("/{id}") // 10
	public ResponseDTO<BillDTO> get(@PathVariable("id") int id) {
		BillDTO BillDTO = billService.getById(id);
		return ResponseDTO.<BillDTO>builder().status(200).data(BillDTO).build();
	}

	@GetMapping("/statistic") // 10
	public ResponseDTO<PageDTO<BillStatisticDTO>> get() {
		PageDTO<BillStatisticDTO> pageRS = billService.statistic();
		return ResponseDTO.<PageDTO<BillStatisticDTO>>builder().status(200).data(pageRS).build();
	}

	@DeleteMapping("/{id}") // /1
	public ResponseDTO<Void> delete(@PathVariable("id") int id) {
		billService.delete(id);
		return ResponseDTO.<Void>builder().status(200).build();
	}

	@PutMapping("/")
	public ResponseDTO<Void> update(@RequestBody @Valid BillDTO bill) {
		billService.update(bill);
		return ResponseDTO.<Void>builder().status(200).build();
	}
}
