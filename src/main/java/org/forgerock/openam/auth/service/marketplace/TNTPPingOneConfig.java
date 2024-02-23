package org.forgerock.openam.auth.service.marketplace;

import org.forgerock.am.config.ServiceComponentConfig;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.annotations.sm.Id;

public interface TNTPPingOneConfig extends ServiceComponentConfig{
	
	
	@Id
	String id();

    @Attribute(order = 1)
    default String environmentId(){ return ""; };    
    
    @Attribute(order = 200)
    PingOneRegion environmentRegion();

    @Attribute(order = 300)
    default String p1APIKey(){ return ""; };

    @Attribute(order = 400)
    default String p1APISecret(){ return ""; };

    @Attribute(order = 500)
    default String p1RedirectURL(){ return ""; };
    
    @Attribute(order = 600)
    default String dvAPIKey(){ return ""; };

    @Attribute(order = 700)
    default String clientIdWorkerApp(){ return ""; };

    @Attribute(order = 800)
    default String clientIdWorkerSecret(){ return ""; };
    
    
    public enum PingOneRegion {
        NA(".com"),
        CA(".ca"),
        EU(".eu"),
        ASIA(".asia");

        private final String domainSuffix;

        PingOneRegion(String domainSuffix) {
          this.domainSuffix = domainSuffix;
        }

        public String getDomainSuffix() {
          return domainSuffix;
        }
      }
    
    
	
}





