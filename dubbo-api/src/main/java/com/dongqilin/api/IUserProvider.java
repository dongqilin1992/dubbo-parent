package com.dongqilin.api;

import com.dongqilin.entity.User;

/**
 * @description:
 * @author: dongql
 * @date: 2017/10/24 13:34
 */
public interface IUserProvider {
    User findUserById(Integer id);
    void findRedisLock();
}
