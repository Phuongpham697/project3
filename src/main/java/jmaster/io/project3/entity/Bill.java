package jmaster.io.project3.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Bill extends TimeAuditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

//	@CreatedDate // tu gen
//	@Column(updatable = false)
//	private Date buyDate;
	
	private String status;//NEW, PENDING, ACTIVE

	@ManyToOne
	private User user;

	@OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, 
			orphanRemoval = true)
	private List<BillItem> billItems;
}
