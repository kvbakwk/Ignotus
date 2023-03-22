package me.thekuba.managers;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

public class DatabaseManager {
    private final Ignotus plugin;
    private final FileConfiguration config;

    private DataSource dataSource;

    public DatabaseManager(Ignotus plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        try {
            this.dataSource = initMySQLDataSource();
            initDb();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }


    public void addPlayer(Player player) {
        giveQuery("INSERT INTO ignotus_players(uuid, nick) VALUES(?, ?);", new String[]{player.getUniqueId().toString(), player.getName()});
    }
    public void setValue(OfflinePlayer player, String media, String value) {
        giveQuery("UPDATE ignotus_players SET " + media + " = ? WHERE uuid = ?;", new String[]{value, player.getUniqueId().toString()});
    }
    public String getValue(OfflinePlayer player, String media) {
        return getQuery("SELECT " + media + " FROM ignotus_players WHERE uuid = ?;", new String[]{player.getUniqueId().toString()}, media);
    }
    public boolean isPlayer(Player player) {
        String answer = getQuery("SELECT nick FROM ignotus_players WHERE uuid = ?;", new String[]{player.getUniqueId().toString()}, "nick");
        if(Objects.equals(answer, null))
            return false;
        else
            return true;
    }


    private boolean giveQuery(String query, String[] variables) {
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < variables.length; i++) {
                stmt.setString(i + 1, variables[i]);
            }
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private String getQuery(String query, String[] variables, String queryLabel) {
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < variables.length; i++) {
                stmt.setString(i + 1, variables[i]);
            }
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next() && !Objects.equals(queryLabel, null)) {
                return resultSet.getString(queryLabel);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void initDb() throws SQLException, IOException {
        String setup;
        try (InputStream in = plugin.getResource("dbsetup.sql")) {
            setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw e;
        }
        String[] queries = setup.split(";");
        for (String query : queries) {
            if (query.isBlank()) continue;
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.execute();
            }
        }
        plugin.getLogger().info("Database setup complete.");
    }
    private DataSource initMySQLDataSource() throws SQLException {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();

        dataSource.setServerName(config.getString("mysql.host"));
        dataSource.setPortNumber(config.getInt("mysql.port"));
        dataSource.setDatabaseName(config.getString("mysql.database"));
        dataSource.setUser(config.getString("mysql.user"));
        dataSource.setPassword(config.getString("mysql.password"));

        testDataSource(dataSource);
        return dataSource;
    }
    private void testDataSource(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1)) {
                throw new SQLException("Could not establish database connection.");
            }
        }
    }
}
