package com.hai.controller;

import com.hai.base.BaseController;
import com.hai.bean.BaseResponse;
import com.hai.bean.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hai.bean.BaseResponse.CODE_SUCCESS;

/**
 * Created by 黄海 on 2017/4/10.
 */
@RestController
public class RetrofitController extends BaseController {

    @RequestMapping("getUser")
    public User getUser() {
        logger.info("getUser() called with: " + "");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User("huang", "hai");
    }

    /**
     * 服务延迟delaySec秒返回结果
     *
     * @return
     */
    @RequestMapping("getDelay/{delaySec}")
    public String getDelay(@PathVariable(name = "delaySec") int delaySec) {
        logger.info("getDelay() called with: delaySec=" + delaySec);
        try {
            TimeUnit.SECONDS.sleep(delaySec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "the server returns after " + delaySec + " second";
    }

    @RequestMapping("getValidatedUser")
    public BaseResponse<User> getValidatedUser(@RequestParam("userName") String userName, @RequestParam("passwrod") String passwrod) {
        logger.info("getValidatedUser() called with: " + "userName = [" + userName + "], passwrod = [" + passwrod + "]");
        return new BaseResponse<User>(CODE_SUCCESS, new User(userName, passwrod));
    }

    @RequestMapping("getValidatedUsers")
    public BaseResponse<List<User>> getValidatedUser(@RequestParam("userName") String[] userName) {
        List<User> users = new ArrayList<User>();
        for (String name : userName) {
            users.add(new User(name, "pwd123密码"));
        }
        return new BaseResponse<List<User>>(CODE_SUCCESS, users);
    }

    @RequestMapping("postUser")
    public User postUser(@RequestParam("name") String userName) {
        logger.info("postUser() called with: " + "userName = [" + userName + "]");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User(userName, "hai");
    }
}
