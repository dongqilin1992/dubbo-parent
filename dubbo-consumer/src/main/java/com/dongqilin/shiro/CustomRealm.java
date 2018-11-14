package com.dongqilin.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: dongql
 * @date: 2018/3/30 12:23
 */
public class CustomRealm extends AuthorizingRealm {
    @Override
    public String getName() {
        return "customRealm";
    }

    // 支持什么类型的token
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) {

        // 从token中 获取用户身份信息
        String username = (String) token.getPrincipal();
        // 拿username从数据库中查询
        // ....
        // 如果查询不到则返回null
        if (!username.equals("zhang")) {// 这里模拟查询不到
            return null;
        }

        // 获取从数据库查询出来的用户密码
        String password = "123";// 这里使用静态数据模拟。。

        // 根据用户id从数据库取出菜单
        //...先用静态数据
        List<SysPermission> menus = new ArrayList<SysPermission>();;

        SysPermission sysPermission_1 = new SysPermission();
        sysPermission_1.setName("商品管理");
        sysPermission_1.setUrl("/item/queryItem.action");
        SysPermission sysPermission_2 = new SysPermission();
        sysPermission_2.setName("用户管理");
        sysPermission_2.setUrl("/user/query.action");

        menus.add(sysPermission_1);
        menus.add(sysPermission_2);

        // 构建用户身份信息
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserid(username);
        activeUser.setUsername(username);
        activeUser.setUsercode(username);
        activeUser.setMenus(menus);

        // 返回认证信息由父类AuthenticatingRealm进行认证
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                activeUser, password, getName());

        return simpleAuthenticationInfo;
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        // 获取身份信息
        ActiveUser activeUser = (ActiveUser) principals.getPrimaryPrincipal();
        //用户id
        String userid = activeUser.getUserid();
        // 根据用户id从数据库中查询权限数据
        // ....这里使用静态数据模拟
        List<String> permissions = new ArrayList<String>();
        permissions.add("item:query");
        permissions.add("item:update");

        // 将权限信息封闭为AuthorizationInfo

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (String permission : permissions) {
            simpleAuthorizationInfo.addStringPermission(permission);
        }

        return simpleAuthorizationInfo;
    }
}
