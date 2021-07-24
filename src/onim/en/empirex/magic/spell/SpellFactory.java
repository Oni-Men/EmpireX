package onim.en.empirex.magic.spell;

import onim.en.empirex.base.FactoryBase;
import onim.en.empirex.magic.spell.ball.SpellFireBall;
import onim.en.empirex.magic.spell.ball.SpellFrozenBall;
import onim.en.empirex.magic.spell.ball.SpellHealBall;
import onim.en.empirex.magic.spell.impl.SpellCollect;
import onim.en.empirex.magic.spell.impl.SpellFangLance;
import onim.en.empirex.magic.spell.impl.SpellFangWall;
import onim.en.empirex.magic.spell.impl.SpellGrabbing;
import onim.en.empirex.magic.spell.impl.SpellHarvest;
import onim.en.empirex.magic.spell.impl.SpellLightUp;
import onim.en.empirex.magic.spell.impl.SpellTeleport;
import onim.en.empirex.magic.spell.impl.SpellVanishing;

public class SpellFactory extends FactoryBase<Spell> {

  @Override
  protected void init() {
    set(new SpellTeleport());
    set(new SpellFangLance());
    set(new SpellFangWall());
    set(new SpellVanishing());
    set(new SpellFireBall());
    set(new SpellFrozenBall());
    set(new SpellHealBall());
    set(new SpellGrabbing());
    set(new SpellHarvest());
    set(new SpellCollect());
    set(new SpellLightUp());
  }

  @Override
  protected Spell value(String key) {
    return null;
  }

}
