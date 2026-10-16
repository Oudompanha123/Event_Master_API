package com.example.final_project.jwt;

import com.example.final_project.model.Member;
import com.example.final_project.model.Organization;
import com.example.final_project.model.constant.Roles;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Plain unit tests - no Spring context and no Docker, so these run everywhere.
 *
 * <p>This is the code that decides who a request is authenticated as, and it had
 * no coverage at all.
 */
class JwtServiceTest {

    // Any Base64 value that decodes to at least the 32 bytes HS256 needs. What
    // these tests care about is that the key decides whether a token verifies,
    // not what the key happens to be.
    private static final String SECRET =
            Base64.getEncoder().encodeToString("unit-test-signing-key-at-least-32-bytes".getBytes());
    private static final String OTHER_SECRET =
            Base64.getEncoder().encodeToString("a-different-key-of-a-comparable-length!".getBytes());

    private JwtService jwtService;
    private Member member;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // The key is @Value-injected from JWT_SECRET, so a unit test has to place it.
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);

        member = new Member();
        member.setMemberId(7);
        member.setEmail("organiser@example.com");
        member.setRole(Roles.ROLE_ADMIN);
        member.setOrganization(new Organization(3, "Test Org", "ABC123", null, null));
    }

    @Test
    void generatedTokenCarriesTheMemberAsSubjectAndClaims() {
        String token = jwtService.generateToken(member);

        // Held in typed locals rather than inlined: extractClaim is generic, and
        // feeding it straight to the overloaded assertThat leaves T uninferable.
        Integer memberId = jwtService.extractClaim(token, c -> c.get("memberId", Integer.class));
        Integer orgId = jwtService.extractClaim(token, c -> c.get("orgId", Integer.class));
        String role = jwtService.extractClaim(token, c -> c.get("role", String.class));

        // getUsername() is the email, so that is what lands in 'sub'.
        assertThat(jwtService.extractUsername(token)).isEqualTo("organiser@example.com");
        assertThat(memberId).isEqualTo(7);
        assertThat(orgId).isEqualTo(3);
        assertThat(role).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void validatesATokenForTheMemberItWasIssuedTo() {
        String token = jwtService.generateToken(member);

        assertThat(jwtService.validateToken(token, member)).isTrue();
    }

    @Test
    void rejectsATokenPresentedByADifferentMember() {
        String token = jwtService.generateToken(member);

        Member someoneElse = new Member();
        someoneElse.setEmail("attendee@example.com");
        someoneElse.setRole(Roles.ROLE_USER);

        assertThat(jwtService.validateToken(token, someoneElse)).isFalse();
    }

    /**
     * The reason the signing key was moved out of the source and into JWT_SECRET:
     * a token minted with any other key must not verify. If this test ever stops
     * throwing, the signature is not being checked and anyone can mint an admin
     * token.
     */
    @Test
    void rejectsATokenSignedWithADifferentKey() {
        JwtService otherIssuer = new JwtService();
        ReflectionTestUtils.setField(otherIssuer, "secret", OTHER_SECRET);
        String forged = otherIssuer.generateToken(member);

        assertThatThrownBy(() -> jwtService.extractUsername(forged))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    void tokenExpiresAfterTheConfiguredValidity() {
        Date expiry = jwtService.extractExpirationDate(jwtService.generateToken(member));

        long secondsFromNow = (expiry.getTime() - System.currentTimeMillis()) / 1000;
        assertThat(secondsFromNow)
                .isBetween(JwtService.JWT_TOKEN_VALIDITY - 60, JwtService.JWT_TOKEN_VALIDITY);
    }
}
