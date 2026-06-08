package Service;

import Entity.Phone;
import Entity.GenuinePhone;
import Entity.HandcarriedPhone;
import PhoneFactory.GenuinePhoneFactory;
import PhoneFactory.HandcarriedPhoneFactory;
import Repository.PhoneRepository;
import Validator.PhoneValidator;
import Exception.NotFoundProductException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PhoneService {
    private static PhoneService instance;
    private final LinkedList<Phone> phoneList = new LinkedList<>();
    private final PhoneRepository repository = PhoneRepository.getInstance();

    private PhoneService() {
        loadFromFile();
    }

    public static PhoneService getInstance() {
        if (instance == null) {
            instance = new PhoneService();
        }
        return instance;
    }

    private int generateNextId() {
        if (phoneList.isEmpty()) {
            return 1;
        }
        return phoneList.getLast().getId() + 1;
    }

    // --- CHỨC NĂNG 1: THÊM MỚI ---
    public void addPhone(String type, String name, String priceStr, String quantityStr,
                         String manufacturer, String warrantyPeriodStr, String warrantyScope,
                         String originCountry, String status) {

        boolean isValid = PhoneValidator.validate(type, name, priceStr, quantityStr, manufacturer,
                warrantyPeriodStr, warrantyScope, originCountry, status);
        if (!isValid) return;

        double price = Double.parseDouble(priceStr.trim());
        int quantity = Integer.parseInt(quantityStr.trim());
        int newId = generateNextId();
        Phone newPhone = null;

        if (type.equalsIgnoreCase("GEN")) {
            int warrantyPeriod = Integer.parseInt(warrantyPeriodStr.trim());
            newPhone = GenuinePhoneFactory.create(newId, name, price, quantity, manufacturer, warrantyPeriod, warrantyScope);
        } else if (type.equalsIgnoreCase("HAND")) {
            newPhone = HandcarriedPhoneFactory.create(newId, name, price, quantity, manufacturer, originCountry, status);
        }

        if (newPhone != null) {
            phoneList.add(newPhone);
            repository.appendPhone(newPhone);
            System.out.println("🎉 Đã thêm dữ liệu thành công vào file CSV.");
        }
    }

    // --- CHỨC NĂNG 2: XÓA ĐIỆN THOẠI (Kiểm tra ID và ném Exception nếu không thấy) ---
    /**
     * Kiểm tra xem ID có tồn tại không.
     * @throws NotFoundProductException nếu không tìm thấy ID sản phẩm trong danh sách.
     */
    public Phone checkIdExist(int id) throws NotFoundProductException {
        for (Phone p : phoneList) {
            if (p.getId() == id) {
                return p; // Trả về đối tượng tìm thấy để View hỏi xác nhận Yes/No
            }
        }
        // Nếu chạy hết vòng lặp không thấy, ném ngay ngoại lệ tùy chỉnh
        throw new NotFoundProductException("ID điện thoại không tồn tại.");
    }

    /**
     * Thực hiện xóa điện thoại khỏi bộ nhớ RAM và đồng bộ ghi đè lại file CSV.
     */
    public void deletePhone(Phone p) {
        phoneList.remove(p);            // Xóa khỏi RAM
        repository.saveAll(phoneList);  // Gọi Repository ghi đè lại toàn bộ file CSV mới
        System.out.println("🗑️ Đã xóa thành công điện thoại ID: " + p.getId());

        // Hiển thị lại danh sách sau khi xóa ra màn hình Console
        displayAll();
    }

    // --- CHỨC NĂNG 3: XEM DANH SÁCH ĐIỆN THOẠI (Dùng toString) ---
    public void displayAll() {
        if (phoneList.isEmpty()) {
            System.out.println("Danh sách điện thoại hiện tại đang trống.");
            return;
        }
        System.out.println("\n======================= DANH SÁCH ĐIỆN THOẠI =======================");
        for (Phone p : phoneList) {
            // Tận dụng phương thức toString() đã được override ở các class con (Genuine/Handcarried)
            System.out.println(p.toString());
        }
        System.out.println("====================================================================");
    }

    // --- CHỨC NĂNG 4: TÌM KIẾM GẦN ĐÚNG (Theo ID hoặc Tên) ---
    public void searchFeatures(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("⚠️ Từ khóa tìm kiếm không được để trống.");
            return;
        }

        String searchKey = keyword.trim().toLowerCase();
        List<Phone> searchResult = new ArrayList<>();

        for (Phone p : phoneList) {
            String idStr = String.valueOf(p.getId());
            String nameStr = p.getName().toLowerCase();

            // Kiểm tra gần đúng (Chứa từ khóa): ID chứa từ khóa HOẶC Tên chứa từ khóa
            if (idStr.contains(searchKey) || nameStr.contains(searchKey)) {
                searchResult.add(p);
            }
        }

        // Hiển thị kết quả tìm kiếm ra màn hình Console
        if (searchResult.isEmpty()) {
            System.out.println("❌ Không tìm thấy điện thoại nào khớp với từ khóa: '" + keyword + "'");
        } else {
            System.out.println("\n🔍 KẾT QUẢ TÌM KIẾM CHO TỪ KHÓA '" + keyword + "':");
            for (Phone p : searchResult) {
                System.out.println(p.toString());
            }
            System.out.println("--------------------------------------------------------------------");
        }
    }

    // --- NẠP DỮ LIỆU TỪ FILE ---
    private void loadFromFile() {
        phoneList.clear();
        List<Phone> loadedPhones = repository.loadAll();
        if (loadedPhones != null) {
            phoneList.addAll(loadedPhones);
        }
    }

    public List<Phone> search(String field, String keyword) {
        List<Phone> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return result;

        String searchKey = keyword.trim().toLowerCase();
        for (Phone p : phoneList) {
            if ("name".equalsIgnoreCase(field)) {
                if (p.getName().toLowerCase().contains(searchKey) || String.valueOf(p.getId()).contains(searchKey)) {
                    result.add(p);
                }
            } else if ("manufacturer".equalsIgnoreCase(field)) {
                if (p.getManufacturer().toLowerCase().contains(searchKey)) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public void deleteById(int id) throws NotFoundProductException {
        Phone phoneToDelete = checkIdExist(id); // Nếu không có, tự ném NotFoundProductException ở đây
        phoneList.remove(phoneToDelete);
        repository.saveAll(phoneList);
        System.out.println("🗑️ Đã xóa thành công điện thoại ID: " + id);
        displayAll();
    }
}