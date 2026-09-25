package com.exam.controller;

import com.exam.dao.PlayerDAO;
import com.exam.model.Indexer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/player")
public class PlayerServlet extends HttpServlet {
    private final PlayerDAO playerDAO = new PlayerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            playerDAO.deletePlayerRecord(id);
            resp.sendRedirect("player");
            return;
        }
        req.setAttribute("indexList", playerDAO.getAllIndexes());
        req.setAttribute("playerList", playerDAO.getAllPlayerRecords());
        req.getRequestDispatcher("/player.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        String playerIdStr = req.getParameter("playerId");
        String name = req.getParameter("playerName");
        String age = req.getParameter("playerAge");
        String indexIdStr = req.getParameter("indexId");
        String valueStr = req.getParameter("value");

        StringBuilder error = new StringBuilder();
        if (name == null || name.trim().isEmpty()) {
            error.append("Player name không được để trống. ");
        }
        try {
            int ageNum = Integer.parseInt(age);
            if (ageNum <= 0 || ageNum > 100) error.append("Tuổi không hợp lệ (1-100). ");
        } catch (Exception e) {
            error.append("Tuổi phải là số nguyên. ");
        }

        int indexId = Integer.parseInt(indexIdStr);
        try {
            float value = Float.parseFloat(valueStr);
            List<Indexer> indexes = playerDAO.getAllIndexes();
            for (Indexer idx : indexes) {
                if (idx.getIndexId() == indexId) {
                    if (value < idx.getValueMin() || value > idx.getValueMax()) {
                        error.append(String.format("Giá trị chỉ số %s phải nằm trong khoảng [%.0f, %.0f]. ",
                                idx.getName(), idx.getValueMin(), idx.getValueMax()));
                    }
                    break;
                }
            }
        } catch (Exception e) {
            error.append("Value phải là số thực. ");
        }

        if (error.length() > 0) {
            req.setAttribute("errorMessage", error.toString());
            req.setAttribute("indexList", playerDAO.getAllIndexes());
            req.setAttribute("playerList", playerDAO.getAllPlayerRecords());
            req.getRequestDispatcher("/player.jsp").forward(req, resp);
            return;
        }

        float value = Float.parseFloat(valueStr);
        if ("edit".equals(action)) {
            playerDAO.updatePlayerRecord(Integer.parseInt(idStr), Integer.parseInt(playerIdStr), name, age, indexId, value);
        } else {
            playerDAO.addPlayerRecord(name, age, indexId, value);
        }
        resp.sendRedirect("player");
    }
}