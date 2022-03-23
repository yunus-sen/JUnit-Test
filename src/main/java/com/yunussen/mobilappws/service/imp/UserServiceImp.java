package com.yunussen.mobilappws.service.imp;

import com.yunussen.mobilappws.exception.UserServiceException;
import com.yunussen.mobilappws.io.entity.PasswordResetTokenEntity;
import com.yunussen.mobilappws.io.entity.RoleEntity;
import com.yunussen.mobilappws.io.entity.UserEntity;
import com.yunussen.mobilappws.io.repository.PasswordResetTokenRepository;
import com.yunussen.mobilappws.io.repository.RoleRepository;
import com.yunussen.mobilappws.io.repository.UserRepository;
import com.yunussen.mobilappws.security.UserPrincipal;
import com.yunussen.mobilappws.service.UserService;
import com.yunussen.mobilappws.shared.Utils;
import com.yunussen.mobilappws.shared.dto.AdressDto;
import com.yunussen.mobilappws.shared.dto.UserDto;
import com.yunussen.mobilappws.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    //@Autowired
    //AmazonSES amazonSES;

    public UserDto createUser(UserDto user) {

        if (userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("record already exists.");

        for (int i = 0; i < user.getAddresses().size(); i++) {
            AdressDto address = user.getAddresses().get(i);
            address.setUserDetails(user);
            address.setAddressId(utils.generateAddressId(30));
            user.getAddresses().set(i, address);
        }
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        String publicUserId = utils.generateUserId(32);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));

        userEntity.setEmailVerificationStatus(Boolean.TRUE);
        userEntity.setUserId(publicUserId);

        RoleEntity roleEntity = roleRepository.findByName(user.getRole());
        if (roleEntity != null) {
            userEntity.setRoles(Arrays.asList(roleEntity));
        }
        UserEntity storeadUserDetails = userRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(storeadUserDetails, UserDto.class);

        //amazonSES.verifyEmail(returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity==null)throw new UsernameNotFoundException("user not found with email. "+email);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new UserPrincipal(userEntity);
    }

    @Override
    public UserDto findUserByid(String id) {
        UserEntity storeadUser = userRepository.findByUserId(id);
        if (storeadUser == null) throw new UsernameNotFoundException(id + "is not found");
        UserDto returnValue = modelMapper.map(storeadUser, UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserDto returnValue = new UserDto();
        UserEntity storeadUser = userRepository.findByUserId(userId);
        if (storeadUser == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        storeadUser.setFirstName(user.getFirstName());
        storeadUser.setLastName(user.getLastName());

        UserEntity userEntity = userRepository.save(storeadUser);
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public void delete(String userId) {
        UserEntity userStoread = userRepository.findByUserId(userId);
        if (userStoread == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userRepository.delete(userStoread);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<UserDto>();
        Pageable pageable = PageRequest.of(page, limit);
        Page<UserEntity> allUsers = userRepository.findAll(pageable);
        List<UserEntity> users = allUsers.getContent();

        for (UserEntity userEntity : users) {
            UserDto userModel = new UserDto();
            BeanUtils.copyProperties(userEntity, userModel);
            returnValue.add(userModel);
        }
        return returnValue;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        // Find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hastokenExpired = Utils.hasTokenExpired(token);
            if (!hastokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }
        return returnValue;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        boolean returnValue = false;
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            return returnValue;
        }

        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);
        return Boolean.TRUE;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        boolean returnValue = false;

        if (Utils.hasTokenExpired(token)) {
            return returnValue;
        }
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);
        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        // Prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        // Update User password in database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        // Verify if password was saved successfully
        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }

        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);
        return returnValue;
    }
}
