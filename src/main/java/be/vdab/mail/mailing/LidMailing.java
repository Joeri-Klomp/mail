package be.vdab.mail.mailing;

import be.vdab.mail.domain.Lid;
import be.vdab.mail.exceptions.KanMailNietZendenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
//Zodat Spring een bean maakt van de class
public class LidMailing {
    private final JavaMailSender sender;
    private final String emailAdresWebMaster;

    public LidMailing(JavaMailSender sender, @Value("${emailAdresWebMaster") String emailAdresWebMaster) {
        //om de waarde van emailAdresWebMaster te lezen uit application.properties
        this.sender = sender;
        this.emailAdresWebMaster = emailAdresWebMaster;
    }

    public void stuurMailNaRegistratie_ZonderOpmaak(Lid lid, String ledenURL) {
        try {
            var message = new SimpleMailMessage(); //SimpleMailMessage is een mail zonder opmaak
            //We vullen wat eigenschappen van de mail in:
            message.setTo(lid.getEmailAdres());
            message.setSubject("Geregistreerd");
            message.setText("Je bent nu lid. Je nummer is:" + lid.getId());
            //Verstuur de mail:
            sender.send(message);
        } catch (MailException ex) {
            throw new KanMailNietZendenException(ex);
        }
    }

    @Async
    public void stuurMailNaRegistratie(Lid lid, String ledenURL) {
        try {
            var message = sender.createMimeMessage(); //MimeMessage is een email met HTML opmaak
            var helper = new MimeMessageHelper(message); //Helper wordt gebruikt om de eigenschappen van de mail in te stellen
            helper.setTo(lid.getEmailAdres());
            helper.setSubject("Geregistreerd");
            var urlVanDeLidInfo = ledenURL + "/" + lid.getId();
            var tekst = "<h1>Je bent nu lid.</h1>Je nummer is:" + lid.getId() + "." + "Je ziet je info <a href='" + urlVanDeLidInfo + "'>hier</a>.";
            helper.setText(tekst, true); //true omdat we HTML elementen gebruiken in de mail
            sender.send(message);
        } catch (MailException | MessagingException ex) {
            throw new KanMailNietZendenException(ex);
        }
    }

    public void stuurMailMetAantalLeden(long aantalLeden) {
        try {
            var message = new SimpleMailMessage();
            message.setTo(emailAdresWebMaster);
            message.setSubject("Aantal Leden");
            message.setText(aantalLeden + " leden");
            sender.send(message);
        } catch (MailException ex) {
            throw new KanMailNietZendenException(ex);
        }
    }
}
