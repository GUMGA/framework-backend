package br.com.gumga.domain.tag;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;
import gumga.framework.domain.tag.GumgaTagDefinition;


/**
 * QGumgaTagDefinition is a Querydsl query type for GumgaTagDefinition
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGumgaTagDefinition extends EntityPathBase<GumgaTagDefinition> {

    private static final long serialVersionUID = -532591694L;

    public static final QGumgaTagDefinition gumgaTagDefinition = new QGumgaTagDefinition("gumgaTagDefinition");

    public final gumga.framework.domain.QGumgaModel _super = new gumga.framework.domain.QGumgaModel(this);

    public final ListPath<GumgaTagDefinition, QGumgaTagDefinition> attributes = this.<GumgaTagDefinition, QGumgaTagDefinition>createList("attributes", GumgaTagDefinition.class, QGumgaTagDefinition.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final ComparablePath<gumga.framework.domain.domains.GumgaOi> oi = _super.oi;

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QGumgaTagDefinition(String variable) {
        super(GumgaTagDefinition.class, forVariable(variable));
    }

    public QGumgaTagDefinition(Path<? extends GumgaTagDefinition> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGumgaTagDefinition(PathMetadata<?> metadata) {
        super(GumgaTagDefinition.class, metadata);
    }

}

