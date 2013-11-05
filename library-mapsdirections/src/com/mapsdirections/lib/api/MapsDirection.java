package com.mapsdirections.lib.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.mapsdirections.lib.DirectionProviderInterface;
import com.mapsdirections.lib.api.data.DirectionStep;
import com.mapsdirections.lib.api.data.Route;
import com.mapsdirections.lib.exceptions.DirectionsException;
import com.mapsdirections.lib.impl.DirectionResult;
import com.mapsdirections.lib.impl.GoogleMapsDirection;

public class MapsDirection {

	public static int DEFAULT_PROVIDER = 0;

	public static int GOOGLE_MAPS_PROVIDER = 1;

	private DirectionProviderInterface provider;
	private DirectionsCallbacks callback;
	private DirectionsAsyncTask directionsAsync;

	public static MapsDirection getDirectionsService(Context context,
			DirectionsCallbacks callbacks) {
		// context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return new MapsDirection(context, callbacks);
	}

	private MapsDirection(Context context, DirectionsCallbacks callback) {
		this.provider = new GoogleMapsDirection();
		this.callback = callback;
	}

	public void updateDirections(final Route route) {
		cancelAsync();

		this.directionsAsync = new DirectionsAsyncTask();
		this.directionsAsync.execute(route);
	}

	public void updateDirections(final Location origin, final Location destiny) {
		cancelAsync();

		final Route route = new Route();
		route.setOrigin(origin);
		route.setDestiny(destiny);

		this.directionsAsync = new DirectionsAsyncTask();
		this.directionsAsync.execute(route);
	}

	public void updateDirections(final Location origin,
			final Collection<Location> destiny) {
		cancelAsync();

		final Route[] routes = new Route[destiny.size()];
		Route route;
		int index = 0;

		for (Location location : destiny) {
			route = new Route();
			route.setOrigin(origin);
			route.setDestiny(location);

			routes[index] = route;
			index++;
		}

		this.directionsAsync = new DirectionsAsyncTask();
		this.directionsAsync.execute(routes);
	}

	public void onStop() {
		this.provider.stop();
	}

	private void cancelAsync() {
		if (this.directionsAsync != null) {
			this.directionsAsync.cancel(true);
		}
	}

	private class DirectionsAsyncTask extends
			AsyncTask<Route, Integer, List<DirectionResult>> {

		@Override
		protected List<DirectionResult> doInBackground(Route... params) {
			final List<DirectionResult> results = new ArrayList<DirectionResult>();
			DirectionResult result;

			if (params == null) {
				result = new DirectionResult();
				result.setResultType(DirectionsError.FATAL_ERROR);
				results.add(result);

				return results;
			}

			Route route;

			for (int i = 0; i < params.length; i++) {
				route = params[i];

				try {
					final List<DirectionStep> directions = provider
							.getDirections(route.getOrigin(),
									route.getDestiny());

					final List<Location> points = new ArrayList<Location>();

					for (DirectionStep step : directions) {
						points.addAll(step.getLocationsList());
					}

					result = new DirectionResult();
					result.setResultType(0);
					result.setDirections(directions);
					result.setPoints(points);

				} catch (DirectionsException e) {
					e.printStackTrace();

					result = new DirectionResult();
					result.setResultType(e.getDirectionError());
				}

				results.add(result);
			}

			return results;
		}

		@Override
		protected void onPostExecute(List<DirectionResult> results) {
			super.onPostExecute(results);
			DirectionResult result;

			for (int i = 0; i < results.size(); i++) {
				result = results.get(i);

				final int resultType = result.getResultType();

				if (resultType == 0) {
					callback.onDirectionsReceived(i, result.getDirections());
					callback.onDirectionsPointsReceived(i, result.getPoints());
				} else {
					callback.onDirectionsError(resultType);
				}
			}
		}
	}

	public interface DirectionsCallbacks {

		void onDirectionsReceived(int id, List<DirectionStep> directions);

		void onDirectionsPointsReceived(int id, List<Location> directions);

		void onDirectionsError(int directionError);
	}
}
