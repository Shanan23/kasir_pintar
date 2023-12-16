package id.dimas.kasirpintar.helper;


import static java.net.HttpURLConnection.HTTP_OK;

import android.util.Log;

import org.json.JSONObject;

import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailHelper {
    //APi Key : 95a18cc38ef64db6370aeffb2561ce4c-07f37fca-ca59499c
    //sanbox : https://api.mailgun.net/v3/sandbox9098277bd371485db93041d4f7f2f601.mailgun.org/
    public static void sendSimpleMessage(String toEmail, String name, String otp) {

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

        RetrofitClient.getInstance()
                .getApi()
                .sendEmail("gaagiigoo23@gmail.com", toEmail, subject, body)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == HTTP_OK) {
                            try {
                                JSONObject obj = new JSONObject(response.body().string());
                                Log.d("sendSimpleMessage", obj.getString("message"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("sendSimpleMessage", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("sendSimpleMessage", t.getMessage());

                    }
                });
    }

//    public static void sendEmail(String toEmail, String name, String otp) {
//
//        String subject = "Kode OTP untuk Verifikasi Akun Anda";
//        String body = "Dear " + name + ",\n" +
//                "\n" +
//                "Halo! Terima kasih telah memilih layanan kami. Untuk melindungi keamanan akun Anda, kami memberikan kode OTP untuk verifikasi.\n" +
//                "\n" +
//                "Kode OTP Anda adalah: " + otp +
//                "\n\n" +
//                "Mohon masukkan kode ini untuk menyelesaikan proses verifikasi.\n" +
//                "\n" +
//                "Jangan bagikan kode ini kepada siapapun, termasuk pihak layanan pelanggan kami. Kami hanya akan meminta Anda untuk memasukkan kode ini melalui situs resmi kami.\n" +
//                "\n" +
//                "Terima kasih atas kerja sama Anda.\n" +
//                "\n" +
//                "Hormat kami,\n" +
//                "\n" +
//                "Kasir Pintar";
//
//
//        final String username = "gandharyanto@gmail.com"; // Your email address
//        final String password = "nanNahs2301"; // Your email password
//
//        Properties props = new Properties();
//        props.put("mail.smtps.host", "smtp.mailgun.org");
//        props.put("mail.smtps.auth", "true");
//        try {
//            Session session = Session.getInstance(props, null);
//
//            SMTPTransport t =
//                    (SMTPTransport) session.getTransport("smtps");
//            t.connect("smtp.mailgun.org", username, password);
//
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//            message.setSubject(subject);
//            message.setText(body);
//            t.sendMessage(message, message.getAllRecipients());
//
//            Log.d("sendEmail", "Email sent successfully.");
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
