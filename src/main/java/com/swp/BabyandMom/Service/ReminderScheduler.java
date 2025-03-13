package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.Entity.Reminder;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Repository.ReminderRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ReminderScheduler {
    private final ReminderRepository reminderRepository;
    private final JavaMailSender mailSender;

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Ho_Chi_Minh")
    public void sendReminderEmails() throws MessagingException {
        LocalDate today = LocalDate.now();

        List<Reminder> reminders = reminderRepository.findByReminderDateTimeBetween(
                today.atStartOfDay(),
                today.atTime(23, 59, 59)
        );

        for (Reminder reminder : reminders) {
            sendEmail(reminder);
        }
    }

    private void sendEmail(Reminder reminder) throws MessagingException {
        User user = reminder.getPregnancy().getUser();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String htmlContent = """
                <html>
                <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
                    <div style="max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);">
                        <div style="text-align: center; padding: 10px 0; background: #007bff; color: #ffffff; font-size: 20px; border-radius: 8px 8px 0 0;">
                            📅 Hi\s""" + user.getFullName() + "," + """
                </div>
                <div style="padding: 20px; text-align: center;">
                    <p style="font-size: 24px; font-weight: bold; color: #333; margin: 10px 0;">
                        """ + reminder.getTitle() + """
                </p>
                <p style="font-size: 16px; color: #555; margin: 10px 0;">
                    """ + reminder.getDescription() + """
                            </p>
                        </div>
                        <div style="text-align: center; padding: 10px; font-size: 14px; color: #777;">
                            Thank you for using our services! 
                        </div>
                    </div>
                </body>
                </html>
                """;

        helper.setTo(user.getEmail());
        helper.setSubject(reminder.getType().name().replace("_", " ") + " Reminder");
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}

