package fr.bourgmapper.tub.presentation.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import fr.bourgmapper.tub.domain.Line;
import fr.bourgmapper.tub.domain.exception.DefaultErrorBundle;
import fr.bourgmapper.tub.domain.exception.ErrorBundle;
import fr.bourgmapper.tub.domain.interactor.DefaultObserver;
import fr.bourgmapper.tub.domain.interactor.GetLineDetails;
import fr.bourgmapper.tub.domain.interactor.GetLineDetails.Params;
import fr.bourgmapper.tub.presentation.exception.ErrorMessageFactory;
import fr.bourgmapper.tub.presentation.internal.di.PerFragment;
import fr.bourgmapper.tub.presentation.mapper.LineModelDataMapper;
import fr.bourgmapper.tub.presentation.model.LineModel;
import fr.bourgmapper.tub.presentation.view.LineDetailsView;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerFragment
public class LineDetailsFragmentPresenter implements Presenter {
    private static String TAG = "LineDetailPresenter";

    private final GetLineDetails getLineDetailsUseCase;
    private final LineModelDataMapper lineModelDataMapper;
    private LineDetailsView viewDetailsView;

    @Inject
    LineDetailsFragmentPresenter(GetLineDetails getLineDetailsUseCase,
                                 LineModelDataMapper lineModelDataMapper) {
        this.getLineDetailsUseCase = getLineDetailsUseCase;
        this.lineModelDataMapper = lineModelDataMapper;
    }

    public void setView(@NonNull LineDetailsView view) {
        this.viewDetailsView = view;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.viewDetailsView = null;
        this.getLineDetailsUseCase.dispose();
    }

    /**
     * Initializes the presenter by showing/hiding proper views
     * and retrieving line details.
     */
    public void initialize(long lineId) {
        this.hideViewRetryLineDetails();
        this.showViewLoadingLineDetails();
        this.getLineDetails(lineId);
    }

    private void getLineDetails(long lineId) {
        this.getLineDetailsUseCase.execute(new LineDetailsObserver(), Params.forLine(lineId));
    }

    private void showViewLoadingLineDetails() {
        this.viewDetailsView.showLoadingLineDetails();
    }

    private void hideViewLoadingLineDetails() {
        this.viewDetailsView.hideLoadingLineDetails();
    }

    private void showViewRetryLineDetails() {
        this.viewDetailsView.showRetryLineDetails();
    }

    private void hideViewRetryLineDetails() {
        this.viewDetailsView.hideRetryLineDetails();
    }

    private void showErrorLineDetailsMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.viewDetailsView.context(),
                errorBundle.getException());
        this.viewDetailsView.showErrorLineDetails(errorMessage);
    }

    private void showLineDetailsInView(Line line) {
        final LineModel lineModel = this.lineModelDataMapper.transform(line);
        this.viewDetailsView.renderLine(lineModel);
    }

    private final class LineDetailsObserver extends DefaultObserver<Line> {

        @Override
        public void onComplete() {
            LineDetailsFragmentPresenter.this.hideViewLoadingLineDetails();
        }

        @Override
        public void onError(Throwable e) {
            LineDetailsFragmentPresenter.this.hideViewLoadingLineDetails();
            LineDetailsFragmentPresenter.this.showErrorLineDetailsMessage(new DefaultErrorBundle((Exception) e));
            LineDetailsFragmentPresenter.this.showViewRetryLineDetails();
        }

        @Override
        public void onNext(Line line) {
            LineDetailsFragmentPresenter.this.showLineDetailsInView(line);
        }
    }
}