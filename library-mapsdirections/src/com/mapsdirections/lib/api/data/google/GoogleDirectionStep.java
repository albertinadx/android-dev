package com.mapsdirections.lib.api.data.google;

import java.util.ArrayList;
import java.util.List;

import com.mapsdirections.lib.api.data.DirectionStep;

import android.location.Location;

public class GoogleDirectionStep implements DirectionStep {

	private GoogleDirectionDistance distance;
	private Location startLocation;
	private Location endLocation;
	private List<Location> polyline;
	private String travelMode;

	public GoogleDirectionDistance getDistance() {
		return distance;
	}

	public void setDistance(GoogleDirectionDistance distance) {
		this.distance = distance;
	}

	@Override
	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public List<Location> getPolyline() {
		return polyline;
	}

	public void setPolyline(List<Location> polyline) {
		this.polyline = polyline;
	}

	@Override
	public Location getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
	}

	@Override
	public String getTravelMode() {
		return travelMode;
	}

	public void setTravelMode(String travelMode) {
		this.travelMode = travelMode;
	}

	@Override
	public String getDistanceText() {
		return this.distance.getText();
	}

	@Override
	public List<Location> getLocationsList() {
		final List<Location> locations = new ArrayList<Location>();

		locations.add(startLocation);
		locations.addAll(polyline);
		locations.add(endLocation);

		return locations;
	}

	@Override
	public String toString() {
		final StringBuilder values = new StringBuilder();
		values.append(distance.getText());
		values.append(" ");

		final List<Location> locations = getLocationsList();
		for (Location location : locations) {
			values.append(location.getLatitude());
			values.append(",");
			values.append(location.getLongitude());
			values.append("\n");
		}

		values.append(" ");
		values.append(travelMode);
		values.append("\n\n");

		return values.toString();
	}
}
