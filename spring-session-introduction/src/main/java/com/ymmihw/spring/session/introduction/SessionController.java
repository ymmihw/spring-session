package com.ymmihw.spring.session.introduction;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
  @RequestMapping("/")
  public String helloAdmin() {
    return "hello admin";
  }
}
