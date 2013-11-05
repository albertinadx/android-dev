package com.mapsdirections.lib.api.data;

import android.location.Location;

public class Route {

	private Location origin;
	private Location destiny;

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getDestiny() {
		return destiny;
	}

	public void setDestiny(Location destiny) {
		this.destiny = destiny;
	}
}
