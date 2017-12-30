# Tradish - Track Your Dishes

Version Alpha: Tradish is a food tracking application that allows users to order food and find out the real-time estimate time of arrival. Version Alpha is the prototype of Tradish and it is by no means a completed product. This version is intent for demonstration only. No commercial usage is allowed at this point.


The project involves a food delivery system that is defined in terms of both hardware and software. The hardware involved in this project is a cell phone with GPS module and internet connections. The software involved included MySQL database, Android Application, Ubuntu Server 16 hosted by Amazon EC2 and Web page.


For all the inquiries, please contact the following developers.

- Android Application: vannesschancc@live.com 
- Server Side: shuang@unomaha.edu 
- Android Application: ywang01@unomaha.edu


## Dependencies: 

Android Application:
- Volley Network for Android: https://github.com/google/volley
- SQLite Database: https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
- Parse User: http://parseplatform.org/Parse-SDK-Android/api/com/parse/ParseUser.html
- Google Maps Android Api: https://developers.google.com/maps/documentation/android-api/
- PayPal Android SDK: https://github.com/paypal/PayPal-Android-SDK

Server Side Development:  
- Node Js: https://nodejs.org/docs/latest-v5.x/api/
- mysql: https://www.npmjs.com/package/node-mysql
- express: https://expressjs.com/
- ejs: https://www.npmjs.com/package/ejs
- Passport: http://passportjs.org/
- Body-parser: https://www.npmjs.com/package/body-parser-json
- connect: https://github.com/senchalabs/connect
- Cookie-parser: https://github.com/expressjs/cookie-parser

## Breakdown of the Android Client: 

Customer end: 
- Log in, log off and sign up
- Setup and and update profile
- Select restaurants
- Select single or multiple dishes
- View shopping cart 
- Edit shopping cart 
- Make payment with paypal
- Find out real-time estimate time of arrival for delivery orders
- View order history (Unavailable for version alpha) 
- View payment history (Unavailable for version alpha) 
- Find out estimate time of arrival in real-time.
- Store user data for express log in. 
- Request permissions if not previously granted. 

Driver end: 
- Log in, log off and sign up
- Setup and and update profile
- View delivery orders assigned by restaurant owners. 
- View addresses associated with orders on map. 
- Send Geo location to server periodically. 
- Update delivery status (In progress, Unscheduled, completed) 
- View delivery history (Unavailable for version alpha)
- Store user data for express login. 
- Request permissions if not previously granted. 

Restaurant Owner end: 
- Log in, log off and sign up
- Setup and and update profile
- View orders made by customers
- Select orders for delivery scheduling
- View addresses associated with orders on map.
- Select drivers for delivery and send scheduled delivery to server
- View order history (Unavailable for version alpha)
- View payment history (Unavailable for version alpha)
- Store user data for express log in. 
- Request permissions if not previously granted. 

## Demonstration:

Video Link: https://www.youtube.com/watch?v=0o0XblNL808

  

	
 



