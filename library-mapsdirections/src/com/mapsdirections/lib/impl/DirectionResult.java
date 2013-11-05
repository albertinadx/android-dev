package com.mapsdirections.lib.impl;

import java.util.List;

import android.location.Location;

import com.mapsdirections.lib.api.data.DirectionStep;

public class DirectionResult {

	private int resultType;
	private List<DirectionStep> directions;
	private List<Location> points;

	public int getResultType() {
		return resultType;
	}

	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	public List<DirectionStep> getDirections() {
		return directions;
	}

	public void setDirections(List<DirectionStep> directions) {
		this.directions = directions;
	}

	public List<Location> getPoints() {
		return points;
	}

	public void setPoints(List<Location> points) {
		this.points = points;
	}
}
