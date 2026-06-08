package Validator;

public class PhoneValidator {

    /**
     * Hàm kiểm tra tính hợp lệ của toàn bộ dữ liệu nhập vào.
     * @return true nếu TẤT CẢ dữ liệu hợp lệ, false nếu có ít nhất 1 lỗi.
     */
    public static boolean validate(String type, String name, String priceStr, String quantityStr,
                                   String manufacturer, String warrantyPeriodStr, String warrantyScope,
                                   String originCountry, String status) {
        boolean isValid = true;

        // 1. KIỂM TRA LOẠI ĐIỆN THOẠI (Bắt buộc phải là GEN hoặc HAND)
        if (isNullOrBlank(type) || (!type.equalsIgnoreCase("GEN") && !type.equalsIgnoreCase("HAND"))) {
            System.out.println("❌ Lỗi: Loại điện thoại không hợp lệ! (Phải nhập 'GEN' hoặc 'HAND')");
            return false;
        }

        // 2. KIỂM TRA CÁC TRƯỜNG BẮT BUỘC CHUNG
        if (isNullOrBlank(name)) {
            System.out.println("❌ Lỗi: Tên điện thoại không được để trống.");
            isValid = false;
        }
        if (isNullOrBlank(manufacturer)) {
            System.out.println("❌ Lỗi: Nhà sản xuất không được để trống.");
            isValid = false;
        }

        // 3. KIỂM TRA GIÁ BÁN (Phải là số và phải là số dương)
        if (isNullOrBlank(priceStr)) {
            System.out.println("❌ Lỗi: Giá bán không được để trống.");
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    System.out.println("❌ Lỗi: Giá bán phải là một số dương (> 0).");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Lỗi: Giá bán phải là một số hợp lệ.");
                isValid = false;
            }
        }

        // 4. KIỂM TRA SỐ LƯỢNG (Phải là số nguyên và phải là số dương)
        if (isNullOrBlank(quantityStr)) {
            System.out.println("❌ Lỗi: Số lượng không được để trống.");
            isValid = false;
        } else {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    System.out.println("❌ Lỗi: Số lượng phải là một số nguyên dương (> 0).");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Lỗi: Số lượng phải là một số nguyên hợp lệ.");
                isValid = false;
            }
        }

        // 5. KIỂM TRA RÀNG BUỘC RIÊNG THEO LOẠI ĐIỆN THOẠI
        if (type.equalsIgnoreCase("GEN")) {
            // --- ĐIỆN THOẠI CHÍNH HÃNG ---

            // Kiểm tra Thời gian bảo hành (Số dương, không quá 730 ngày)
            if (isNullOrBlank(warrantyPeriodStr)) {
                System.out.println("❌ Lỗi: Thời gian bảo hành không được để trống.");
                isValid = false;
            } else {
                try {
                    int warrantyPeriod = Integer.parseInt(warrantyPeriodStr);
                    if (warrantyPeriod <= 0 || warrantyPeriod > 730) {
                        System.out.println("❌ Lỗi: Thời gian bảo hành phải là số dương và không vượt quá 730 ngày (2 năm).");
                        isValid = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Lỗi: Thời gian bảo hành phải là một số nguyên hợp lệ.");
                    isValid = false;
                }
            }

            // Kiểm tra Phạm vi bảo hành (Chỉ nhận "Toan Quoc" hoặc "Quoc Te")
            if (isNullOrBlank(warrantyScope)) {
                System.out.println("❌ Lỗi: Phạm vi bảo hành không được để trống.");
                isValid = false;
            } else {
                String scopeTrim = warrantyScope.trim();
                if (!scopeTrim.equalsIgnoreCase("Toan Quoc") && !scopeTrim.equalsIgnoreCase("Quoc Te")) {
                    System.out.println("❌ Lỗi: Phạm vi bảo hành chỉ được chấp nhận 2 giá trị: 'Toan Quoc' hoặc 'Quoc Te'.");
                    isValid = false;
                }
            }

        } else if (type.equalsIgnoreCase("HAND")) {
            // --- ĐIỆN THOẠI XÁCH TAY ---

            // Kiểm tra Quốc gia xách tay (Không được là "Viet Nam")
            if (isNullOrBlank(originCountry)) {
                System.out.println("❌ Lỗi: Quốc gia xách tay không được để trống.");
                isValid = false;
            } else if (originCountry.trim().equalsIgnoreCase("Viet Nam")) {
                System.out.println("❌ Lỗi: Quốc gia xách tay không được là 'Viet Nam'.");
                isValid = false;
            }

            // Kiểm tra Trạng thái (Chỉ nhận "Da sua chua" hoặc "Chua sua chua")
            if (isNullOrBlank(status)) {
                System.out.println("❌ Lỗi: Trạng thái không được để trống.");
                isValid = false;
            } else {
                String statusTrim = status.trim();
                if (!statusTrim.equalsIgnoreCase("Da sua chua") && !statusTrim.equalsIgnoreCase("Chua sua chua")) {
                    System.out.println("❌ Lỗi: Trạng thái chỉ được chấp nhận 2 giá trị: 'Da sua chua' hoặc 'Chua sua chua'.");
                    isValid = false;
                }
            }
        }

        return isValid; // Trả về kết quả tổng hợp sau khi quét qua toàn bộ các lỗi
    }

    // Hàm phụ trợ kiểm tra chuỗi rỗng hoặc chỉ chứa khoảng trắng
    private static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}