package app.finplan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.http.enabled}")
    private Boolean useHttp;

    @Value("${app.mail.http.key}")
    private String httpKey;

    public void sendConfirmationCode(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("Подтверждение регистрации");
        msg.setText("Ваш код подтверждения: " + code);
        if (useHttp) {
            sendHttp(msg);
        } else {
            mailSender.send(msg);
        }
    }

    public void sendResetLinkEmail(String to, String link) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("Ссылка на восстановление пароля");
        msg.setText(link);
        if (useHttp) {
            sendHttp(msg);
        } else {
            mailSender.send(msg);
        }
    }

    private void sendHttp(SimpleMailMessage msg) {
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(httpKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("from", msg.getFrom());
        body.put("to", List.of(msg.getTo()));
        body.put("subject", msg.getSubject());
        body.put("html", "<p>" + msg.getText() + "</p>");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        rest.postForEntity("https://api.resend.com/emails", request, String.class);
    }
}

