package utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cloudfoundry.org.codehaus.jackson.map.ObjectMapper;
import org.cloudfoundry.runtime.env.AbstractServiceInfo;
import org.cloudfoundry.runtime.env.ApplicationInstanceInfo;
import org.cloudfoundry.runtime.env.CloudServiceException;
import org.cloudfoundry.runtime.env.MysqlServiceInfo;
import org.cloudfoundry.runtime.env.RabbitServiceInfo;
import org.cloudfoundry.runtime.env.RedisServiceInfo;

/*
 * @author Ramnivas Laddad
 * @author BenoĂŽt Courtine, 2011-07-10 : modified mapping of MongoServiceInfo class.
 * @version 2011.07.10
 */
public class CloudEnvironment {

	private ObjectMapper objectMapper = new ObjectMapper();
	private EnvironmentAccessor environment = new EnvironmentAccessor();

	private static Map<Class<? extends AbstractServiceInfo>, String> serviceTypeToNameMap = new HashMap<Class<? extends AbstractServiceInfo>, String>();

	static Logger logger = Logger.getLogger(CloudEnvironment.class);
	static {
		serviceTypeToNameMap.put(MysqlServiceInfo.class, "mysql-5.1");
	}

	/* package for testing purpose */
	void setCloudEnvironment(EnvironmentAccessor environment) {
		this.environment = environment;
	}

	public String getValue(String key) {
		return environment.getValue(key);
	}

	@SuppressWarnings("unchecked")
	public ApplicationInstanceInfo getInstanceInfo() {
		String instanceInfoString = getValue("VCAP_APPLICATION");
		if (instanceInfoString == null || instanceInfoString.trim().isEmpty()) {
			return null;
		}
		try {
			Map<String,Object> infoMap = objectMapper.readValue(instanceInfoString, Map.class);
			return new ApplicationInstanceInfo(infoMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getCloudApiUri() {
		ApplicationInstanceInfo instanceInfo = getInstanceInfo();
		if (instanceInfo == null) {
			throw new IllegalArgumentException("There is no cloud API urls in a non-cloud deployment");
		}
		List<String> uris = instanceInfo.getUris();
		String defaultUri = uris.get(0);
		return "api" + defaultUri.substring(defaultUri.indexOf("."));
	}

	/**
	 * Return object representation of the VCAP_SERIVCES environment variable
	 * <p>
	 * Returns a map whose key is the label (for example "redis-2.2") of the
	 * service and value is a list of services for that label. Each list element
	 * is a map with service attributes.
	 * </p>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<Map<String,Object>>> getRawServices() {
		String servicesString = getValue("VCAP_SERVICES");
		if (servicesString == null || servicesString.length() == 0) {
			return new HashMap<String, List<Map<String,Object>>>();
		}
		try {
			return objectMapper.readValue(servicesString, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Map<String,Object>> getServices() {
		Map<String, List<Map<String,Object>>> rawServices = getRawServices();

		List<Map<String,Object>> flatServices = new ArrayList<Map<String,Object>>();
		for (Map.Entry<String, List<Map<String,Object>>> entry : rawServices.entrySet()) {
			flatServices.addAll(entry.getValue());
		}
		return flatServices;
	}

	private Map<String, Object> getServiceDataByName(String name) {
		List<Map<String, Object>> services = getServices();

		for (Map<String, Object> service : services) {
			if (service.get("name").equals(name)) {
				return service;
			}
		}
		return null;
	}

	private List<Map<String, Object>> getServiceDataByServiceType(String type) {
		List<Map<String, Object>> services = getServices();

		List<Map<String, Object>> matchedServices = new ArrayList<Map<String,Object>>();

		for (Map<String, Object> service : services) {
			if (service.get("label").equals(type)) {
				matchedServices.add(service);
			}
		}
		return matchedServices;
	}


	public <T extends AbstractServiceInfo> T getServiceInfo(String name, Class<T> serviceInfoType) {
		Map<String,Object> serviceInfoMap = getServiceDataByName(name);
		String typeName = serviceTypeToNameMap.get(serviceInfoType);

		if (serviceInfoMap.get("label").equals(typeName)) {
			return getServiceInfo(serviceInfoMap, serviceInfoType);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<? extends AbstractServiceInfo> getServiceInfos() {
		List serviceInfos = new ArrayList();
		for (Map.Entry<Class<? extends AbstractServiceInfo>, String> serviceTypeToNameEntry : serviceTypeToNameMap.entrySet()) {
			serviceInfos.addAll(getServiceInfos(serviceTypeToNameEntry.getKey()));
		}
		return serviceInfos;
	}

	public <T extends AbstractServiceInfo> List<T> getServiceInfos(Class<T> serviceInfoType) {
		String typeName = serviceTypeToNameMap.get(serviceInfoType);
		List<Map<String,Object>> serviceInfoMaps = getServiceDataByServiceType(typeName);
		logger.info("serviceInfoMaps -> " + serviceInfoMaps);
		List<T> serviceInfos = new ArrayList<T>();
		for (Map<String,Object> serviceInfoMap : serviceInfoMaps) {
			
			serviceInfos.add(getServiceInfo(serviceInfoMap, serviceInfoType));
		}
		return serviceInfos;
	}

	private <T extends AbstractServiceInfo> T getServiceInfo(Map<String,Object> serviceInfoMap, Class<T> serviceInfoType) {
		try {
			Constructor<T> ctor = serviceInfoType.getConstructor(Map.class);
			return ctor.newInstance(serviceInfoMap);
		} catch (Exception e) {
			throw new CloudServiceException("Failed to create service information for " + serviceInfoMap.get("name"), e);
		}
	}

	/**
	 * Environment available to the deployed app.
	 *
	 * The main purpose of this class is to allow unit-testing of {@link CloudEnvironment}
	 *
	 */
	public static class EnvironmentAccessor {
		public String getValue(String key) {
			return System.getenv(key);
		}
	}
}