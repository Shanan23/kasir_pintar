package id.dimas.kasirpintar.helper;


import android.util.Log;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailHelper {

    public static void sendEmail(String toEmail, String name, String otp) {
        String subject = "Kode OTP untuk Verifikasi Akun Anda";
        String body = "Dear " + name + ",\n" +
                "\n" +
                "Halo! Terima kasih telah memilih layanan kami. Untuk melindungi keamanan akun Anda, kami memberikan kode OTP untuk verifikasi.\n" +
                "\n" +
                "Kode OTP Anda adalah: " + otp +
                "\n\n" +
                "Mohon masukkan kode ini untuk menyelesaikan proses verifikasi.\n" +
                "\n" +
                "Jangan bagikan kode ini kepada siapapun, termasuk pihak layanan pelanggan kami. Kami hanya akan meminta Anda untuk memasukkan kode ini melalui situs resmi kami.\n" +
                "\n" +
                "Terima kasih atas kerja sama Anda.\n" +
                "\n" +
                "Hormat kami,\n" +
                "\n" +
                "Kasir Pintar";


        final String username = "gaagiigoo23@gmail.com"; // Your email address
        final String password = "nanNahs15"; // Your email password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Update this with your SMTP server
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            Log.d("sendEmail", "Email sent successfully.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
