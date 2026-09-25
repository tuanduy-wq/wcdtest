package com.exam.dao;

import com.exam.model.Indexer;
import com.exam.model.PlayerViewModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {

    public List<Indexer> getAllIndexes() {
        List<Indexer> list = new ArrayList<>();
        String sql = "SELECT * FROM indexer";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Indexer(rs.getInt("index_id"), rs.getString("name"),
                        rs.getFloat("valueMin"), rs.getFloat("valueMax")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PlayerViewModel> getAllPlayerRecords() {
        List<PlayerViewModel> list = new ArrayList<>();
        String sql = "SELECT pi.id, p.player_id, p.name AS player_name, p.age AS player_age, " +
                "idx.index_id, idx.name AS index_name, pi.value " +
                "FROM player_index pi " +
                "JOIN player p ON pi.player_id = p.player_id " +
                "JOIN indexer idx ON pi.index_id = idx.index_id " +
                "ORDER BY pi.id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PlayerViewModel item = new PlayerViewModel();
                item.setId(rs.getInt("id"));
                item.setPlayerId(rs.getInt("player_id"));
                item.setPlayerName(rs.getString("player_name"));
                item.setPlayerAge(rs.getString("player_age"));
                item.setIndexId(rs.getInt("index_id"));
                item.setIndexName(rs.getString("index_name"));
                item.setValue(rs.getFloat("value"));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addPlayerRecord(String name, String age, int indexId, float value) {
        String insertPlayer = "INSERT INTO player (name, full_name, age, index_id) VALUES (?, ?, ?, ?)";
        String insertPI = "INSERT INTO player_index (player_id, index_id, value) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(insertPlayer, Statement.RETURN_GENERATED_KEYS)) {
                ps1.setString(1, name);
                ps1.setString(2, name);
                ps1.setString(3, age);
                ps1.setInt(4, indexId);
                ps1.executeUpdate();

                ResultSet rs = ps1.getGeneratedKeys();
                int playerId = 0;
                if (rs.next()) {
                    playerId = rs.getInt(1);
                }

                try (PreparedStatement ps2 = conn.prepareStatement(insertPI)) {
                    ps2.setInt(1, playerId);
                    ps2.setInt(2, indexId);
                    ps2.setFloat(3, value);
                    ps2.executeUpdate();
                }
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerRecord(int id, int playerId, String name, String age, int indexId, float value) {
        String updatePlayer = "UPDATE player SET name = ?, age = ?, index_id = ? WHERE player_id = ?";
        String updatePI = "UPDATE player_index SET index_id = ?, value = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps1 = conn.prepareStatement(updatePlayer)) {
                    ps1.setString(1, name);
                    ps1.setString(2, age);
                    ps1.setInt(3, indexId);
                    ps1.setInt(4, playerId);
                    ps1.executeUpdate();
                }
                try (PreparedStatement ps2 = conn.prepareStatement(updatePI)) {
                    ps2.setInt(1, indexId);
                    ps2.setFloat(2, value);
                    ps2.setInt(3, id);
                    ps2.executeUpdate();
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePlayerRecord(int id) {
        String selectPlayerId = "SELECT player_id FROM player_index WHERE id = ?";
        String deletePlayer = "DELETE FROM player WHERE player_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psSel = conn.prepareStatement(selectPlayerId)) {
            psSel.setInt(1, id);
            ResultSet rs = psSel.executeQuery();
            if (rs.next()) {
                int playerId = rs.getInt("player_id");
                try (PreparedStatement psDel = conn.prepareStatement(deletePlayer)) {
                    psDel.setInt(1, playerId);
                    psDel.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}