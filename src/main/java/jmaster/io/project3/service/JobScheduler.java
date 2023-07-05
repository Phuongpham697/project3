//package jmaster.io.project3.service;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import jmaster.io.project3.entity.Bill;
//import jmaster.io.project3.entity.User;
//import jmaster.io.project3.repo.BillRepo;
//import jmaster.io.project3.repo.UserRepo;
//
//@Component
//public class JobScheduler {
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
//	@Scheduled(fixedDelay = 1000 * 60 * 5)
//	public void sendAdminEmail() {
//		// chi gio hien tai - 5 phut
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MINUTE, -5);
//		Date date = cal.getTime();
//
//		List<Bill> bills = billRepo.searchByDate(date);
//
//		for (Bill b : bills) {
//			System.out.println(b.getId());
//
//			mailService.sendEmail("ADMIN@gmail.com", "BILL ID " + b.getId(), "aa");
//		}
//
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
//}
