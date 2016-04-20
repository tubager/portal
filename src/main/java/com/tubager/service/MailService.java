package com.tubager.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tubager.exception.UserExistException;
import com.tubager.utility.TokenCache;
import com.tubager.utility.Utility;

@Service
public class MailService {
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void restPassword(String email) throws RuntimeException{
		MimeMessage mail = javaMailSender.createMimeMessage();
		String uuid = Utility.getUuid();
		try{
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			System.out.println(email);
            helper.setTo(email);
            helper.setFrom("shihui8262@163.com");
            helper.setSubject("重置密码");
            helper.setText("<html><body><div>您已申请重置密码，如果不是本人操作，请忽略该邮件。请在24小时之内点击下面链接重置密码：</div><a href='http://www.tubager.com/resetpassword.html?resetid="+uuid+"'>重置密码</a><div>该邮件为自动生成，请勿直接回复。</div></body></html>", true);
		}catch (MessagingException e) {
            e.printStackTrace();
            throw new UserExistException(e.getMessage());
        } finally {}
        javaMailSender.send(mail);
        TokenCache.getInstance().set(uuid, email);
	}
	
	public void verifyMail(String email) throws RuntimeException{
		MimeMessage mail = javaMailSender.createMimeMessage();
		String uuid = Utility.getUuid();
		try{
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(email);
            helper.setFrom("shihui8262@163.com");
            helper.setSubject("邮箱验证");
            helper.setText("<html><body><div>您已申请验证邮箱，如果不是本人操作，请忽略该邮件。请在24小时之内点击下面链接验证邮箱：</div><a href='http://www.tubager.com/verifyemailpage.html?resetid="+uuid+"'>验证邮箱</a><div>该邮件为自动生成，请勿直接回复。</div></body></html>", true);
		}catch (MessagingException e) {
            e.printStackTrace();
            throw new UserExistException(e.getMessage());
        } finally {}
        javaMailSender.send(mail);
        TokenCache.getInstance().set(uuid, email);
	}
}
