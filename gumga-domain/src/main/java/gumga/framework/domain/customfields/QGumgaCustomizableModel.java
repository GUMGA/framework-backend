package gumga.framework.domain.customfields;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

/**
 * QGumgaCustomizableModel is a Querydsl query type for GumgaCustomizableModel
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaCustomizableModel extends EntityPathBase<GumgaCustomizableModel<? extends java.io.Serializable>> {

    private static final long serialVersionUID = -874622509L;

    public static final QGumgaCustomizableModel gumgaCustomizableModel = new QGumgaCustomizableModel("gumgaCustomizableModel");

    public final gumga.framework.domain.QGumgaModel _super = new gumga.framework.domain.QGumgaModel(this);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    //inherited
    public final ComparablePath<gumga.framework.domain.domains.GumgaOi> oi = _super.oi;

    @SuppressWarnings("all")
    public QGumgaCustomizableModel(String variable) {
        super((Class) GumgaCustomizableModel.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QGumgaCustomizableModel(Path<? extends GumgaCustomizableModel> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QGumgaCustomizableModel(PathMetadata<?> metadata) {
        super((Class) GumgaCustomizableModel.class, metadata);
    }

}
