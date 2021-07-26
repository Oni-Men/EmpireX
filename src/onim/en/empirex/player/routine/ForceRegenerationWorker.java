package onim.en.empirex.player.routine;

import onim.en.empirex.EmpireX;
import onim.en.empirex.player.Civilian;

public class ForceRegenerationWorker implements Runnable {

  @Override
  public void run() {

    for (Civilian civilian : EmpireX.civilianFactory.all()) {
      civilian.addForce(civilian.getForceRegen());
    }

  }

}
