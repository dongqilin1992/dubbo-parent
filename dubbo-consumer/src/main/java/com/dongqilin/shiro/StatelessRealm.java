package com.dongqilin.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Code.Ai on 16/8/10.
 * Description:
 */
public class StatelessRealm extends AuthorizingRealm {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(AuthenticationToken token) {
        // 仅支持StatelessToken类型的Token
        return token instanceof StatelessToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //根据用户名查找角色，请根据需求实现
        String username = (String) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo info    = new SimpleAuthorizationInfo();
        info.addRole("admin");
        info.addStringPermission("user");
        return info;
        //</editor-fold>
    }

    //登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        StatelessToken statelessToken = (StatelessToken) token;
        String username = statelessToken.getUsername();
        String key ="";
        if("admin".equals(username)) {
            key="123";
        }
        return new SimpleAuthenticationInfo(
                username,
                key,
                getName());
    }
}
