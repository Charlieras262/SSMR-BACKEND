package com.g5.ssmr.config.security;

import com.g5.ssmr.models.User;
import com.g5.ssmr.services.UserService;
import io.jsonwebtoken.*;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class JwtUtil {

    public static String generateToken(String text) {
        return generateAccessToken(text, (1000L * 60 * 20));
    }

    public static String generateAccessToken(String text, long expiration) {
        return Jwts.builder()
                .setSubject(text)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, System.getenv("ENV_SIGNING_KEY"))
                .compact();
    }

    public static String generateRefreshToken(String text, long expiration) {
        return Jwts.builder()
                .setSubject(text)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, System.getenv("ENV_SIGNING_KEY"))
                .compact();
    }

    public static Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response, UserService userService) throws IOException {
        String token = request.getHeader("Authorization");
        String origin = request.getHeader("Origin");
        /*if (!Pattern.compile("^(https?:\\/\\/(www\\.)?enactusumg\\.com)$").matcher(origin).matches()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Host.");
        }*/
        if (token != null) {
            try {
                String username = parseToken(token);
                User user = userService.getUser(username);
                if (user != null && user.getToken() != null && user.getToken().equals(token.replace(String.join(".", chunk(System.getenv("ENV_TOKEN_SECRET"), 4)), ""))) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.emptyList()
                    );
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    return authentication;
                }
            } catch (ExpiredJwtException | SignatureException | MalformedJwtException ex) {
                ex.printStackTrace();
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Token.");
                return null;
            }
        }
        return null;
    }

    public static String parseToken(@Nullable String token) throws SignatureException {
        assert token != null;
        assert System.getenv("ENV_TOKEN_SECRET") != null;
        return Jwts.parser()
                .setSigningKey(System.getenv("ENV_SIGNING_KEY"))
                .parseClaimsJws(token.replace(String.join(".", chunk(System.getenv("ENV_TOKEN_SECRET"), 4)), ""))
                .getBody()
                .getSubject();
    }

    public static boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(System.getenv("ENV_SIGNING_KEY"))
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return true;
        } catch (Exception ignored) {} return false;
    }

    private static List<String> chunk(String str, int size) {
        ArrayList<String> split = new ArrayList<>();
        for (int i = 0; i < str.length() / size; i++) {
            split.add(str.substring(i * size, Math.min((i + 1) * size, str.length())));
        }
        return split;
    }
}
