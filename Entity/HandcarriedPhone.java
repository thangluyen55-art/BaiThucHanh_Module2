package Entity;

public class HandcarriedPhone extends Phone {
    private String originCountry; // Quốc gia xách tay (Yêu cầu mới: không được là "Viet Nam")
    private String status; // Trạng thái (Yêu cầu mới: "Da sua chua" hoặc "Chua sua chua")

    // Constructor hoàn chỉnh
    public HandcarriedPhone(int id, String name, double price, int quantity, String manufacturer,
                            String originCountry, String status) {
        // Gọi Constructor của lớp cha (Phone)
        super(id, name, price, quantity, manufacturer);
        this.originCountry = originCountry;
        this.status = status;
    }

    // --- GETTER VÀ SETTER ---
    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // --- CHỨC NĂNG 3: OVERRIDE PHƯƠNG THỨC toString() ĐỂ HIỂN THỊ DANH SÁCH ---
    @Override
    public String toString() {
        return String.format("ID: %d | [Xách Tay] Tên: %s | Giá: %,.0f VND | SL: %d | Hãng: %s | Quốc gia: %s | Trạng thái: %s",
                getId(),
                getName(),
                getPrice(),
                getQuantity(),
                getManufacturer(),
                this.originCountry,
                this.status
        );
    }

    // --- TRIỂN KHAI PHƯƠNG THỨC TRỪU TƯỢNG ---
    @Override
    public void displayInfo() {
        // Tái sử dụng lại hàm toString() ở trên để in ra màn hình Console đồng bộ
        System.out.println(this.toString());
    }
}