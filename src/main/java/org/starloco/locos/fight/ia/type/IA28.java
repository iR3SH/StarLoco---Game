package org.starloco.locos.fight.ia.type;

import org.starloco.locos.common.PathFinding;
import org.starloco.locos.fight.Fight;
import org.starloco.locos.fight.Fighter;
import org.starloco.locos.fight.ia.AbstractIA;
import org.starloco.locos.fight.ia.util.Function;

/**
 * Created by Locos on 04/10/2015.
 */
public class IA28 extends AbstractIA  {

    private boolean tp = false, invoc = false;

    public IA28(Fight fight, Fighter fighter, byte count) {
        super(fight, fighter, count);
    }

    @Override
    public void apply() {
        if (!this.stop && this.fighter.canPlay() && this.count > 0) {
            Fighter target = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 0, 11);
            int time = 1000, dist = 1000;

            if(target == null) {
                for (Fighter t : Function.INSTANCE.getgetLowHpEnnemyList(this.fight, this.fighter).values()) {
                    if (t != null && !t.isHide()) {
                        int tDist = PathFinding.getDistanceBetweenTwoCase(this.fight.getMap(), this.fighter.getCell(), t.getCell());
                        if (tDist < dist) {
                            target = t;
                            dist = tDist;
                        }
                    }
                }
            }

            if(dist == 1000) {
                if (target != null) {
                    dist = PathFinding.getDistanceBetweenTwoCase(this.fight.getMap(), this.fighter.getCell(), target.getCell());
                } else {
                    target = Function.INSTANCE.getgetNearestEnnemynbrcasemax(this.fight, this.fighter, 0, 50);
                    dist = PathFinding.getDistanceBetweenTwoCase(this.fight.getMap(), this.fighter.getCell(), target.getCell());
                }
            }

            if (!this.invoc && Function.INSTANCE.gettryTurtleInvocation(this.fight, this.fighter)) {
                time = 1500;
                this.invoc = true;
            }

            if(!this.tp && Function.INSTANCE.getTPIfPossiblesphinctercell(this.fight, this.fighter, target)) {
                this.tp = true;
                time = 400;
            }
            if(dist <= 5 && this.fighter.getCurPm(this.fight) >= dist && Function.INSTANCE.getmoveNearIfPossible(this.fight, this.fighter, target)) {
                time = 600;
            }
            if(!this.tp && Function.INSTANCE.getTPIfPossiblesphinctercell(this.fight, this.fighter, target)) {
                this.tp = true;
                time = 400;
            }

            if(Function.INSTANCE.getattackIfPossiblesphinctercell(this.fight, this.fighter, target) == 0) {
                time = 800;
            }

            this.addNext(this::decrementCount, time);
        } else {
            this.stop = true;
        }
    }
}