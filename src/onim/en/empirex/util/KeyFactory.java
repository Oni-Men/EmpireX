package onim.en.empirex.util;

import java.util.Map;

import org.bukkit.NamespacedKey;

import com.google.common.collect.Maps;

import onim.en.empirex.EmpireX;

public class KeyFactory {

  private static Map<String, NamespacedKey> keys = Maps.newHashMap();
  
  public static NamespacedKey get(String key) {
    NamespacedKey namespacedKey = keys.get(key);

    if (namespacedKey == null) {
      namespacedKey = new NamespacedKey(EmpireX.instance, key);
      keys.put(key, namespacedKey);
    }

    return namespacedKey;
  }

}
