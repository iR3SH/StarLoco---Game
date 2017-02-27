package org.starloco.locos.fight.ia.type;

import org.starloco.locos.fight.Fight;
import org.starloco.locos.fight.Fighter;
import org.starloco.locos.fight.ia.AbstractNeedSpell;
import org.starloco.locos.fight.ia.util.Function;
import org.starloco.locos.fight.spells.Spell;

/**
 * Created by Locos on 04/10/2015.
 */
public class IA54 extends AbstractNeedSpell  {

    private byte attack = 0;
    
    public IA54(Fight fight, Fighter fighter, byte count) {
        super(fight, fighter, count);
    }

    @Override
    public void apply() {
        if (!this.stop && this.fighter.canPlay() && this.count > 0) {
            int time = 100, maxPo = 1;
            boolean action = false;

            for(Spell.SortStats spellStats : this.highests)
                if(spellStats.getMaxPO() > maxPo)
                    maxPo = spellStats.getMaxPO();

            Fighter E = Function.INSTANCE.getgetNearestEnnemy(this.fight, this.fighter);
            Fighter L = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 1, maxPo + 1);// pomax +1;
            Fighter C = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 0, 2);//2 = po min 1 + 1;

            if(maxPo == 1) L = null;
            if(C != null && C.isHide()) C = null;
            if(L != null && L.isHide()) L = null;

            if(this.fighter.getCurPm(this.fight) > 0 && C == null) {
                int value = Function.INSTANCE.getmoveautourIfPossible(this.fight, this.fighter, E);
                if(value != 0) {
                    time = value;
                    action = true;
                    L = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 1, maxPo + 1);// pomax +1;
                    C = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 0, 2);//2 = po min 1 + 1;
                    if(maxPo == 1)
                        L = null;
                }
            }
            if(this.fighter.getCurPm(this.fight) > 0 && C != null && !action && this.attack > 0) {
                int value = Function.INSTANCE.getmoveFarIfPossible(this.fight, this.fighter);
                if(value != 0) {
                    time = value;
                    action = true;
                    L = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 1, maxPo + 1);// pomax +1;
                    C = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 0, 2);//2 = po min 1 + 1;
                    if(maxPo == 1)
                        L = null;
                }
            }
            if(this.fighter.getCurPa(this.fight) > 0 && !action) {
                if (Function.INSTANCE.getinvocIfPossible(this.fight, this.fighter, this.invocations)) {
                    time = 2000;
                    action = true;
                }
            }
            if(this.fighter.getCurPa(this.fight) > 0 && L != null && C == null && !action) {
                int value = Function.INSTANCE.getattackIfPossible(this.fight, this.fighter, this.highests);
                if(value != 0) {
                    time = value;
                    action = true;
                }
            } else if(this.fighter.getCurPa(this.fight) > 0 && C != null && !action) {
                int value = Function.INSTANCE.getattackIfPossible(this.fight, this.fighter, this.cacs);
                if(value != 0) {
                    this.attack++;
                    time = value;
                    action = true;
                }
            }
            if(this.fighter.getCurPa(this.fight) > 0 && C != null && !action) {
                int value = Function.INSTANCE.getattackIfPossible(this.fight, this.fighter, this.highests);
                if(value != 0) {
                    time = value;
                    action = true;
                }
            }
            if(this.fighter.getCurPm(this.fight) > 0 && !action) {
                int value = Function.INSTANCE.getmoveFarIfPossible(this.fight, this.fighter);
                if(value != 0) time = value;
            }

            if(this.fighter.getCurPa(this.fight) == 0 && this.fighter.getCurPm(this.fight) == 0) this.stop = true;
            addNext(this::decrementCount, time);
        } else {
            this.stop = true;
        }
    }
}