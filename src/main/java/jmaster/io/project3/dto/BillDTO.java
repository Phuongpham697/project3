package jmaster.io.project3.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

//GRAPQL tham khao cai nay
@Data
public class BillDTO {
	private Integer id;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date createdAt;
	private UserDTO user;
	
//	@JsonManagedReference
	private List<BillItemDTO> billItems;
}
