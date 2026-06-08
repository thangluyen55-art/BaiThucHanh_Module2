package Exception;

// Custom Exception kế thừa từ Exception để bắt buộc phải handle (Checked Exception)
public class NotFoundProductException extends Exception {
    public NotFoundProductException(String message) {
        super(message);
    }
}