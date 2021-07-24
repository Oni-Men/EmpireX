package onim.en.empirex.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.kyori.adventure.text.Component;

public class ComponentsBuilder {

  private List<Component> list;

  public ComponentsBuilder() {
    list = new LinkedList<>();
  }

  public ComponentsBuilder append(String s) {
    this.list.add(Component.text(s));
    return this;
  }

  public ComponentsBuilder append(Component c) {
    this.list.add(c);
    return this;
  }

  public ComponentsBuilder appendTexts(List<String> l) {
    for (String s : l) {
      this.append(s);
    }
    return this;
  }

  public ComponentsBuilder appendComps(List<Component> l) {
    this.list.addAll(l);
    return this;
  }

  public List<Component> get() {
    return new ArrayList<>(list);
  }

}
