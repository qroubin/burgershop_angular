# burgers



## Introduction
This project is a burger shop website that uses both angular and java spring-boot.
There are two main pages accessible from the navbar : "Administration" and "Commandes" (administration and orders).
On the first one you can see the list of available burgers, select them via checkboxes and delete them. You can also
create a new burger using a form by giving it a name and a list of ingredients.

On the second page, you can also see the list of burgers and select them, but this time the button is used to order the
selected burgers. When you click on "Commander" (order), each burger that is selected will be added to a new order that
will be sent to the database and put in the "Commandes en cours" (in process orders) part of the grid on the right.

The API also puts the order in a RabbitMQ queue that is linked to a restaurant service in the backend. The restaurant
treats each order when it receives it and cooks for a while before sending it back in a queue to the API. Once the API
receives it, it updates it in the database to show that the order was processed, and it goes in the "commandes terminée"
(processed order) part of the grid. 

In order for the grid to update automatically, that is for the orders to go from one
side to the other, a Web Socket is used to listen to changes on the processed state of an order.

## Usage
In order to use this project, you must follow those steps :
- Execute the command rabbitmq-server.bat and wait for it to finish initializing
- Inside of the root folder, execute the command "mvn spring-boot:run" to launch the backend with java spring boot
- Inside of src/main/front/burgers, execute the command "ng serve" to launch the frontend with angular
- Finally, execute the command "mvn exec:java -Dexec.mainClass="com.burgers.burgers.restaurant.Restaurant" to launch
the restaurant service

## Tests
To execute tests for the backend you can use "mvn test" inside the root folder.
To execute tests for the frontend you can use "ng test" inside of src/main/front/burgers.
The pipeline of Gitlab CI should launch the backend tests automatically each time something is pushed on the repository.