package com.chemicaltracker.service.util;

import com.chemicaltracker.persistence.model.Chemical;

import java.util.List;

public class FireDiamondEvaluator {

    public boolean checkFlammability(List<Chemical> chemicals) {
        for (Chemical chemical : chemicals) {
            if (chemical.getFireDiamond().getFlammability() > 0) {
                return true; // only need one chemical to be flammable
            }
        }
        return false;
    }

    public boolean checkHealth(List<Chemical> chemicals) {
        for (Chemical chemical : chemicals) {
            if (chemical.getFireDiamond().getHealth() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean checkInstability(List<Chemical> chemicals) {
        for (Chemical chemical : chemicals) {
            if (chemical.getFireDiamond().getInstability() > 0) {
                return true;
            }
        }
        return false;
    }
}