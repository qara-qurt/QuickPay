package kz.iitu.quick_pay.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@RequestMapping(AnalyticController.BASE_URL)
public class AnalyticController {

    public static final String BASE_URL = "/api/analytics";

    @Value("${metabase.secret}")
    String secretKey;

    @Value("${metabase.site-url}")
    String metabaseSiteUrl;

    @Value("${metabase.dashboard-id}")
    String dashboardId;

    @GetMapping("/embed-url")
    public ResponseEntity<Map<String, String>> getEmbedUrl() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("resource", Collections.singletonMap("dashboard", Integer.parseInt(dashboardId)));
        payload.put("params", new HashMap<>());
        payload.put("exp", Instant.now().plus(Duration.ofMinutes(10)).getEpochSecond());

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String token = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(payload)
                .compact();

        String iframeUrl = metabaseSiteUrl + "/embed/dashboard/" + token + "#theme=light";

        return ResponseEntity.ok(Map.of("iframeUrl", iframeUrl));
    }
}
