package repositories;

import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class URLRepository {

    private static final String INSERT_URL_SQL = "INSERT INTO URLS (HASH_CODE, URL) VALUES (?, ?)";
    private static final String GET_URL_SQL = "SELECT id FROM URLS WHERE HASH_CODE = ? AND URL = ?";
    private static final String GET_URL_FOR_ID_SQL = "SELECT URL FROM URLS WHERE ID = ?";

    private final Database database;

    @Inject
    public URLRepository(Database database) {
        this.database = database;
    }

    private CompletionStage<Long> checkIfUrlExistInDatabase(final String url) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = database.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(GET_URL_SQL)) {

                preparedStatement.setInt(1 , url.hashCode());
                preparedStatement.setString(2 , url);

                final ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    return -1L;
                }

            } catch (Exception e) {
                return -1L;
            }
        });
    }

    public CompletionStage<Long> insertUrl(final String url) {
        return checkIfUrlExistInDatabase(url).thenApplyAsync(databaseRecordId -> {
            if (databaseRecordId >= 0) {
                return databaseRecordId;
            } else {
                try (Connection connection = database.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(INSERT_URL_SQL, Statement.RETURN_GENERATED_KEYS)) {

                    preparedStatement.setInt(1 , url.hashCode());
                    preparedStatement.setString(2 , url);

                    preparedStatement.executeUpdate();
                    final ResultSet resultSet = preparedStatement.getGeneratedKeys();

                    if (resultSet.next()) {
                        return resultSet.getLong(1);
                    } else {
                        return -1L;
                    }

                } catch (Exception e) {
                    return -1L;
                }
            }
        });
    }

    public CompletionStage<String> getUrlForId(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = database.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(GET_URL_FOR_ID_SQL)) {

                preparedStatement.setLong(1 , id);

                final ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getString(1);
                } else {
                    return "";
                }

            } catch (Exception e) {
                return "";
            }
        });
    }
}
