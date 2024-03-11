/*
 * This code is to be used exclusively in connection with Ping Identity Corporation software or services. 
 * Ping Identity Corporation only offers such software or services to legal entities who have entered into 
 * a binding license agreement with Ping Identity Corporation.
 *
 * Copyright 2024 Ping Identity Corporation. All Rights Reserved
 */

package org.forgerock.openam.auth.service.marketplace;


import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.annotations.sm.Config;
import org.forgerock.openam.annotations.sm.SubConfig;
import org.forgerock.openam.sm.annotations.subconfigs.Multiple;

import com.sun.identity.sm.RequiredValueValidator;

/**
 * Common Configurations for the PingOne Nodes.
 */
@Config(scope = Config.Scope.REALM_AND_GLOBAL)
public interface TNTPPingOneService {


	@SubConfig
	Multiple<TNTPPingOneConfig> PingOneService();

    @Attribute(order = 90, validators = {RequiredValueValidator.class})
    default boolean enable() { return true; }
	
}