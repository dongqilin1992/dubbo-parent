package com.dongqilin.web;

import com.dongqilin.entity.User;
import com.dongqilin.service.UserService;
import com.dongqilin.shiro.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: dongql
 * @date: 2017/10/11 17:57
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/getUser/{id}")
    public ModelAndView getUser(
            @PathVariable
                    Integer id) {
        User user = userService.findUserById(id);
        System.out.println(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("demo");
        return modelAndView;
    }

    @RequestMapping("/getRedisLockDubbo")
    public String getRedisLockDubbo() {
        userService.getRedisLockDubbo();
        return "success";
    }

    @RequestMapping("/getRedisLock")
    public String getRedisLock() {
        userService.getRedisLock();

        return "success";
    }

    @RequestMapping("/getZkLock")
    public String getZkLock() {
        userService.getZkLock();

        return "success";
    }

    @RequestMapping("/login")
    public String loginsubmit(Model model, HttpServletRequest request)
            throws Exception {
        String error = null;
        // shiro在认证过程中出现错误后将异常类路径通过request返回
        String exceptionClassName = (String) request
                .getAttribute("shiroLoginFailure");
        if (exceptionClassName != null) {
            if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
                error = "用户名/密码错误";
            } else if (IncorrectCredentialsException.class.getName().equals(
                    exceptionClassName)) {
                error = "用户名/密码错误";
            } else if ("randomCodeError".equals(exceptionClassName)) {
            } else {
                error = "其他错误：" + exceptionClassName;
            }
        }
        model.addAttribute("error", error);
        //如果service校验通过，将用户身份记录到session
        //session.setAttribute("activeUser", activeUser);
        //重定向到商品查询页面
        return "redirect:/first.action";
    }

    @RequestMapping("/first")
    public String first(Model model) throws Exception {

        //主体
        Subject subject = SecurityUtils.getSubject();
        //身份
        ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
        model.addAttribute("activeUser", activeUser);
        return "/first";
    }

    // 查询商品列表
    @RequestMapping("/queryItem")
    @RequiresPermissions("item:query")
    public ModelAndView queryItem() throws Exception {

        return null;
    }

    //清除缓存
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        //super.clearCache(principals);
    }
}
