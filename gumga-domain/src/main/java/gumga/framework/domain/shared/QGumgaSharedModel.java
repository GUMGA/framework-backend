package gumga.framework.domain.shared;

import gumga.framework.domain.shared.*;
import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QGumgaSharedModel is a Querydsl query type for GumgaSharedModel
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaSharedModel extends EntityPathBase<GumgaSharedModel<? extends java.io.Serializable>> {

    private static final long serialVersionUID = -1157887249L;

    public static final QGumgaSharedModel gumgaSharedModel = new QGumgaSharedModel("gumgaSharedModel");

    public final gumga.framework.domain.QGumgaModel _super = new gumga.framework.domain.QGumgaModel(this);

    public final StringPath gumgaOrganizations = createString("gumgaOrganizations");

    public final StringPath gumgaUsers = createString("gumgaUsers");

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    //inherited
    public final ComparablePath<gumga.framework.domain.domains.GumgaOi> oi = _super.oi;

    @SuppressWarnings("all")
    public QGumgaSharedModel(String variable) {
        super((Class)GumgaSharedModel.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QGumgaSharedModel(Path<? extends GumgaSharedModel> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QGumgaSharedModel(PathMetadata<?> metadata) {
        super((Class)GumgaSharedModel.class, metadata);
    }

}

