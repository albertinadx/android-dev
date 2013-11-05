package com.mapsdirections.lib;

import java.util.List;

import android.location.Location;

import com.mapsdirections.lib.api.data.DirectionConfig;
import com.mapsdirections.lib.api.data.DirectionStep;
import com.mapsdirections.lib.exceptions.DirectionsException;

public interface DirectionProviderInterface {

	public List<DirectionStep> getDirections(Location origin,
			Location destiny) throws DirectionsException;

	public List<DirectionStep> getDirections(Location origin,
			Location destiny, DirectionConfig... params)
			throws DirectionsException;

	public String getDirections(Location origin, Location destiny,
			boolean enable) throws DirectionsException;

	public List<Location> getDirectionsPoints(Location origin, Location destiny)
			throws DirectionsException;

	public void stop();
}
