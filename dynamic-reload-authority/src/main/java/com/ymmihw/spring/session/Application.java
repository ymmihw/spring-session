package com.ymmihw.spring.session;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @RequestMapping("/s1")
  public String s1(HttpSession session) {
    Object attribute = session.getAttribute("SPRING_SECURITY_CONTEXT");
    System.out.println("session = " + attribute);
    return "s1";
  }

  @Autowired
  private FindByIndexNameSessionRepository sessionRepository;

  private final Random random = new Random();

  @RequestMapping("/s2")
  public String s2() {
    String name = getRelatedUserName();

    Map<String, Session> sessions = sessionRepository.findByIndexNameAndIndexValue(
        FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, name);

    for (Entry<String, Session> entry : sessions.entrySet()) {

      Session session = entry.getValue();
      Object attribute = session.getAttribute("SPRING_SECURITY_CONTEXT");
      if (attribute instanceof SecurityContextImpl) {
        SecurityContextImpl securityContext = (SecurityContextImpl) attribute;
        Authentication authentication = securityContext.getAuthentication();
        Collection<? extends GrantedAuthority> ss =
            Arrays.asList(new SimpleGrantedAuthority("ROLE_R" + random.nextInt()));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            authentication.getPrincipal(), authentication.getCredentials(), ss);
        token.setDetails(authentication.getDetails());
        securityContext.setAuthentication(token);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        sessionRepository.save(session);
      }
    }
    return "s2";
  }

  private String getRelatedUserName() {
    return "1";
  }

}
