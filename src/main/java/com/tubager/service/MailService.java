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
            helper.setSubject("��������");
            helper.setText("<html><body><div>���������������룬������Ǳ��˲���������Ը��ʼ�������24Сʱ֮�ڵ�����������������룺</div><a href='http://www.tubager.com/resetpassword.html?resetid="+uuid+"'>��������</a><div>���ʼ�Ϊ�Զ����ɣ�����ֱ�ӻظ���</div></body></html>", true);
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
            helper.setSubject("������֤");
            helper.setText("<html><body><div>����������֤���䣬������Ǳ��˲���������Ը��ʼ�������24Сʱ֮�ڵ������������֤���䣺</div><a href='http://www.tubager.com/verifyemailpage.html?resetid="+uuid+"'>��֤����</a><div>���ʼ�Ϊ�Զ����ɣ�����ֱ�ӻظ���</div></body></html>", true);
		}catch (MessagingException e) {
            e.printStackTrace();
            throw new UserExistException(e.getMessage());
        } finally {}
        javaMailSender.send(mail);
        TokenCache.getInstance().set(uuid, email);
	}
}
