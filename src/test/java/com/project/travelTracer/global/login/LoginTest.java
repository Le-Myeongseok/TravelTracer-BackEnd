package com.project.travelTracer.global.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.travelTracer.member.entity.Member;
import com.project.travelTracer.member.entity.Role;
import com.project.travelTracer.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.xml.transform.Result;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    ObjectMapper objectMapper = new ObjectMapper();

    private static String KEY_USERID = "username";
    private static String KEY_PASSWORD = "password";
    private static String USERID = "dldpcks345";
    private static String PASSWORD = "123456789";

    private static String LOGIN_RUL = "/login";

    private void clear(){
        em.flush();
        em.clear();
    }

    @BeforeEach
    private void init() {
        memberRepository.save(Member.builder()
                .userId(USERID)
                .userPassword(passwordEncoder.encode(PASSWORD))
                .userName("Member1")
                .role(Role.USER)
                .age(22)
                .build());
        clear();
    }
    private Map getUserIdPasswordMap(String userId, String password) {
        Map<String, String> map = new HashMap<>();
        map.put(KEY_USERID, userId);
        map.put(KEY_PASSWORD, password);
        return map;
    }

    private ResultActions perform(String url, MediaType mediaType, Map userIdPasswordMap) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(mediaType)
                .content(objectMapper.writeValueAsString(userIdPasswordMap)));
    }
}
