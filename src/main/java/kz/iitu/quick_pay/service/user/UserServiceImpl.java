package kz.iitu.quick_pay.service.user;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.enitity.OrganizationUsersEntity;
import kz.iitu.quick_pay.enitity.Role;
import kz.iitu.quick_pay.enitity.UserEntity;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.exception.user.UserAlreadyExistsException;
import kz.iitu.quick_pay.exception.user.UserNotFoundException;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import kz.iitu.quick_pay.repository.OrganizationUsersRepository;
import kz.iitu.quick_pay.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    OrganizationUsersRepository organizationUsersRepository;
    OrganizationRepository organizationRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Long createUser(UserDto userDto) {
        // Checking if user with this username or email already exists
        boolean username = userRepository.existsByUsername(userDto.getUsername());
        boolean email = userRepository.existsByEmail(userDto.getEmail());

        if(username) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        if (email) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        UserEntity user = userRepository.save(
                UserEntity.builder()
                        .name(userDto.getName())
                        .surname(userDto.getSurname())
                        .isActive(true) // DEFAULT is_active is TRUE
                        .username(userDto.getUsername())
                        .email(userDto.getEmail())
                        .password(passwordEncoder.encode(userDto.getPassword()))
                        .role(userDto.getRoles()) // DEFAULT role is ROLE_USER
                        .build()
        );

        Optional<OrganizationEntity> organization = organizationRepository.findById(userDto.getOrganizationId());
        if (organization.isEmpty()) {
            throw new OrganizationNotFoundException("Organization not found with id " + userDto.getOrganizationId());
        }

        organizationUsersRepository.save(
                OrganizationUsersEntity.builder()
                        .user(user)
                        .organization(organization.get())
                        .build()
        );

        return user.getId();
    }

    @Transactional
    @Override
    public Page<UserDto> getUsers(
            int page,
            int limit,
            String sort,
            String order,
            String search,
            Long organization_id
    ) {
        Specification<UserEntity> spec = Specification.where(UserSpecification.hasSearch(search));

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

        return userRepository.findAll(spec, pageable).map(userEntity -> {
            Optional<OrganizationUsersEntity> organizationUser = organizationUsersRepository.findByUserId(userEntity.getId());
            if (organizationUser.isEmpty()) {
                throw new OrganizationNotFoundException("Organization not found for user with id " + userEntity.getId());
            }

            return UserDto.convertTo(userEntity, organizationUser.get().getOrganization());
        });
    }



    @Transactional
    @Override
    public UserDto getUserById(Long id) {

        UserEntity user = userRepository
                .findById(id)
                .orElseThrow(()->
                        new UserNotFoundException(String.format("User with id %s not found", id))
                );

        Optional<OrganizationUsersEntity> organizationUser = organizationUsersRepository.findByUserId(user.getId());
        if (organizationUser.isEmpty()) {
            throw new OrganizationNotFoundException("Organization not found for user with id " + user.getId());
        }

        return UserDto.convertTo(user, organizationUser.get().getOrganization());
    }

    @Override
    public UserDto getByUsername(String username) {
       UserEntity userEntity = userRepository.findByUsername(username)
               .orElseThrow(()->
                       new UserNotFoundException(String.format("User with username %s not found", username))
               );

       Optional<OrganizationUsersEntity> organizationUser = organizationUsersRepository.findByUserId(userEntity.getId());
         if (organizationUser.isEmpty()) {
              throw new OrganizationNotFoundException("Organization not found for user with id " + userEntity.getId());
         }

       return UserDto.convertTo(userEntity,organizationUser.get().getOrganization());

    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        UserEntity user = userRepository
                .findById(id)
                .orElseThrow(()->
                        new UserNotFoundException(String.format("User with id %s not found", id))
                );
        // Make user inactive
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long id, Map<String,Object> updates) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(()->
                        new UserNotFoundException(String.format("User with id %s not found", id))
                );

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    user.setName((String) value);
                    break;
                case "surname":
                    user.setSurname((String) value);
                    break;
                case "username":
                    user.setUsername((String) value);
                    break;
                case "email":
                    String email = (String) value;
                    if (!isValidEmail(email)) {
                        throw new IllegalArgumentException("Invalid email format: " + email);
                    }
                    user.setEmail((String) value);
                    break;
                case "is_active":
                   user.setActive((Boolean) value);
                    break;
                case "roles":
                    if (value instanceof List<?> rolesList) {
                        try {
                            List<Role> updatedRoles = rolesList.stream()
                                    .map(roleStr -> Role.valueOf(roleStr.toString()))
                                    .collect(Collectors.toCollection(ArrayList::new));
                            user.setRole(updatedRoles);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("Invalid role: " + value, e);
                        }
                    } else {
                        throw new IllegalArgumentException("Roles should be a list of strings");
                    }
                    break;
            }
        });


        UserEntity userEntity = userRepository.save(user);
        Optional<OrganizationUsersEntity> organizationUser = organizationUsersRepository.findByUserId(userEntity.getId());
        if (organizationUser.isEmpty()) {
            throw new OrganizationNotFoundException("Organization not found for user with id " + userEntity.getId());
        }

        return UserDto.convertTo(userEntity,organizationUser.get().getOrganization());
    }

    // Helper method to validate email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email != null && email.matches(emailRegex);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity  userEntity = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new UsernameNotFoundException("User with username " + username + " not found")
                );

        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().stream().map(Enum::name).toArray(String[]::new))
                .build();
    }
}
