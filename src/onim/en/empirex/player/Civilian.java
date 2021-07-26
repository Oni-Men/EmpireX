package onim.en.empirex.player;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.google.common.base.Strings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import onim.en.empirex.EmpireX;
import onim.en.empirex.base.Identifiable;
import onim.en.empirex.profession.ProfessionManager;
import onim.en.empirex.profession.ProfessionType;
import onim.en.empirex.util.KeyFactory;

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
      this.playLevelUpAnimation(oldLevel, getLevel());
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
    float d = (float) (Math.sqrt(10 * level));
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

  public int getMaxForce() {
    return (int) (Math.pow(this.getLevel(), 1.7)) + 99;
  }

  public int getForceRegen() {
    return 5 + (int) (Math.sqrt(this.getLevel()));
  }

  public void setForce(int i) {
    this.force = i;
    if (this.force < 0) {
      this.force = 0;
    }

    if (this.force > this.getMaxForce()) {
      this.force = this.getMaxForce();
    }

    NamespacedKey keyForceBar = KeyFactory.get(String.format("force_bar_%s", this.id()));
    BossBar forceBar = Bukkit.getBossBar(keyForceBar);

    if (forceBar == null) {
      forceBar = Bukkit.createBossBar(keyForceBar, "", BarColor.PURPLE, BarStyle.SOLID);
      forceBar.addPlayer(bukkitPlayer);
      forceBar.setVisible(true);
    }

    float progress = (float) this.getForce() / (float) this.getMaxForce();
    String text = String.format("Force - %d/%d", this.getForce(), this.getMaxForce());

    forceBar.setTitle(text);
    forceBar.setProgress(progress);
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

  public void playLevelUpAnimation(int oldLevel, int newLevel) {

    Bukkit.getScheduler().runTask(EmpireX.instance, () -> {
      bukkitPlayer.sendTitle(ChatColor.GREEN + Integer.toString(oldLevel), "", 5, 20, 0);
    });

    AtomicInteger count = new AtomicInteger(0);

    Bukkit.getScheduler().runTaskTimer(EmpireX.instance, task -> {
      char[] chars = Strings.repeat(ChatColor.DARK_GRAY + ">", 5).toCharArray();
      chars[count.getAndAdd(1) * 3 + 1] = ChatColor.YELLOW.getChar();
      bukkitPlayer.sendTitle(new String(chars), "", 0, 4, 0);

      if (count.get() >= 5) {
        task.cancel();
      }
    }, 25, 2);

    Bukkit.getScheduler().runTaskLater(EmpireX.instance, () -> {
      bukkitPlayer.sendTitle(ChatColor.GREEN + Integer.toString(newLevel), "", 5, 20, 10);
    }, 35);
  }
}
