package com.mapsdirections.lib.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.mapsdirections.lib.DirectionProviderInterface;
import com.mapsdirections.lib.api.DirectionsError;
import com.mapsdirections.lib.api.data.DirectionConfig;
import com.mapsdirections.lib.api.data.DirectionStep;
import com.mapsdirections.lib.api.data.google.DirectionsStatus;
import com.mapsdirections.lib.api.data.google.GoogleDirectionDistance;
import com.mapsdirections.lib.api.data.google.GoogleDirectionStep;
import com.mapsdirections.lib.exceptions.DirectionsException;

public class GoogleMapsDirection implements DirectionProviderInterface {

	private static final String BASE_URL = "http://maps.googleapis.com/maps/api/directions/json?";
	private static final String DIRECTIONS_URL = BASE_URL
			+ "origin=%s,%s&destination=%s,%s&sensor=false&units=metric&mode=driving";

	private HttpClient httpClient;

	public GoogleMapsDirection() {
		this.httpClient = new DefaultHttpClient();
	}

	@Override
	public List<DirectionStep> getDirections(Location origin, Location destiny)
			throws DirectionsException {
		final String url = String.format(DIRECTIONS_URL, origin.getLatitude(),
				origin.getLongitude(), destiny.getLatitude(),
				destiny.getLongitude());

		Log.i("MapsDirections", url);

		HttpResponse response = null;
		try {
			response = doGet(url);

			final int code = response.getStatusLine().getStatusCode();

			if (HttpStatus.SC_OK != code) {
				throw new DirectionsException(DirectionsError.HTTP_ERROR);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new DirectionsException(DirectionsError.HTTP_ERROR,
					e.getLocalizedMessage());
		}

		final HttpEntity entity = response.getEntity();

		try {
			return parseDirections(entity);
		} catch (ParseException e) {
			e.printStackTrace();

			throw new DirectionsException(DirectionsError.PARSING_ERROR);
		} catch (IOException e) {
			e.printStackTrace();

			throw new DirectionsException(DirectionsError.PARSING_ERROR);
		} catch (JSONException e) {
			e.printStackTrace();

			throw new DirectionsException(DirectionsError.PARSING_ERROR);
		}
	}

	@Override
	public List<DirectionStep> getDirections(Location origin, Location destiny,
			DirectionConfig... params) throws DirectionsException {
		// TODO Not implemented yet
		return null;
	}

	@Override
	public String getDirections(Location origin, Location destiny,
			boolean enable) throws DirectionsException {
		final List<DirectionStep> steps = getDirections(origin, destiny);

		if (steps != null) {
			final StringBuilder stepsValue = new StringBuilder();

			for (DirectionStep step : steps) {
				stepsValue.append(step.toString());
			}

			return stepsValue.toString();
		} else {
			return "steps == null";
		}

	}

	@Override
	public List<Location> getDirectionsPoints(Location origin, Location destiny)
			throws DirectionsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stop() {
		if (this.httpClient != null) {
			final ClientConnectionManager connection = this.httpClient
					.getConnectionManager();

			connection.shutdown();
		}
	}

	private List<DirectionStep> parseDirections(HttpEntity entity)
			throws ParseException, IOException, JSONException,
			DirectionsException {
		final String result = EntityUtils.toString(entity);

		final JSONArray routes = parseRoutes(result);

		final List<JSONObject> legs = parseLegsFromRoutes(routes);

		final List<JSONObject> jsonSteps = parseStepsArrayFromLegs(legs);

		return convertSteps(jsonSteps);
	}

	private JSONArray parseRoutes(String directionsResult)
			throws JSONException, DirectionsException {
		final JSONObject jsonObject = new JSONObject(directionsResult);
		final String status = jsonObject.getString("status");

		if (!DirectionsStatus.OK.equals(status)) {
			throw new DirectionsException(DirectionsError.PROVIDER_ERROR,
					status);
		}

		final JSONArray routes = (JSONArray) jsonObject.get("routes");

		Log.i("MapsDirections", "routes size =  " + routes.length());

		return routes;
	}

	private List<JSONObject> parseLegsFromRoutes(JSONArray routes)
			throws JSONException {
		final List<JSONObject> legs = new ArrayList<JSONObject>();

		JSONObject route = null;
		JSONArray legsArray = null;

		for (int i = 0; i < routes.length(); i++) {
			route = (JSONObject) routes.get(i);
			legsArray = (JSONArray) route.get("legs");

			for (int j = 0; j < legsArray.length(); j++) {
				legs.add(legsArray.getJSONObject(j));
			}
		}

		Log.i("MapsDirections", "legs size for route =  " + legs.size());

		return legs;
	}

	private List<JSONObject> parseStepsArrayFromLegs(List<JSONObject> legs)
			throws JSONException {
		final List<JSONObject> steps = new ArrayList<JSONObject>();

		JSONObject leg = null;
		JSONArray jsonArraySteps = null;

		for (int i = 0; i < legs.size(); i++) {
			leg = legs.get(i);
			jsonArraySteps = (JSONArray) leg.get("steps");

			for (int n = 0; n < jsonArraySteps.length(); n++) {
				steps.add(jsonArraySteps.getJSONObject(n));
			}
		}

		Log.i("MapsDirections", "total steps size =  " + steps.size());

		return steps;
	}

	private List<Location> decodePolylineFromStep(String encoded) {
		final List<Location> poly = new ArrayList<Location>();
		int len = encoded.length();
		int index = 0, lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;

			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);

			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));

			lat += dlat;
			shift = 0;
			result = 0;

			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);

			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			final Location position = new Location("");
			position.setLatitude((double) lat / 1E5);
			position.setLongitude((double) lng / 1E5);

			poly.add(position);
		}

		return poly;
	}

	private List<DirectionStep> convertSteps(List<JSONObject> jsonSteps)
			throws JSONException {
		final List<DirectionStep> steps = new ArrayList<DirectionStep>();
		JSONObject aux;
		GoogleDirectionStep step;
		GoogleDirectionDistance distance;
		Location location;
		List<Location> polylines;
		double lat, lng;

		for (JSONObject jsonStep : jsonSteps) {
			step = new GoogleDirectionStep();

			// start location
			aux = jsonStep.getJSONObject("start_location");
			lat = aux.getDouble("lat");
			lng = aux.getDouble("lng");

			location = new Location("");
			location.setLatitude(lat);
			location.setLongitude(lng);

			step.setStartLocation(location);

			// polylines
			aux = jsonStep.getJSONObject("polyline");
			polylines = decodePolylineFromStep(aux.getString("points"));

			step.setPolyline(polylines);

			// end location
			aux = jsonStep.getJSONObject("end_location");
			lat = aux.getDouble("lat");
			lng = aux.getDouble("lng");

			location = new Location("");
			location.setLatitude(lat);
			location.setLongitude(lng);

			step.setEndLocation(location);

			// distance
			aux = jsonStep.getJSONObject("distance");
			distance = new GoogleDirectionDistance();
			distance.setText(aux.getString("text"));
			distance.setValue(aux.getInt("value"));

			step.setDistance(distance);

			// travel mode
			step.setTravelMode(jsonStep.getString("travel_mode"));

			steps.add(step);
		}

		return steps;
	}

	private HttpResponse doGet(final String url)
			throws ClientProtocolException, IOException {
		final HttpGet httpGet = new HttpGet(url);

		try {
			return this.httpClient.execute(httpGet);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}
}
