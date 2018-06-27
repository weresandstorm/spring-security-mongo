/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.konglong.springsecurity.domain.Entity;
import me.konglong.springsecurity.domain.Repo;
import me.konglong.springsecurity.domain.RepositoryException;
import org.bson.conversions.Bson;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class MgoRepoBase<TDoc extends Entity<TID>, TID>
    implements Repo<TDoc, TID>, ApplicationContextAware {

  protected static final String ID = "_id";

  private MongoCollection<TDoc> coll;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    MongoHolder mongoHolder = applicationContext.getBean(MongoHolder.class);
    initialize(mongoHolder);
  }

  public void initialize(MongoHolder mongoHolder) {
    Class<TDoc> typeOfActualDoc =
        (Class<TDoc>)
            ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    this.coll = mongoHolder.targetDb().getCollection(collectionName(), typeOfActualDoc);
  }

  public abstract String collectionName();

  /**
   * Inserts the given document. If the document is missing an identifier, the driver should
   * generate one.
   */
  @Override
  public TDoc save(TDoc doc) {
    coll.insertOne(doc);
    return doc;
  }

  /**
   * Replaces a whole document in the collection with the specified new doc according to the given
   * {@code newDoc}'s id.
   */
  @Override
  public boolean update(TDoc newDoc) {
    return coll.replaceOne(eq(ID, newDoc.id()), newDoc).wasAcknowledged();
  }

  /** Deletes the only one document with the given id. */
  @Override
  public boolean delete(TID id) {
    return coll.deleteOne(eq(ID, id)).wasAcknowledged();
  }

  /**
   * Retrieves the document with given id and translates the document to a POJO. If the document is
   * absent, this method will throw an {@link RepositoryException}.
   */
  @Override
  public TDoc get(TID id) {
    Optional<TDoc> theDoc = getOptional(id);
    if (theDoc.isPresent()) {
      return theDoc.get();
    }
    throw new RepositoryException(String.format("No such doc with id:%s", id));
  }

  /**
   * Retrieves the document with given id, and whether or not the result is null, wraps it in {@link
   * Optional}.
   */
  @Override
  public Optional<TDoc> getOptional(TID id) {
    return Optional.ofNullable(coll.find(eq(ID, id)).first());
  }

  @Override
  public List<TDoc> findAll() {
    List<TDoc> target = new ArrayList<>(20);
    return coll.find().into(target);
  }

  protected final TDoc findOne(Bson filter) {
    return coll.find(filter).first();
  }

  protected final List<TDoc> find(Bson filter) {
    List<TDoc> target = new ArrayList<>(20);
    return coll.find(filter).into(target);
  }

  /**
   * Applies the specified {@code update} to a single document in the collection according to the
   * given {@code id}.
   */
  protected boolean update(TID id, Bson update) {
    return coll.updateOne(eq(ID, id), update).wasAcknowledged();
  }

  protected boolean updateOne(Bson filter, Bson update) {
    return coll.updateOne(filter, update).wasAcknowledged();
  }

  protected boolean insertOrUpdate(Bson filter, Bson update) {
    UpdateOptions options = new UpdateOptions().upsert(true);
    return coll.updateMany(filter, update, options).wasAcknowledged();
  }

  protected final boolean delete(Bson filter) {
    return coll.deleteMany(filter).wasAcknowledged();
  }
}
