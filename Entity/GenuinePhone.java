package Entity;

public class GenuinePhone extends Phone {
    private int warrantyPeriod; // Tính theo số ngày (Yêu cầu mới: không quá 730 ngày)
    private String warrantyScope; // "Toan Quoc" hoặc "Quoc Te"

    // Constructor hoàn chỉnh
    public GenuinePhone(int id, String name, double price, int quantity, String manufacturer,
                        int warrantyPeriod, String warrantyScope) {
        // Gọi Constructor của lớp cha (Phone)
        super(id, name, price, quantity, manufacturer);
        this.warrantyPeriod = warrantyPeriod;
        this.warrantyScope = warrantyScope;
    }

    // --- GETTER VÀ SETTER ---
    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getWarrantyScope() {
        return warrantyScope;
    }

    public void setWarrantyScope(String warrantyScope) {
        this.warrantyScope = warrantyScope;
    }

    // --- CHỨC NĂNG 3: OVERRIDE PHƯƠNG THỨC toString() ĐỂ HIỂN THỊ DANH SÁCH ---
    @Override
    public String toString() {
        return String.format("ID: %d | [Chính Hãng] Tên: %s | Giá: %,.0f VND | SL: %d | Hãng: %s | Bảo hành: %d ngày | Phạm vi: %s",
                getId(),
                getName(),
                getPrice(),
                getQuantity(),
                getManufacturer(),
                this.warrantyPeriod,
                this.warrantyScope
        );
    }

    // --- TRIỂN KHAI PHƯƠNG THỨC TRỪU TƯỢNG (Giữ lại để tương thích nếu các chức năng khác cần dùng) ---
    @Override
    public void displayInfo() {
        System.out.println(this.toString());
    }
}