package Controller;

import Network.Request;
import Service.PhoneService;
import Entity.Phone;
import Exception.NotFoundProductException;

import java.util.*;
import java.util.function.Consumer;

public class PhoneController {
    private final PhoneService service = PhoneService.getInstance();
    private final Map<String, Consumer<Request>> commands = new HashMap<>();

    public PhoneController() {

        // 1. CHỨC NĂNG THÊM MỚI
        commands.put("add", req -> {
            try {
                service.addPhone(
                        req.params.get("type"),
                        req.params.get("name"),
                        req.params.get("price"),
                        req.params.get("quantity"),
                        req.params.get("manufacturer"),
                        req.params.get("warrantyPeriod"),
                        req.params.get("warrantyScope"),
                        req.params.get("originCountry"),
                        req.params.get("status")
                );
            } catch (Exception e) {
                System.out.println("❌ Lỗi hệ thống khi thêm điện thoại: " + e.getMessage());
            }
        });

        // 2. CHỨC NĂNG TÌM KIẾM (Đã sửa gọi đúng hàm search trả về List)
        commands.put("searchName", req -> printResults(service.search("name", req.keyword)));
        commands.put("searchManufacturer", req -> printResults(service.search("manufacturer", req.keyword)));

        // 3. CHỨC NĂNG XÓA (Đã đồng bộ bắt Custom Exception)
        commands.put("delete", req -> {
            try {
                int idToDelete = Integer.parseInt(req.keyword.trim());

                // Gọi hàm xóa vừa bổ sung trong Service
                service.deleteById(idToDelete);

            } catch (NumberFormatException e) {
                System.out.println("❌ Lỗi: ID cần xóa phải là một số nguyên hợp lệ! (Bạn đã nhập: " + req.keyword + ")");
            } catch (NotFoundProductException e) {
                // Bắt đúng exception từ service ném ra và in thông điệp chuẩn "ID điện thoại không tồn tại."
                System.out.println("❌ " + e.getMessage());
            }
        });

        // 4. CHỨC NĂNG HIỂN THỊ DANH SÁCH
        commands.put("displayAll", req -> {
            service.displayAll();
        });
    }

    // --- Hàm phụ trợ in danh sách kết quả ---
    private void printResults(List<Phone> results) {
        if (results == null || results.isEmpty()) {
            System.out.println("❌ Không có thông tin phù hợp.");
        } else {
            System.out.println("\n🔍 KẾT QUẢ TÌM KIẾM KHỚP DỮ LIỆU:");
            results.forEach(p -> System.out.println(p.toString()));
            System.out.println("--------------------------------------------------------------------");
        }
    }

    // --- Hàm trung tâm để nhận và thực thi Request ---
    public void execute(Request req) {
        if (req == null || req.action == null) {
            System.out.println("❌ Request hoặc hành động không hợp lệ!");
            return;
        }
        commands.getOrDefault(req.action, r -> System.out.println("❌ Hành động (action: " + r.action + ") không hợp lệ!")).accept(req);
    }
}