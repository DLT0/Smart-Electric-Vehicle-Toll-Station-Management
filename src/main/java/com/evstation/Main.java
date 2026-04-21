package com.evstation;

import io.javalin.Javalin;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Start Web Server running on Port 8080
        Javalin app = Javalin.create(config -> {
            // Phục vụ trực tiếp các file HTML, CSS, JS trong src/main/resources/static/ để không cần Rebuild
            config.staticFiles.add("src/main/resources/static", io.javalin.http.staticfiles.Location.EXTERNAL);
        }).start(8080);

        System.out.println("Web Server đang chạy tại: http://localhost:8080/index.html");

        // Tạo REST API
        app.get("/api/dashboard/stats", ctx -> {
            // Lấy dữ liệu từ logic Java
            int totalStations = 150;
            int activeSessions = 42;

            // Trả về JSON cho Javascript fetch
            ctx.json(Map.of(
                    "totalStations", totalStations,
                    "activeSessions", activeSessions,
                    "revenue", "25.4M"));
        });

        // Loop ứng dụng console cũ vẫn chạy song song
        Module module = new Module();
        Menu menu = new Menu(module);
        menu.chayChuongTrinh();
    }

}