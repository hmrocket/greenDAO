/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "rezzza.model.greendao");
     // heritage level= 0
        Entity timezone = addTimezone(schema);
        Entity HotelAddress = addHotelAddress(schema);
        Entity HotelDescription = addHotelDescription(schema);
        Entity HotelPicture = addHotelPicture(schema);
        Entity PictureCollection = addPictureCollection(schema);
     // heritage level= 1
        Entity City = addCity(schema);
        Entity Hotel = addHotel(schema);
     // heritage level= 2
        Entity Deal = addDeal(schema);
       
        //relation city et timezone
        Property timezoneId = City.addLongProperty("timezoneId").getProperty();
        City.addToOne(timezone, timezoneId, "__Timezone");
        //relation Deal et Hotel
        Property hotelId = Deal.addLongProperty("hotelId").getProperty();
        Deal.addToOne(Hotel, hotelId, "__Hotel");
        //relation Hotel et HotelDescription, HotelAdress, HotelPicture
        Property hotelAddress = Hotel.addLongProperty("hotelAddressId").getProperty();
        Property hotelDescription = Hotel.addLongProperty("hotelDescriptionId").getProperty();
        Property hotelPicture = HotelPicture.addLongProperty("hotelId").getProperty();
		Property positionPicture = HotelPicture.addLongProperty("position").getProperty();
        Hotel.addToOne(HotelAddress, hotelAddress, "__HotelAddress");
        Hotel.addToOne(HotelDescription, hotelDescription, "__HotelDescription");
        Hotel.addToMany(HotelPicture, hotelPicture, "__HotelPicture").orderAsc(positionPicture);
        //relation HotelPicture et pictureCollection
        Property pictureCollectionId = HotelPicture.addLongProperty("pictureCollectionId").getProperty();
        HotelPicture.addToOne(PictureCollection, pictureCollectionId, "__picture_collection");
        
        new DaoGenerator().generateAll(schema, "../../../../src");
    }

	private static Entity addDeal(Schema schema) {
		// 17 - 1 relation Deal-Hotel 1:n,
    	Entity deal = schema.addEntity("Deal");
		deal.addIdProperty();
		deal.addStringProperty("name");
		deal.addFloatProperty("bar");
		deal.addFloatProperty("bdr");
		deal.addIntProperty("bdp");
		deal.addStringProperty("currency");
		deal.addIntProperty("guest_nb");
		deal.addIntProperty("night_nb");
		deal.addBooleanProperty("is_cancelable");
		deal.addBooleanProperty("is_refundable");
		deal.addStringProperty("status");
		deal.addDateProperty("start_at");
		deal.addDateProperty("end_at");
		deal.addStringProperty("discount_formula");
		deal.addDateProperty("created_at");
		deal.addDateProperty("updated_at");
		return deal;
	}

	private static Entity addHotel(Schema schema) {
		// 13 - 3 relation Hotel-HotelAdress 1:1, Hotel-HotelPicture 1:n, Hotel-HotelDescription 1:1
		Entity hotel = schema.addEntity("Hotel");
		hotel.addIdProperty();
		hotel.addStringProperty("name");
		hotel.addIntProperty("nb_star");
		hotel.addIntProperty("nb_room");
		hotel.addStringProperty("check_in_from");
		hotel.addStringProperty("check_in_to");
		hotel.addStringProperty("check_out_from");
		hotel.addStringProperty("check_out_to");
		hotel.addStringProperty("email_address");
		hotel.addStringProperty("phone_number");
		return hotel;
	}

	private static Entity addCity(Schema schema) {
		// 14 Property - n:1 relation city:Timezone 
		Entity city = schema.addEntity("City");
		city.addIdProperty();
		city.addStringProperty("name");
		city.addStringProperty("zip_code");
		city.addStringProperty("state");
		city.addStringProperty("country");
		city.addDateProperty("created_at");
		city.addDateProperty("updated_at");
		city.addDoubleProperty("latitude");
		city.addDoubleProperty("longitude");
		city.addDateProperty("deal_start_at");
		city.addIntProperty("deal_nb");
		city.addDateProperty("deal_end_at");
		city.addDateProperty("expires_at");
		return city;
	}

	private static Entity addHotelPicture(Schema schema) {
		// 4 Property +
		Entity hotelPicture = schema.addEntity("HotelPicture");
		hotelPicture.addIdProperty();
		// * position field used in main() to order HotelPicture asc depending on the position
		hotelPicture.addStringProperty("caption");
		hotelPicture.addStringProperty("base_url");
		return hotelPicture; 
	}
	
	private static Entity addPictureCollection(Schema schema) {
		Entity pictureCollection = schema.addEntity("PictureCollection");
		pictureCollection.addIdProperty().autoincrement();
		pictureCollection.addStringProperty("medium");
		pictureCollection.addStringProperty("small");
		pictureCollection.addStringProperty("thumbnail");
		pictureCollection.addStringProperty("square");
		return pictureCollection; 
	}

	private static Entity addHotelDescription(Schema schema) {
		// 6 Property + one to one relation with Hotel
		Entity hotelDescription = schema.addEntity("HotelDescription");
		hotelDescription.addIdProperty();
		hotelDescription.addStringProperty("teaser");
		hotelDescription.addStringProperty("a_propos");
		hotelDescription.addStringProperty("about_rooms");
		hotelDescription.addStringProperty("food_and_drink");
		hotelDescription.addStringProperty("environs");
		return hotelDescription;
	}

	private static Entity addHotelAddress(Schema schema) {
		// 10 Property + one to one relation with Hotel
		Entity hotelAddress = schema.addEntity("HotelAddress");
		hotelAddress.addIdProperty();
		hotelAddress.addStringProperty("street_1");
		hotelAddress.addStringProperty("street_2");
		hotelAddress.addStringProperty("country");
		hotelAddress.addStringProperty("state");
		hotelAddress.addStringProperty("city");
		hotelAddress.addStringProperty("zip_code");
		hotelAddress.addIntProperty("city_id");
		hotelAddress.addDoubleProperty("latitude");
		hotelAddress.addDoubleProperty("longitude");
		return hotelAddress;
	}

	private static Entity addTimezone(Schema schema) {
		// 5 Property
		Entity timezone = schema.addEntity("Timezone");
		timezone.addIdProperty();
		timezone.addStringProperty("identifier");
		timezone.addIntProperty("gmtOffset");
		timezone.addIntProperty("dstOffset");
		timezone.addIntProperty("rawOffset");   
		return timezone;
	}

//    private static void addCustomerOrder(Schema schema) {
//        Entity customer = schema.addEntity("Customer");
//        customer.addIdProperty();
//        customer.addStringProperty("name").notNull();
//
//        Entity order = schema.addEntity("Order");
//        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
//        order.addIdProperty();
//        Property orderDate = order.addDateProperty("date").getProperty();
//        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
//        order.addToOne(customer, customerId);
//
//        ToMany customerToOrders = customer.addToMany(order, customerId);
//        customerToOrders.setName("orders");
//        customerToOrders.orderAsc(orderDate);
//    }

}
