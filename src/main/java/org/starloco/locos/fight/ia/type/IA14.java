package org.starloco.locos.fight.ia.type;

import org.starloco.locos.fight.Fight;
import org.starloco.locos.fight.Fighter;
import org.starloco.locos.fight.ia.AbstractIA;
import org.starloco.locos.fight.ia.util.Function;
import org.starloco.locos.fight.spells.Spell.SortStats;

/**
 * Created by Locos on 04/10/2015.
 */
public class IA14 extends AbstractIA  {

    public IA14(Fight fight, Fighter fighter, byte count) {
        super(fight, fighter, count);
    }

    @Override
    public void apply() {
        if (this.count > 0 && this.fighter.canPlay() && !this.stop) {
            if (!this.fighter.haveInvocation()) {
                if (!Function.INSTANCE.getinvocIfPossible(this.fight, this.fighter)) {
                    Fighter target = Function.INSTANCE.getgetNearestEnnemy(this.fight, this.fighter);
                    int value = Function.INSTANCE.getmoveToAttackIfPossible(this.fight, this.fighter), cellId = value - (value / 1000) * 1000;
                    SortStats spellStats = this.fighter.getMob().getSpells().get(value / 1000);

                    if (this.fight.canCastSpell1(this.fighter, spellStats, this.fighter.getCell(), cellId)) this.fight.tryCastSpell(this.fighter, spellStats, cellId);
                    else Function.INSTANCE.getmoveNearIfPossible(fight, this.fighter, target);
                }
            } else {
                Fighter target = Function.INSTANCE.getgetNearestEnnemy(this.fight, this.fighter);
                int value = Function.INSTANCE.getmoveToAttackIfPossible(this.fight, this.fighter), cellId = value - (value / 1000) * 1000;
                SortStats spellStats = this.fighter.getMob().getSpells().get(value / 1000);

                if (this.fight.canCastSpell1(this.fighter, spellStats, this.fighter.getCell(), cellId)) this.fight.tryCastSpell(this.fighter, spellStats, cellId);
                else Function.INSTANCE.getmoveNearIfPossible(this.fight, this.fighter, target);
            }

            addNext(this::decrementCount, 1000);
        } else {
            this.stop = true;
        }
    }
}