package onim.en.empirex.item.interfaces;

import org.bukkit.event.entity.ProjectileLaunchEvent;

public interface Shootable {

  public boolean onShoot(ProjectileLaunchEvent event);

}
