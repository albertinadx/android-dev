package com.mapsdirections.lib.api.data.google;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class LatLngUtils {

	public static LatLng convertLocationToLatLng(Location location) {
		LatLng latlng = new LatLng(location.getLatitude(),
				location.getLongitude());

		return latlng;
	}

	public static List<LatLng> convertLocationToLatLng(List<Location> locations) {
		final List<LatLng> latlngs = new ArrayList<LatLng>();

		for (Location location : locations) {
			latlngs.add(convertLocationToLatLng(location));
		}

		return latlngs;
	}

	public static Location convertLatLngToLocation(LatLng latlng) {
		final Location location = new Location("");
		location.setLatitude(latlng.latitude);
		location.setLongitude(latlng.longitude);

		return location;
	}
}
