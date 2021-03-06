package fr.bourgmapper.tub.data.entity.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.bourgmapper.tub.data.entity.LineEntity;
import fr.bourgmapper.tub.domain.Line;

/**
 * Mapper class used to transform {@link LineEntity} (in the data layer) to {@link Line} in the
 * domain layer.
 */
@Singleton
public class LineEntityDataMapper {
    @Inject
    LineEntityDataMapper() {
    }

    public Line transform(LineEntity lineEntity) {
        Line lineModel = new Line(lineEntity.lineId);
        lineModel.setLabel(lineEntity.label);
        lineModel.setNumber(lineEntity.number);
        lineModel.setColor(lineEntity.color);
        lineModel.setKmlPath(lineEntity.kmlPath);
        return lineModel;
    }

    public List<Line> transform(List<LineEntity> lineEntities) {
        List<Line> listModels = new ArrayList<>();
        for (LineEntity lineEntitie : lineEntities) {
            listModels.add(transform(lineEntitie));
        }
        return listModels;
    }
}
