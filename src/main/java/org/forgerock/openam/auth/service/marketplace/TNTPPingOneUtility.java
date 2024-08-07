/*
 * This code is to be used exclusively in connection with Ping Identity Corporation software or services. 
 * Ping Identity Corporation only offers such software or services to legal entities who have entered into 
 * a binding license agreement with Ping Identity Corporation.
 *
 * Copyright 2024 Ping Identity Corporation. All Rights Reserved
 */

package org.forgerock.openam.auth.service.marketplace;

import java.net.URI;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.forgerock.http.handler.HttpClientHandler;
import org.forgerock.http.header.AuthorizationHeader;
import org.forgerock.http.header.authorization.BasicCredentials;
import org.forgerock.http.protocol.Form;
import org.forgerock.http.protocol.Request;
import org.forgerock.http.protocol.Response;
import org.forgerock.http.protocol.Status;
import org.forgerock.json.JsonValue;
import org.forgerock.json.jose.common.JwtReconstruction;
import org.forgerock.json.jose.jwt.Jwt;
import org.forgerock.oauth2.core.AccessToken;
import org.forgerock.openam.core.realms.Realm;
import org.forgerock.openam.http.HttpConstants;
import org.forgerock.openam.oauth2.token.stateless.StatelessAccessToken;
import org.forgerock.services.context.RootContext;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TNTPPingOneUtility {

	private static TNTPPingOneUtility single_instance = null;
	private String endpoint = "https://auth.pingone";
	private final LoadingCache<WorkerKey, String> accessTokenCache;

	// Constructor
	// Here we will be creating private constructor
	// restricted to this class itself
	private TNTPPingOneUtility() {
		this.accessTokenCache = CacheBuilder.newBuilder().build(new CacheLoader<>() {
			@Override
			public String load(WorkerKey key) throws Exception {
				return getToken(key.realm, key.worker);
			}
		});

	}

	// Static method
	// Static method to create instance of Singleton class
	public static synchronized TNTPPingOneUtility getInstance() {
		if (single_instance == null)
			single_instance = new TNTPPingOneUtility();

		return single_instance;
	}

	/**
	 * Get Access Token.
	 *
	 * @param realm  The realm.
	 * @param worker PingOne Worker Client Service
	 * @return The PingOne Worker AccessToken
	 */
	public String getAccessToken(Realm realm, TNTPPingOneConfig worker) throws Exception {

		try {
			WorkerKey key = new WorkerKey(realm, worker);
			String token = accessTokenCache.get(key);
			if (StringUtils.isBlank(token)) {
				return token;
			} else {
				accessTokenCache.invalidate(key);
			}
			return accessTokenCache.get(key);

		} catch (Exception e) {
			throw new Exception("Failed to retrieve PingOne Worker access token", e);
		}
	}

	private String getToken(Realm realm, TNTPPingOneConfig worker) throws Exception {
		HttpClientHandler handler = null;
		Request request = null;

		try {

			handler = new HttpClientHandler();			
			URI uri = URI.create(endpoint + worker.environmentRegion().getDomainSuffix() + "/" + worker.environmentId() + "/as/token");
			request = new Request().setUri(uri).setMethod(HttpConstants.Methods.POST);
			Form form = new Form();
			form.putSingle("grant_type", "client_credentials");
			form.putSingle("scope", "openid");
			request.getEntity().setForm(form);
			AuthorizationHeader header = new AuthorizationHeader();
			BasicCredentials basicCredentials = new BasicCredentials(worker.clientIdWorkerApp(), worker.clientIdWorkerSecret());
			header.setRawValue("Basic " + basicCredentials);
			request.addHeaders(header);
			Response response = handler.handle(new RootContext(), request).getOrThrow();
			if (response.getStatus() == Status.OK) {
				JsonValue resp = JsonValue.json(response.getEntity().getJson());
				String accessToken = resp.get("access_token").asString();
				return accessToken;

			} else {
				throw new Exception("Failed to retrieve Worker Access Token." + response.getStatus() + "-" + response.getEntity().getString());
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {
			if (handler != null) {
				try {
					handler.close();
				} catch (Exception e) {
					// DO NOTHING
				}
			}

			if (request != null) {
				try {
					request.close();
				} catch (Exception e) {
					// DO NOTHING
				}
			}
		}
	}

	/**
	 * The Worker Key, if the clientId and environmentId are equals, we consider the
	 * key are equal.
	 */
	private static class WorkerKey {

		private final TNTPPingOneConfig worker;
		private final Realm realm;

		WorkerKey(Realm realm, TNTPPingOneConfig worker) {
			this.realm = realm;
			this.worker = worker;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			WorkerKey other = (WorkerKey) o;
			return Objects.equals(worker.clientIdWorkerApp(), other.worker.clientIdWorkerApp()) && Objects.equals(worker.environmentId(), other.worker.environmentId());
		}

		@Override
		public int hashCode() {
			return Objects.hash(worker.clientIdWorkerApp(), worker.environmentId());
		}
	}

}
