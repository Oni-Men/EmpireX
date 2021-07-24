package onim.en.empirex.magic;

import java.util.Map;
import java.util.UUID;

import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.Maps;

public class MagicMessageHandler {

  private static final Map<UUID, BukkitTask> uuidToMessages = Maps.newHashMap();

  public static void registerTask(UUID uniqueId, BukkitTask task) {
    if (uuidToMessages.containsKey(uniqueId)) {
      uuidToMessages.get(uniqueId).cancel();
    }
    uuidToMessages.put(uniqueId, task);
  }
}
