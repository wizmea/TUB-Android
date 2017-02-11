package fr.bourgmapper.tub.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.bourgmapper.tub.R;
import fr.bourgmapper.tub.presentation.internal.di.components.StopComponent;
import fr.bourgmapper.tub.presentation.model.StopModel;
import fr.bourgmapper.tub.presentation.presenter.StopListPresenter;
import fr.bourgmapper.tub.presentation.view.adapter.StopListAdapter;
import fr.bourgmapper.tub.presentation.view.adapter.StopListLayoutManager;
import fr.bourgmapper.tub.presentation.view.StopListView;

/**
 * Fragment that shows a list of Stops.
 */
public class StopListFragment extends BaseFragment implements StopListView {
    /**
     * Interface for listening Stop list events.
     */
    public interface StopListListener {
        void onStopClicked(final StopModel userModel);
    }

    @Inject
    StopListPresenter stopListPresenter;
    @Inject
    StopListAdapter stopListAdapter;


    @BindView(R.id.list_stop_recycler_view)
    RecyclerView rv_stops;

    private StopListListener stopListListener;

    public StopListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof StopListListener) {
            this.stopListListener = (StopListListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(StopComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_stop_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupRecyclerView();
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.stopListPresenter.setView(this);
        if (savedInstanceState == null) {
            this.loadStopList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.stopListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.stopListPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rv_stops.setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopListPresenter.destroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.stopListListener = null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void renderStopList(Collection<StopModel> stopModelCollection) {
        if (stopModelCollection != null) {
            this.stopListAdapter.setStopCollection(stopModelCollection);
        }
    }

    @Override
    public void viewStop(StopModel stopModel) {
        if (this.stopListListener != null) {
            this.stopListListener.onStopClicked(stopModel);
        }
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context context() {
        return this.getActivity().getApplicationContext();
    }

    private void setupRecyclerView() {
        this.stopListAdapter.setOnItemClickListener(onItemClickListener);
        this.rv_stops.setLayoutManager(new StopListLayoutManager(context()));
        this.rv_stops.setAdapter(stopListAdapter);
    }

    /**
     * Loads all stops.
     */
    private void loadStopList() {
        this.stopListPresenter.initialize();
    }

    //TODO : Bind retry Button
    void onButtonRetryClick() {
        StopListFragment.this.loadStopList();
    }

    private StopListAdapter.OnItemClickListener onItemClickListener =
            new StopListAdapter.OnItemClickListener() {
                @Override
                public void onStopItemClicked(StopModel stopModel) {
                    if (StopListFragment.this.stopListPresenter != null && stopModel != null) {
                        StopListFragment.this.stopListPresenter.onStopClicked(stopModel);
                    }
                }
            };

}