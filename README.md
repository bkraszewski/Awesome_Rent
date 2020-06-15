# Awesome_Rent
Welcome to sample Android/Firebase application.

Base requirements:
Write an application that manages apartment rentals

Users must be able to create an account and log in. (If a mobile application, this means that multiple users can use the app from the same phone).
Implement a client role:
   * Clients are able to browse rentable apartments in a list and on a map.
Implement a realtor role:
   * Realtors would be able to browse all rentable- and already rented apartments in a list and on a map.
   * Realtors would be able to CRUD all apartments and set the apartment state to available/rented.
Implement an admin role:
   * Admins would be able CRUD all apartments, realtors, and clients.
When an apartment is added, each new entry must have a name, description, floor area size, price per month, number of rooms, valid geolocation coordinates, date added and an associated realtor.
Geolocation coordinates should be added either by providing latitude/longitude directly or through address geocoding (https://developers.google.com/maps/documentation/javascript/geocoding).
All users should be able to filter the displayed apartments by size, price, and the number of rooms.
REST API. Make it possible to perform all user actions via the API, including authentication (If a mobile application and you don’t know how to create your own backend you can use Firebase.com or similar services to create the API).
In both cases, you should be able to explain how a REST API works and demonstrate that by creating functional tests that use the REST Layer directly. Please be prepared to use REST clients like Postman, cURL, etc. for this purpose.
If it’s a web application, it must be a single-page application. All actions need to be done client side using AJAX, refreshing the page is not acceptable. (If a mobile application, disregard this).
Functional UI/UX design is needed. You are not required to create a unique design, however, do follow best practices to make the project as functional as possible.
Bonus: unit and e2e tests.

Screens:
![alt text](https://github.com/bkraszewski/Awesome_Rent/blob/master/Screenshot_1592205739.png?raw=true)
![alt text](https://github.com/bkraszewski/Awesome_Rent/blob/master/Screenshot_1592205745.png)
![alt text](https://github.com/bkraszewski/Awesome_Rent/blob/master/Screenshot_1592205836.png)
![alt text](https://github.com/bkraszewski/Awesome_Rent/blob/master/Screenshot_1592205862.png)
![alt text](https://github.com/bkraszewski/Awesome_Rent/blob/master/Screenshot_1592205872.png)
![alt text](https://github.com/bkraszewski/Awesome_Rent/blob/master/Screenshot_1592205881.png)
