package org.robolectric.internal;

import org.robolectric.internal.bytecode.InstrumentationConfiguration;
import org.robolectric.internal.bytecode.SandboxClassLoader;
import org.robolectric.internal.dependency.DependencyResolver;
import org.robolectric.util.Pair;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.robolectric.util.ReflectionHelpers.setStaticField;

public class SandboxFactory {

  /** The factor for cache size. See {@link #CACHE_SIZE} for details. */
  private static final int CACHE_SIZE_FACTOR = 3;

  /** We need to set the cache size of class loaders more than the number of supported APIs as different tests may have different configurations. */
  private static final int CACHE_SIZE = SdkConfig.getSupportedApis().size() * CACHE_SIZE_FACTOR;

  // Simple LRU Cache. SdkEnvironments are unique across InstrumentationConfiguration and SdkConfig
  private static final LinkedHashMap<Pair<InstrumentationConfiguration, SdkConfig>, SdkEnvironment> sdkToEnvironment = new LinkedHashMap<Pair<InstrumentationConfiguration, SdkConfig>, SdkEnvironment>() {
    @Override
    protected boolean removeEldestEntry(Map.Entry<Pair<InstrumentationConfiguration, SdkConfig>, SdkEnvironment> eldest) {
      return size() > CACHE_SIZE;
    }
  };

  private final InstrumentationConfiguration instrumentationConfig;
  private final DependencyResolver dependencyResolver;

  public SandboxFactory(InstrumentationConfiguration instrumentationConfig, DependencyResolver dependencyResolver) {
    this.instrumentationConfig = instrumentationConfig;
    this.dependencyResolver = dependencyResolver;
  }

  public synchronized SdkEnvironment getSdkEnvironment(SdkConfig sdkConfig) {
    Pair<InstrumentationConfiguration, SdkConfig> key = Pair.create(instrumentationConfig, sdkConfig);

    SdkEnvironment sdkEnvironment = sdkToEnvironment.get(key);
    if (sdkEnvironment == null) {
      URL url = dependencyResolver.getLocalArtifactUrl(sdkConfig.getAndroidSdkDependency());

      ClassLoader robolectricClassLoader = new SandboxClassLoader(instrumentationConfig, url);
      sdkEnvironment = new SdkEnvironment(sdkConfig, robolectricClassLoader);

      sdkToEnvironment.put(key, sdkEnvironment);
    }
    return sdkEnvironment;
  }
}