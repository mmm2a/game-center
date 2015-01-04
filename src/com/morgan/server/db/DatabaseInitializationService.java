package com.morgan.server.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.PersistService;
import com.morgan.server.util.log.AdvancedLogger;

/**
 * A GUICE service that will create SQL tables using the given persistence unit if necessary, but
 * will not fatally fail if the tables already exists.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DatabaseInitializationService {
  private static final AdvancedLogger logger = new AdvancedLogger(
      DatabaseInitializationService.class);

  private final PersistService persistService;

  private void handleDatabaseException(DatabaseException e) {
    if (e.getDatabaseErrorCode() == 20000) {
      logger.warning("Database Table/Statement already exists!");
    } else {
      throw e;
    }
  }

  private void handlePersistenceException(PersistenceException e) {
    Throwable cause = e.getCause();
    if (cause instanceof DatabaseException) {
      handleDatabaseException((DatabaseException) cause);
    } else {
      Throwables.propagate(e);
    }
  }

  private void executeCreateStatement(EntityManager entityManager, String sqlStatement) {
    logger.info("Attempting to execute \"%s\"", sqlStatement);

    Query q = entityManager.createNativeQuery(sqlStatement);

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      q.executeUpdate();
      transaction.commit();
    } catch (DatabaseException e) {
      transaction.rollback();
      handleDatabaseException(e);
    } catch (PersistenceException e) {
      transaction.rollback();
      handlePersistenceException(e);
    }
  }

  @Inject DatabaseInitializationService(PersistService persistService,
    Provider<EntityManager> entityManagerProvider) {

    this.persistService = persistService;

    this.persistService.start();

    EntityManager entityManager = entityManagerProvider.get();

    StringWriter writer = new StringWriter();

    ServerSession session = entityManager.unwrap(ServerSession.class);
    SchemaManager schemaManager = new SchemaManager(session);
    schemaManager.setCreateSQLFiles(true);
    schemaManager.outputCreateDDLToWriter(writer);
    schemaManager.createDefaultTables(true);

    logger.info("Creating database tables!");
    BufferedReader reader = new BufferedReader(new StringReader(
      writer.toString()));
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (!Strings.isNullOrEmpty(line) &&
          !line.equalsIgnoreCase("INSERT INTO SEQUENCE")) {
          int lcv;
          for (lcv = line.length() - 1; lcv >= 0; lcv--) {
            if (line.charAt(lcv) != ';') {
              break;
            }
          }
          line = line.substring(0, lcv + 1);

          executeCreateStatement(entityManager, line);
        }
      }
    } catch (IOException ioe) {
      // This shouldn't happen
      Throwables.propagate(ioe);
    }
  }
}