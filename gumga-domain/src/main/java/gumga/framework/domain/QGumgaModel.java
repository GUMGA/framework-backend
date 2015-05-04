package gumga.framework.domain;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;
import gumga.framework.domain.domains.GumgaOi;

/**
 * QGumgaModel is a Querydsl query type for GumgaModel
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaModel extends EntityPathBase<GumgaModel<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1949626837L;

    public static final QGumgaModel gumgaModel = new QGumgaModel("gumgaModel");

    public final SimplePath<java.io.Serializable> id = createSimple("id", java.io.Serializable.class);

    public final StringPath oi = createString("descricao");

    @SuppressWarnings("all")
    public QGumgaModel(String variable) {
        super((Class) GumgaModel.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QGumgaModel(Path<? extends GumgaModel> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QGumgaModel(PathMetadata<?> metadata) {
        super((Class) GumgaModel.class, metadata);
    }

}
