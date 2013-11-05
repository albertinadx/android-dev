package com.mapsdirections.lib.api.data;

import java.util.List;

import android.location.Location;

public interface DirectionStep {

	public String getDistanceText();

	public Location getStartLocation();

	public Location getEndLocation();

	public List<Location> getLocationsList();

	public String getTravelMode();
}
