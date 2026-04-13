package com.clinic.config;

import com.clinic.entity.Drug;
import com.clinic.entity.User;
import com.clinic.enums.Role;
import com.clinic.repository.DrugRepository;
import com.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DrugRepository drugRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedDrugs();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        User doctor = new User();
        doctor.setUsername("doctor1");
        doctor.setPassword(passwordEncoder.encode("doctor123"));
        doctor.setRole(Role.DOCTOR);
        userRepository.save(doctor);

        User nurse = new User();
        nurse.setUsername("nurse1");
        nurse.setPassword(passwordEncoder.encode("nurse123"));
        nurse.setRole(Role.NURSE);
        userRepository.save(nurse);

        log.info("Seeded default users: admin / doctor1 / nurse1");
    }

    private void seedDrugs() {
        if (drugRepository.count() > 0) return;

        String[][] drugs = {
            {"阿莫西林", "100", "片"},
            {"布洛芬",   "200", "片"},
            {"氯雷他定", "150", "片"},
            {"头孢克肟", "80",  "粒"},
            {"生理盐水", "50",  "瓶"}
        };
        for (String[] d : drugs) {
            Drug drug = new Drug();
            drug.setName(d[0]);
            drug.setStock(Integer.parseInt(d[1]));
            drug.setUnit(d[2]);
            drugRepository.save(drug);
        }
        log.info("Seeded {} drugs", drugs.length);
    }
}
