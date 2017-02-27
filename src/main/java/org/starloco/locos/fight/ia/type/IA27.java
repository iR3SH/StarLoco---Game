package org.starloco.locos.fight.ia.type;

import org.starloco.locos.fight.Fight;
import org.starloco.locos.fight.Fighter;
import org.starloco.locos.fight.ia.AbstractNeedSpell;
import org.starloco.locos.fight.ia.util.Function;
import org.starloco.locos.fight.spells.Spell.SortStats;

/**
 * Created by Locos on 04/10/2015.
 */
public class IA27 extends AbstractNeedSpell  {

    public IA27(Fight fight, Fighter fighter, byte count) {
        super(fight, fighter, count);
    }

    @Override
    public void apply() {
        if (!this.stop && this.fighter.canPlay() && this.count > 0) {
            int time = 100, maxPo = 1;
            boolean action = false;
            Fighter E = Function.INSTANCE.getgetNearestEnnemy(this.fight, this.fighter);

            for(SortStats spellStats : this.highests)
                if(spellStats != null && spellStats.getMaxPO() > maxPo)
                    maxPo = spellStats.getMaxPO();

            Fighter firstEnnemy = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 1, maxPo + 1);
            Fighter secondEnnemy = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 0, 2);

            if(maxPo == 1) firstEnnemy = null;
            if(secondEnnemy != null) if(secondEnnemy.isHide()) secondEnnemy = null;
            if(firstEnnemy != null) if(firstEnnemy.isHide()) firstEnnemy = null;

            if(this.fighter.getCurPm(this.fight) > 0 && firstEnnemy == null && secondEnnemy == null) {
                int num = Function.INSTANCE.getmoveautourIfPossible(this.fight, this.fighter, E);
                if(num != 0) {
                    time = num;
                    action = true;
                    firstEnnemy = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 1, maxPo + 1);
                    secondEnnemy = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 0, 2);
                    if(maxPo == 1) firstEnnemy = null;
                }
            }

            if(this.fighter.getCurPa(this.fight) > 0 && !action) {
                if (Function.INSTANCE.getinvocIfPossible(this.fight, this.fighter, this.invocations)) {
                    time = 2000;
                    action = true;
                }
            }

            if(this.fighter.getCurPa(this.fight) > 0 && firstEnnemy != null && secondEnnemy == null && !action) {
                int num = Function.INSTANCE.getattackIfPossible(this.fight, this.fighter, this.highests);
                if(num != 0) {
                    time = num;
                    action = true;
                }
            } else if(this.fighter.getCurPa(this.fight) > 0 && secondEnnemy != null && !action) {
                int num = Function.INSTANCE.getattackIfPossible(this.fight, this.fighter, this.cacs);
                if(num != 0) {
                    time = num;
                    action = true;
                }
            }

            if(this.fighter.getCurPa(this.fight) > 0 && secondEnnemy != null && !action) {
                int num = Function.INSTANCE.getattackIfPossible(this.fight, this.fighter, this.highests);
                if(num != 0) {
                    time = num;
                    action = true;
                }
            }

            if(this.fighter.getCurPm(this.fight) > 0 && !action) {
                int num = Function.INSTANCE.getmoveautourIfPossible(this.fight, this.fighter, E);
                if(num != 0) time = num;
            }

            if(this.fighter.getCurPa(this.fight) == 0 && this.fighter.getCurPm(this.fight) == 0) this.stop = true;
            addNext(this::decrementCount, time);
        } else {
            this.stop = true;
        }
    }
}