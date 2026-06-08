package PhoneFactory;

import Entity.GenuinePhone;

public class GenuinePhoneFactory {
    public static GenuinePhone create(int id, String name, double price, int quantity,
                                      String manufacturer, int warrantyPeriod, String warrantyScope) {
        return new GenuinePhone(id, name, price, quantity, manufacturer, warrantyPeriod, warrantyScope);
    }
}