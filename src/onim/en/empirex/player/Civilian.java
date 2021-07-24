package onim.en.empirex.player;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import onim.en.empirex.base.Identifiable;
import onim.en.empirex.profession.ProfessionManager;
import onim.en.empirex.profession.ProfessionType;

public class Civilian implements Identifiable {

  private final Player bukkitPlayer;

  private int level = 1;

  private int exp = 0;

  private int deaths = 0;

  private int killStreak = 0;

  private int force = 0;

  private ProfessionType professionType;

  public Civilian(Player player) {
    if (player == null) {
      throw new RuntimeException("player is null");
    }
    this.bukkitPlayer = player;
    this.professionType = ProfessionType.NEET;
  }

  public Civilian(UUID uuid) {
    this(Bukkit.getPlayer(uuid));
  }

  @Override
  public String id() {
    return bukkitPlayer.getUniqueId().toString();
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int i) {

    level = i;
    if (level <= 0) {
      level = 1;
    }
    ProfessionManager.initialize(this);
  }

  public void addLevel(int i) {
    setLevel(getLevel() + i);
  }

  public int getExp() {
    return exp;
  }

  public void setExp(int i) {
    this.exp = i;

    long expToLevelUp = getExpToNextLevel();
    int oldLevel = getLevel();

    while (exp >= expToLevelUp) {
      addLevel(1);
      exp -= expToLevelUp;
    }

    if (oldLevel != getLevel()) {
      ExpUtil.playLevelUpAnimation(bukkitPlayer, oldLevel, getLevel());
      bukkitPlayer.playSound(bukkitPlayer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 1f);
    }

    float progress = (float) exp / (float) expToLevelUp;
    bukkitPlayer.setLevel(level);
    bukkitPlayer.setExp(progress);
  }

  public void addExp(int i) {
    setExp(getExp() + i);
  }

  public int getExpToNextLevel() {
    float d = (float) (Math.log(100) * level);
    return (int) (15 + Math.round(d));
  }

  public ProfessionType getProfessionType() {
    return professionType;
  }

  public void setProfessionType(ProfessionType professionType) {
    if (this.professionType == professionType) {
      return;
    }
    ProfessionManager.finalize(this);
    this.professionType = professionType;
    ProfessionManager.initialize(this);

    TextComponent text = Component.text(b -> {
      b.append(Component.text("職業 "));
      b.color(NamedTextColor.GREEN);
      b.append(Component.text(this.professionType.getName()));
    });
    
    bukkitPlayer.sendMessage(text);
  }

  public Player bukkitPlayer() {
    return bukkitPlayer;
  }

  public void incrementDeath() {
    this.deaths++;
    this.killStreak = 0;
  }

  public int getDeathCount() {
    return this.deaths;
  }

  public void setDeathCount(int i) {
    this.deaths = i;
  }

  public void incrementKillStreak() {
    this.killStreak++;
  }

  public int getKillStreak() {
    return this.killStreak;
  }

  public void setKillStreak(int i) {
    this.killStreak = i;
  }

  public int getForce() {
    return this.force;
  }

  public void setForce(int i) {
    this.force = i;
    if (this.force < 0) {
      this.force = 0;
    }


  }

  public void addForce(int i) {
    setForce(getForce() + i);
  }

  public void printInfo() {
    printInfo(bukkitPlayer);
  }

  public void printInfo(Player player) {
    ArrayList<String> list = new ArrayList<>();

    list.add(String.format("%dレベル, 経験値 %dポイント", getLevel(), getExp()));
    list.add(String.format("次のレベルまであと %dポイント", getExpToNextLevel() - getExp()));
    list.add("職業 " + professionType.getName());
    list.add("死亡回数 " + getDeathCount());

    for (String s : list) {
      player.sendMessage(ChatColor.YELLOW + s);
    }
  }
}
