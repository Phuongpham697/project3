package jmaster.io.project3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jmaster.io.project3.dto.BillDTO;
import jmaster.io.project3.dto.BillItemDTO;
import jmaster.io.project3.dto.BillStatisticDTO;
import jmaster.io.project3.dto.PageDTO;
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.entity.Bill;
import jmaster.io.project3.entity.BillItem;
import jmaster.io.project3.entity.User;
import jmaster.io.project3.repo.BillRepo;
import jmaster.io.project3.repo.ProductRepo;
import jmaster.io.project3.repo.UserRepo;

@Service
public class BillService {

	@Autowired
	BillRepo billRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	ProductRepo productRepo;

	@Transactional
	public void create(BillDTO billDTO) {
		User user = userRepo.findById(billDTO.getUser().getId()).orElseThrow(NoResultException::new);

		Bill bill = new Bill();
		bill.setUser(user);

		List<BillItem> billItems = new ArrayList<>();

		for (BillItemDTO billItemDTO : billDTO.getBillItems()) {
			BillItem billItem = new BillItem();
			billItem.setBill(bill);
			billItem.setProduct(
					productRepo.findById(billItemDTO.getProduct().getId()).orElseThrow(NoResultException::new));

			billItem.setPrice(billItemDTO.getPrice());
			billItem.setQuantity(billItemDTO.getQuantity());

			billItems.add(billItem);
		}

		bill.setBillItems(billItems);
		billRepo.save(bill);
	}

	@Transactional
	public void update(BillDTO billDTO) {
		User user = userRepo.findById(billDTO.getUser().getId()).orElseThrow(NoResultException::new);

		Bill bill = billRepo.findById(billDTO.getId()).orElseThrow(NoResultException::new);

//		bill.getBillItems().remove(0);//xoa het billitem
//		for (BillItemDTO billItemDTO : billDTO.getBillItems()) {
//			BillItem billItem = new BillItem();
//			billItem.setBill(bill);
//			billItem.setProduct(
//					productRepo.findById(billItemDTO.getProduct().getId()).orElseThrow(NoResultException::new));
//
//			billItem.setPrice(billItemDTO.getPrice());
//			billItem.setQuantity(billItemDTO.getQuantity());
//
//			bill.getBillItems().add(billItem);
//		}

		bill.setUser(user);

		billRepo.save(bill);
	}

	@Transactional
	public void delete(int id) {
		billRepo.deleteById(id);
	}

	public PageDTO<BillDTO> search(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize());

		Page<Bill> pageRS = billRepo.findAll(pageable);

		PageDTO<BillDTO> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(pageRS.getTotalPages());
		pageDTO.setTotalElements(pageRS.getTotalElements());

		// java 8 : lambda, stream
		List<BillDTO> billDTOs = pageRS.get().map(b -> new ModelMapper().map(b, BillDTO.class))
				.collect(Collectors.toList());

		pageDTO.setContents(billDTOs);// set vao pagedto
		return pageDTO;
	}

	public BillDTO getById(int id) { // java8, optinal
		Bill bill = billRepo.findById(id).orElseThrow(NoResultException::new);// java8 lambda
		return new ModelMapper().map(bill, BillDTO.class);
	}

	public PageDTO<BillStatisticDTO> statistic() {
		List<Object[]> list = billRepo.thongKeBill();

		PageDTO<BillStatisticDTO> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(1);
		pageDTO.setTotalElements(list.size());

		List<BillStatisticDTO> billStatisticDTOs = new ArrayList<>();

		for (Object[] arr : list) {
			BillStatisticDTO billStatisticDTO = new BillStatisticDTO((long) (arr[0]),
					String.valueOf(arr[1]) + "/" + String.valueOf(arr[2]));

			billStatisticDTOs.add(billStatisticDTO);
		}

		pageDTO.setContents(billStatisticDTOs);

		return pageDTO;
	}
}
