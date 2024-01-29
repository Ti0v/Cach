package com.CachWeb.Cach.service;

import com.CachWeb.Cach.Utils;
import com.CachWeb.Cach.dto.UserDto;
import com.CachWeb.Cach.email.EmailService;
import com.CachWeb.Cach.entity.ExchangeRequest;
import com.CachWeb.Cach.entity.PasswordReset;
import com.CachWeb.Cach.entity.Role;
import com.CachWeb.Cach.entity.User;
import com.CachWeb.Cach.repository.PasswordResetRepository;
import com.CachWeb.Cach.repository.RoleRepository;
import com.CachWeb.Cach.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private ExchangeRequestService exchangeRequestService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private PasswordResetRepository passwordResetRepository;

    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository
            , ExchangeRequestService exchangeRequestService,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, PasswordResetRepository passwordResetRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.exchangeRequestService=exchangeRequestService;
        this.passwordResetRepository = passwordResetRepository;
        this.emailService = emailService;
    }


    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFullName());
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhoneNumber(userDto.getPhoneNumber());
        Role role;
        if(userDto.getRoleName() != null) {
            role = roleRepository.findByName(userDto.getRoleName());
        } else role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public List<ExchangeRequest> getExchangeRequestsForUser(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getExchangeRequests();
        } else {
            // Handle user not found
            return Collections.emptyList();
        }
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void savePasswordResetToken(User user) {

        if (passwordResetRepository.existsByUserId(user.getId())){
            passwordResetRepository.deleteByUserId(user.getId());
        }

        final String token = UUID.randomUUID().toString();
        this.createPasswordResetToken(user, token);

        // Send Email
        String subject = "Password Reset";
        String content = "http://localhost:8001" + "/new-password?token=" + token;
        emailService.sendEmail(user.getEmail(), subject, content);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        final PasswordReset passwordResetToken = passwordResetRepository.findByToken(token);
        if (passwordResetToken == null) {
            return "INVALID_TOKEN";
        }
        final Calendar cal = Calendar.getInstance();
        if ((passwordResetToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            return "TOKEN_EXPIRED";
        }
        return "VALID_TOKEN";
    }

    @Override
    public User getUserByToken(String resetToken) {
        PasswordReset passwordReset = passwordResetRepository.findByToken(resetToken);
        return userRepository.findById(passwordReset.getUser().getId()).get();
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        try {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
        passwordResetRepository.deleteByUserId(user.getId());
    }

    public void createPasswordResetToken(User user, String token) {
        final PasswordReset myToken = new PasswordReset(token, user, OffsetDateTime.now(), user.getEmail());
        passwordResetRepository.save(myToken);
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String email) {
        User user=  userRepository.findByEmail(email);
        userRepository.delete(user);
    }

    @Override
    public void Save(ExchangeRequest exchangeRequest) {
        exchangeRequestService.Save(exchangeRequest);
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }
}
