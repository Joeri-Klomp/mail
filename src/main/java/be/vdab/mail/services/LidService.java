package be.vdab.mail.services;

import be.vdab.mail.domain.Lid;
import be.vdab.mail.mailing.LidMailing;
import be.vdab.mail.repositories.LidRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LidService {
    private final LidRepository lidRepository;
    private final LidMailing lidMailing;

    public LidService(LidRepository lidRepository, LidMailing lidMailing) {
        this.lidRepository = lidRepository;
        this.lidMailing = lidMailing;
    }

    @Transactional
    public void registreer(Lid lid, String ledenURL) {
        lidRepository.save(lid);
        lidMailing.stuurMailNaRegistratie(lid, ledenURL);
    }

    public Optional<Lid> findById(long id) {
        return lidRepository.findById(id);
    }

    @Scheduled(fixedRate = 60_000)
    //wordt elke minuut gestuurd
    public void stuurMailMetAantalLeden() {
        lidMailing.stuurMailMetAantalLeden(lidRepository.count());
    }
}
