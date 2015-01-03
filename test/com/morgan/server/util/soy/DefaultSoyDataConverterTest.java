package com.morgan.server.util.soy;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.truth.Truth;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.template.soy.data.SoyData;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.data.UnsafeSanitizedContentOrdainer;
import com.google.template.soy.data.SanitizedContent.ContentKind;

/**
 * Tests for the {@link DefaultSoyDataConverter} class.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class DefaultSoyDataConverterTest {

  private static final SoyData CONVERTABLE_DATA =
      SoyData.createFromExistingData(42);
  private static final SoyConvertable CONVERTABLE =
      new SoyConvertable() {
        @Override public SoyData toSoyData() {
          return CONVERTABLE_DATA;
        }
      };
  
  private DefaultSoyDataConverter converter;
  
  @Before public void createTestInstances() {
    converter = new DefaultSoyDataConverter();
  }
  
  @Test public void addToSoyMapData_optional_absent() {
    SoyMapData mapData = new SoyMapData();
    converter.addToSoyMapData(mapData, "foo", Optional.absent());
    Truth.assertThat(mapData.asMap()).isEqualTo(new SoyMapData().asMap());
  }
  
  @Test public void addToSoyMapData_optional_present() {
    SoyMapData mapData = new SoyMapData();
    converter.addToSoyMapData(mapData, "foo", Optional.of("bar"));
    Truth.assertThat(mapData.asMap())
        .isEqualTo(new SoyMapData("foo", "bar").asMap());
  }
  
  @Test public void addToSoyMapData_null() {
    SoyMapData mapData = new SoyMapData();
    converter.addToSoyMapData(mapData, "foo", null);
    Truth.assertThat(mapData.asMap())
        .isEqualTo(new SoyMapData("foo", null).asMap());
  }
  
  @Test public void addToSoyMapData_soyConvertable() {
    SoyMapData mapData = new SoyMapData();
    converter.addToSoyMapData(mapData, "foo", CONVERTABLE);
    Truth.assertThat(mapData.asMap())
        .isEqualTo(new SoyMapData("foo", CONVERTABLE_DATA).asMap());
  }
  
  @Test public void addToSoyMapData_basicType() {
    SoyMapData mapData = new SoyMapData();
    converter.addToSoyMapData(mapData, "foo", 69);
    Truth.assertThat(mapData.asMap())
        .isEqualTo(new SoyMapData("foo", 69).asMap());
  }
  
  @Test public void addToSoymapData_safeHtml() {
    SoyMapData mapData = new SoyMapData();
    converter.addToSoyMapData(mapData, "foo", SafeHtmlUtils.fromString("safe string"));
    Truth.assertThat(mapData.asMap())
        .isEqualTo(new SoyMapData("foo",
            UnsafeSanitizedContentOrdainer.ordainAsSafe("safe string", ContentKind.HTML)).asMap());
  }
  
  @Test public void addToSoymapData_safeUri() {
    SoyMapData mapData = new SoyMapData();
    converter.addToSoyMapData(mapData, "foo", UriUtils.fromString("safestring"));
    Truth.assertThat(mapData.asMap())
        .isEqualTo(new SoyMapData("foo",
            UnsafeSanitizedContentOrdainer.ordainAsSafe("safestring", ContentKind.URI)).asMap());
  }
}
