package Main;

import Repository.PhoneRepository;
import Service.PhoneService;
import Controller.PhoneController; // Nếu bạn dùng luồng qua Controller
// import View.PhoneView; // Bạn có thể đổi tên lớp MainApp cũ thành PhoneView nếu muốn đồng bộ 100%

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Khởi tạo tầng lưu trữ dữ liệu (Repository) dạng Singleton
        PhoneRepository phoneRepository = PhoneRepository.getInstance();

        // 2. Khởi tạo tầng xử lý nghiệp vụ (Service) dạng Singleton và tự động liên kết Repo bên trong
        PhoneService phoneService = PhoneService.getInstance();

        // 3. Khởi tạo tầng điều khiển (Nếu bạn dùng Controller nhận Request như file trước)
        PhoneController controller = new PhoneController();

        // 4. Khởi chạy giao diện chính (Sử dụng trực tiếp lớp MainApp mà chúng ta đã làm ở bước trước)
        // Mẹo: Bạn có thể đổi tên class "MainApp" thành "PhoneView" cho giống hoàn toàn với mẫu của bạn.
        MainApp app = new MainApp();
        app.start(); // Khởi chạy vòng lặp menu hệ thống
    }
}