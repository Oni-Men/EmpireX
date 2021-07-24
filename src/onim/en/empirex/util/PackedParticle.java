package onim.en.empirex.util;

import org.bukkit.Location;
import org.bukkit.Particle;

import com.google.common.base.Preconditions;

public class PackedParticle<T> {
  public Particle particle;
  public T data = null;
  public int count = 5;
  public double extra = 1;
  public double offsetX = 0d, offsetY = 0d, offsetZ = 0d;

  public PackedParticle(Particle particle, int count) {
    this(particle, count, 1);
  }

  public PackedParticle(Particle particle, int count, double extra) {
    this(particle, count, 0, 0, 0, extra);
  }

  public PackedParticle(Particle particle, int count, T data) {
    this(particle, count, 0, 0, 0, 0, data);
  }

  public PackedParticle(Particle particle, int count, double extra, T data) {
    this(particle, count, 0, 0, 0, extra, data);
  }

  public PackedParticle(Particle particle, int count, double x, double y, double z, double extra) {
    this(particle, count, x, y, z, extra, null);
  }

  public PackedParticle(Particle particle, int count, double x, double y, double z, double extra,
      T data) {
    Preconditions.checkNotNull(particle);
    this.particle = particle;
    this.count = count;
    this.extra = extra;
    this.offsetX = x;
    this.offsetY = y;
    this.offsetZ = z;
    this.data = data;
  }

  public void spawnParticle(Location location) {
    location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
  }
}