package com.sparta.admin.member.service;

import com.sparta.admin.member.dto.request.LoginRequest;
import com.sparta.admin.member.dto.request.SignupRequest;
import com.sparta.admin.member.dto.response.SignupResponse;
import com.sparta.common.dto.MemberSession;
import com.sparta.impostor.commerce.backend.domain.adminMember.entity.AdminMember;
import com.sparta.impostor.commerce.backend.domain.adminMember.repository.AdminMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminMemberAuthService {

    private final AdminMemberRepository adminMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, MemberSession> redisTemplate;

    public SignupResponse signup(SignupRequest request) {

        String email = request.email();
        String password = passwordEncoder.encode(request.password());
        String name = request.name();

        if (adminMemberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입한 회원입니다.");
        }

        AdminMember createMember = AdminMember.createMember(email, password, name);
        adminMemberRepository.save(createMember);

        return SignupResponse.from(createMember);
    }

    public ResponseCookie login(LoginRequest request) {
        String email = request.email();
        String rawPassword = request.password();

        AdminMember member = adminMemberRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("회원정보가 존재하지 않습니다."));

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }

        String sessionId = new StandardSessionIdGenerator().generateSessionId();
        redisTemplate.opsForValue().set(sessionId, new MemberSession(member.getId()), 30L, TimeUnit.MINUTES);

        return ResponseCookie
                .from("SESSION", sessionId)
                .path("/")
                .httpOnly(true)
                .maxAge(1800)
                .build();
    }
}
