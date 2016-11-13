package xyz.lebot.tub.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.lebot.tub.R;
import xyz.lebot.tub.data.model.LineModel;
import xyz.lebot.tub.ui.navigator.Navigator;
import xyz.lebot.tub.ui.presenter.LineDetailFragmentPresenter;

public class LineDetailFragment extends Fragment {


    @BindView(R.id.fragment_line_detail_layout)
    LinearLayoutCompat linearLayout;

    @BindView(R.id.fragment_line_detail_number)
    TextView tvNumber;

    @BindView(R.id.fragment_line_detail_label)
    TextView tvLabel;


    private Navigator navigator;
    private String lineId;
    private LineDetailFragmentPresenter presenter;


    public LineDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void setArguments(Bundle args) {
        this.navigator = (Navigator) args.get("NAVIGATOR");
        this.lineId=(String) args.get("LINE_ID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_detail, container, false);
        ButterKnife.bind(this, view);

        presenter = new LineDetailFragmentPresenter(this, navigator);
        presenter.initialize();
        presenter.initView(this.lineId);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }

    public void initView(LineModel lineModel) {
        int color = Color.parseColor(lineModel.getColor());

        GradientDrawable bgShape = (GradientDrawable) tvNumber.getBackground();
        bgShape.setColor(color);

        tvNumber.setText(lineModel.getNumber());
        tvLabel.setText(lineModel.getLabel());
    }
}
