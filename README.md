# zItemStacker

Allows you to stack your items

## API

Java docs: https://javadocs.groupez.xyz/zitemstacker/

Get ItemManager:
```java

ItemManager itemManager = getProvider(ItemManager.class);

public <T> T getProvider(Class<T> classz) {
	RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
	if (provider == null) {
		log.log("Unable to retrieve the provider " + classz.toString(), LogType.WARNING);
		return null;
	}
	return provider.getProvider() != null ? (T) provider.getProvider() : null;
}
```
