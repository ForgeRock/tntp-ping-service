/*
 * This code is to be used exclusively in connection with Ping Identity Corporation software or services. 
 * Ping Identity Corporation only offers such software or services to legal entities who have entered into 
 * a binding license agreement with Ping Identity Corporation.
 *
 * Copyright 2024 Ping Identity Corporation. All Rights Reserved
 */

package org.forgerock.openam.auth.service.marketplace;

import org.forgerock.am.config.RealmConfiguration;
import org.forgerock.am.config.ServiceComponentConfig;
import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.annotations.sm.Config;
import org.forgerock.openam.annotations.sm.Id;

public interface TNTPPingOneConfig extends ServiceComponentConfig,  RealmConfiguration<TNTPPingOneConfig.Realm> {
	
	
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
  
    
    
    /**
     * The Realm.
     */
    @Config(scope = Config.Scope.REALM_AND_GLOBAL)
    
    interface Realm {

        /**
         * Whether this specific service config is enabled.
         *
         * @return true if it's enabled
         */
        @Attribute(order = 100, requiredValue = true)
        default boolean enabled() {
            return true;
        }

    }
	
}





