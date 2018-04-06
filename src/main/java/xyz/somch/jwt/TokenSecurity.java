package xyz.somch.jwt;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import static xyz.somch.jwt.passwordToHMAC.stringToHMAC;
import xyz.somch.model.User;

public class TokenSecurity {
    
	static String issuer = "localhost";
        static int tiempoDeExpiracion = 5;
        private static RsaJsonWebKey rsaJsonWebKey = null;
        static {
		try {
			rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
		} catch (JoseException e) {
			e.printStackTrace();
		}
        }
        
	public static String generarJwt( User user ) throws JoseException, UnsupportedEncodingException {
	    Key key = stringToHMAC( user.getPassword() );
	    JwtClaims claims = new JwtClaims();
	    claims.setIssuer( issuer );  
	    claims.setExpirationTimeMinutesInTheFuture( tiempoDeExpiracion ); 
	    claims.setGeneratedJwtId(); 
	    claims.setIssuedAtToNow();  
	    claims.setNotBeforeMinutesInThePast(2); 
	    claims.setClaim( "id", user.getId());
            claims.setClaim("rol" , user.getRol());
	    JsonWebSignature jws = new JsonWebSignature();
	    jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue( AlgorithmIdentifiers.HMAC_SHA256 );
	    jws.setKey(key);
            jws.setDoKeyValidation(false);
	    String jwt = jws.getCompactSerialization();
            System.out.println( "token generado: "+jwt );
	    return jwt;
        }
	
	public static void validateJwtToken( String jwt, String secret ) throws InvalidJwtException, UnsupportedEncodingException {
            String[] datos = new String[2]; 
            JwtConsumer preJwtConsumer = new JwtConsumerBuilder()
                                .setSkipSignatureVerification()
                                .setSkipAllValidators()
                                .build();
            JwtClaims preJwtClaims = preJwtConsumer.processToClaims(jwt);
            String id =preJwtClaims.getClaimValue( "id" ).toString();
            Key key = stringToHMAC( secret );
	    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
	            .setRequireExpirationTime()
	            .setMaxFutureValidityInMinutes( 5 ) 
	            .setAllowedClockSkewInSeconds( 30 ) 
	            .setExpectedIssuer( issuer ) 
	            .setVerificationKey( key )
	            .build();
            
        JwtClaims jwtClaims = jwtConsumer.processToClaims( jwt );
        System.out.println( "JWT validacion hecha! " + jwtClaims );
	}
        
        public static String getClaimsJwtToken(String jwt) throws InvalidJwtException{
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                                .setSkipSignatureVerification()
                                .setSkipAllValidators()
                                .build();
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            String id = (String) jwtClaims.getClaimValue("id");
            return id;
        }
        
        public static String refreshJwtToken( User user ) throws InvalidJwtException, JoseException, Exception{
            String jwt = generarJwt(user);
            return jwt;
        }
}
