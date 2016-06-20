package gumga.framework.domain.customfields;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import static com.mysema.query.types.PathMetadataFactory.*;
import com.mysema.query.types.path.*;
import javax.annotation.Generated;

/**
 * QGumgaCustomField is a Querydsl query type for GumgaCustomField
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGumgaCustomField extends EntityPathBase<GumgaCustomField> {

    private static final long serialVersionUID = 671344271L;

    public static final QGumgaCustomField gumgaCustomField = new QGumgaCustomField("gumgaCustomField");

    public final gumga.framework.domain.QGumgaModel _super = new gumga.framework.domain.QGumgaModel(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath clazz = createString("clazz");

    public final StringPath defaultValueScript = createString("defaultValueScript");

    public final StringPath description = createString("description");

    public final StringPath fieldGroup = createString("fieldGroup");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final ComparablePath<gumga.framework.domain.domains.GumgaOi> oi = _super.oi;

    public final StringPath options = createString("options");

    public final EnumPath<CustomFieldType> type = createEnum("type", CustomFieldType.class);

    public final StringPath validationDescription = createString("validationDescription");

    public final StringPath validationScript = createString("validationScript");

    public final NumberPath<Double> visualizationOrder = createNumber("visualizationOrder", Double.class);

    public QGumgaCustomField(String variable) {
        super(GumgaCustomField.class, forVariable(variable));
    }

    public QGumgaCustomField(Path<? extends GumgaCustomField> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGumgaCustomField(PathMetadata<?> metadata) {
        super(GumgaCustomField.class, metadata);
    }

}
