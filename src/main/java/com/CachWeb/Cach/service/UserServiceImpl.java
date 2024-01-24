package com.CachWeb.Cach.service;

import com.CachWeb.Cach.dto.UserDto;
import com.CachWeb.Cach.entity.ExchangeRequest;
import com.CachWeb.Cach.entity.Role;
import com.CachWeb.Cach.entity.User;
import com.CachWeb.Cach.repository.RoleRepository;
import com.CachWeb.Cach.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private ExchangeRequestService exchangeRequestService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository
            ,ExchangeRequestService exchangeRequestService,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.exchangeRequestService=exchangeRequestService;
    }


    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhoneNumber(userDto.getPhoneNumber());

        Role role = roleRepository.findByName("ROLE_USER");

        if(role == null){
            role = checkRoleExist();
        }
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

    private Role checkRoleExist(){

//        String ROLE_ADMIN="ROLE_ADMIN";
//        Role role = new Role();
//        role.setName(ROLE_ADMIN);
//        roleRepository.save(role);
        String ROLE_USER="ROLE_ADMIN";
        Role role1 = new Role();
        role1.setName(ROLE_USER);

        return roleRepository.save(role1);
    }
}
