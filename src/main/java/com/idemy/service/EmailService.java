package com.idemy.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void sendOtpEmail(String to, String otp) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject("Idemy hesabı doğrulama kodunuz (OTP)");

            // Profesyonel (HTML) OTP taslağı
            String html = """
                    <div style="font-family: Arial, sans-serif; color:#111827;">
                      <h2 style="margin-bottom: 8px;">Idemy Hesap Doğrulama</h2>
                      <p style="margin-top: 0;">
                        Qeydiyyatdan keçdiyiniz hesab üçün doğrulama kodu aşağıdadır:
                      </p>
                      <div style="font-size: 28px; font-weight: 700; letter-spacing: 3px; margin: 18px 0;">
                        %s
                      </div>
                      <p style="color:#6b7280; margin-top: 0;">
                        Bu kodun geçerlilik süresi 5 dakikadır. Xahiş olunur kodu kimləsə paylaşmayın.
                      </p>
                    </div>
                    """.formatted(otp);

            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            throw new RuntimeException("OTP e-maila  göndərilə bilmədi.", ex);
        }
    }
}

