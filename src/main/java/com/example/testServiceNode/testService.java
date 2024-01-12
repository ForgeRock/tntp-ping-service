/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2017-2023 ForgeRock AS.
 */


package com.example.testServiceNode;


import org.forgerock.openam.annotations.sm.Attribute;
import org.forgerock.openam.annotations.sm.Config;

/**
 * Common Configurations for the OneSpan Auth Tree Nodes.
 */
@Config(scope = Config.Scope.REALM)
public interface testService {

    @Attribute(order = 1)
    default String environmentId(){ return ""; };

    @Attribute(order = 200)
    default String region(){ return ""; };

    @Attribute(order = 300)
    default String apiKey(){ return ""; };

    @Attribute(order = 400)
    default String clientId(){ return ""; };

    @Attribute(order = 500)
    default String clientSecret(){ return ""; };

    @Attribute(order = 600)
    default String redirectURL(){ return ""; };

    @Attribute(order = 700)
    default String clientIdWorkerApp(){ return ""; };

    @Attribute(order = 800)
    default String clientIdWorkerSecret(){ return ""; };



}