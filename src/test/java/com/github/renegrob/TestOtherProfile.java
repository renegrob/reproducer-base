package com.github.renegrob;

import java.util.Set;

import com.github.renegrob.service.MyServiceOtherImpl;

import io.quarkus.test.junit.QuarkusTestProfile;

public class TestOtherProfile implements QuarkusTestProfile {
  @Override
  public Set<Class<?>> getEnabledAlternatives() {
    return Set.of(MyServiceOtherImpl.class);
  }
}