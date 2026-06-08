package Repository;

import Entity.Phone;
import Entity.GenuinePhone;
import Entity.HandcarriedPhone;
import PhoneFactory.GenuinePhoneFactory;
import PhoneFactory.HandcarriedPhoneFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneRepository {
    private static final String FILE_PATH = "data/mobiles.csv";
    private static PhoneRepository instance;

    // Áp dụng Singleton Pattern để quản lý file tập trung
    private PhoneRepository() {
        // Tự động kiểm tra và tạo thư mục "data" nếu hệ thống chưa có
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static PhoneRepository getInstance() {
        if (instance == null) {
            instance = new PhoneRepository();
        }
        return instance;
    }

    /**
     * THÊM MỚI (APPEND MODE):
     * Ghi thêm một dòng dữ liệu điện thoại vào cuối file "data/mobiles.csv"
     * mà không làm ảnh hưởng hay phải ghi đè lại các dữ liệu cũ.
     */
    public void appendPhone(Phone p) {
        // Tham số true kích hoạt chế độ Append (Ghi tiếp vào cuối file)
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            if (p instanceof GenuinePhone gp) {
                // Định dạng CSV chính hãng: GEN,Id,Tên,Giá,Số lượng,Nhà sản xuất,Thời gian BH,Phạm vi BH
                writer.printf("GEN,%d,%s,%.1f,%d,%s,%d,%s%n",
                        gp.getId(),
                        gp.getName(),
                        gp.getPrice(),
                        gp.getQuantity(),
                        gp.getManufacturer(),
                        gp.getWarrantyPeriod(),
                        gp.getWarrantyScope());
            } else if (p instanceof HandcarriedPhone hp) {
                // Định dạng CSV xách tay: HAND,Id,Tên,Giá,Số lượng,Nhà sản xuất,Quốc gia,Trạng thái
                writer.printf("HAND,%d,%s,%.1f,%d,%s,%s,%s%n",
                        hp.getId(),
                        hp.getName(),
                        hp.getPrice(),
                        hp.getQuantity(),
                        hp.getManufacturer(),
                        hp.getOriginCountry(),
                        hp.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Lỗi hệ thống khi thêm mới điện thoại vào file CSV: " + e.getMessage());
        }
    }

    /**
     * GHI ĐÈ TOÀN BỘ (OVERWRITE MODE):
     * Dùng để cập nhật lại file CSV sau khi thực hiện chức năng XÓA điện thoại.
     */
    public void saveAll(List<Phone> phoneList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Phone p : phoneList) {
                if (p instanceof GenuinePhone gp) {
                    writer.printf("GEN,%d,%s,%.1f,%d,%s,%d,%s%n",
                            gp.getId(), gp.getName(), gp.getPrice(), gp.getQuantity(),
                            gp.getManufacturer(), gp.getWarrantyPeriod(), gp.getWarrantyScope());
                } else if (p instanceof HandcarriedPhone hp) {
                    writer.printf("HAND,%d,%s,%.1f,%d,%s,%s,%s%n",
                            hp.getId(), hp.getName(), hp.getPrice(), hp.getQuantity(),
                            hp.getManufacturer(), hp.getOriginCountry(), hp.getStatus());
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi hệ thống khi cập nhật dữ liệu file CSV: " + e.getMessage());
        }
    }

    /**
     * ĐỌC TOÀN BỘ FILE CSV:
     * Nạp lại toàn bộ dữ liệu từ file "data/mobiles.csv" lên bộ nhớ khi ứng dụng khởi động.
     * Giúp hệ thống biết được ID của điện thoại cuối cùng là bao nhiêu để tự động tăng chính xác.
     */
    public List<Phone> loadAll() {
        List<Phone> phoneList = new ArrayList<>();
        File file = new File(FILE_PATH);

        // Nếu file chưa tồn tại (Lần đầu chạy ứng dụng), trả về danh sách rỗng để bắt đầu tự tăng từ ID 1
        if (!file.exists()) return phoneList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Bỏ qua dòng trống nếu có

                String[] data = line.split(",");
                if (data.length < 6) continue; // Tránh lỗi mảng thiếu phần tử

                String type = data[0];
                int id = Integer.parseInt(data[1]);
                String name = data[2];
                double price = Double.parseDouble(data[3]);
                int quantity = Integer.parseInt(data[4]);
                String manufacturer = data[5];

                Phone p = null;
                if (type.equals("GEN") && data.length >= 8) {
                    int warranty = Integer.parseInt(data[6]);
                    String scope = data[7];
                    p = GenuinePhoneFactory.create(id, name, price, quantity, manufacturer, warranty, scope);
                } else if (type.equals("HAND") && data.length >= 8) {
                    String country = data[6];
                    String status = data[7];
                    p = HandcarriedPhoneFactory.create(id, name, price, quantity, manufacturer, country, status);
                }

                if (p != null) {
                    phoneList.add(p);
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi hệ thống khi nạp dữ liệu từ file CSV: " + e.getMessage());
        }
        return phoneList;
    }
}