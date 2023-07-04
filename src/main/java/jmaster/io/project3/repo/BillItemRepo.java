package jmaster.io.project3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jmaster.io.project3.entity.BillItem;

public interface BillItemRepo extends JpaRepository<BillItem, Integer> {

}
