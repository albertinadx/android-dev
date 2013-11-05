package com.customfragments.lib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

public class DefaultFragment extends Fragment {

	private View viewContent;
	private View viewProgress;
	private boolean contentShown;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ensureView();
		super.onViewCreated(view, savedInstanceState);
	}

	public void ensureView() {
		final View view = getView();

		if (viewProgress != null && viewContent != null) {
			return;
		}

		viewProgress = view.findViewById(R.id.progress_container);

		if (viewProgress == null) {
			viewProgress = LayoutInflater.from(getActivity()).inflate(
					R.layout.progress_container, null);
			((ViewGroup) view).addView(viewProgress);
		}

		viewContent = view.findViewById(R.id.content_container);

		viewContent.setVisibility(View.GONE);
		viewProgress.setVisibility(View.VISIBLE);
	}

	public void setContentShown(boolean shown) {
		ensureView();

		if (contentShown == shown) {
			return;
		}

		contentShown = shown;

		if (shown) {
			viewProgress.startAnimation(AnimationUtils.loadAnimation(
					getActivity(), android.R.anim.fade_out));
			viewContent.startAnimation(AnimationUtils.loadAnimation(
					getActivity(), android.R.anim.fade_in));

			viewProgress.setVisibility(View.GONE);
			viewContent.setVisibility(View.VISIBLE);
		} else {
			viewProgress.setVisibility(View.VISIBLE);
			viewContent.setVisibility(View.GONE);
		}
	}

}
