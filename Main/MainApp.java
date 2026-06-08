package Main;

import Service.PhoneService;
import Entity.Phone;
import Exception.NotFoundProductException;

import java.util.Scanner;

public class MainApp {
    // Đưa Scanner và Service ra làm thuộc tính của lớp để dùng chung
    private final Scanner scanner = new Scanner(System.in);
    private final PhoneService phoneService = PhoneService.getInstance();

    /**
     * Phương thức khởi chạy Menu chính - Thay thế hoàn toàn cho hàm main() cũ
     */
    public void start() {
        while (true) {
            System.out.println("\n================ CHƯƠNG TRÌNH QUẢN LÝ ĐIỆN THOẠI ================");
            System.out.println("1. Thêm mới điện thoại");
            System.out.println("2. Xóa điện thoại theo ID");
            System.out.println("3. Xem danh sách điện thoại");
            System.out.println("4. Tìm kiếm điện thoại (gần đúng)");
            System.out.println("5. Thoát chương trình");
            System.out.print("👉 Chọn chức năng (1-5): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAddNewPhone();
                    break;
                case "2":
                    handleDeletePhone();
                    break;
                case "3":
                    System.out.println("\n--- 3. DANH SÁCH ĐIỆN THOẠI TRONG HỆ THỐNG ---");
                    phoneService.displayAll();
                    break;
                case "4":
                    System.out.println("\n--- 4. TÌM KIẾM ĐIỆN THOẠI GẦN ĐÚNG ---");
                    System.out.print("Nhập từ khóa tìm kiếm (ID hoặc Tên điện thoại): ");
                    String keyword = scanner.nextLine();
                    phoneService.searchFeatures(keyword);
                    break;
                case "5":
                    System.out.println("👋 Đang đóng hệ thống... Tạm biệt!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ Lỗi: Lựa chọn không hợp lệ! Vui lòng nhập từ 1 đến 5.");
                    break;
            }
        }
    }

    /**
     * Logic chức năng 1: Thêm mới điện thoại
     */
    private void handleAddNewPhone() {
        System.out.println("\n--- 1. CHỨC NĂNG THÊM MỚI ĐIỆN THOẠI ---");
        System.out.print("Nhập loại điện thoại (GEN - Chính hãng / HAND - Xách tay): ");
        String type = scanner.nextLine().trim();

        System.out.print("Nhập tên điện thoại: ");
        String name = scanner.nextLine();

        System.out.print("Nhập giá bán: ");
        String priceStr = scanner.nextLine().trim();

        System.out.print("Nhập số lượng: ");
        String quantityStr = scanner.nextLine().trim();

        System.out.print("Nhập nhà sản xuất: ");
        String manufacturer = scanner.nextLine();

        // Khởi tạo các biến đặc thù bằng chuỗi rỗng để tránh lỗi null truyền vào hàm
        String warrantyPeriodStr = "";
        String warrantyScope = "";
        String originCountry = "";
        String status = "";

        if (type.equalsIgnoreCase("GEN")) {
            System.out.print("Nhập thời gian bảo hành (số ngày, không quá 730): ");
            warrantyPeriodStr = scanner.nextLine().trim();
            System.out.print("Nhập phạm vi bảo hành (Toan Quoc / Quoc Te): ");
            warrantyScope = scanner.nextLine();
        } else if (type.equalsIgnoreCase("HAND")) {
            System.out.print("Nhập quốc gia xách tay (không được là Viet Nam): ");
            originCountry = scanner.nextLine();
            System.out.print("Nhập trạng thái (Da sua chua / Chua sua chua): ");
            status = scanner.nextLine();
        }

        System.out.println("\n[Hệ thống]: Đang tiến hành kiểm tra và thêm mới...");

        // Đẩy thẳng qua Service xử lý. Service sẽ tự gọi Validator.
        // Nếu hợp lệ sẽ tự tăng ID và ghi file, nếu lỗi sẽ tự báo lỗi ra Console.
        phoneService.addPhone(
                type, name, priceStr, quantityStr, manufacturer,
                warrantyPeriodStr, warrantyScope, originCountry, status
        );
    }

    /**
     * Logic chức năng 2: Xóa điện thoại kèm kiểm tra Exception vòng lặp nhập lại
     */
    private void handleDeletePhone() {
        System.out.println("\n--- 2. CHỨC NĂNG XÓA ĐIỆN THOẠI ---");
        while (true) {
            try {
                System.out.print("Nhập ID điện thoại cần xóa: ");
                String idInput = scanner.nextLine().trim();
                int idDelete = Integer.parseInt(idInput);

                // Bước 1: Gọi service kiểm tra ID có tồn tại không.
                // Nếu không có, hàm này tự throw NotFoundProductException.
                Phone phoneToDelete = phoneService.checkIdExist(idDelete);

                // Bước 2: Hỏi xác nhận từ phía người dùng
                System.out.print("⚠️ Bạn có chắc chắn muốn xóa sản phẩm này không? (Yes/No): ");
                String confirm = scanner.nextLine().trim();

                if (confirm.equalsIgnoreCase("Yes")) {
                    phoneService.deletePhone(phoneToDelete); // Tiến hành xóa trên RAM & ghi đè CSV
                } else {
                    System.out.println("❌ Đã hủy thao tác xóa. Quay về menu chính.");
                }
                break; // Thành công thì bẻ gãy vòng lặp nhập ID

            } catch (NumberFormatException e) {
                System.out.println("❌ Lỗi: ID bắt buộc phải là một số nguyên hợp lệ! Vui lòng nhập lại.");
            } catch (NotFoundProductException e) {
                // Bắt đúng ngoại lệ tự chế và in ra thông điệp "ID điện thoại không tồn tại."
                System.out.println("❌ Lỗi: " + e.getMessage() + " Vui lòng kiểm tra lại.");
            }
        }

        // Nhấn Enter để quay lại menu chính
        System.out.println("\n👉 Nhấn nút Enter để quay lại menu chính...");
        scanner.nextLine();
    }
}