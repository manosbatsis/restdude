package com.restdude.domain.base.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by manos on 25/12/2016.
 */
public interface IEmbeddableManyToManyId<L extends CalipsoPersistable<LID>, LID extends Serializable, R extends CalipsoPersistable<RID>, RID extends Serializable> {
    void init(@NotNull String value);

    void init(LID left, @NotNull RID right);

    void init(L left, @NotNull R right);

    String toStringRepresentation();

    L getLeft();

    void setLeft(L left);

    R getRight();

    void setRight(R right);
}
