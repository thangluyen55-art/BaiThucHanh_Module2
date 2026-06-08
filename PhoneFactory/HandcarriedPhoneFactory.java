package PhoneFactory;

import Entity.HandcarriedPhone;

public class HandcarriedPhoneFactory {
    public static HandcarriedPhone create(int id, String name, double price, int quantity,
                                          String manufacturer, String originCountry, String status) {
        return new HandcarriedPhone(id, name, price, quantity, manufacturer, originCountry, status);
    }
}