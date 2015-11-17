package br.com.gumga.domain.tag;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;
import gumga.framework.domain.tag.GumgaTag;
import gumga.framework.domain.tag.GumgaTagValue;


/**
 * QGumgaTag is a Querydsl query type for GumgaTag
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGumgaTag extends EntityPathBase<GumgaTag> {

    private static final long serialVersionUID = -501273345L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGumgaTag gumgaTag = new QGumgaTag("gumgaTag");

    public final gumga.framework.domain.QGumgaModel _super = new gumga.framework.domain.QGumgaModel(this);

    public final QGumgaTagDefinition definition;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> objectId = createNumber("objectId", Long.class);

    public final StringPath objectType = createString("objectType");

    //inherited
    public final ComparablePath<gumga.framework.domain.domains.GumgaOi> oi = _super.oi;

    public final ListPath<GumgaTagValue, QGumgaTagValue> values = this.<GumgaTagValue, QGumgaTagValue>createList("values", GumgaTagValue.class, QGumgaTagValue.class, PathInits.DIRECT2);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QGumgaTag(String variable) {
        this(GumgaTag.class, forVariable(variable), INITS);
    }

    public QGumgaTag(Path<? extends GumgaTag> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGumgaTag(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGumgaTag(PathMetadata<?> metadata, PathInits inits) {
        this(GumgaTag.class, metadata, inits);
    }

    public QGumgaTag(Class<? extends GumgaTag> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.definition = inits.isInitialized("definition") ? new QGumgaTagDefinition(forProperty("definition")) : null;
    }

}

