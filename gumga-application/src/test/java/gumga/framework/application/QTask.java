package gumga.framework.application;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import static com.mysema.query.types.PathMetadataFactory.forVariable;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import gumga.framework.domain.domains.GumgaOi;

/**
 * Created by rafael on 08/04/15.
 */
public class QTask extends EntityPathBase<Task> {

    private static final long serialVersionUID = -1527314424L;

    public static final QTask task = new QTask("task");

    public final gumga.framework.domain.QGumgaModel _super = new gumga.framework.domain.QGumgaModel(this);

    public final StringPath name = createString("name");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public final NumberPath<java.math.BigDecimal> valor = createNumber("valor", java.math.BigDecimal.class);

    public QTask(String variable) {
        super(Task.class, forVariable(variable));
    }

    public QTask(Path<? extends Task> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTask(PathMetadata<?> metadata) {
        super(Task.class, metadata);
    }

}