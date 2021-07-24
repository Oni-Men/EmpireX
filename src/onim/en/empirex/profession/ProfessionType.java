package onim.en.empirex.profession;

public enum ProfessionType {

    NEET("ニート"),
    RIKISI("力士"),
    BUILDER("建築家"),
    STRIDER("馳夫"),
    SERIAL_KILLER("連続殺人犯");

  private final String name;

  ProfessionType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }



}
