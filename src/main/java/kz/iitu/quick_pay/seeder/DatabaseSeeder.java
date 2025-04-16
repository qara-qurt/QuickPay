package kz.iitu.quick_pay.seeder;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.enitity.OrganizationUsersEntity;
import kz.iitu.quick_pay.enitity.Role;
import kz.iitu.quick_pay.enitity.UserEntity;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import kz.iitu.quick_pay.repository.OrganizationUsersRepository;
import kz.iitu.quick_pay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationUsersRepository organizationUsersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedOrganizations();
        seedUsers();
    }

    private void seedOrganizations() {
        if (organizationRepository.count() == 0) {
            OrganizationEntity org = OrganizationEntity.builder().name("QuickPay").bin("adminBIN").isActive(true).build();

            organizationRepository.saveAll(List.of(org));
            System.out.println("✅ Organizations seeded");
        }
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            Optional<OrganizationEntity> organization = organizationRepository.findById(1L);
            if (organization.isEmpty()) {
                System.out.println("⚠ Organization not found, skipping user seeding.");
                return;
            }

            UserEntity user = UserEntity.builder()
                    .name("Dias")
                    .surname("Serikov")
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("qaraqurt"))
                    .role(List.of(Role.ADMIN))
                    .isActive(true)
                    .build();

            userRepository.save(user);

            organizationUsersRepository.save(
                    OrganizationUsersEntity.builder()
                            .user(user)
                            .organization(organization.get())
                            .build()
            );

            System.out.println("✅ Users seeded");
        }
    }
}
