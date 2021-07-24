package onim.en.empirex.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class WindowRegistry {

  private static final HashMap<String, WindowInterface> allIdToWindow = Maps.newHashMap();
  private static final Set<String> unsubscribed = Sets.newHashSet();

  public static void subscribe(WindowInterface window) {
    allIdToWindow.computeIfAbsent(window.getTitle(), k -> window);
  }

  public static void subscribeAndOpen(Player player, WindowInterface window) {
    subscribe(window);
    open(player, window);
  }

  public static void unsubscribe(WindowInterface window) {
    unsubscribed.add(window.toString());
  }

  public static void update() {
    if (unsubscribed.isEmpty())
      return;
    allIdToWindow.keySet().removeIf(t -> unsubscribed.contains(t));
    unsubscribed.clear();
  }

  public static void open(Player player, WindowInterface window) {
    WindowInterface iwindow = allIdToWindow.get(window.getTitle());
    if (iwindow != null) {
      player.openInventory(iwindow.getWindow());
    }

  }

  public static WindowInterface getWindow(String id) {
    return allIdToWindow.get(id);
  }

  public static void executeIfPresent(String id, Consumer<WindowInterface> executor) {
    WindowInterface window = allIdToWindow.get(id);

    if (window == null) {
      return;
    }

    executor.accept(window);
  }

  public static List<WindowInterface> getAllWindow() {
    return new ArrayList<>(allIdToWindow.values());
  }

}
