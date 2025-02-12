package springboot.emailsender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class EmailController {

    private final JavaMailSender javaMailSender;

    public EmailController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RequestMapping("/send-email")
    public String sendEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("elchibek.zh@gmail.com");
            message.setTo("elchibek.kamalov@gmail.com");
            message.setSubject("Simple test email!");
            message.setText("This is a sample email body for my test email!");

            javaMailSender.send(message);
            return "Success!";
        } catch (MailException e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/send-email-with-attachment")
    public String sendEmailWithAttachment() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("elchibek.zh@gmail.com");
            helper.setTo("elchibek.kamalov@gmail.com");
            helper.setSubject("Java email with attachment!");
            helper.setText("Please find the attached documents below!");

            helper.addAttachment("HELP.md",
                    new File("/Users/elchibek/Java/Email-Sender/HELP.md"));

            javaMailSender.send(message);
            return "Success!";
        } catch (MailException | MessagingException e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/send-html-email")
    public String sendHtmlEmail() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("elchibek.zh@gmail.com");
            helper.setTo("elchibek.kamalov@gmail.com");
            helper.setSubject("Java email with attachment!");

            try (var inputStream = Objects.requireNonNull(
                    EmailController.class.getResourceAsStream("/templates/email-content.html"))) {
                helper.setText(
                        new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),
                        true
                );

            }

            helper.addAttachment("HELP.md",
                    new File("/Users/elchibek/Java/Email-Sender/HELP.md"));

            javaMailSender.send(message);
            return "Success!";
        } catch (MailException | MessagingException | IOException e) {
            return e.getMessage();
        }
    }
}
