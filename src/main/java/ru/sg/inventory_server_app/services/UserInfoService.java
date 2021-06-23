package ru.sg.inventory_server_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sg.inventory_server_app.models.UserInfo;
import ru.sg.inventory_server_app.repositories.UserInfoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserInfo createUser(UserInfo userInfo) {
        Optional<UserInfo> userInfoByEmail = userInfoRepository.findUserInfoByEmail(userInfo.getEmail());
        if (userInfoByEmail.isPresent()) {
            throw new IllegalStateException("The email address is already taken.");
        }
        return userInfoRepository.save(userInfo);
    }

    public List<UserInfo> getUsers() {
        return userInfoRepository.findAll(Sort.by("id"));
    }

    public Optional<UserInfo> getUser(Long id) {
        return userInfoRepository.findById(id);
    }

    public UserInfo updateUser(UserInfo updatedUserInfo, Long id) {
        boolean idExist = userInfoRepository.existsById(id);
        if (!idExist) {
            throw new IllegalStateException("User with id " + id + " does not exist!");
        }

        Optional<UserInfo> userInfoByEmail = userInfoRepository.findUserInfoByEmail(updatedUserInfo.getEmail());
        if (userInfoByEmail.isPresent()) {
            throw new IllegalStateException("The email address is already taken.");
        }

        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(id);
        UserInfo oldUserInfo = optionalUserInfo.get();

        oldUserInfo.setLName(updatedUserInfo.getLName());
        oldUserInfo.setFName(updatedUserInfo.getFName());
        oldUserInfo.setMName(updatedUserInfo.getMName());
        oldUserInfo.setPosition(updatedUserInfo.getPosition());
        oldUserInfo.setPhone(updatedUserInfo.getPhone());
        oldUserInfo.setEmail(updatedUserInfo.getEmail());
        oldUserInfo.setAdditionalData(updatedUserInfo.getAdditionalData());

        return userInfoRepository.save(oldUserInfo);
    }

    public void deleteUser(Long id) {
        boolean exist = userInfoRepository.existsById(id);
        if (!exist) {
            throw new IllegalStateException("User with id " + id + " does not exist!");
        }
        userInfoRepository.deleteById(id);
    }
}
