package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.utils.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate template;

    static final String KEY_PREFIX = "user:code:phone:";

    public Boolean checkUser(String data, Integer type) {
                User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
        }
                return userMapper.select(user)==null?true:false;
    }

    public Boolean sendVerifyCode(String phone) {

        String code = NumberUtils.generateCode(5);

       try{
           template.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
           return true;
       }catch (Exception e){
           return false;
       }

    }

    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        String codeCheck = template.opsForValue().get(key);
        if(!code.equals(codeCheck)){
            return false;
        }
        user.setCreated(new Date());
        user.setId(null);
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        int insert = userMapper.insert(user);
        if(insert==1){
            template.delete(key);
            return true;
        }else {
            return false;
        }
    }

    public User login(String username, String password) {
        User user = new User();
        user.setUsername(username);
        User user1 = userMapper.selectOne(user);
        if(user1==null){
            return null;
        }
        String pass = CodecUtils.md5Hex(password,user1.getSalt());
        if(user1.getPassword().equals(pass)){
            return user1;
        }
        return null;
    }
}
