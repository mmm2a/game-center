package com.morgan.server.util.soy;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.template.soy.data.SoyData;
import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

/**
 * Default implementation of the {@link SoyDataConverter} interface.  This default simply
 * uses the mechanisms built into {@link SoyData}, {@link SoyListData}, and {@link SoyMapData}
 * (plus a few other good choices like {@link SafeHtml} and {@link Optional}).
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultSoyDataConverter implements SoyDataConverter {

  @Override public void addToSoyMapData(SoyMapData map, String name, @Nullable Object value) {
    Preconditions.checkNotNull(map);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    
    if (value instanceof Optional) {
      Optional<?> opt = (Optional<?>) value;
      if (opt.isPresent()) {
        addToSoyMapData(map, name, opt.get());
      }
      
      return;
    }
    
    if (value == null) {
      map.put((Object) name, null);;
    } else if (value instanceof SoyConvertable) {
      map.put(name, ((SoyConvertable) value).toSoyData());
    } else {
      map.put(name, value);
    }
  }
}
