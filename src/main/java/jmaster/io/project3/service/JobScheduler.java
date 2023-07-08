package jmaster.io.project3.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jmaster.io.project3.entity.Bill;
import jmaster.io.project3.entity.User;
import jmaster.io.project3.repo.BillRepo;
import jmaster.io.project3.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class JobScheduler {

	@Autowired
	MailService mailService;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	BillRepo billRepo;
	
	//Happy birthday today
	@Scheduled(cron = "0 45 16 * * *")
	public void cron() {
		log.info("hello");
		Calendar calendar= Calendar.getInstance();
		int date= calendar.get(Calendar.DATE);
		int month= calendar.get(Calendar.MONTH) +1;		
		List<User> listUsers= userRepo.searchByBirthday(date, month);
		for(User u:listUsers) {
			mailService.sendBirthday(u.getEmail());
			log.info("happy birthday:" +u.getName());
		}
		
	}
	
	
	@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void sendAdminEmail() {
		// chi gio hien tai - 5 phut
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		java.util.Date date =  cal.getTime();

		List<Bill> bills = billRepo.searchByDate(date);
		for (Bill b : bills) {
			System.out.println(b.getId());

			mailService.sendEmail("phuongphamdinh@gmail.com", "BILL ID " + b.getId(), "Co don hang moi");
		}
	}
	

}
