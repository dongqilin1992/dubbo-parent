package com.dongqilin.shiro;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Code.Ai on 16/8/10.
 * Description: 自定义拦截器,拦截需要登录验证的请求
 * 获取 header 中的 token 委托给 StatelessRealm 进行请求验证
 * 类似于 FormAuthenticationFilter，但是根据当前请求上下文信息每次请求时都要登录的认证
 * 过滤器。
 */
public class StatelessAuthcFilter extends AccessControlFilter {
    /**
     * The Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {

        //1、客户端生成的消息摘要
        String clientDigest = request.getParameter("token");

        //2、客户端传入的用户身份
        String username = request.getParameter("username");
        Map<String, String[]> params =
                new HashMap<String, String[]>(request.getParameterMap());
        // 生成无状态Token
        StatelessToken token = new StatelessToken(username, params, clientDigest);

        try {
            // 委托给Realm进行登录
            getSubject(request, response).login(token);

        } catch (Exception e) {
            e.printStackTrace();
            onLoginFail(request, response);

            return true;
        }
        return true;
    }

    /**
     * On login fail.
     * 登录失败时默认返回401状态码
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    private void onLoginFail(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("login error");
    }
}

