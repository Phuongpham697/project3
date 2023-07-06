package jmaster.io.project3.service;

import java.sql.Date;
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

//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.stereotype.Component;
//
//import jmaster.io.project3.entity.Bill;
//import jmaster.io.project3.entity.User;
//import jmaster.io.project3.repo.BillRepo;
//import jmaster.io.project3.repo.UserRepo;
//
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
	@Scheduled(cron = "0 35 14 * * *")
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
	
	
//	@Autowired
//	UserRepo userRepo;
//
//	@Autowired
//	BillRepo billRepo;
//
////	@Autowired
////	MailService mailService;
//
//	// Lên lịch quét 5 phút 1 lần, xem có đơn hàng mới ko, thì gửi mặc định về tài
//	// khoản email của mình,
//	// (Đơn hàng mới là ngày tạo > ngày hiện tại - 5 phút )
//	// gợi ý: Viết hàm jpql tìm bill theo buyDate > :date
	@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void sendAdminEmail() {
		// chi gio hien tai - 5 phut
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		java.util.Date date =  cal.getTime();

		List<Bill> bills = billRepo.searchByDate(date);
		log.info("hello");
		for (Bill b : bills) {
			System.out.println(b.getId());

			mailService.sendEmail("phuongphamdinh@gmail.com", "BILL ID " + b.getId(), "Co don hang moi");
		}
	}
//	}
//
//	// GUI EMAIL SINH NHAT
////	@Scheduled(fixedDelay = 5000)
//	// QUARZT SCHEDULER
//	// GIAY - PHUT - GIO - NGAY - THANG - THU
//	@Scheduled(cron = "0 0 9 * * *")
//	// https://crontab.guru/
//	public void sendEmail() {
//		System.out.println("HELLO JOB");
//		// chi lay ngay
//		Calendar cal = Calendar.getInstance();
//
//		List<User> users = userRepo.searchByBirthday(cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1);
//
//		for (User u : users) {
//			System.out.println(u.getName());
//			mailService.sendEmail(u.getEmail(), "HPBD", "aa");
//		}
//	}
}
