package site.thaiky.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.thaiky.entity.User;
import site.thaiky.repository.AuthenticationRepository;


@Service
public class AuthenticationService {
    @Autowired
    AuthenticationRepository authenticationRepository;
    public User register (User user) {
        //xử lý logic
        // kiểm tra user đã tồn tại hay chưa

        //lưu xuống databse
        User newUser = authenticationRepository.save(user);
        return newUser;
    }

}
