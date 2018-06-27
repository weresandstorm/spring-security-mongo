package me.konglong.springsecurity.commons;

import static me.konglong.springsecurity.builders.ApprovalBuilder.approvalBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import me.konglong.springsecurity.builders.KApprovalBuilder;
import me.konglong.springsecurity.builders.KAccessTokenBuilder;
import me.konglong.springsecurity.domain.KAccessToken;
import me.konglong.springsecurity.domain.KApproval;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.characters.CharacterSetFilter;

public class SecurityRDG extends uk.org.fyodor.generators.RDG {

    public static Generator<String> ofEscapedString() {
        return () -> string(30, CharacterSetFilter.LettersAndDigits).next();
    }

    public static Generator<GrantedAuthority> ofGrantedAuthority() {
        return () -> new SimpleGrantedAuthority(string().next());
    }

    public static Generator<GrantedAuthority> ofInvalidAuthority() {
        return () -> new SimpleGrantedAuthority("");
    }

    public static Generator<KAccessToken> ofMongoOAuth2AccessToken() {
        return () -> KAccessTokenBuilder.kAccessTokenBuilder().build();
    }

    public static Generator<Object> objectOf(final Generator generator) {
        return () -> generator.next();
    }

    public static Generator<Serializable> serializableOf(final Generator<? extends Serializable> generator) {
        return () -> generator.next();
    }

    public static Generator<org.springframework.security.oauth2.provider.approval.Approval> ofApproval() {
        return () -> approvalBuilder().build();
    }

    public static Generator<KApproval> ofMongoApproval() {
        return () -> KApprovalBuilder.kApprovalBuilder().build();
    }

    public static Generator<LocalDateTime> localDateTime() {
        return () -> LocalDateTime.now().minusDays(longVal(30).next());
    }
}
