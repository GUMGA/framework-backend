package br.com.gumga.domain.tag;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import static com.mysema.query.types.PathMetadataFactory.*;
import com.mysema.query.types.path.*;
import gumga.framework.domain.tag.GumgaTagValueDefinition;
import javax.annotation.Generated;

/**
 * QGumgaTagValueDefinition is a Querydsl query type for GumgaTagValueDefinition
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGumgaTagValueDefinition extends EntityPathBase<GumgaTagValueDefinition> {

    private static final long serialVersionUID = 1597724325L;

    public static final QGumgaTagValueDefinition gumgaTagValueDefinition = new QGumgaTagValueDefinition("gumgaTagValueDefinition");

    public final gumga.framework.domain.QGumgaModel _super = new gumga.framework.domain.QGumgaModel(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final ComparablePath<gumga.framework.domain.domains.GumgaOi> oi = _super.oi;

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QGumgaTagValueDefinition(String variable) {
        super(GumgaTagValueDefinition.class, forVariable(variable));
    }

    public QGumgaTagValueDefinition(Path<? extends GumgaTagValueDefinition> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGumgaTagValueDefinition(PathMetadata<?> metadata) {
        super(GumgaTagValueDefinition.class, metadata);
    }

}
