package fr.bourgmapper.tub.presentation.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fernandocejas.arrow.checks.Preconditions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.bourgmapper.tub.R;
import fr.bourgmapper.tub.presentation.internal.di.components.LineComponent;
import fr.bourgmapper.tub.presentation.model.LineModel;
import fr.bourgmapper.tub.presentation.presenter.LineDetailsPresenter;
import fr.bourgmapper.tub.presentation.view.LineDetailsView;

public class LineDetailsFragment extends BaseFragment implements LineDetailsView {
    private static final String PARAM_LINE_ID = "param_line_id";

    @Inject
    LineDetailsPresenter lineDetailsPresenter;

    @BindView(R.id.fragment_line_detail_layout)
    LinearLayoutCompat linearLayout;

    @BindView(R.id.fragment_line_detail_number)
    TextView tvNumber;

    @BindView(R.id.fragment_line_detail_label)
    TextView tvLabel;

    public static LineDetailsFragment forLine(int lineId) {
        final LineDetailsFragment lineDetailsFragment = new LineDetailsFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt(PARAM_LINE_ID, lineId);
        lineDetailsFragment.setArguments(arguments);
        return lineDetailsFragment;
    }

    public LineDetailsFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(LineComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_line_details, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.lineDetailsPresenter.setView(this);
        if (savedInstanceState == null) {
            this.loadLineDetails();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.lineDetailsPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.lineDetailsPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.lineDetailsPresenter.destroy();
    }

    public void renderLine(LineModel line) {
        if (line != null) {
            int color = Color.parseColor(line.getColor());

            GradientDrawable bgShape = (GradientDrawable) tvNumber.getBackground();
            bgShape.setColor(color);

            tvNumber.setText(line.getNumber());
            tvLabel.setText(line.getLabel());
        }
    }

    @Override
    public void showLoadingLineDetails() {
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoadingLineDetails() {
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showRetryLineDetails() {
    }

    @Override
    public void hideRetryLineDetails() {
    }

    @Override
    public void showErrorLineDetails(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context context() {
        return getActivity().getApplicationContext();
    }

    /**
     * Load line details.
     */
    private void loadLineDetails() {
        if (this.lineDetailsPresenter != null) {
            this.lineDetailsPresenter.initialize(currentLineId());
        }
    }

    /**
     * Get current line id from fragments arguments.
     */
    private int currentLineId() {
        final Bundle arguments = getArguments();
        Preconditions.checkNotNull(arguments, "Fragment arguments cannot be null");
        return arguments.getInt(PARAM_LINE_ID);
    }

    //TODO : Add retry btn
    void onButtonRetryClick() {
        LineDetailsFragment.this.loadLineDetails();
    }
}

