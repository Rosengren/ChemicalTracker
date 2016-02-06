package com.chemicaltracker.util;

import java.util.List;
import com.chemicaltracker.model.storage.*;

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