package com.lookatme.server.auth.filter;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

// JWT 토큰 검증 필터
@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final RedisRepository redisRepository;
    private final JwtTokenizer jwtTokenizer;
    private final MemberAuthorityUtils authorityUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);
        } catch (SignatureException se) {
            request.setAttribute("exception", ErrorCode.TOKEN_INVALID); // 토큰 서명 검증 실패
        } catch (ExpiredJwtException ee) {
            request.setAttribute("exception", ErrorCode.TOKEN_EXPIRE); // 토큰 만료
        } catch (ErrorLogicException ele) {
            request.setAttribute("exception", ele.getErrorCode()); // 로직 에러
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.EXCEPTION); // 기타
        }
        filterChain.doFilter(request, response);
    }

    @Override // Authorization 헤더가 없거나 Bearer로 시작하지 않으면 필터를 실행하지 않음
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");
        return authorization == null || !authorization.startsWith("Bearer ");
    }

    // Access 토큰 검증 - 예외가 발생하지 않으면 토큰 검증 통과한 것 (Key가 맞지 않으면 claims를 얻어올 수 없으므로..)
    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        
        // AccessToken이 블랙리스트에 등록되어있는 경우 예외 발생
        if (redisRepository.hasAccessTokenInBlacklist(jws)) {
            throw new ErrorLogicException(ErrorCode.TOKEN_LOGOUT);
        }
        
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
    }

    // Access 토큰에 있는 값들을 SecurityContext에 저장해서 컨트롤러에서 사용할 수 있도록 (세션 사용 X -> 1회성)
    private void setAuthenticationToContext(Map<String, Object> claims) {
        // claims 안에 있는 내용을 통해 컨트롤러에서 @AuthenticationPrincipal으로 얻을 수 있는 MemberPrincipal 객체 생성
        MemberPrincipal memberPrincipal = new MemberPrincipal(claims);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberPrincipal,
                        null,
                        authorityUtils.createAuthorities((List<String>) claims.get("roles")));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
