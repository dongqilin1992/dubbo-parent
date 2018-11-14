package com.dongqilin.provider.impl;

import com.dongqilin.api.IUserProvider;
import com.dongqilin.entity.User;
import com.dongqilin.mapper.UserMapper;
import com.dongqilin.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: dongql
 * @date: 2017/10/24 13:36
 */
//@Service(interfaceClass = IUserProvider.class)
public class UserProviderImpl implements IUserProvider {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(Integer id) {
        System.out.println("into UserProviderImpl.......");
        return userMapper.selectByPrimaryKey(id);
       // return new User();
    }

    @Override
    public void findRedisLock() {
        RLock lock = RedissonLock.getRedisson().getLock("TEST_KEY");

        try {
            boolean res = lock.tryLock(10, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}
