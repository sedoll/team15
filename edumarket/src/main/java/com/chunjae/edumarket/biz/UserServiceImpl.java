package com.chunjae.edumarket.biz;

import com.chunjae.edumarket.entity.Euser;
import com.chunjae.edumarket.per.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<Euser> getUserList() {
        return userMapper.getUserList();
    }

    @Override
    public Euser getUser(String name) {
        Euser euser = userMapper.getUser(name);
        return euser;
    }

    @Override
    public int getWithdraw(Integer id) {
        return userMapper.getWithdraw(id);
    }

    @Override
    public int getActivate(String name) {
        return userMapper.getActivate(name);
    }

    @Override
    public int getDormant(String name) {
        return userMapper.getDormant(name);
    }

    @Override
    public Euser getByEmail(String email) {
        return userMapper.getByEmail(email);
    }

    @Override
    public Euser getByName(String name) {
        return userMapper.getByName(name);
    }

    @Override
    public Euser findById(String email, String tel) {
        return userMapper.findById(email, tel);
    }

    @Override
    public Euser findByPw(String email, String tel, String name) {
        return userMapper.findByPw(email, tel, name);
    }

    @Override
    public int userJoin(Euser euser) {
        euser.setPassword(passwordEncoder.encode(euser.getPassword()));
        return userMapper.userJoin(euser);
    }

    @Override
    public int updateUser(Euser euser) {
        euser.setPassword(passwordEncoder.encode(euser.getPassword()));
        return userMapper.updateUser(euser);
    }

    @Override
    public int updateLevel(String name, String lev) {
        return userMapper.updateLevel(name, lev);
    }

    @Override
    public int removeUser(String name) { return userMapper.removeUser(name);    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    @Override
    public Euser getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public Euser getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    @Override
    public int updatePasswordNoChange(Euser user) {
        return userMapper.updatePasswordNoChange(user);
    }
}
