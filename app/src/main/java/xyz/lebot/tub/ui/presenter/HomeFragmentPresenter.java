package xyz.lebot.tub.ui.presenter;

import android.support.design.widget.BottomSheetBehavior;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.lebot.tub.App;
import xyz.lebot.tub.R;
import xyz.lebot.tub.data.model.LineModel;
import xyz.lebot.tub.data.model.StopModel;
import xyz.lebot.tub.ui.fragment.HomeFragment;
import xyz.lebot.tub.ui.navigator.Navigator;
import xyz.lebot.tub.ui.view.StopMapClusterItem;

/**
 * Created by axell on 05/11/2016.
 */

public class HomeFragmentPresenter implements Presenter {
    private static String TAG = "HomeFragmentPresenter";

    private final HomeFragment view;
    private final Navigator navigator;

    public HomeFragmentPresenter(final HomeFragment view, final Navigator navigator) {
        this.view = view;
        this.navigator = navigator;
    }

    @Override
    public void initialize() {

        view.getGoogleMap().getUiSettings().setMyLocationButtonEnabled(true);
        view.getGoogleMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                onMapClicked(latLng);
            }
        });
        view.getGoogleMap().setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                onMapDraged();
            }
        });

        int peekHeight = (int) view.getResources().getDimension(R.dimen.bottom_sheet_journey_search_top_bar_height);
        view.getmBottomSheetBehavior().setPeekHeight(peekHeight);
        view.getmBottomSheetBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        view.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        view.moveCamera(new LatLng(46.205539, 5.227177), 13f);
        addStopsClusterToMap();
        addLinesKMLToMap();
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    public void onStopClusterItemClicked(final StopMapClusterItem stopMapClusterItem) {
        view.getmClusterAdapter().setCurrentClusterItem(stopMapClusterItem);
    }

    public void onMapClicked(LatLng latLng) {
        this.view.getmBottomSheetBehavior().setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void onMapDraged() {
        this.view.getmBottomSheetBehavior().setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void addStopsClusterToMap() {
        //initMapWithStopsCLuster
        App.getInstance().getDataRepository().getAllStopsCall()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<StopModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.addStopsToMapWithCluster(App.getInstance().getDataRepository().getAllStopsCache());
                    }

                    @Override
                    public void onNext(List<StopModel> stopModels) {
                        view.addStopsToMapWithCluster(stopModels);
                    }
                });
    }

    private void addLinesKMLToMap() {
        App.getInstance().getDataRepository().getAllLinesCall()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LineModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<LineModel> lineModels) {

                        for (LineModel lineModel : lineModels) {
                            addLineKMLToMap(lineModel.getId());
                        }
                    }
                });
    }

    private void addLineKMLToMap(String id) {
        App.getInstance().getDataRepository().getLineKMLCall(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InputStream>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InputStream inputStream) {
                        view.addLineKMLToMap(inputStream);
                    }
                });
    }

}