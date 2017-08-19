package com.example.transactionTest.controller;

import com.example.transactionTest.pojo.RUser;
import com.example.transactionTest.response.DefaultResp;
import com.example.transactionTest.service.RUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@RestController
@RequestMapping(value = "/users",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)     // 通过这里配置使下面的映射都在/users下，可去除
public class UserController {
   static Map<Long, RUser> users = Collections.synchronizedMap(new HashMap<Long, RUser>());

   @Autowired
   private RUserService rUserService;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public List<RUser> getUserList() {
        List<RUser> r = new ArrayList<RUser>(users.values());
        return r;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public DefaultResp postUser(@RequestBody RUser user) throws Exception {
        DefaultResp result = new DefaultResp();

            List<RUser> rUserList = null;
            rUserList = rUserService.insert(user);

        result.setCode("10001");
        result.setMessage("生成成功");
        result.setData(rUserList);

        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RUser getUser(@PathVariable Long id) {
        return users.get(id);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String putUser(@PathVariable Long id, @RequestBody RUser user) {
        RUser u = users.get(id);
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u);
        return "success";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id) {
        users.remove(id);
        return "success";
    }
}
