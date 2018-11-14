package com.dongqilin.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

/**
 * Created by Code.Ai on 16/8/10.
 * Description:获取客户端传入的用户名、请求参数、消息摘要，生成 StatelessToken；然后交给相应的
 Realm 进行认证
 */
public class StatelessToken implements AuthenticationToken {
    private static final long serialVersionUID = 7725129693827821256L;
    private String clientDigest;
    private Map<String, String[]> params;
    private String username;

    public StatelessToken(String clientDigest, Map<String, String[]> params, String username) {
        this.clientDigest = clientDigest;
        this.params = params;
        this.username = username;
    }

    public String getClientDigest() {
        return clientDigest;
    }

    public void setClientDigest(String clientDigest) {
        this.clientDigest = clientDigest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, String[]> getParams() {
        return params;
    }

    public void setParams(Map<String, String[]> params) {
        this.params = params;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return clientDigest;
    }
}
