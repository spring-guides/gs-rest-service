package com.example.restservice;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwk.Jwk;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import java.security.Key;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwkKeyResolver extends SigningKeyResolverAdapter {
    // private static String wellKnownUri = "https://login.microsoftonline.com/common/.well-known/openid-configuration";
    private final JwkProvider keyStore;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public JwkKeyResolver() throws java.net.URISyntaxException, java.net.MalformedURLException {
        this.keyStore = new UrlJwkProvider(
                (new URI("https://login.microsoftonline.com/common/discovery/keys").toURL()));
    }

    @Override
    public Key resolveSigningKey(final JwsHeader jwsHeader, final Claims claims) {
        try {
            final String keyId = jwsHeader.getKeyId();
            final Jwk pub = keyStore.get(keyId);
            return pub.getPublicKey();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}