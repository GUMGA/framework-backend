package gumga.framework.domain;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import static com.mysema.query.types.PathMetadataFactory.forVariable;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;
import javax.annotation.Generated;

/**
 * QGumgaModel is a Querydsl query type for GumgaModel
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaModel extends EntityPathBase<GumgaModel<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1949626837L;

    public static final QGumgaModel gumgaModel = new QGumgaModel("gumgaModel");

    public final SimplePath<java.io.Serializable> id = createSimple("id", java.io.Serializable.class);
    
 public final NumberPath<Integer> version = createNumber("version", Integer.class);
 
    public final StringPath oi = createString("oi");

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
