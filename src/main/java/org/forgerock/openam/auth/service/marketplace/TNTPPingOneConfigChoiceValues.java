package org.forgerock.openam.auth.service.marketplace;

import static org.forgerock.openam.utils.StringUtils.isBlank;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.forgerock.guice.core.InjectorHolder;
import org.forgerock.openam.core.realms.Realm;
import org.forgerock.openam.core.realms.RealmLookupException;
import org.forgerock.openam.core.realms.Realms;
import org.forgerock.openam.sm.AnnotatedServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iplanet.sso.SSOException;
import com.sun.identity.shared.Constants;
import com.sun.identity.sm.ChoiceValues;
import com.sun.identity.sm.SMSEntry;
import com.sun.identity.sm.SMSException;

public class TNTPPingOneConfigChoiceValues extends ChoiceValues {
	
	
	
    private static final Logger logger = LoggerFactory.getLogger(TNTPPingOneConfigChoiceValues.class);
	private static String loggerPrefix = "[TNTPPingOneConfigChoiceValues]" + TNTPPingOneServicePlugin.logAppender;

	
	public static String createTNTPPingOneConfigName(String id) {
		return id;
	}

	public static String createTNTPPingOneConfigName(String id, String realm) {
		return id + " [" + realm + "]";
	}

	public static String createTNTPPingOneConfigName(String id, Realm realm) {
		return id + " [" + realm.asPath() + "]";
	}

	public static boolean isGlobalTNTPPingOneConfig(String tntpPingOneConfigName) {
		return !tntpPingOneConfigName.endsWith("]");
	}

	public static String getRealmString(String tntpPingOneConfigName) {
		if (isGlobalTNTPPingOneConfig(tntpPingOneConfigName))
			return null;
		return tntpPingOneConfigName.substring(tntpPingOneConfigName.lastIndexOf('[') + 1, tntpPingOneConfigName.length() - 1);
	}

	public static Realm getRealm(String tntpPingOneConfigName) throws RealmLookupException {
		if (isGlobalTNTPPingOneConfig(tntpPingOneConfigName))
			return null;
		return Realms.of(getRealmString(tntpPingOneConfigName));
	}

	public static String getId(String tntpPingOneConfigName) {
		if (isGlobalTNTPPingOneConfig(tntpPingOneConfigName))
			return tntpPingOneConfigName;
		return tntpPingOneConfigName.substring(0, tntpPingOneConfigName.lastIndexOf('[') - 1);
	}

	public static TNTPPingOneConfig getTNTPPingOneConfig(String tntpPingOneConfigName) {
		AnnotatedServiceRegistry serviceRegistry = InjectorHolder.getInstance(AnnotatedServiceRegistry.class);
		try {
			TNTPPingOneService igCommService;
			if (isGlobalTNTPPingOneConfig(tntpPingOneConfigName)) {
				igCommService = serviceRegistry.getGlobalSingleton(TNTPPingOneService.class);
			} else {
				igCommService = serviceRegistry
						.getRealmSingleton(TNTPPingOneService.class, getRealm(tntpPingOneConfigName)).get();
			}
			return igCommService.commConfigs().get(getId(tntpPingOneConfigName));
		} catch (SSOException | SMSException | RealmLookupException e) {
			logger.error(loggerPrefix + "Couldn't load igComm configs", e);
			throw new IllegalStateException(loggerPrefix + "Couldn't load PingOne Service configs", e);
		}
	}

	public static boolean isIGCommServiceEnabled(String tntpPingOneConfigName) {
		AnnotatedServiceRegistry serviceRegistry = InjectorHolder.getInstance(AnnotatedServiceRegistry.class);
		try {
			TNTPPingOneService igCommService;
			if (isGlobalTNTPPingOneConfig(tntpPingOneConfigName)) {
				igCommService = serviceRegistry.getGlobalSingleton(TNTPPingOneService.class);
			} else {
				igCommService = serviceRegistry
						.getRealmSingleton(TNTPPingOneService.class, getRealm(tntpPingOneConfigName)).get();
			}
			return igCommService.enable();
		} catch (SSOException | SMSException | RealmLookupException e) {
			logger.error(loggerPrefix + "Couldn't load igComm configs", e);
			throw new IllegalStateException(loggerPrefix + "Couldn't load igComm configs", e);
		}
	}

	@Override
	public Map getChoiceValues(Map envParams) throws IllegalStateException {
		String realm = null;
		if (envParams != null) {
			realm = (String) envParams.get(Constants.ORGANIZATION_NAME);
		}
		if (isBlank(realm)) {
			realm = SMSEntry.getRootSuffix();
		}
		AnnotatedServiceRegistry serviceRegistry = InjectorHolder.getInstance(AnnotatedServiceRegistry.class);
		try {
			Map<String, String> configs = new TreeMap<String, String>();
			TNTPPingOneService globalIGCommService = serviceRegistry
					.getGlobalSingleton(TNTPPingOneService.class);
			Iterator<String> globalConfigIterator = globalIGCommService.commConfigs().idSet().iterator();
			while (globalConfigIterator.hasNext()) {
				String id = globalConfigIterator.next();
				configs.put(TNTPPingOneConfigChoiceValues.createTNTPPingOneConfigName(id), "");
			}
			if (serviceRegistry.getRealmSingleton(TNTPPingOneService.class, Realms.of(realm)).isPresent()) {
				TNTPPingOneService realmIGCommService = serviceRegistry
						.getRealmSingleton(TNTPPingOneService.class, Realms.of(realm)).get();
				Iterator<String> realmConfigIterator = realmIGCommService.commConfigs().idSet().iterator();
				while (realmConfigIterator.hasNext()) {
					String id = realmConfigIterator.next();
					configs.put(TNTPPingOneConfigChoiceValues.createTNTPPingOneConfigName(id, Realms.of(realm)), "");
				}
			}
			return configs;
		} catch (SSOException | SMSException | RealmLookupException e) {
			logger.error(loggerPrefix + "Couldn't load igComm configs", e);
			throw new IllegalStateException(loggerPrefix + "Couldn't load igComm configs", e);
		}
	}

	@Override
	public Map<String, String> getChoiceValues() {
		return getChoiceValues(Collections.EMPTY_MAP);
	}
	
	
	
}
